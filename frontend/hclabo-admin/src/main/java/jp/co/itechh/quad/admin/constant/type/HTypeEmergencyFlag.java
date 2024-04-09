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
 * 異常フラグ
 *
 * @author kaneda
 *
 */
@Getter
@AllArgsConstructor
public enum HTypeEmergencyFlag implements EnumType {

    /** 正常 */
    OFF("通常", "0"),

    /** 異常 */
    ON("異常あり", "1");

    /**
     * boolean に対応するフラグを返します。<br/>
     *
     * @param bool true, false
     * @return 引数がtrue:ON false:OFF
     */
    public static HTypeEmergencyFlag getFlagByBoolean(boolean bool) {
        return bool ? ON : OFF;
    }

    /** doma用ファクトリメソッド */
    public static HTypeEmergencyFlag of(String value) {

        HTypeEmergencyFlag hType = EnumTypeUtil.getEnumFromValue(HTypeEmergencyFlag.class, value);

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