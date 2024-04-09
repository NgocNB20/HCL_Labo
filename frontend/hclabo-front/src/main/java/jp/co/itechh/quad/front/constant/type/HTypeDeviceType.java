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
 * デバイス種別
 *
 * @author kaneda
 *
 */
@Getter
@AllArgsConstructor
public enum HTypeDeviceType implements EnumType {

    /** PC:0 */
    PC("PC", "0");

    /** doma用ファクトリメソッド */
    public static HTypeDeviceType of(String value) {

        HTypeDeviceType hType = EnumTypeUtil.getEnumFromValue(HTypeDeviceType.class, value);

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
