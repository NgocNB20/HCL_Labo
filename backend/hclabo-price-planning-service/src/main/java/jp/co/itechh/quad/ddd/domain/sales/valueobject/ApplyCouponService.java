/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.sales.valueobject;

import jp.co.itechh.quad.core.base.application.AppLevelFacesMessage;
import jp.co.itechh.quad.core.base.util.AppLevelFacesMessageUtil;
import jp.co.itechh.quad.core.base.utility.ConversionUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.constant.type.HTypeCouponTargetType;
import jp.co.itechh.quad.core.dao.shop.coupon.CouponDao;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsDetailsDto;
import jp.co.itechh.quad.core.entity.shop.coupon.CouponEntity;
import jp.co.itechh.quad.ddd.domain.pricecalculator.valueobject.CouponPaymentPrice;
import jp.co.itechh.quad.ddd.domain.product.adapter.IProductAdapter;
import jp.co.itechh.quad.ddd.domain.sales.entity.SalesSlipEntity;
import jp.co.itechh.quad.ddd.domain.sales.entity.SalesSlipForRevisionEntity;
import jp.co.itechh.quad.ddd.domain.sales.repository.ISalesSlipRepository;
import jp.co.itechh.quad.ddd.domain.user.adapter.ICustomerAdapter;
import jp.co.itechh.quad.ddd.domain.user.adapter.model.Customer;
import jp.co.itechh.quad.ddd.exception.AssertChecker;
import jp.co.itechh.quad.ddd.exception.DomainException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * クーポン 値オブジェクト ドメインサービス
 */
@Service
public class ApplyCouponService {

    /** クーポンDao */
    private final CouponDao couponDao;

    /** 販売伝票集約リポジトリ */
    private final ISalesSlipRepository salesSlipRepository;

    /** 商品アダプター */
    private final IProductAdapter productAdapter;

    /** 会員アダプター */
    private final ICustomerAdapter customerAdapter;

    /** 日付関連Utility */
    private final DateUtility dateUtility;

    /** 変換Utility */
    private final ConversionUtility conversionUtility;

    /**
     * コンストラクタ
     *
     * @param couponDao クーポンDao
     * @param salesSlipRepository 販売伝票集約リポジトリ
     * @param productAdapter 商品アダプター
     * @param customerAdapter 会員アダプター
     * @param dateUtility 日付関連Utility
     * @param conversionUtility 変換Utility
     */
    @Autowired
    public ApplyCouponService(CouponDao couponDao,
                              ISalesSlipRepository salesSlipRepository,
                              IProductAdapter productAdapter,
                              ICustomerAdapter customerAdapter,
                              DateUtility dateUtility,
                              ConversionUtility conversionUtility) {
        this.couponDao = couponDao;
        this.salesSlipRepository = salesSlipRepository;
        this.productAdapter = productAdapter;
        this.customerAdapter = customerAdapter;
        this.dateUtility = dateUtility;
        this.conversionUtility = conversionUtility;
    }

    /**
     * チェック結果：列挙型
     */
    @Getter
    @AllArgsConstructor
    private enum CheckResult {

        /** クーポン開始日時になっていない */
        NOT_START("PRICE-PLANNING-STRT0001-E", null, false),

        /** クーポン終了日時を超えている */
        EXPIRED("PRICE-PLANNING-ENDT0001-E", null, false),

        /** 利用回数超過 */
        USE_COUNT_LIMIT("PRICE-PLANNING-COPE0001-E", null, false),

        /** クーポン対象の商品なし */
        NO_TARGET_GOODS("PRICE-PLANNING-TARG0001-E", "PRICE-PLANNING-TARG0002-W", true),

        /** クーポン対象の会員ではない */
        NOT_TARGET_MEMBER("PRICE-PLANNING-TARM0001-E", null, false),

        /** クーポン適用金額に満たない */
        LOWER_PRICE("PRICE-PLANNING-IPPT0001-E", "PRICE-PLANNING-IPPT0003-W", true),

