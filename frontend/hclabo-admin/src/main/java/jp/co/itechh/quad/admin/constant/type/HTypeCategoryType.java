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
 *
 * カテゴリ種別
 *
 * @author kaneda
 *
 */
@Getter
@AllArgsConstructor
public enum HTypeCategoryType implements EnumType {

    /** 手動 */
    NORMAL("手動", "0"),

    /** 自動 */
    AUTO("自動", "1");

    /** doma用ファクトリメソッド */
    public static HTypeCategoryType of(String value) {

        HTypeCategoryType hType = EnumTypeUtil.getEnumFromValue(HTypeCategoryType.class, value);

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