package jp.co.itechh.quad.ddd.infrastructure.logistic.adapter;

import jp.co.itechh.quad.ddd.domain.logistic.adapter.model.ShippingMethod;
import jp.co.itechh.quad.method.presentation.api.param.ShippingMethodResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

/**
 * 配送方法アダプターHelperクラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class ShippingMethodAdapterHelper {

    /**
     * 配送方法に変換
     *
     * @param response 配送方法レスポンス
     * @return 配送方法
     */
    public ShippingMethod toShippingMethod(ShippingMethodResponse response) {

        if (ObjectUtils.isEmpty(response)) {
            return null;
        }

        ShippingMethod shippingMethod = new ShippingMethod();

        if (!ObjectUtils.isEmpty(response.getDeliveryMethodResponse())) {
            shippingMethod.setDeliveryMethodName(response.getDeliveryMethodResponse().getDeliveryMethodName());
        }

        shippingMethod.setShippingMethodId(response.getDeliveryMethodResponse().getDeliveryMethodSeq());

        return shippingMethod;
    }

}
