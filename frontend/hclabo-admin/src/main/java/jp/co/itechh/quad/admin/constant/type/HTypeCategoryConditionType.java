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
 * カテゴリ条件種別
 *
 * @author PHAM QUANG DIEU (VJP)
 *
 */
@Getter
@AllArgsConstructor
public enum HTypeCategoryConditionType implements EnumType {

    /** 通常 */
    AND("すべての条件", "0"),

    /** 自動 */
    OR("いずれかの条件", "1");

    /** doma用ファクトリメソッド */
    public static HTypeCategoryConditionType of(String value) {

        HTypeCategoryConditionType hType = EnumTypeUtil.getEnumFromValue(HTypeCategoryConditionType.class, value);

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