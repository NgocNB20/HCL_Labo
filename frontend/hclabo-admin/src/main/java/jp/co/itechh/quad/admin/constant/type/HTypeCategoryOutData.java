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
 * カテゴリCSVデータタイプ：列挙型
 *
 * @author kaneda
 *
 */
@Getter
@AllArgsConstructor
public enum HTypeCategoryOutData implements EnumType {

    /** カテゴリCSV */
    OUTDATA("カテゴリCSV", "0");

    /** doma用ファクトリメソッド */
    public static HTypeCategoryOutData of(String value) {

        HTypeCategoryOutData hType = EnumTypeUtil.getEnumFromValue(HTypeCategoryOutData.class, value);

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