/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.constant.type;

import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.seasar.doma.Domain;

/**
 * 同梱単位区分<br/>
 *
 * @author PHAM QUANG DIEU (VJP)
 *
 */
@Domain(valueType = String.class, factoryMethod = "of")
@Getter
@AllArgsConstructor
public enum HTypeEnclosureUnitType implements EnumType {

    /** 受注単位 */
    ORDER("受注単位", "0"),

    /** 受注商品単位 */
    ORDER_GOODS("受注商品単位", "1");

    /** doma用ファクトリメソッド */
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