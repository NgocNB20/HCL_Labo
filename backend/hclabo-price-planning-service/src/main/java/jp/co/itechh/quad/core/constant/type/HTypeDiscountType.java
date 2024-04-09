/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.core.constant.type;

import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.seasar.doma.Domain;

@Domain(valueType = String.class, factoryMethod = "of")
@Getter
@AllArgsConstructor
public enum HTypeDiscountType implements EnumType {

    /** パーセント割引 */
    PERCENT("パーセント割引", "0"),

    /** 金額割引 */
    AMOUNT("金額割引", "1");

    /** doma用ファクトリメソッド */
    public static HTypeDiscountType of(String value) {

        HTypeDiscountType hType = EnumTypeUtil.getEnumFromValue(HTypeDiscountType.class, value);

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
