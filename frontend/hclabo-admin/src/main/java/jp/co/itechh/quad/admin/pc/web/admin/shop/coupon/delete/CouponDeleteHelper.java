package jp.co.itechh.quad.admin.pc.web.admin.shop.coupon.delete;

import jp.co.itechh.quad.admin.base.utility.ConversionUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeDiscountType;
import jp.co.itechh.quad.admin.entity.shop.coupon.CouponEntity;
import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import jp.co.itechh.quad.coupon.presentation.api.param.CouponResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * クーポン削除HELPERクラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class CouponDeleteHelper {

    /** 変換Utilityクラス */
    private final ConversionUtility conversionUtility;

    /**
     * コンストラクタ
     *
     * @param conversionUtility
     */
    @Autowired
    public CouponDeleteHelper(ConversionUtility conversionUtility) {
        this.conversionUtility = conversionUtility;
    }

    /**
     * 画面初期表示。<br />
     *
     * <pre>
     * クーポン情報を画面に反映する。
     * </pre>
     *
     * @param couponResponse     クーポンレスポンス
     * @param couponModel 削除画面ページ
     */
    public void toPageForLoad(CouponResponse couponResponse, CouponDeleteModel couponModel) {

        CouponEntity coupon = convertToCouponEntity(couponResponse);

        // クーポンID
        couponModel.setCouponId(coupon.getCouponId());
        // クーポン名
        couponModel.setCouponName(coupon.getCouponName());
        // クーポン表示名PC
        couponModel.setCouponDisplayNamePc(coupon.getCouponDisplayNamePC());
        // 開催開始日
        couponModel.setCouponStartDate(coupon.getCouponStartTime());
        // 開催開始時間
        couponModel.setCouponStartTime(coupon.getCouponStartTime());
        // 開催終了日
        couponModel.setCouponEndDate(coupon.getCouponEndTime());
        // 開催終了時間
        couponModel.setCouponEndTime(coupon.getCouponEndTime());
        // クーポンコード
        couponModel.setCouponCode(coupon.getCouponCode());
        // 割引種別
        couponModel.setDiscountDetailsType(coupon.getDiscountDetailsType().getValue());
        // 割引率
        couponModel.setDiscountRate(conversionUtility.toString(coupon.getDiscountRate()));
        // 割引金額
        couponModel.setDiscountPrice(coupon.getDiscountPrice());
        // 適用金額
        couponModel.setDiscountLowerOrderPrice(coupon.getDiscountLowerOrderPrice());
        // 利用回数
        couponModel.setUseCountLimit(conversionUtility.toString(coupon.getUseCountLimit()));
        // 対象商品
        couponModel.setTargetGoodsType(coupon.getTargetGoodsType());
        couponModel.setTargetGoods(coupon.getTargetGoods());
        // 対象会員
        couponModel.setTargetMembersType(coupon.getTargetMembersType());
        couponModel.setTargetMembers(coupon.getTargetMembers());
        // 管理用メモ
        couponModel.setMemo(coupon.getMemo());

        /*
         * 画面項目以外
         */
        // 削除対象クーポン情報
        couponModel.setDeleteCoupon(coupon);
    }

    /**
     * クーポンエンティティに変換
     *
     * @param couponResponse クーポンレスポンス
     * @return クーポンエンティティ
     */
    public CouponEntity convertToCouponEntity(CouponResponse couponResponse) {

        CouponEntity couponEntity = new CouponEntity();

        couponEntity.setCouponSeq(couponResponse.getCouponSeq());
        couponEntity.setCouponVersionNo(couponResponse.getCouponVersionNo());
        couponEntity.setShopSeq(1001);
        couponEntity.setCouponId(couponResponse.getCouponId());
        couponEntity.setCouponName(couponResponse.getCouponName());
        couponEntity.setCouponDisplayNamePC(couponResponse.getCouponDisplayName());
        couponEntity.setCouponCode(couponResponse.getCouponCode());
        couponEntity.setCouponStartTime(conversionUtility.toTimestamp(couponResponse.getCouponStartTime()));
        couponEntity.setCouponEndTime(conversionUtility.toTimestamp(couponResponse.getCouponEndTime()));
        couponEntity.setDiscountDetailsType(
                        EnumTypeUtil.getEnumFromValue(HTypeDiscountType.class, couponResponse.getDiscountType()));
        couponEntity.setDiscountRate(couponResponse.getDiscountRate());
        couponEntity.setDiscountPrice(couponResponse.getDiscountPrice());
        couponEntity.setDiscountLowerOrderPrice(couponResponse.getDiscountLowerOrderPrice());
        couponEntity.setUseCountLimit(conversionUtility.toInteger(couponResponse.getUseCountLimit()));
        couponEntity.setTargetGoodsType(couponResponse.getTargetGoodsType());
        couponEntity.setTargetGoods(couponResponse.getTargetGoods());
        couponEntity.setTargetMembersType(couponResponse.getTargetMembersType());
        couponEntity.setTargetMembers(couponResponse.getTargetMembers());
        couponEntity.setMemo(couponResponse.getMemo());
        couponEntity.setAdministratorId(couponResponse.getAdministratorId());
        couponEntity.setRegistTime(conversionUtility.toTimestamp(couponResponse.getRegistTime()));

        return couponEntity;
    }
}