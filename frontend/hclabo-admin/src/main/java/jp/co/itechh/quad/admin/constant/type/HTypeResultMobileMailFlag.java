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
 * モバイルメールフラグ：列挙型
 *
 * @author kaneda
 */
@Getter
@AllArgsConstructor
public enum HTypeResultMobileMailFlag implements EnumType {

    /** モバイルメールフラグOFF */
    OFF("－", "0"),

    /** モバイルメールフラグON */
    ON("○", "1");

    /** doma用ファクトリメソッド */
    public static HTypeResultMobileMailFlag of(String value) {

        HTypeResultMobileMailFlag hType = EnumTypeUtil.getEnumFromValue(HTypeResultMobileMailFlag.class, value);

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