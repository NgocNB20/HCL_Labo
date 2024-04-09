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
 * 保留中フラグ：列挙型
 *
 * @author kaneda
 *
 */
@Getter
@AllArgsConstructor
public enum HTypeWaitingFlag implements EnumType {

    /** 通常 */
    OFF("通常", "0"),

    /** 保留中 */
    ON("保留中", "1");

    /**
     * boolean に対応するフラグを返します。<br/>
     *
     * @param bool true, false
     * @return 引数がtrue:ON false:OFF
     */
    public static HTypeWaitingFlag getFlagByBoolean(boolean bool) {
        return bool ? ON : OFF;
    }

    /** doma用ファクトリメソッド */
    public static HTypeWaitingFlag of(String value) {

        HTypeWaitingFlag hType = EnumTypeUtil.getEnumFromValue(HTypeWaitingFlag.class, value);

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