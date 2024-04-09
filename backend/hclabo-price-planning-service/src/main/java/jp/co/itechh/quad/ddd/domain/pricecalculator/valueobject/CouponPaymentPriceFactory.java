/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.pricecalculator.valueobject;

import jp.co.itechh.quad.core.base.utility.ConversionUtility;
import jp.co.itechh.quad.core.constant.type.HTypeCouponTargetType;
import jp.co.itechh.quad.core.constant.type.HTypeDiscountType;
import jp.co.itechh.quad.core.entity.shop.coupon.CouponEntity;
import jp.co.itechh.quad.ddd.domain.sales.valueobject.AdjustmentAmount;
import jp.co.itechh.quad.ddd.domain.sales.valueobject.ApplyCoupon;
import jp.co.itechh.quad.ddd.domain.sales.valueobject.ApplyCouponService;
import jp.co.itechh.quad.ddd.exception.AssertChecker;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * クーポン支払い額 ファクトリ
 */
@Service
public class CouponPaymentPriceFactory {

    /** クーポン値オブジェクトドメインサービス */
    private final ApplyCouponService applyCouponService;

    /** 変換Utility */
    private final ConversionUtility conversionUtility;

    /** 割引率算出用の定数:100 */
    private static final BigDecimal ONE_HUNDRED = new BigDecimal(100);

    /** パーセント割引のクーポン支払い額算出時のROUNDINGモード:切り捨て */
    private static final RoundingMode ROUNDING_MODE_PERCENT_DISCOUNT = RoundingMode.DOWN;

    /**
     * コンストラクタ
     *
     * @param applyCouponService クーポン値オブジェクトドメインサービス
     * @param conversionUtility 変換Utility
     */
    @Autowired
    public CouponPaymentPriceFactory(ApplyCouponService applyCouponService, ConversionUtility conversionUtility) {
        this.applyCouponService = applyCouponService;
        this.conversionUtility = conversionUtility;
    }

    /**
     * クーポン支払い額 算出コンストラクト
     * <pre>
     *     クーポンSEQとクーポン枝番で算出する
     *     注文時・受注修正時に関わらず、料金計算時は、適用時点のクーポン情報で計算を行う
     *     クーポンコードから最新情報を取得するのは、フロント注文のクーポン適用時・伝票最新化時・伝票チェック時のみ（@see参照）
     * </pre>
     *
     * @see jp.co.itechh.quad.ddd.domain.sales.valueobject.ApplyCouponService
     * @param applyCoupon クーポン
     * @param itemSalesPriceTotal 商品販売金額合計
     * @param carriage 送料
     * @param adjustmentAmountList 調整金額リスト
     * @return クーポン支払い額
     */
    public CouponPaymentPrice constructCouponPaymentPrice(ApplyCoupon applyCoupon,
                                                          ItemSalesPriceTotal itemSalesPriceTotal,
                                                          Carriage carriage,
                                                          List<AdjustmentAmount> adjustmentAmountList) {

        // クーポン情報が存在しない場合・クーポン利用フラグが立っていない場合は、0円
        if (applyCoupon == null || !applyCoupon.isCouponUseFlag()) {
            return new CouponPaymentPrice(0);
        }

        // チェック
        AssertChecker.assertNotNull("itemSalesPriceTotal is null", itemSalesPriceTotal);
        AssertChecker.assertNotNull("carriage is null", carriage);

        // クーポン情報取得
        CouponEntity couponDb = applyCouponService.getCouponByVersion(applyCoupon);
        if (couponDb.getDiscountPrice() == null && couponDb.getDiscountRate() == null) {
            return new CouponPaymentPrice(0);
        }

        // クーポン支払い額計算
        int couponPaymentPrice = calcCouponPaymentPrice(couponDb, itemSalesPriceTotal, carriage, adjustmentAmountList);

        return new CouponPaymentPrice(couponPaymentPrice);
    }

    /**
     * クーポン支払い額計算
     *
     * @param couponDb クーポンDB
     * @param itemSalesPriceTotal 商品販売金額合計
     * @param carriage 送料
     * @param adjustmentAmountList 調整金額リスト
     * @return クーポン支払い額
     */
    private int calcCouponPaymentPrice(CouponEntity couponDb,
                                       ItemSalesPriceTotal itemSalesPriceTotal,
                                       Carriage carriage,
                                       List<AdjustmentAmount> adjustmentAmountList) {

        // クーポン対象商品の小計リストのみを保持した商品販売金額合計を生成
        ItemSalesPriceTotal targetPriceTotal = getTargetPriceTotal(itemSalesPriceTotal, couponDb);

        if (couponDb.getDiscountType() == HTypeDiscountType.PERCENT) {
            return calcCouponPaymentPricePercent(couponDb, targetPriceTotal);
        } else {
            return calcCouponPaymentPriceAmount(couponDb, targetPriceTotal, carriage, adjustmentAmountList);
        }
    }

