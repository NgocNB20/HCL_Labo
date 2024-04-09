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
 * 対象データ（集計）：列挙型
 *
 * @author kaneda
 *
 */
@Getter
@AllArgsConstructor
public enum HTypeTargetData implements EnumType {

    /** 受注 */
    ORDER("受注", "1"),

    /** 売上 */
    SALES("売上", "0");

    /** doma用ファクトリメソッド */
    public static HTypeTargetData of(String value) {

        HTypeTargetData hType = EnumTypeUtil.getEnumFromValue(HTypeTargetData.class, value);

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