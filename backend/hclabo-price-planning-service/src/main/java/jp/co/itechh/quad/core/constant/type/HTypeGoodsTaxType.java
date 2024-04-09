/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.constant.type;

import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.seasar.doma.Domain;

/**
 * 商品消費税種別
 *
 * @author kaneda
 *
 */
@Domain(valueType = String.class, factoryMethod = "of")
@Getter
@AllArgsConstructor
public enum HTypeGoodsTaxType implements EnumType {

    /** 内税 ※ラベル未使用 */
    IN_TAX("", "0"),

    /** 外税 ※ラベル未使用 */
    OUT_TAX("", "1"),

    /** 非課税 ※ラベル未使用 */
    NO_TAX("", "2");

    /** doma用ファクトリメソッド */
    public static HTypeGoodsTaxType of(String value) {

        HTypeGoodsTaxType hType = EnumTypeUtil.getEnumFromValue(HTypeGoodsTaxType.class, value);

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