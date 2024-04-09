package jp.co.itechh.quad.ddd.domain.payment.adapter.model;

import lombok.Data;

/**
 * クレジットカード決済
 */
@Data
public class CreditPayment {

    /** 決済トークン */
    private String paymentToken;

    /** 有効期限(月) */
    private String expirationMonth;

    /** 有効期限(年) */
    private String expirationYear;

    /** 支払区分（1：一括, 2：分割, 5：リボ） */
    private String paymentType;

    /** 分割回数 */
    private String dividedNumber;

    /** カード保存フラグ（保存時true） */
    private boolean registCardFlag;

    /** 登録済カード使用フラグ（登録済みtrue） */
    private boolean useRegistedCardFlag;

    /** 登録済みカード番号のマスク値 */
    private String registedCardMaskNo;

}
