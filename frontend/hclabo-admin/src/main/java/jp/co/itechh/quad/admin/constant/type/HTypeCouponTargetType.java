/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.admin.constant.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * クーポン対象種別
 */
@Getter
@AllArgsConstructor
public enum HTypeCouponTargetType implements EnumType {

    /** 除外指定 */
    EXCLUDE_TARGET("除外指定", "0"),

    /** 絞込指定 */
    INCLUDE_TARGET("絞込指定", "1");

    /** ラベル */
    private final String label;

    /** 区分値 */
    private final String value;
}
