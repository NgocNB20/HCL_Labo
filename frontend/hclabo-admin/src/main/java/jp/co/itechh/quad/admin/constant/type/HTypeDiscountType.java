/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.admin.constant.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HTypeDiscountType implements EnumType {

    /** 金額割引 */
    PERCENT("パーセント割引", "0"),

    /** パーセント割引 */
    AMOUNT("金額割引", "1");

    /** ラベル */
    private final String label;

    /** 区分値 */
    private final String value;
}
