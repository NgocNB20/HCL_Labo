/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.constant.type;

import jp.co.itechh.quad.front.util.common.EnumTypeUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * キャンセルフラグ
 *
 * @author kaneda
 *
 */
@Getter
@AllArgsConstructor
public enum HTypeCancelFlag implements EnumType {

    /** 通常 */
    OFF("通常", "0"),

    /** キャンセル */
    ON("キャンセル", "1");

    /** doma用ファクトリメソッド */
    public static HTypeCancelFlag of(String value) {

        HTypeCancelFlag hType = EnumTypeUtil.getEnumFromValue(HTypeCancelFlag.class, value);

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