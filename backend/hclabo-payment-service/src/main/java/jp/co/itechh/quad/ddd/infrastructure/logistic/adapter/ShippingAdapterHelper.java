package jp.co.itechh.quad.ddd.infrastructure.logistic.adapter;

import jp.co.itechh.quad.ddd.domain.logistic.adapter.model.ShippingSlip;
import jp.co.itechh.quad.ddd.domain.logistic.adapter.model.ShippingSlipForRevision;
import jp.co.itechh.quad.shippingslip.presentation.api.param.ShippingSlipResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

/**
 * 配送アダプター実装Helperクラス
 */
@Component
public class ShippingAdapterHelper {

    /**
     * 配送伝票に変換
     *
     * @param response 配送伝票レスポンス
     * @return 配送伝票
     */
    public ShippingSlip toShippingSlip(ShippingSlipResponse response) {

        if (ObjectUtils.isEmpty(response)) {
            return null;
        }

        ShippingSlip shippingSlip = new ShippingSlip();

        shippingSlip.setShippingSlipId(response.getShippingSlipId());
        shippingSlip.setShippingMethodId(response.getShippingMethodId());
        shippingSlip.setShippingMethodName(response.getShippingMethodName());
        shippingSlip.setShippingAddressId(response.getShippingAddressId());
        shippingSlip.setReceiverDate(response.getReceiverDate());
        shippingSlip.setReceiverTimeZone(response.getReceiverTimeZone());

        return shippingSlip;
    }

    /**
     * 改訂用配送伝票に変換
     *
     * @param response 配送伝票レスポンス
     * @return 改訂用配送伝票
     */
    public ShippingSlipForRevision toShippingSlipForRevision(ShippingSlipResponse response) {

        if (ObjectUtils.isEmpty(response)) {
            return null;
        }

        ShippingSlipForRevision shippingSlipForRevision = new ShippingSlipForRevision();

        shippingSlipForRevision.setShippingSlipId(response.getShippingSlipId());
        shippingSlipForRevision.setShippingMethodId(response.getShippingMethodId());
        shippingSlipForRevision.setShippingMethodName(response.getShippingMethodName());
        shippingSlipForRevision.setShippingAddressId(response.getShippingAddressId());
        shippingSlipForRevision.setReceiverDate(response.getReceiverDate());
        shippingSlipForRevision.setReceiverTimeZone(response.getReceiverTimeZone());

        return shippingSlipForRevision;
    }

}
