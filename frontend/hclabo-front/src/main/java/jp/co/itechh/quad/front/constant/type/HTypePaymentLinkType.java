package jp.co.itechh.quad.front.constant.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 支払いリンクの種類
 *
 * @author PHAM QUANG DIEU (VJP)
 *
 */
@Getter
@AllArgsConstructor
public enum HTypePaymentLinkType implements EnumType {

    /** 即時払い */
    IMMEDIATEPAYMENT("即時払い", "0"),

    /** 後日払い */
    LATERDATEPAYMENT("後日払い", "1");

    /**
     * ラベル<br/>
     */
    private String label;

    /**
     * 区分値<br/>
     */
    private String value;
}