package jp.co.itechh.quad.ddd.infrastructure.order.adapter;

import jp.co.itechh.quad.ddd.domain.order.adapter.model.OrderReceived;
import jp.co.itechh.quad.orderreceived.presentation.api.param.OrderReceivedResponse;
import org.springframework.stereotype.Component;

/**
 * 受注情報アダプターHelperクラス
 */
@Component
public class OrderReceivedAdapterHelper {

    /**
     * 受注情報に変換
     *
     * @param orderReceivedResponse 受注情報レスポンス
     * @return 受注情報
     */
    public OrderReceived toOrderReceived(OrderReceivedResponse orderReceivedResponse) {

        if (orderReceivedResponse == null) {
            return null;
        }

        OrderReceived orderReceived = new OrderReceived();

        orderReceived.setOrderCode(orderReceivedResponse.getOrderCode());
        orderReceived.setShippedFlag(orderReceivedResponse.getShippedFlag());

        return orderReceived;
    }

}
