package jp.co.itechh.quad.ddd.infrastructure.order.adapter;

import jp.co.itechh.quad.ddd.domain.order.adapter.model.OrderReceived;
import jp.co.itechh.quad.ddd.domain.order.adapter.model.OrderReceivedCount;
import jp.co.itechh.quad.orderreceived.presentation.api.param.OrderReceivedCountResponse;
import jp.co.itechh.quad.orderreceived.presentation.api.param.OrderReceivedResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

/**
 * 受注アダプターHelperクラス
 */
@Component
public class OrderReceivedAdapterHelper {

    /**
     * 受注に変換.
     *
     * @param orderReceivedResponse 受注取得レスポンス
     * @return 受注取得
     */
    public OrderReceived toOrderReceived(OrderReceivedResponse orderReceivedResponse) {
        if (ObjectUtils.isEmpty(orderReceivedResponse)) {
            return null;
        }

        OrderReceived orderReceived = new OrderReceived();

        orderReceived.setOrderCode(orderReceivedResponse.getOrderCode());
        orderReceived.setLatestTransactionId(orderReceivedResponse.getLatestTransactionId());
        orderReceived.setOrderReceivedDate(orderReceivedResponse.getOrderReceivedDate());
        orderReceived.setCancelDate(orderReceivedResponse.getCancelDate());
        orderReceived.setOrderStatus(orderReceivedResponse.getOrderStatus());
        orderReceived.setTransactionStatus(orderReceivedResponse.getTransactionStatus());
        orderReceived.setPaidFlag(orderReceivedResponse.getPaidFlag());
        orderReceived.setPaymentStatusDetail(orderReceivedResponse.getPaymentStatusDetail());

        orderReceived.setShippedFlag(orderReceivedResponse.getShippedFlag());
        orderReceived.setBillPaymentErrorFlag(orderReceivedResponse.getBillPaymentErrorFlag());
        orderReceived.setNotificationFlag(orderReceivedResponse.getNotificationFlag());
        orderReceived.setReminderSentFlag(orderReceivedResponse.getReminderSentFlag());
        orderReceived.setExpiredSentFlag(orderReceivedResponse.getExpiredSentFlag());
        orderReceived.setAdminMemo(orderReceivedResponse.getAdminMemo());

        if (orderReceivedResponse.getCustomerId() != null) {
            orderReceived.setCustomerId(Integer.parseInt(orderReceivedResponse.getCustomerId()));
        }

        orderReceived.setProcessTime(orderReceivedResponse.getProcessTime());
        orderReceived.setProcessType(orderReceivedResponse.getProcessType());
        orderReceived.setProcessPersonName(orderReceivedResponse.getProcessPersonName());
        orderReceived.setNoveltyPresentJudgmentStatus(orderReceivedResponse.getNoveltyPresentJudgmentStatus());

        return orderReceived;
    }

    /**
     * 受注件数取得に変換.
     *
     * @param orderReceivedCountResponse 受注件数レスポンス
     * @return 受注件数
     */
    public OrderReceivedCount toOrderReceivedCount(OrderReceivedCountResponse orderReceivedCountResponse) {
        if (ObjectUtils.isEmpty(orderReceivedCountResponse)) {
            return null;
        }

        OrderReceivedCount orderReceivedCount = new OrderReceivedCount();
        orderReceivedCount.setOrderReceivedCount(orderReceivedCountResponse.getOrderReceivedCount());

        return orderReceivedCount;
    }
}