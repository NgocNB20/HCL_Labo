package jp.co.itechh.quad.ddd.infrastructure.priceplanning.adapter;

import jp.co.itechh.quad.coupon.presentation.api.param.CouponResponse;
import jp.co.itechh.quad.ddd.domain.priceplanning.adapter.model.Coupon;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

/**
 * クーポンアダプタHelperクラス。
 */
@Component
public class CouponAdapterHelper {

    /**
     * クーポンに変換
     *
     * @param response クーポンレスポンス
     * @return クーポン
     */
    public Coupon toCoupon(CouponResponse response) {
        if (ObjectUtils.isEmpty(response)) {
            return null;
        }

        Coupon coupon = new Coupon();

        coupon.setCouponSeq(response.getCouponSeq());
        coupon.setCouponId(response.getCouponId());
        coupon.setCouponName(response.getCouponName());

        return coupon;
    }
}
