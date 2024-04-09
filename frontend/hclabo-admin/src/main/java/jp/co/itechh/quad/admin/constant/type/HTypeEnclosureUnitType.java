package jp.co.itechh.quad.admin.constant.type;

import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HTypeEnclosureUnitType implements EnumType {

    /** 0:受注単位 */
    ORDER("ORDER", "0"),

    /** 1:受注商品単位 */
    ORDER_GOODS("ORDER_GOODS", "1");

    /**
     * 隠蔽コンストラクタ<br/>
     * 区分値の設定
     *
     * @param value String
     */
    public static HTypeEnclosureUnitType of(String value) {
        HTypeEnclosureUnitType hType = EnumTypeUtil.getEnumFromValue(HTypeEnclosureUnitType.class, value);

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
