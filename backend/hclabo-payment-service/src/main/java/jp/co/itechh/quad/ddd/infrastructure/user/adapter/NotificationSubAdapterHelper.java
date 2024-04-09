package jp.co.itechh.quad.ddd.infrastructure.user.adapter;

import jp.co.itechh.quad.ddd.domain.billing.entity.TargetSalesOrderPaymentDto;
import jp.co.itechh.quad.notificationsub.presentation.api.param.LinkpayCancelReminderRequest;
import jp.co.itechh.quad.notificationsub.presentation.api.param.TargetOrder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 通知アダプタHelperクラス
 */
@Component
public class NotificationSubAdapterHelper {

    /**
     * GMO決済キャンセル漏れメール送信要求リクエストに変換.
     *
     * @param orderPaymentReminder
     * @return GMO決済キャンセル漏れメール送信要求リクエスト
     */
    public LinkpayCancelReminderRequest toLinkpayCancelReminderRequest(List<TargetSalesOrderPaymentDto> orderPaymentReminder) {

        List<TargetOrder> targetOrderList = orderPaymentReminder.stream().map(orderPayment -> {
            TargetOrder targetOrder = new TargetOrder();
            targetOrder.setOrderCode(orderPayment.getOrderCode());
            targetOrder.setCancelLimit(orderPayment.getCancelLimit());

            return targetOrder;
        }).collect(Collectors.toList());

        LinkpayCancelReminderRequest request = new LinkpayCancelReminderRequest();
        request.setTargetOrderList(targetOrderList);

        return request;
    }
}