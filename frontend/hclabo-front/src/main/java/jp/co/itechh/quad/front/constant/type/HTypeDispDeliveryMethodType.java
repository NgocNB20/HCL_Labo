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
 * 配送方法区分(画面表示用)
 *
 * @author kaneda
 *
 */
@Getter
@AllArgsConstructor
public enum HTypeDispDeliveryMethodType implements EnumType {

    /** 全国一律 */
    FLAT("全国一律", "0"),

    /** 都道府県別 */
    PREFECTURE("都道府県別", "1");

    /** doma用ファクトリメソッド */
    public static HTypeDispDeliveryMethodType of(String value) {

        HTypeDispDeliveryMethodType hType = EnumTypeUtil.getEnumFromValue(HTypeDispDeliveryMethodType.class, value);

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