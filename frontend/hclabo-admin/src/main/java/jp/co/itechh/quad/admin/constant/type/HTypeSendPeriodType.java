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
 * 配信期間種別：列挙型
 *
 * @author kaneda
 *
 */
@Getter
@AllArgsConstructor
public enum HTypeSendPeriodType implements EnumType {

    /** START_DATE ※ラベル未使用 */
    START_DATE("", "0"),

    /** STOP_DATE ※ラベル未使用 */
    STOP_DATE("", "1");

    /** doma用ファクトリメソッド */
    public static HTypeSendPeriodType of(String value) {

        HTypeSendPeriodType hType = EnumTypeUtil.getEnumFromValue(HTypeSendPeriodType.class, value);

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