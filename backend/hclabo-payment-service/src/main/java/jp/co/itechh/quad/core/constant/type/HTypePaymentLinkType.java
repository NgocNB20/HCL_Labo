package jp.co.itechh.quad.core.constant.type;

import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.seasar.doma.Domain;

/**
 * リンク決済タイプ
 *
 * @author PHAM QUANG DIEU (VJP)
 */
@Domain(valueType = String.class, factoryMethod = "of")
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