        /** クーポン適用後0円不可 */
        UNUSABLE_ALL_COUPON("PRICE-PLANNING-IPPT0004-E", "PRICE-PLANNING-IPPT0005-E", false);

        /** メッセージコード ※フロント注文用 */
        private String frontCode;

        /** メッセージコード ※バック受注修正・バッチ用 */
        private String adminCode;

        /** バックで発生の際、警告メッセージとして返却するかどうか */
        private boolean adminWarnFlag;
    }

    /**
     * 適用するクーポンを取得 ※該当クーポンコードの最新を取得
     *
     * @param salesSlip 販売伝票
     * @param couponCode クーポンコード
     * @return 適用するクーポン ※クーポン支払い額は未計算
     */
    public ApplyCoupon getApplyCoupon(SalesSlipEntity salesSlip, String couponCode) {

        // やっていることメモ：
        //  クーポンコードから最新のクーポンを取得し、販売伝票にセット
        //  クーポン支払い額の計算は一括金額計算ドメインサービス側に委譲し、ここでは行っていない
        //  一旦0円とし、この後UseCaseでPriceCalculationServiceForSalesSlipServiceをコールしている

        // クーポンコードが設定されている場合のみ、適用処理を行う
        if (StringUtils.isBlank(couponCode)) {
            return null;
        }

        // 送料未設定（配送方法未選択状態）でクーポン適用された場合は、エラー
        if (salesSlip.getCarriage() == null) {
            throw new DomainException("PRICE-PLANNING-CDBE0003-E");
        }

        // クーポン情報取得
        CouponEntity couponDb = getCouponByCode(couponCode);

        return new ApplyCoupon(couponCode, couponDb.getCouponSeq(), couponDb.getCouponVersionNo(),
                               couponDb.getCouponName(), new CouponPaymentPrice(0), true
        );
    }

    /**
     * クーポンチェック ※販売伝票
     *
     * @param salesSlip 販売伝票
     */
    public void checkApplyCoupon(SalesSlipEntity salesSlip) {

        // クーポンコードが設定されている場合のみ、後続の妥当性チェックを行う
        ApplyCoupon applyCoupon = salesSlip.getApplyCoupon();
        if (applyCoupon == null || StringUtils.isBlank(applyCoupon.getCouponCode())) {
            return;
        }

        // クーポン情報取得
        CouponEntity couponDb = getCouponByCode(applyCoupon.getCouponCode());

        // クーポン利用期間（開始・終了日時）チェック
        CheckResult checkResult = checkExpiryDate(couponDb);
        // クーポン利用回数チェック
        if (checkResult == null) {
            checkResult = checkUseCountLimit(salesSlip, couponDb);
        }

        // クーポン対象の商品購入価格小計リストを抽出
        List<ItemPurchasePriceSubTotal> targetSubTotalList = getTargetSubTotalList(salesSlip, couponDb);

        // クーポン適用商品（対象商品）チェック
        if (checkResult == null) {
            checkResult = checkTargetGoods(targetSubTotalList);
        }
        // クーポン適用会員（対象会員）チェック
        if (checkResult == null) {
            checkResult = checkTargetMembers(salesSlip, couponDb);
        }
        // クーポン適用金額チェック
        if (checkResult == null) {
            checkResult = checkDiscountLowerPrice(salesSlip, couponDb, targetSubTotalList);
        }
        // クーポン適用後決済金額0円チェック
        if (checkResult == null) {
            checkResult = checkDiscountAfterPrice(salesSlip);
        }

        if (checkResult == null) {
            return;
        }
        throw new DomainException(checkResult.getFrontCode(), null);
    }

