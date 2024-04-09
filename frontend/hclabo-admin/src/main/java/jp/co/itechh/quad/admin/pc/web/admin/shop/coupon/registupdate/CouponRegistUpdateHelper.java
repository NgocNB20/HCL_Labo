/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.shop.coupon.registupdate;

import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.ConversionUtility;
import jp.co.itechh.quad.admin.base.utility.DateUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeDiscountType;
import jp.co.itechh.quad.admin.entity.shop.coupon.CouponEntity;
import jp.co.itechh.quad.admin.pc.web.admin.shop.coupon.delete.CouponDeleteHelper;
import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import jp.co.itechh.quad.coupon.presentation.api.param.CouponCheckRequest;
import jp.co.itechh.quad.coupon.presentation.api.param.CouponRegistRequest;
import jp.co.itechh.quad.coupon.presentation.api.param.CouponRequest;
import jp.co.itechh.quad.coupon.presentation.api.param.CouponResponse;
import jp.co.itechh.quad.coupon.presentation.api.param.CouponUpdateRequest;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.sql.Timestamp;

/**
 * クーポン登録更新用Helperクラス。
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class CouponRegistUpdateHelper {

    /** 変換Utilityクラス */
    private final ConversionUtility conversionUtility;

    /** 日付関連Utilityクラス */
    private final DateUtility dateUtility;

    /**
     * コンストラクタ
     *
     * @param conversionUtility
     * @param dateUtility
     */
    @Autowired
    public CouponRegistUpdateHelper(ConversionUtility conversionUtility, DateUtility dateUtility) {

        this.conversionUtility = conversionUtility;
        this.dateUtility = dateUtility;
    }

    /**
     * 更新時の初期画面表示項目のセット。<br />
     *
     * <pre>
     * couponEntity→page
     * </pre>
     *
     * @param couponResponse            クーポンエンティティ
     * @param registUpdateModel 登録更新画面ページ
     */
    public void toPageForLoad(CouponResponse couponResponse, CouponRegistUpdateModel registUpdateModel) {

        CouponDeleteHelper couponDeleteHelper = ApplicationContextUtility.getBean(CouponDeleteHelper.class);
        CouponEntity coupon = couponDeleteHelper.convertToCouponEntity(couponResponse);

        // クーポンID
        registUpdateModel.setCouponId(coupon.getCouponId());
        // クーポン名
        registUpdateModel.setCouponName(coupon.getCouponName());
        // クーポン表示名PC
        registUpdateModel.setCouponDisplayNamePc(coupon.getCouponName());
        // クーポン開催開始日
        registUpdateModel.setCouponStartDate(conversionUtility.toYmd(coupon.getCouponStartTime()));
        // クーポン開催開始時間
        registUpdateModel.setCouponStartTime(conversionUtility.toHms(coupon.getCouponStartTime()));
        // クーポン開催終了日
        registUpdateModel.setCouponEndDate(conversionUtility.toYmd(coupon.getCouponEndTime()));
        // クーポン開催終了時間
        registUpdateModel.setCouponEndTime(conversionUtility.toHms(coupon.getCouponEndTime()));
        // クーポンコード
        registUpdateModel.setCouponCode(coupon.getCouponCode());
        // 割引種別
        registUpdateModel.setDiscountDetailsType(coupon.getDiscountDetailsType().getValue());
        // 割引率
        registUpdateModel.setDiscountRate(conversionUtility.toString(coupon.getDiscountRate()));
        // 割引金額
        registUpdateModel.setDiscountPrice(conversionUtility.toString(coupon.getDiscountPrice()));
        // 適用金額
        registUpdateModel.setDiscountLowerOrderPrice(conversionUtility.toString(coupon.getDiscountLowerOrderPrice()));
        // 利用回数
        registUpdateModel.setUseCountLimit(conversionUtility.toString(coupon.getUseCountLimit()));
        // 対象商品
        registUpdateModel.setTargetGoodsType(coupon.getTargetGoodsType());
        registUpdateModel.setTargetGoods(coupon.getTargetGoods());
        // 対象会員
        registUpdateModel.setTargetMembersType(coupon.getTargetMembersType());
        registUpdateModel.setTargetMembers(coupon.getTargetMembers());
        // 管理用メモ
        registUpdateModel.setMemo(coupon.getMemo());
        /* 画面以外 */
        // 変更前クーポン情報
        registUpdateModel.setPreUpdateCoupon(coupon);

        // 不正操作対策の情報をセットする
        registUpdateModel.setScSeq(registUpdateModel.getCouponSeq());
        registUpdateModel.setDbSeq(registUpdateModel.getPreUpdateCoupon().getCouponSeq());

    }

    /**
     * 確認画面遷移時の処理。<br />
     *
     * <pre>
     * 自動入力情報項目をセットし変更後クーポン情報を作成する。
     * </pre>
     *
     * @param coupon            クーポンエンティティ
     * @param registUpdateModel 登録更新画面ページ
     */
    public void toPageForConfirm(CouponEntity coupon, CouponRegistUpdateModel registUpdateModel) {

        /*
         * 自動入力項目のセット
         */

        /* 開催日時の自動入力 */
        // 開催開始時間が設定されていない場合は「00:00:00」をセット
        registUpdateModel.setCouponStartTime(conversionUtility.toDefaultHms(registUpdateModel.getCouponStartDate(),
                                                                            registUpdateModel.getCouponStartTime(),
                                                                            ConversionUtility.DEFAULT_START_TIME
                                                                           ));

        // 開催終了時間が設定されていない場合は「23：59：59」をセット
        registUpdateModel.setCouponEndTime(conversionUtility.toDefaultHms(registUpdateModel.getCouponEndDate(),
                                                                          registUpdateModel.getCouponEndTime(),
                                                                          ConversionUtility.DEFAULT_END_TIME
                                                                         ));

        /* 終了日時の自動修正 */
        // 終了日時が現在より過去の場合は現在の時刻をセットする（※特にチェックは行わない）
        // 終了日時が過去であるときは開催中のクーポン以外では起こり得ない
        // 開催前（新規）はバリデータチェックにより弾かれており、終了したものは変更できないため
        Timestamp currentTime = dateUtility.getCurrentTime();
        Timestamp endTime = conversionUtility.toTimeStamp(registUpdateModel.getCouponEndDate(),
                                                          registUpdateModel.getCouponEndTime()
                                                         );
        if (endTime != null && endTime.compareTo(currentTime) < 0) {
            registUpdateModel.setCouponEndDate(conversionUtility.toYmd(currentTime));
            registUpdateModel.setCouponEndTime(conversionUtility.toHms(currentTime));
            registUpdateModel.setChangeEndTime(true);
        }

        /* 適用金額の自動入力 */
        // 適用金額が未入力の場合は"1円"をセットする
        if (registUpdateModel.getDiscountLowerOrderPrice() == null) {
            registUpdateModel.setDiscountLowerOrderPrice("1");
        }

        /*
         * クーポン情報作成
         */
        // クーポンSEQ
        coupon.setCouponSeq(registUpdateModel.getCouponSeq());
        // クーポンID
        coupon.setCouponId(registUpdateModel.getCouponId());
        // クーポン名
        coupon.setCouponName(registUpdateModel.getCouponName());
        // クーポン表示名PC
        coupon.setCouponDisplayNamePC(registUpdateModel.getCouponName());
        // 開催開始日時
        coupon.setCouponStartTime(conversionUtility.toTimeStamp(registUpdateModel.getCouponStartDate(),
                                                                registUpdateModel.getCouponStartTime()
                                                               ));
        // 開催終了日時
        coupon.setCouponEndTime(conversionUtility.toTimeStamp(registUpdateModel.getCouponEndDate(),
                                                              registUpdateModel.getCouponEndTime()
                                                             ));
        // クーポンコード
        coupon.setCouponCode(registUpdateModel.getCouponCode());
        // 割引種別
        coupon.setDiscountDetailsType(EnumTypeUtil.getEnumFromValue(HTypeDiscountType.class,
                                                                    registUpdateModel.getDiscountDetailsType()
                                                                   ));
        // 割引率
        coupon.setDiscountRate(conversionUtility.toInteger(registUpdateModel.getDiscountRate()));
        // 割引金額
        coupon.setDiscountPrice(conversionUtility.toBigDecimal(registUpdateModel.getDiscountPrice()));
        // 適用金額
        coupon.setDiscountLowerOrderPrice(
                        conversionUtility.toBigDecimal(registUpdateModel.getDiscountLowerOrderPrice()));
        // 利用回数
        coupon.setUseCountLimit(conversionUtility.toInteger(registUpdateModel.getUseCountLimit()));
        // 対象商品
        coupon.setTargetGoodsType(registUpdateModel.getTargetGoodsType());
        coupon.setTargetGoods(conversionUtility.toSumLineSeparator(registUpdateModel.getTargetGoods()));
        registUpdateModel.setTargetGoodsType(registUpdateModel.getTargetGoodsType());
        registUpdateModel.setTargetGoods(conversionUtility.toSumLineSeparator(registUpdateModel.getTargetGoods()));
        // 対象会員
        coupon.setTargetMembersType(registUpdateModel.getTargetMembersType());
        coupon.setTargetMembers(conversionUtility.toSumLineSeparator(registUpdateModel.getTargetMembers()));
        registUpdateModel.setTargetMembersType(registUpdateModel.getTargetMembersType());
        registUpdateModel.setTargetMembers(conversionUtility.toSumLineSeparator(registUpdateModel.getTargetMembers()));
        // 管理用メモ
        coupon.setMemo(registUpdateModel.getMemo());
        /* 画面以外 */
        // 変更後クーポン情報
        registUpdateModel.setPostUpdateCoupon(coupon);
    }

    /**
     * 新規登録時にクーポンコードをセットする。
     *
     * @param registUpdateModel 登録更新画面ページ
     * @param couponCode        クーポンコード
     */
    public void toNewRegistPageForLoad(CouponRegistUpdateModel registUpdateModel, String couponCode) {

        registUpdateModel.setCouponCode(couponCode);

    }

    /**
     * グローバルメニュー・サイドメニューからの新規登録画面遷移時に以前のクーポン情報をクリアする。<br />
     *
     * <pre>
     * 変更内容表示後、グローバルメニュー・サイドメニューから遷移してきたことを想定。
     * </pre>
     *
     * @param registUpdateModel registUpdateModel
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public void toPageForNewRegistPage(CouponRegistUpdateModel registUpdateModel)
                    throws IllegalAccessException, InvocationTargetException {

        BeanUtils.copyProperties(registUpdateModel, new CouponRegistUpdateModel());

    }

    /**
     * クーポン登録リクエストに変換
     *
     * @param couponEntity クーポンエンティティクラス
     * @param administratorId        管理者ID
     * @return クーポン登録リクエスト
     */
    public CouponRegistRequest toCouponRegistRquest(CouponEntity couponEntity, String administratorId) {

        CouponRegistRequest couponRegistRequest = new CouponRegistRequest();

        couponRegistRequest.setCouponId(couponEntity.getCouponId());
        couponRegistRequest.setCouponName(couponEntity.getCouponName());
        couponRegistRequest.setCouponDisplayNamePC(couponEntity.getCouponDisplayNamePC());
        couponRegistRequest.setCouponCode(couponEntity.getCouponCode());
        couponRegistRequest.setCouponStartTime(couponEntity.getCouponStartTime());
        couponRegistRequest.setCouponEndTime(couponEntity.getCouponEndTime());
        couponRegistRequest.setDiscountType(couponEntity.getDiscountDetailsType().getValue());
        couponRegistRequest.setDiscountRate(couponEntity.getDiscountRate());
        couponRegistRequest.setDiscountPrice(couponEntity.getDiscountPrice());
        couponRegistRequest.setDiscountLowerOrderPrice(couponEntity.getDiscountLowerOrderPrice());
        couponRegistRequest.setUseCountLimit(couponEntity.getUseCountLimit());
        couponRegistRequest.setTargetGoodsType(couponEntity.getTargetGoodsType());
        couponRegistRequest.setTargetGoods(couponEntity.getTargetGoods());
        couponRegistRequest.setTargetMembersType(couponEntity.getTargetMembersType());
        couponRegistRequest.setTargetMembers(couponEntity.getTargetMembers());
        couponRegistRequest.setMemo(couponEntity.getMemo());
        couponRegistRequest.setAdministratorId(administratorId);

        return couponRegistRequest;
    }

    /**
     * クーポン更新リクエストに変換
     *
     * @param couponRegistUpdateModel クーポンエンティティクラス
     * @param administratorId        管理者ID
     * @return クーポン更新リクエスト
     */
    public CouponUpdateRequest toCouponUpdateRequest(CouponRegistUpdateModel couponRegistUpdateModel,
                                                     String administratorId) {

        CouponUpdateRequest couponUpdateRequest = new CouponUpdateRequest();

        couponUpdateRequest.setPreUpdateCoupon(
                        toCouponRequest(couponRegistUpdateModel.getPreUpdateCoupon(), administratorId));
        couponUpdateRequest.setPostUpdateCoupon(
                        toCouponRequest(couponRegistUpdateModel.getPostUpdateCoupon(), administratorId));

        return couponUpdateRequest;
    }

    /**
     * クーポンリクエストに変換
     *
     * @param couponEntity クーポンエンティティクラス
     * @param administratorId        管理者ID
     * @return クーポンリクエスト
     */
    public CouponRequest toCouponRequest(CouponEntity couponEntity, String administratorId) {

        if (couponEntity == null) {
            return null;
        }

        CouponRequest couponRequest = new CouponRequest();

        couponRequest.setCouponSeq(couponEntity.getCouponSeq());
        couponRequest.setCouponVersionNo(couponEntity.getCouponVersionNo());
        couponRequest.setCouponId(couponEntity.getCouponId());
        couponRequest.setCouponName(couponEntity.getCouponName());
        couponRequest.setCouponDisplayName(couponEntity.getCouponDisplayNamePC());
        couponRequest.setCouponCode(couponEntity.getCouponCode());
        couponRequest.setCouponStartTime(couponEntity.getCouponStartTime());
        couponRequest.setCouponEndTime(couponEntity.getCouponEndTime());
        couponRequest.setDiscountType(couponEntity.getDiscountDetailsType().getValue());
        couponRequest.setDiscountRate(couponEntity.getDiscountRate());
        couponRequest.setDiscountPrice(couponEntity.getDiscountPrice());
        couponRequest.setDiscountLowerOrderPrice(couponEntity.getDiscountLowerOrderPrice());
        couponRequest.setUseCountLimit(couponEntity.getUseCountLimit());
        couponRequest.setTargetGoodsType(couponEntity.getTargetGoodsType());
        couponRequest.setTargetGoods(couponEntity.getTargetGoods());
        couponRequest.setTargetMembersType(couponEntity.getTargetMembersType());
        couponRequest.setTargetMembers(couponEntity.getTargetMembers());
        couponRequest.setMemo(couponEntity.getMemo());
        couponRequest.setAdministratorId(administratorId);
        couponRequest.setRegistTime(couponEntity.getRegistTime());

        return couponRequest;
    }

    /**
     * クーポン登録更新チェックリクエストに変換
     *
     * @param couponRegistUpdateModel 登録更新画面ページ
     * @param administratorId        管理者ID
     * @return クーポン登録更新チェックリクエスト
     */
    public CouponCheckRequest toCouponCheckRequest(CouponRegistUpdateModel couponRegistUpdateModel,
                                                   String administratorId) {

        CouponCheckRequest couponCheckRequest = new CouponCheckRequest();

        couponCheckRequest.setPreUpdateCoupon(
                        toCouponRequest(couponRegistUpdateModel.getPreUpdateCoupon(), administratorId));
        couponCheckRequest.setPostUpdateCoupon(
                        toCouponRequest(couponRegistUpdateModel.getPostUpdateCoupon(), administratorId));

        return couponCheckRequest;
    }
}