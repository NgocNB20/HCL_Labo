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
 * 出荷状態：列挙型
 *
 * @author kaneda
 *
 */
@Getter
@AllArgsConstructor
public enum HTypeShipmentStatus implements EnumType {

    /** 未出荷 */
    UNSHIPMENT("未出荷", "0"),

    /** 出荷済み */
    SHIPPED("出荷済み", "1");

    /** doma用ファクトリメソッド */
    public static HTypeShipmentStatus of(String value) {

        HTypeShipmentStatus hType = EnumTypeUtil.getEnumFromValue(HTypeShipmentStatus.class, value);

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