    /**
     * クーポン対象商品の小計リストのみを保持した商品販売金額合計を生成
     *
     * @param itemSalesPriceTotal 商品販売金額合計
     * @param couponDb クーポンDB
     * @return 商品販売金額合計 ※クーポン対象のみ
     */
    private ItemSalesPriceTotal getTargetPriceTotal(ItemSalesPriceTotal itemSalesPriceTotal, CouponEntity couponDb) {

        // 商品数>0の明細のみを抽出
        List<ItemSalesPriceSubTotal> subTotalList = itemSalesPriceTotal.getItemSalesPriceSubTotalList();
        List<ItemSalesPriceSubTotal> targetList = subTotalList.stream()
                                                              .filter(subTotal -> subTotal.getOrderItemCount() > 0)
                                                              .collect(Collectors.toList());

        // 全返品状態の場合は、空で返す
        if (CollectionUtils.isEmpty(targetList)) {
            return new ItemSalesPriceTotal(new ArrayList<>());
        }

        // 対象商品が空の場合は全商品対象のため、そのまま返す
        String targetGoods = couponDb.getTargetGoods();
        if (StringUtils.isBlank(targetGoods)) {
            return new ItemSalesPriceTotal(targetList);
        }

        // 対象商品のみ、もしくは、除外商品を除いた商品のみのリストにして返す
        List<String> targetGoodsList = Arrays.asList(conversionUtility.toDivArray(targetGoods));
        boolean isExclude = (couponDb.getTargetGoodsType() == HTypeCouponTargetType.EXCLUDE_TARGET);
        List<ItemSalesPriceSubTotal> resultList = targetList.stream()
                                                            .filter(subTotal -> isExclude != (targetGoodsList.contains(
                                                                            subTotal.getOrderItem().getItemCode())))
                                                            .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(resultList)) {
            return new ItemSalesPriceTotal(new ArrayList<>());
        } else {
            return new ItemSalesPriceTotal(resultList);
        }
    }

    /**
     * クーポン支払い額計算 ※パーセント割引
     *
     * @param couponDb クーポンDB
     * @param targetPriceTotal 商品販売金額合計 ※クーポン対象のみ
     * @return クーポン支払い額
     */
    private int calcCouponPaymentPricePercent(CouponEntity couponDb, ItemSalesPriceTotal targetPriceTotal) {

        Integer discountRate = couponDb.getDiscountRate();
        if (discountRate == null) {
            return 0;
        }

        // 対象商品の金額合計
        BigDecimal targetPrice = BigDecimal.valueOf(targetPriceTotal.getItemPriceTotal());
        // 割引率
        BigDecimal rate = BigDecimal.valueOf(discountRate);

        BigDecimal result = targetPrice.multiply(rate).divide(ONE_HUNDRED, 0, ROUNDING_MODE_PERCENT_DISCOUNT);
        return result.intValue();
    }

    /**
     * クーポン支払い額計算 ※金額割引
     *
     * @param couponDb クーポンDB
     * @param targetPriceTotal 商品販売金額合計 ※クーポン対象のみ
     * @param carriage 送料
     * @param adjustmentAmountList 調整金額リスト
     * @return クーポン支払い額
     */
    private int calcCouponPaymentPriceAmount(CouponEntity couponDb,
                                             ItemSalesPriceTotal targetPriceTotal,
                                             Carriage carriage,
                                             List<AdjustmentAmount> adjustmentAmountList) {

        // 割引金額（最大クーポン支払い額）
        BigDecimal discountPrice = couponDb.getDiscountPrice();
        if (discountPrice == null) {
            return 0;
        }
        int maxPrice = discountPrice.intValue();

        // 商品販売金額合計・送料・調整金額に対する消費税
        // メモ：本当の消費税額ではない、決済手数料がクーポン算出時点では含まれていないため
        //  なので、調整金額マイナスで入れ過ぎて0円割り込んだりすると、計算結果が1円ずれることがある
        SalesPriceConsumptionTax tax =
                        new SalesPriceConsumptionTax(targetPriceTotal, carriage, null, adjustmentAmountList);

        // クーポン支払い額（最大クーポン支払い額を考慮せず、ただ足しこんだ値）
        int result = targetPriceTotal.getItemPriceTotal() + carriage.getValue() + getAdjustmentAmountTotal(
                        adjustmentAmountList) + tax.getSalesPriceConsumptionTax();

        // 最大クーポン支払い額に満たない場合は、計算結果をそのままクーポン支払い額とする
        return Math.min(result, maxPrice);
    }

    /**
     * 調整金額合計を取得
     * TODO 商品販売金額合計のように値オブジェクト化したいが、現状諦めている
     *  INTの最大値Overチェックとかも入れておいた方が良い
     *
     * @param adjustmentAmountList 調整金額リスト
     * @return 調整金額合計
     */
    private int getAdjustmentAmountTotal(List<AdjustmentAmount> adjustmentAmountList) {

        if (CollectionUtils.isEmpty(adjustmentAmountList)) {
            return 0;
        }

        return adjustmentAmountList.stream().mapToInt(AdjustmentAmount::getAdjustPrice).sum();
    }

}
