package jp.co.itechh.quad.hclabo.ddd.domain.order.adapter.model;

import lombok.Data;

/**
 * 受注レスポンス
 */
@Data
public class OrderReceived {

    /** 受注番号 */
    private String orderCode;

    /**  受注状態 */
    private String orderStatus;

    /** 受注ID */
    private String orderReceivedId;

    /** 顧客ID */
    private String customerId;

}