    /**
     * クーポンチェック ※改訂用販売伝票
     *
     * @param salesSlipForRevision 改訂用販売伝票
     * @return チェック結果 ※警告メッセージの場合のみ、throw せず return
     */
    public AppLevelFacesMessage checkApplyCoupon(SalesSlipForRevisionEntity salesSlipForRevision) {

        // クーポンが利用されている場合のみ、後続の妥当性チェックを行う
        ApplyCoupon applyCoupon = salesSlipForRevision.getApplyCoupon();
        if (applyCoupon == null || !applyCoupon.isCouponUseFlag()) {
            return null;
        }

        // クーポン情報取得
        CouponEntity couponDb = getCouponByVersion(applyCoupon);

        // クーポン適用後決済金額0円チェック
        // ※受注修正ではクーポン適用対象外時にも警告無視して強制適用することができるため、本チェックは必ず先行うようにする
        // TODO この影響で、クーポン適用金額未達だが受注金額が0になっている状況において、未達ダイアログが出せない
        //  金額0チェックが勝ってしまう
        //  金額0チェックの優先度を下げると、未達ダイアログで「無効にしない」選択時に0円修正が通ってしまう
        //  Ver.3も全額クーポンチェックの方が勝っているため、一旦同じ仕様としている
        //  ちゃんと判定するなら、error / warn 共にthrowせず返すようにして、画面側で制御が必要
        CheckResult checkResult = checkDiscountAfterPrice(salesSlipForRevision);

        // クーポン対象の商品購入価格小計リストを抽出
        List<ItemPurchasePriceSubTotal> targetSubTotalList = getTargetSubTotalList(salesSlipForRevision, couponDb);

        // クーポン適用商品（対象商品）チェック
        if (checkResult == null) {
            checkResult = checkTargetGoods(targetSubTotalList);
        }
        // クーポン適用金額チェック
        if (checkResult == null) {
            checkResult = checkDiscountLowerPrice(salesSlipForRevision, couponDb, targetSubTotalList);
        }

        if (checkResult == null) {
            return null;
        } else if (checkResult.isAdminWarnFlag()) {
            return AppLevelFacesMessageUtil.getAllMessage(checkResult.getAdminCode(), null);
        }
        throw new DomainException(checkResult.getAdminCode(), null);
    }

    /**
     * DBのクーポン情報取得 ※該当クーポンコードの最新を取得
     *
     * @param couponCode クーポンコード
     * @return クーポンDB
     */
    private CouponEntity getCouponByCode(String couponCode) {

        // FIXME Ver3/Ver4共に不具合あり 別クーポンでクーポンコード流用した場合に、適用中のクーポンが取得できない
        //  SQL内に詳細コメントあり
        CouponEntity couponDb = couponDao.getCouponByCouponCode(couponCode);
        if (couponDb == null) {
            throw new DomainException("PRICE-PLANNING-CDBE0001-E", new String[] {couponCode});
        }

        return couponDb;
    }

    /**
     * DBのクーポン情報取得 ※指定VersionNoの世代を取得
     * <pre>
     *     TODO SEメンテしない限り取得できないことはないと思うがnullチェック必要か？
     *      先に販売伝票取得時に同じSQLで取得しているので、そのタイミングでNullPointerになる（@see参照）
     *      元々SalesSlipForRevisionEntityServiceにあったチェックを、とりあえず持ってきている状態
     * </pre>
     *
     * @see jp.co.itechh.quad.ddd.infrastructure.sales.repository.SalesSlipRepositoryImpl#getByTransactionId(String)
     * @param applyCoupon クーポン
     * @return クーポンDB
     */
    public CouponEntity getCouponByVersion(ApplyCoupon applyCoupon) {

        Integer couponSeq = applyCoupon.getCouponSeq();
        Integer couponVersionNo = applyCoupon.getCouponVersionNo();

        // チェック
        AssertChecker.assertNotNull("couponSeq is null", couponSeq);
        AssertChecker.assertNotNull("couponVersionNo is null", couponVersionNo);

        CouponEntity couponDb = couponDao.getCouponByCouponVersionNo(couponSeq, couponVersionNo);
        if (couponDb == null) {
            throw new DomainException("PRICE-PLANNING-CDBE0002-E",
                                      new String[] {String.valueOf(couponSeq), String.valueOf(couponVersionNo)}
            );
        }

        return couponDb;
    }

