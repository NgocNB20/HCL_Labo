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
 * 同梱フラグ：列挙型
 *
 * @author kaneda
 */
@Getter
@AllArgsConstructor
public enum HTypeResultEnclosureFlag implements EnumType {

    /** 同梱フラグOFF */
    OFF("－", "0"),

    /** 同梱フラグON */
    ON("○", "1");

    /** doma用ファクトリメソッド */
    public static HTypeResultEnclosureFlag of(String value) {

        HTypeResultEnclosureFlag hType = EnumTypeUtil.getEnumFromValue(HTypeResultEnclosureFlag.class, value);

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