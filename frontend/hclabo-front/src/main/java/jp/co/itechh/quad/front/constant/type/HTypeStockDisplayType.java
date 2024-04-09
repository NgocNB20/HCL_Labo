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
 * 在庫表示方法：列挙型
 *
 * @author kaneda
 *
 */
@Getter
@AllArgsConstructor
public enum HTypeStockDisplayType implements EnumType {

    /** 0:日本語表示 */
    JAPANESE("日本語表示", "0"),

    /** 1:数値表示 */
    NUMERIC("数値表示", "1");

    /** doma用ファクトリメソッド */
    public static HTypeStockDisplayType of(String value) {

        HTypeStockDisplayType hType = EnumTypeUtil.getEnumFromValue(HTypeStockDisplayType.class, value);

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