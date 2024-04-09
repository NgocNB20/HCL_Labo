package jp.co.itechh.quad.ddd.domain.payment.adapter.model;

import lombok.Data;

/**
 * 改訂用請求伝票
 */
@Data
public class BillingSlipForRevision {

    private String billingSlipRevisionId;

    private String orderPaymentRevisionId;

    private String billingSlipId;

    private String billingAddressId;

    private String orderPaymentId;

    private String paymentMethodId;

    private String paymentToken;

    private String expirationMonth;

    private String expirationYear;

    private String paymentType;

    private String dividedNumber;

    private Boolean registCardFlag;

    private Boolean useRegistedCardFlag;

    private String cardMaskNo;
}
