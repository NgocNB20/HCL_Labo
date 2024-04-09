package jp.co.itechh.quad.ddd.domain.priceplanning.adapter;

import jp.co.itechh.quad.ddd.domain.priceplanning.adapter.model.Coupon;

/**
 * 販売企画アダプター
 */
public interface ICouponAdapter {

    /**
     * クーポン情報取得
     *
     * @param couponSeq       クーポンSEQ
     * @param couponVersionNo クーポン枝番
     * @return Coupon
     */
    Coupon getByCouponVersionNo(Integer couponSeq, Integer couponVersionNo);
}
