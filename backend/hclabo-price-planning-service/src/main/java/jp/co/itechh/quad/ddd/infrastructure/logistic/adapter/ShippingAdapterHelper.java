package jp.co.itechh.quad.ddd.infrastructure.logistic.adapter;

import jp.co.itechh.quad.ddd.domain.logistic.adapter.model.ShippingSlip;
import jp.co.itechh.quad.ddd.domain.logistic.adapter.model.ShippingSlipForRevision;
import jp.co.itechh.quad.shippingslip.presentation.api.param.ShippingSlipResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

/**
 * 配送アダプターHelperクラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class ShippingAdapterHelper {

    /**
     * 配送伝票に変換
     *
     * @param shippingSlipResponse 配送伝票レスポンス
     * @return 配送伝票
     */
    public ShippingSlip toShippingSlip(ShippingSlipResponse shippingSlipResponse) {

        if (ObjectUtils.isEmpty(shippingSlipResponse)) {
            return null;
        }

        ShippingSlip shippingSlip = new ShippingSlip();
        shippingSlip.setShippingSlipId(shippingSlipResponse.getShippingSlipId());
        shippingSlip.setShippingAddressId(shippingSlipResponse.getShippingAddressId());
        shippingSlip.setShippingMethodId(shippingSlipResponse.getShippingMethodId());
        shippingSlip.setShippingMethodName(shippingSlipResponse.getShippingMethodName());
        shippingSlip.setReceiverTimeZone(shippingSlipResponse.getReceiverTimeZone());
        shippingSlip.setReceiverDate(shippingSlipResponse.getReceiverDate());
        return shippingSlip;
    }

    /**
     * 配送伝票レ改訂用に変換
     *
     * @param shippingSlipResponse 配送伝票レ改訂用レスポンス
     * @return 配送伝票レ改訂用
     */
    public ShippingSlipForRevision toShippingSlipForRevision(ShippingSlipResponse shippingSlipResponse) {

        if (ObjectUtils.isEmpty(shippingSlipResponse)) {
            return null;
        }

        ShippingSlipForRevision shippingSlipForRevision = new ShippingSlipForRevision();
        shippingSlipForRevision.setShippingSlipId(shippingSlipResponse.getShippingSlipId());
        shippingSlipForRevision.setShippingAddressId(shippingSlipResponse.getShippingAddressId());
        shippingSlipForRevision.setShippingMethodId(shippingSlipResponse.getShippingMethodId());
        shippingSlipForRevision.setShippingMethodName(shippingSlipResponse.getShippingMethodName());
        shippingSlipForRevision.setReceiverTimeZone(shippingSlipResponse.getReceiverTimeZone());
        shippingSlipForRevision.setReceiverDate(shippingSlipResponse.getReceiverDate());

        return shippingSlipForRevision;
    }
}
