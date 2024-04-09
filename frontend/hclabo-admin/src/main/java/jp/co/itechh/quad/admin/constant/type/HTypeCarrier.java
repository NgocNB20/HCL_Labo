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
 * 購入者検索　デバイス：列挙型
 *
 * @author kaneda
 *
 */
@Getter
@AllArgsConstructor
public enum HTypeCarrier implements EnumType {

    /** PCとモバイル */
    PCANDMB("PCとモバイル", ""),

    /** PCのみ */
    PC("PCのみ", "0");

    /** doma用ファクトリメソッド */
    public static HTypeCarrier of(String value) {

        HTypeCarrier hType = EnumTypeUtil.getEnumFromValue(HTypeCarrier.class, value);

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