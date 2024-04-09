/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.shop.coupon;

import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.ConversionUtility;
import jp.co.itechh.quad.admin.base.utility.DateUtility;
import jp.co.itechh.quad.admin.dto.shop.coupon.CouponSearchForDaoConditionDto;
import jp.co.itechh.quad.coupon.presentation.api.param.CouponListGetRequest;
import jp.co.itechh.quad.coupon.presentation.api.param.CouponListResponse;
import jp.co.itechh.quad.coupon.presentation.api.param.CouponResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * クーポン検索画面用Helperクラス。
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class CouponHelper {

    /** 変換ユーティリティクラス */
    private final ConversionUtility conversionUtility;

    /** 日付関連ユーティリティクラス */
    private final DateUtility dateUtility;

    /**
     * コンストラクタ
     *
     * @param conversionUtility
     * @param dateUtility
     */
    @Autowired
    public CouponHelper(ConversionUtility conversionUtility, DateUtility dateUtility) {

        this.conversionUtility = conversionUtility;
        this.dateUtility = dateUtility;
    }

    /**
     * 検索条件の再表示。<br />
     * <pre>
     * 再検索時に検索項目を検索条件入力欄にセットする。
     * </pre>
     *
     * @param couponConditionDto クーポン検索条件
     * @param couponModel クーポン検索画面ページ
     */
    public void toPageForLoad(CouponSearchForDaoConditionDto couponConditionDto, CouponModel couponModel) {

        // クーポン名
        couponModel.setSearchCouponName(couponConditionDto.getCouponName());
        // クーポンID
        couponModel.setSearchCouponId(couponConditionDto.getCouponId());
        // クーポンコード
        couponModel.setSearchCouponCode(couponConditionDto.getCouponCode());
        // クーポン開始日-From
        couponModel.setSearchCouponStartTimeFrom(conversionUtility.toYmd(couponConditionDto.getCouponStartTimeFrom()));
        // クーポン開始日-To
        couponModel.setSearchCouponStartTimeTo(conversionUtility.toYmd(couponConditionDto.getCouponStartTimeTo()));
        // クーポン終了日-From
        couponModel.setSearchCouponEndTimeFrom(conversionUtility.toYmd(couponConditionDto.getCouponEndTimeFrom()));
        // クーポン終了日-To
        couponModel.setSearchCouponEndTimeTo(conversionUtility.toYmd(couponConditionDto.getCouponEndTimeTo()));
        // 対象商品コード
        couponModel.setSearchTargetGoodsCode(couponConditionDto.getTargetGoodsCode());

    }

    /**
     * クーポン検索結果表示。<br />
     * <pre>
     * 検索結果DTOをページにセットする。
     * </pre>
     *
     * @param couponListResponse 検索結果
     * @param couponModel クーポン検索画面ページ
     * @param conditionDto 検索条件DTO
     */
    public void toPageForSearch(CouponListResponse couponListResponse,
                                CouponModel couponModel,
                                CouponSearchForDaoConditionDto conditionDto) {
        int index = couponModel.getPageInfo().getOffset() + 1;
        List<CouponModelItem> resultItemList = new ArrayList<>();

        if (CollectionUtils.isEmpty(couponListResponse.getCouponList())) {
            couponModel.setResultItems(resultItemList);
            return;
        }

        for (CouponResponse coupon : couponListResponse.getCouponList()) {

            CouponModelItem couponModelItem = ApplicationContextUtility.getBean(CouponModelItem.class);
            // クーポンSEQ
            couponModelItem.setCouponSeq(coupon.getCouponSeq());
            // No
            couponModelItem.setResultNo(index++);
            // クーポン名
            couponModelItem.setCouponName(coupon.getCouponName());
            // クーポンID
            couponModelItem.setCouponId(coupon.getCouponId());
            // クーポンコード
            couponModelItem.setCouponCode(coupon.getCouponCode());
            // 割引率
            couponModelItem.setDiscountRate(conversionUtility.toString(coupon.getDiscountRate()));
            // 割引金額
            couponModelItem.setCouponDiscountPrice(coupon.getDiscountPrice());
            // 開催開始日時
            couponModelItem.setCouponStartTime(conversionUtility.toTimestamp(coupon.getCouponStartTime()));
            // 開催終了日時
            couponModelItem.setCouponEndTime(conversionUtility.toTimestamp(coupon.getCouponEndTime()));

            // 現在開催中かをチェックする
            couponModelItem.setCouponStatusClass(getCouponStatusClass(coupon));

            resultItemList.add(couponModelItem);
        }

        couponModel.setResultItems(resultItemList);
    }

    /**
     * クーポン検索用条件保持。<br />
     * <pre>
     * 画面に入力された検索条件をDTOに変換する。
     * </pre>
     *
     * @param couponModel クーポン検索画面ページ
     * @return couponConditionDto クーポン検索条件
     */
    public CouponSearchForDaoConditionDto toCouponConditionDtoForSearch(CouponModel couponModel) {

        CouponSearchForDaoConditionDto couponConditionDto =
                        ApplicationContextUtility.getBean(CouponSearchForDaoConditionDto.class);

        // クーポン名
        couponConditionDto.setCouponName(couponModel.getSearchCouponName());
        // クーポンID
        couponConditionDto.setCouponId(couponModel.getSearchCouponId());
        // クーポンコード
        couponConditionDto.setCouponCode(couponModel.getSearchCouponCode());
        // クーポン開始日-From
        couponConditionDto.setCouponStartTimeFrom(
                        conversionUtility.toTimeStampWithDefaultHms(couponModel.getSearchCouponStartTimeFrom(), null,
                                                                    ConversionUtility.DEFAULT_START_TIME
                                                                   ));
        // クーポン開始日-To
        couponConditionDto.setCouponStartTimeTo(
                        conversionUtility.toTimeStampWithDefaultHms(couponModel.getSearchCouponStartTimeTo(), null,
                                                                    ConversionUtility.DEFAULT_END_TIME
                                                                   ));
        // クーポン終了日-From
        couponConditionDto.setCouponEndTimeFrom(
                        conversionUtility.toTimeStampWithDefaultHms(couponModel.getSearchCouponEndTimeFrom(), null,
                                                                    ConversionUtility.DEFAULT_START_TIME
                                                                   ));
        // クーポン-To
        couponConditionDto.setCouponEndTimeTo(
                        conversionUtility.toTimeStampWithDefaultHms(couponModel.getSearchCouponEndTimeTo(), null,
                                                                    ConversionUtility.DEFAULT_END_TIME
                                                                   ));
        // 対象商品コード
        couponConditionDto.setTargetGoodsCode(couponModel.getSearchTargetGoodsCode());

        return couponConditionDto;
    }

    /**
     *
     * クーポン状態を返す<br/>
     *
     * @param couponResponse クーポン情報
     * @return 利用終了「0」、利用期間中「1」、利用終了「2」
     */
    protected String getCouponStatusClass(CouponResponse couponResponse) {

        // クーポン開始・終了日時
        Timestamp startTime = conversionUtility.toTimestamp(couponResponse.getCouponStartTime());
        Timestamp endTime = conversionUtility.toTimestamp(couponResponse.getCouponEndTime());
        // 現在日時
        Timestamp currentTime = dateUtility.getCurrentTime();

        if (endTime != null && endTime.compareTo(currentTime) < 0) {
            // 終了日時 ＜ 現在日時 の場合 「終了」
            return CouponModel.COUPON_STATUS_PAST;
        } else if (startTime != null && startTime.compareTo(currentTime) <= 0 && currentTime.compareTo(endTime) <= 0) {
            // 開始日時 ＜＝ 現在日時 ＜＝ 終了日時 の場合 「利用期間中」
            return CouponModel.COUPON_STATUS_OPEN;
        } else if (currentTime.compareTo(startTime) <= 0) {
            // 現在日時 ＜＝ 開始日時 の場合 「開始前」
            return CouponModel.COUPON_STATUS_FUTURE;
        }
        return "";
    }

    /**
     * クーポンリクエストに変換
     *
     * @param couponModel
     * @return クーポンリクエスト
     */
    public CouponListGetRequest toCouponListGetRequest(CouponModel couponModel) {

        CouponListGetRequest couponListGetRequest = new CouponListGetRequest();

        couponListGetRequest.setCouponName(couponModel.getSearchCouponName());
        couponListGetRequest.setCouponId(couponModel.getSearchCouponId());
        couponListGetRequest.setCouponCode(couponModel.getSearchCouponCode());
        couponListGetRequest.setCouponStartTimeFrom(
                        conversionUtility.toDate(couponModel.getSearchCouponStartTimeFrom()));
        couponListGetRequest.setCouponStartTimeTo(conversionUtility.toDate(couponModel.getSearchCouponStartTimeTo()));
        couponListGetRequest.setCouponEndTimeFrom(conversionUtility.toDate(couponModel.getSearchCouponEndTimeFrom()));
        couponListGetRequest.setCouponEndTimeTo(conversionUtility.toDate(couponModel.getSearchCouponEndTimeTo()));
        couponListGetRequest.setTargetGoodsCode(couponModel.getSearchTargetGoodsCode());

        return couponListGetRequest;
    }
}