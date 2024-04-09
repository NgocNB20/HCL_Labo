package jp.co.itechh.quad.ddd.domain.payment.adapter.model;

import lombok.Data;

/**
 * 請求伝票
 */
@Data
public class BillingSlip {

    /** 請求伝票ID */
    private String billingSlipId;

    /** 請求先住所ID */
    private String billingAddressId;

    /** 注文決済ID */
    private String orderPaymentId;

    /** 決済方法ID */
    private String paymentMethodId;

    /** クレジットカード決済 */
    private CreditPayment creditPayment;
}