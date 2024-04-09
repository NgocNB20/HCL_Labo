package jp.co.itechh.quad.ddd.domain.payment.adapter.model;

import lombok.Data;

/**
 * 決済方法
 */
@Data
public class PaymentMethod {

    /**
     * 決済方法
     */
    private String settlementMethodName;

    /**
     * 請求種別
     */
    private String billType;

    /**
     * 決済方法ID
     */
    private Integer paymentMethodId;

}

