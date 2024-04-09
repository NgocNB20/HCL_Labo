package jp.co.itechh.quad.ddd.domain.order.adapter.model;

import lombok.Data;

/**
 * 受注レスポンス
 */
@Data
public class OrderReceived {

    private String orderCode;

    private Boolean shippedFlag;

}
