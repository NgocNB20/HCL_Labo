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
 * サイト
 *
 * @author kaneda
 *
 */
@Getter
@AllArgsConstructor
public enum HTypeSite implements EnumType {

    /** PC */
    ONLY_PC("PCのみ", "1");

    /** doma用ファクトリメソッド */
    public static HTypeSite of(String value) {

        HTypeSite hType = EnumTypeUtil.getEnumFromValue(HTypeSite.class, value);

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