    /**
     * クーポン対象の商品購入価格小計リストを抽出
     *
     * @param salesSlip 販売伝票
     * @param couponDb クーポンDB
     * @return クーポン対象の商品購入価格小計リスト
     */
    private List<ItemPurchasePriceSubTotal> getTargetSubTotalList(SalesSlipEntity salesSlip, CouponEntity couponDb) {

        // 商品数>0の明細のみを抽出
        List<ItemPurchasePriceSubTotal> subTotalList = salesSlip.getItemPurchasePriceSubTotalList();
        List<ItemPurchasePriceSubTotal> targetList = subTotalList.stream()
                                                                 .filter(subTotal -> subTotal.getItemCount() > 0)
                                                                 .collect(Collectors.toList());

        // 全返品状態の場合は、空で返す
        if (CollectionUtils.isEmpty(targetList)) {
            return new ArrayList<>();
        }

        // 対象商品が空の場合は全商品対象のため、そのまま返す
        String targetGoods = couponDb.getTargetGoods();
        if (StringUtils.isBlank(targetGoods)) {
            return targetList;
        }

        // DBの商品情報を取得
        // 商品番号を保持していないので、クーポン対象商品の判定のためだけに取得する必要がある
        // 【注意】誤ってここから商品価格を取得しないこと！
        List<GoodsDetailsDto> goodsDetailsList = getGoodsDetails(targetList);

        // 対象商品のみ、もしくは、除外商品を除いた商品のみのリストにして返す
        List<String> targetGoodsList = Arrays.asList(conversionUtility.toDivArray(targetGoods));
        boolean isExclude = (couponDb.getTargetGoodsType() == HTypeCouponTargetType.EXCLUDE_TARGET);

        return targetList.stream()
                         .filter(subTotal -> isExclude != (targetGoodsList.contains(goodsDetailsList.stream()
                                                                                                    .filter(details -> subTotal.getItemId()
                                                                                                                               .equals(conversionUtility.toString(
                                                                                                                                               details.getGoodsSeq())))
                                                                                                    .findFirst()
                                                                                                    .orElseThrow()
                                                                                                    .getGoodsCode())))
                         .collect(Collectors.toList());
    }

    /**
     * DBの商品情報取得
     *
     * @param itemPriceSubTotalList 商品購入価格小計リスト ※クーポン対象のみ
     * @return 商品詳細リスト
     */
    private List<GoodsDetailsDto> getGoodsDetails(List<ItemPurchasePriceSubTotal> itemPriceSubTotalList) {

        Set<Integer> goodsSeqSet = new HashSet<>();
        for (ItemPurchasePriceSubTotal subTotal : itemPriceSubTotalList) {
            goodsSeqSet.add(Integer.valueOf(subTotal.getItemId()));
        }

        return productAdapter.getGoodsDetailsList(new ArrayList<>(goodsSeqSet));
    }

    /**
     * クーポン利用期間（開始・終了日時）チェック
     *
     * @param couponDb クーポンDB
     * @return チェック結果 ※OK時はnull
     */
    private CheckResult checkExpiryDate(CouponEntity couponDb) {

        Timestamp currentTime = dateUtility.getCurrentTime();

        Timestamp startTime = couponDb.getCouponStartTime();
        if (startTime.after(currentTime)) {
            return CheckResult.NOT_START;
        }

        Timestamp endTime = couponDb.getCouponEndTime();
        if (endTime.before(currentTime)) {
            return CheckResult.EXPIRED;
        }

        return null;
    }

    /**
     * クーポン利用回数チェック
     *
     * @param salesSlip 販売伝票
     * @param couponDb クーポンDB
     * @return チェック結果 ※OK時はnull
     */
    private CheckResult checkUseCountLimit(SalesSlipEntity salesSlip, CouponEntity couponDb) {

        // 利用回数0設定のクーポンは無制限に利用可能なため、チェック不要
        Integer useCountLimit = couponDb.getUseCountLimit();
        if (useCountLimit == 0) {
            return null;
        }

        // 該当会員の利用回数を取得
        Integer useCount = salesSlipRepository.getCouponCountByCustomerId(salesSlip.getCustomerId(),
                                                                          couponDb.getCouponSeq(),
                                                                          couponDb.getCouponStartTime()
                                                                         );

        // 利用回数+1(今回利用分)が利用上限数を超えているか判定
        if (useCountLimit < useCount + 1) {
            return CheckResult.USE_COUNT_LIMIT;
        }
        return null;
    }

