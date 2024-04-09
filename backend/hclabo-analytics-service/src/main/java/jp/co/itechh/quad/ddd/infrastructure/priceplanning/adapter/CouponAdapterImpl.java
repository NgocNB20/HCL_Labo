package jp.co.itechh.quad.ddd.infrastructure.priceplanning.adapter;

import jp.co.itechh.quad.core.web.HeaderParamsUtility;
import jp.co.itechh.quad.coupon.presentation.api.CouponApi;
import jp.co.itechh.quad.coupon.presentation.api.param.CouponResponse;
import jp.co.itechh.quad.coupon.presentation.api.param.CouponVersionNoRequest;
import jp.co.itechh.quad.ddd.domain.priceplanning.adapter.ICouponAdapter;
import jp.co.itechh.quad.ddd.domain.priceplanning.adapter.model.Coupon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * クーポンアダプター実装クラス
 */
@Component
public class CouponAdapterImpl implements ICouponAdapter {

    /**
     * クーポンAPI
     */
    private final CouponApi couponApi;

    /**
     * クーポンアダプタHelperクラス
     */
    private final CouponAdapterHelper couponAdapterHelper;

    /**
     * コンストラクタ
     *
     * @param couponApi           クーポンAPI
     * @param couponAdapterHelper クーポンアダプタHelperクラス
     * @param headerParamsUtil    ヘッダパラメーターユーティル
     */
    @Autowired
    public CouponAdapterImpl(CouponApi couponApi,
                             CouponAdapterHelper couponAdapterHelper,
                             HeaderParamsUtility headerParamsUtil) {
        this.couponApi = couponApi;
        this.couponAdapterHelper = couponAdapterHelper;

        // サービス間のAPI呼び出す時、ヘッダーにパラメーターを設定する
        headerParamsUtil.setHeader(this.couponApi.getApiClient());
    }

    /**
     * クーポン取得
     *
     * @param couponSeq       クーポンSEQ
     * @param couponVersionNo クーポン枝番
     * @return クーポン
     */
    @Override
    public Coupon getByCouponVersionNo(Integer couponSeq, Integer couponVersionNo) {

        CouponVersionNoRequest request = new CouponVersionNoRequest();

        request.setCouponSeq(couponSeq);
        request.setCouponVersionNo(couponVersionNo);

        CouponResponse response = couponApi.getByCouponVersionNo(request);

        return couponAdapterHelper.toCoupon(response);
    }

}