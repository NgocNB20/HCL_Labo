package jp.co.itechh.quad.coupon.presentation.api;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.constant.type.HTypeCouponTargetType;
import jp.co.itechh.quad.core.constant.type.HTypeDiscountType;
import jp.co.itechh.quad.core.dto.shop.coupon.CouponSearchForDaoConditionDto;
import jp.co.itechh.quad.core.entity.shop.coupon.CouponEntity;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.coupon.presentation.api.param.CouponCodeResponse;
import jp.co.itechh.quad.coupon.presentation.api.param.CouponListGetRequest;
import jp.co.itechh.quad.coupon.presentation.api.param.CouponListResponse;
import jp.co.itechh.quad.coupon.presentation.api.param.CouponRegistRequest;
import jp.co.itechh.quad.coupon.presentation.api.param.CouponRequest;
import jp.co.itechh.quad.coupon.presentation.api.param.CouponResponse;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * クーポン検索画面用Helperクラス。
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class CouponHelper {

    /** 日付関連Helper取得 */
    private final DateUtility dateUtility;

    /**
     * コンストラクタ
     *
     * @param dateUtility          日付関連Helper取得
     */
    public CouponHelper(DateUtility dateUtility) {
        this.dateUtility = dateUtility;
    }

    /**
     * クーポンレスポンスに変換
     *
     * @param couponEntity クーポン情報
     * @return couponResponse クーポンレスポンス
     */
    public CouponResponse toCouponResponse(CouponEntity couponEntity) {
        CouponResponse couponResponse = new CouponResponse();
        // クーポンSEQ
        couponResponse.setCouponSeq(couponEntity.getCouponSeq());
        couponResponse.setCouponVersionNo(couponEntity.getCouponVersionNo());
        // クーポンID
        couponResponse.setCouponId(couponEntity.getCouponId());
        // クーポン名
        couponResponse.setCouponName(couponEntity.getCouponName());
        // クーポン表示名PC
        couponResponse.setCouponDisplayName(couponEntity.getCouponDisplayNamePC());
        // クーポンコード
        couponResponse.setCouponCode(couponEntity.getCouponCode());
        // 開催開始日時
        couponResponse.setCouponStartTime(couponEntity.getCouponStartTime());
        // 開催終了日時
        couponResponse.setCouponEndTime(couponEntity.getCouponEndTime());
        couponResponse.setTargetGoodsType(couponEntity.getTargetGoodsType().getValue());
        couponResponse.setTargetGoods(couponEntity.getTargetGoods());
        // 対象会員
        couponResponse.setTargetMembersType(couponEntity.getTargetMembersType().getValue());
        couponResponse.setTargetMembers(couponEntity.getTargetMembers());
        couponResponse.setMemo(couponEntity.getMemo());
        couponResponse.setAdministratorId(couponEntity.getAdministratorId());

        couponResponse.setDiscountType(couponEntity.getDiscountType().getValue());
        couponResponse.setDiscountRate(couponEntity.getDiscountRate());
        // 割引金額
        couponResponse.setDiscountPrice(couponEntity.getDiscountPrice());
        // 適用金額
        couponResponse.setDiscountLowerOrderPrice(couponEntity.getDiscountLowerOrderPrice());
        couponResponse.setUseCountLimit(couponEntity.getUseCountLimit().doubleValue());

        couponResponse.setUpdateTime(couponEntity.getUpdateTime());
        couponResponse.setRegistTime(couponEntity.getRegistTime());
        return couponResponse;
    }

    /**
     * クーポン検索用条件保持。<br />
     * <pre>
     * 画面に入力された検索条件をDTOに変換する。
     * </pre>
     *
     * @param couponListGetRequest クーポン一覧リクエスト
     * @return couponConditionDto クーポン検索条件
     */
    public CouponSearchForDaoConditionDto toCouponConditionDtoForSearch(CouponListGetRequest couponListGetRequest) {
        CouponSearchForDaoConditionDto couponConditionDto =
                        ApplicationContextUtility.getBean(CouponSearchForDaoConditionDto.class);
        // クーポン名
        couponConditionDto.setCouponName(couponListGetRequest.getCouponName());
        // クーポンID
        couponConditionDto.setCouponId(couponListGetRequest.getCouponId());
        // クーポンコード
        couponConditionDto.setCouponCode(couponListGetRequest.getCouponCode());
        // クーポン開始日-From
        couponConditionDto.setCouponStartTimeFrom(
                        dateUtility.convertDateToTimestamp(couponListGetRequest.getCouponStartTimeFrom()));
        // クーポン開始日-To
        couponConditionDto.setCouponStartTimeTo(
                        dateUtility.convertDateToTimestamp(couponListGetRequest.getCouponStartTimeTo()));
        // クーポン終了日-From
        couponConditionDto.setCouponEndTimeFrom(
                        dateUtility.convertDateToTimestamp(couponListGetRequest.getCouponEndTimeFrom()));
        // クーポン-To
        couponConditionDto.setCouponEndTimeTo(
                        dateUtility.convertDateToTimestamp(couponListGetRequest.getCouponEndTimeTo()));
        // 対象商品コード
        couponConditionDto.setTargetGoodsCode(couponListGetRequest.getTargetGoodsCode());
        return couponConditionDto;
    }

    /**
     * クーポン一覧レスポンスに変換
     *
     * @param couponEntities
     * @return couponListResponse  クーポン一覧レスポンス
     */
    public CouponListResponse toCouponListResponse(List<CouponEntity> couponEntities) {
        CouponListResponse couponListResponse = new CouponListResponse();
        for (CouponEntity couponEntity : couponEntities) {
            CouponResponse couponResponse = toCouponResponse(couponEntity);
            couponListResponse.addCouponListItem(couponResponse);
        }
        return couponListResponse;
    }

    /**
     * クーポンコードレスポンスに変換
     *
     * @param couponCode クーポンコード
     * @return couponCodeResponse クーポンコードレスポンス
     */
    public CouponCodeResponse toCouponCodeResponse(String couponCode) {
        CouponCodeResponse couponCodeResponse = new CouponCodeResponse();
        couponCodeResponse.setCouponCode(couponCode);
        return couponCodeResponse;
    }

    /**
     * クーポン登録リクエストに変換
     *
     * @param couponRegistRequest クーポン登録リクエスト
     * @return couponEntity クーポン情報
     */
    public CouponEntity toCouponEntityRegist(CouponRegistRequest couponRegistRequest) {
        CouponEntity couponEntity = new CouponEntity();
        // クーポンID
        couponEntity.setCouponId(couponRegistRequest.getCouponId());
        // クーポン名
        couponEntity.setCouponName(couponRegistRequest.getCouponName());
        // クーポン表示名PC
        couponEntity.setCouponDisplayNamePC(couponRegistRequest.getCouponDisplayNamePC());
        // クーポンコード
        couponEntity.setCouponCode(couponRegistRequest.getCouponCode());
        // 開催開始日時
        couponEntity.setCouponStartTime(dateUtility.convertDateToTimestamp(couponRegistRequest.getCouponStartTime()));
        // 開催終了日時
        couponEntity.setCouponEndTime(dateUtility.convertDateToTimestamp(couponRegistRequest.getCouponEndTime()));
        // 割引種別
        couponEntity.setDiscountType(
                        EnumTypeUtil.getEnumFromValue(HTypeDiscountType.class, couponRegistRequest.getDiscountType()));
        // 割引率
        couponEntity.setDiscountRate(couponRegistRequest.getDiscountRate());
        // 割引金額
        couponEntity.setDiscountPrice(couponRegistRequest.getDiscountPrice());
        // 適用金額
        couponEntity.setDiscountLowerOrderPrice(couponRegistRequest.getDiscountLowerOrderPrice());
        // 利用回数
        couponEntity.setUseCountLimit(couponRegistRequest.getUseCountLimit());
        couponEntity.setTargetGoodsType(EnumTypeUtil.getEnumFromValue(HTypeCouponTargetType.class,
                                                                      couponRegistRequest.getTargetGoodsType()
                                                                     ));
        couponEntity.setTargetGoods(couponRegistRequest.getTargetGoods());
        // 対象会員
        couponEntity.setTargetMembersType(EnumTypeUtil.getEnumFromValue(HTypeCouponTargetType.class,
                                                                        couponRegistRequest.getTargetMembersType()
                                                                       ));
        couponEntity.setTargetMembers(couponRegistRequest.getTargetMembers());
        couponEntity.setMemo(couponRegistRequest.getMemo());
        couponEntity.setAdministratorId(couponRegistRequest.getAdministratorId());
        return couponEntity;
    }

    /**
     * クーポンリクエストに変換
     *
     * @param couponRequest クーポンリクエスト
     * @return couponEntity クーポン情報
     */
    public CouponEntity toCouponEntity(CouponRequest couponRequest) {
        CouponEntity couponEntity = new CouponEntity();
        if (couponRequest != null) {
            Integer shopSeq = 1001;
            couponEntity.setShopSeq(shopSeq);
            // クーポンSEQ
            couponEntity.setCouponSeq(couponRequest.getCouponSeq());
            couponEntity.setCouponVersionNo(couponRequest.getCouponVersionNo());
            // クーポンID
            couponEntity.setCouponId(couponRequest.getCouponId());
            // クーポン名
            couponEntity.setCouponName(couponRequest.getCouponName());
            // クーポン表示名PC
            couponEntity.setCouponDisplayNamePC(couponRequest.getCouponDisplayName());
            // クーポンコード
            couponEntity.setCouponCode(couponRequest.getCouponCode());
            // 開催開始日時
            couponEntity.setCouponStartTime(dateUtility.convertDateToTimestamp(couponRequest.getCouponStartTime()));
            // 開催終了日時
            couponEntity.setCouponEndTime(dateUtility.convertDateToTimestamp(couponRequest.getCouponEndTime()));
            // 割引種別
            couponEntity.setDiscountType(
                            EnumTypeUtil.getEnumFromValue(HTypeDiscountType.class, couponRequest.getDiscountType()));
            // 割引率
            couponEntity.setDiscountRate(couponRequest.getDiscountRate());
            // 割引金額
            couponEntity.setDiscountPrice(couponRequest.getDiscountPrice());
            // 適用金額
            couponEntity.setDiscountLowerOrderPrice(couponRequest.getDiscountLowerOrderPrice());
            // 利用回数
            couponEntity.setUseCountLimit(couponRequest.getUseCountLimit());
            couponEntity.setTargetGoodsType(EnumTypeUtil.getEnumFromValue(HTypeCouponTargetType.class,
                                                                          couponRequest.getTargetGoodsType()
                                                                         ));
            couponEntity.setTargetGoods(couponRequest.getTargetGoods());
            // 対象会員
            couponEntity.setTargetMembersType(EnumTypeUtil.getEnumFromValue(HTypeCouponTargetType.class,
                                                                            couponRequest.getTargetMembersType()
                                                                           ));
            couponEntity.setTargetMembers(couponRequest.getTargetMembers());
            couponEntity.setMemo(couponRequest.getMemo());
            couponEntity.setAdministratorId(couponRequest.getAdministratorId());
            couponEntity.setRegistTime(dateUtility.convertDateToTimestamp(couponRequest.getRegistTime()));
        }
        return couponEntity;
    }

}