    /**
     * クーポン適用商品（対象商品）チェック
     *
     * @param itemPriceSubTotalList 商品購入価格小計リスト ※クーポン対象のみ
     * @return チェック結果 ※OK時はnull
     */
    private CheckResult checkTargetGoods(List<ItemPurchasePriceSubTotal> itemPriceSubTotalList) {

        if (CollectionUtils.isEmpty(itemPriceSubTotalList)) {
            return CheckResult.NO_TARGET_GOODS;
        }
        return null;
    }

    /**
     * クーポン適用会員（対象会員）チェック
     *
     * @param salesSlip 販売伝票
     * @param couponDb クーポンDB
     * @return チェック結果 ※OK時はnull
     */
    private CheckResult checkTargetMembers(SalesSlipEntity salesSlip, CouponEntity couponDb) {

        // 対象会員が空の場合は全会員対象のため、チェック不要
        String targetMembers = couponDb.getTargetMembers();
        if (StringUtils.isBlank(targetMembers)) {
            return null;
        }

        // DBの会員情報を取得
        Customer customer = customerAdapter.getByMemberInfoSeq(Integer.valueOf(salesSlip.getCustomerId()));
        if (customer == null) {
            return CheckResult.NOT_TARGET_MEMBER;
        }

        // クーポン対象のメールアドレス であるかチェック
        List<String> targetMemberList = Arrays.asList(conversionUtility.toDivArray(targetMembers));
        boolean isExclude = (couponDb.getTargetMembersType() == HTypeCouponTargetType.EXCLUDE_TARGET);
        if (isExclude != targetMemberList.contains(customer.getMemberInfoId())) {
            return null;
        }
        return CheckResult.NOT_TARGET_MEMBER;
    }

    /**
     * クーポン適用金額チェック
     *
     * @param salesSlip 販売伝票
     * @param couponDb クーポンDB
     * @param itemPriceSubTotalList 商品購入価格小計リスト ※クーポン対象のみ
     * @return チェック結果 ※OK時はnull
     */
    private CheckResult checkDiscountLowerPrice(SalesSlipEntity salesSlip,
                                                CouponEntity couponDb,
                                                List<ItemPurchasePriceSubTotal> itemPriceSubTotalList) {

        // クーポン対象の商品購入価格合計を算出
        int targetPrice;
        if (StringUtils.isBlank(couponDb.getTargetGoods())) {

            // 対象商品が空の場合は全商品対象のため、販売伝票の商品購入価格合計（税抜）
            targetPrice = salesSlip.getItemPurchasePriceTotal();

        } else {

            // 対象商品が設定されている場合、クーポン対象の商品購入価格小計（税抜）を積み上げて算出
            targetPrice = itemPriceSubTotalList.stream()
                                               .mapToInt(ItemPurchasePriceSubTotal::getItemPurchasePriceSubTotal)
                                               .sum();
        }

        // メモ：適用金額は必須項目ではないが、クーポン登録時にブランクだと1円がセットされるため、nullチェックなし
        if (targetPrice < couponDb.getDiscountLowerOrderPrice().intValue()) {
            return CheckResult.LOWER_PRICE;
        }
        return null;
    }

    /**
     * クーポン適用後決済金額0円チェック
     *
     * @param salesSlip 販売伝票
     * @return チェック結果 ※OK時はnull
     */
    private CheckResult checkDiscountAfterPrice(SalesSlipEntity salesSlip) {

        // 注文開始時・料金計算の途中停止時など、まだ請求金額が計算できていない場合は、チェック不要
        Integer billingAmount = salesSlip.getBillingAmount();
        if (billingAmount == null) {
            return null;
        }

        // メモ：料金計算などの意図しないバグに備えて、念のため0ちょうどではなく0以下としておく
        if (billingAmount <= 0) {
            return CheckResult.UNUSABLE_ALL_COUPON;
        }
        return null;
    }

}
