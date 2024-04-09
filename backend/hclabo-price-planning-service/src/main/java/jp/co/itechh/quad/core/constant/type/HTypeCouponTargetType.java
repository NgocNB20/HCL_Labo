package jp.co.itechh.quad.core.constant.type;

import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.seasar.doma.Domain;

@Domain(valueType = String.class, factoryMethod = "of")
@Getter
@AllArgsConstructor
public enum HTypeCouponTargetType implements EnumType {

    /** 除外指定 */
    EXCLUDE_TARGET("除外指定", "0"),

    /** 絞込指定 */
    INCLUDE_TARGET("絞込指定", "1");

    /** doma用ファクトリメソッド */
    public static HTypeCouponTargetType of(String value) {

        HTypeCouponTargetType hType = EnumTypeUtil.getEnumFromValue(HTypeCouponTargetType.class, value);

        if (hType == null) {
            throw new IllegalArgumentException(value);
        } else {
            return hType;
        }
    }

    /** ラベル */
    private final String label;

    /** 区分値 */
    private final String value;
}
