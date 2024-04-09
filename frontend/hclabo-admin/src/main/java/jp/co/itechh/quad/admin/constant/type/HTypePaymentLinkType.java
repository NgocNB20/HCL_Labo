package jp.co.itechh.quad.admin.constant.type;

import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
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

    /** doma用ファクトリメソッド */
    public static HTypePaymentLinkType of(String value) {

        HTypePaymentLinkType hType = EnumTypeUtil.getEnumFromValue(HTypePaymentLinkType.class, value);

        if (hType == null) {
            throw new IllegalArgumentException(value);
        } else {
            return hType;
        }
    }

    /**
     * ラベル<br/>
     */
    private String label;

    /**
     * 区分値<br/>
     */
    private String value;
}