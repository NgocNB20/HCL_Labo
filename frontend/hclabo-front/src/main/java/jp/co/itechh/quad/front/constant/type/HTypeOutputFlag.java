/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */
package jp.co.itechh.quad.front.constant.type;

import jp.co.itechh.quad.front.util.common.EnumTypeUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 出力対象フラグ：列挙型
 *
 * @author kaneda
 *
 */
@Getter
@AllArgsConstructor
public enum HTypeOutputFlag implements EnumType {

    /** 対象外:0 ※ラベル未使用 */
    OFF("", "0"),

    /** 対象:1 ※ラベル未使用 */
    ON("", "1");

    /** doma用ファクトリメソッド */
    public static HTypeOutputFlag of(String value) {

        HTypeOutputFlag hType = EnumTypeUtil.getEnumFromValue(HTypeOutputFlag.class, value);

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