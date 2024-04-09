package jp.co.itechh.quad.ddd.domain.order.adapter;

import jp.co.itechh.quad.ddd.domain.order.adapter.model.OrderReceived;
import jp.co.itechh.quad.ddd.domain.order.adapter.model.OrderReceivedCount;

/**
 * 受注アダプター
 */
public interface IOrderReceivedAdapter {

    /**
     * 受注
     *
     * @param orderReceivedId 受注ID
     * @return OrderReceived
     */
    OrderReceived getByOrderReceivedId(String orderReceivedId);

    /**
     * 受注
     *
     * @param transactionId 受注ID
     * @return 受注
     */
    OrderReceived getByTransactionId(String transactionId);

    /**
     * 受注件数取得
     *
     * @param customerId 顧客ID
     * @return 受注件数
     */
    OrderReceivedCount getOrderReceivedCount(String customerId);
}
