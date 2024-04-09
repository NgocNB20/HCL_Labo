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
 * 検査完了フラグ
 *
 */
@Getter
@AllArgsConstructor
public enum HTypeExamCompletedFlag implements EnumType {

    /** 一部検査完了 */
    MIDDLE("一部検査完了", "M"),

    /** 検査完了 */
    END("検査完了", "E");

    /** doma用ファクトリメソッド */
    public static HTypeExamCompletedFlag of(String value) {

        HTypeExamCompletedFlag hType = EnumTypeUtil.getEnumFromValue(HTypeExamCompletedFlag.class, value);

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
