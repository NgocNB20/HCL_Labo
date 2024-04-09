package jp.co.itechh.quad.ddd.domain.payment.adapter.model;

import lombok.Data;

import java.util.Date;

/**
 * 請求伝票
 */
@Data
public class BillingSlip {

    /**
     * 請求先住所ID
     */
    private String billingAddressId;

    /**
     * 決済方法ID
     */
    private Integer paymentMethodId;

    /**
     * 決済方法名
     */
    private String paymentMethodName;

    /**
     * リンク決済：決済方法（GMO）
     */
    private String payType;

    /**
     * リンク決済手段名
     */
    private String linkPaymentMethodName;

    /**
     * リンク決済：決済手段識別子
     */
    private String linkPayMethod;

    /**
     * 入金日時
     */
    private Date moneyReceiptTime;

    /**
     * GMO後日払い支払期限日時
     */
    private Date laterDateLimit;
}
