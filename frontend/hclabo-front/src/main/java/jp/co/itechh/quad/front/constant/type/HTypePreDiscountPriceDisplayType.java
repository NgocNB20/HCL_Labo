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
 * 値引き前単価表示区分：列挙型
 *
 * @author kaneda
 *
 */
@Getter
@AllArgsConstructor
public enum HTypePreDiscountPriceDisplayType implements EnumType {

    /** 表示しない */
    OFF("表示しない", "0"),

    /** 表示する */
    ON("表示する", "1");

    /** doma用ファクトリメソッド */
    public static HTypePreDiscountPriceDisplayType of(String value) {

        HTypePreDiscountPriceDisplayType hType =
                        EnumTypeUtil.getEnumFromValue(HTypePreDiscountPriceDisplayType.class, value);

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