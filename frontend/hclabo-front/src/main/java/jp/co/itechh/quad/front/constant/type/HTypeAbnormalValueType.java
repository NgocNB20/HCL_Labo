/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.constant.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 異常値区分
 *
 */
@Getter
@AllArgsConstructor
public enum HTypeAbnormalValueType implements EnumType {

    /** 高値 */
    HIGH("高値", "H"),

    /** 低値 */
    LOW("低値", "L"),

    /** 陽性 */
    POSITIVE("陽性", "!");

    /**
     * ラベル
     */
    private String label;

    /**
     * 区分値
     */
    private String value;
}
