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
 * 在庫データタイプ：列挙型
 *
 * @author kaneda
 *
 */
@Getter
@AllArgsConstructor
public enum HTypeStockOutData implements EnumType {

    /** 在庫CSV */
    STOCK_CSV("在庫CSV", "0"),

    /** 入庫履歴CSV */
    STORING_HISTORY_CSV("入庫履歴CSV", "1");

    /** doma用ファクトリメソッド */
    public static HTypeStockOutData of(String value) {

        HTypeStockOutData hType = EnumTypeUtil.getEnumFromValue(HTypeStockOutData.class, value);

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