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

    /** doma用ファクトリメソッド */
    public static HTypeAbnormalValueType of(String value) {

        HTypeAbnormalValueType hType = EnumTypeUtil.getEnumFromValue(HTypeAbnormalValueType.class, value);

        if (hType == null) {
            throw new IllegalArgumentException(value);
        } else {
            return hType;
        }
    }

    /**
     * ラベル
     */
    private String label;

    /**
     * 区分値
     */
    private String value;
}
