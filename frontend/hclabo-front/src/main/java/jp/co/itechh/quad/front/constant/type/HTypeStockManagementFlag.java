/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.constant.type;

import jp.co.itechh.quad.front.util.common.EnumTypeUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 在庫管理フラグ：列挙型
 *
 * @author kaneda
 *
 */
@Getter
@AllArgsConstructor
public enum HTypeStockManagementFlag implements EnumType {

    /** 在庫管理する */
    ON("在庫管理あり", "1"),

    /** 在庫管理しない */
    OFF("在庫管理なし", "0");

    /** doma用ファクトリメソッド */
    public static HTypeStockManagementFlag of(String value) {

        HTypeStockManagementFlag hType = EnumTypeUtil.getEnumFromValue(HTypeStockManagementFlag.class, value);

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