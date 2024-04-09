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
 * 有効無効フラグ 列挙型
 *
 * @author kaneda
 */
@Getter
@AllArgsConstructor
public enum HTypeEffectiveFlag implements EnumType {

    /** 無効 */
    INVALID("無効", "0"),

    /** 有効 */
    VALID("有効", "1");

    /** doma用ファクトリメソッド */
    public static HTypeEffectiveFlag of(String value) {

        HTypeEffectiveFlag hType = EnumTypeUtil.getEnumFromValue(HTypeEffectiveFlag.class, value);

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

    /**
     * true / false を変換
     *
     * @param bool Boolean
     * @return 有効無効フラグ列挙型
     */
    public static HTypeEffectiveFlag valueByBool(Boolean bool) {

        if (bool == null) {
            return HTypeEffectiveFlag.INVALID;
        }

        return bool ? HTypeEffectiveFlag.VALID : HTypeEffectiveFlag.INVALID;
    }

}
