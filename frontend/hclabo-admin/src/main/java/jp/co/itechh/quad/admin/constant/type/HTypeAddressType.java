/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.constant.type;

import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * アドレス種別<br/>
 *
 * @author kaneda
 */
@Getter
@AllArgsConstructor
public enum HTypeAddressType implements EnumType {

    /** PC */
    PC("PC", "0"),

    /** PCと携帯 ※ラベル未使用 */
    BOTH("", "2");

    /** doma用ファクトリメソッド */
    public static HTypeAddressType of(String value) {

        HTypeAddressType hType = EnumTypeUtil.getEnumFromValue(HTypeAddressType.class, value);

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
