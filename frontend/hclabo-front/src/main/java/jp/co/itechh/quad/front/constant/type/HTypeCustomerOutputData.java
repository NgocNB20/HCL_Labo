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
 * 購入者データタイプ：列挙型
 *
 * @author kaneda
 *
 */
@Getter
@AllArgsConstructor
public enum HTypeCustomerOutputData implements EnumType {

    /** 購入者CSV */
    OUTDATA("購入者CSV", "1");

    /** doma用ファクトリメソッド */
    public static HTypeCustomerOutputData of(String value) {

        HTypeCustomerOutputData hType = EnumTypeUtil.getEnumFromValue(HTypeCustomerOutputData.class, value);

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