package jp.co.itechh.quad.front.constant.type;

import jp.co.itechh.quad.front.util.common.EnumTypeUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HTypeGoodsSaleStatusWithNoDeleted implements EnumType {
    /**
     *  非販売
     */
    NO_SALE("非販売", "0"),

    /**
     *  販売中
     */
    SALE("販売中", "1");

    /** doma用ファクトリメソッド */
    public static HTypeGoodsSaleStatusWithNoDeleted of(String value) {

        HTypeGoodsSaleStatusWithNoDeleted hType =
                        EnumTypeUtil.getEnumFromValue(HTypeGoodsSaleStatusWithNoDeleted.class, value);

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