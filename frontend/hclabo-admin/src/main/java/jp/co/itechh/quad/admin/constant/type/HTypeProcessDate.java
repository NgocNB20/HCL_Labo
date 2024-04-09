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
 * 集計結果（集計）：列挙型
 *
 * @author kaneda
 *
 */
@Getter
@AllArgsConstructor
public enum HTypeProcessDate implements EnumType {

    /** 月別 */
    MONTHLY("月別", "1"),

    /** 日別 */
    DAILY("日別", "0");

    /** doma用ファクトリメソッド */
    public static HTypeProcessDate of(String value) {

        HTypeProcessDate hType = EnumTypeUtil.getEnumFromValue(HTypeProcessDate.class, value);

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