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
 * 有効無効状態：列挙型
 *
 * @author kaneda
 *
 */
@Getter
@AllArgsConstructor
public enum HTypeReserveStatus implements EnumType {

    /** 有効 */
    VALID("有効", "0"),

    /** 無効 */
    INVALID("無効", "1");

    /** doma用ファクトリメソッド */
    public static HTypeReserveStatus of(String value) {

        HTypeReserveStatus hType = EnumTypeUtil.getEnumFromValue(HTypeReserveStatus.class, value);

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