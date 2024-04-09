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
 * 規格管理フラグ：列挙型
 *
 * @author kaneda
 *
 */
@Getter
@AllArgsConstructor
public enum HTypeUnitManagementFlag implements EnumType {

    /** 規格管理する  */
    ON("規格表示あり", "1"),

    /** 規格管理しない */
    OFF("規格表示なし", "0");

    /** doma用ファクトリメソッド */
    public static HTypeUnitManagementFlag of(String value) {

        HTypeUnitManagementFlag hType = EnumTypeUtil.getEnumFromValue(HTypeUnitManagementFlag.class, value);

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