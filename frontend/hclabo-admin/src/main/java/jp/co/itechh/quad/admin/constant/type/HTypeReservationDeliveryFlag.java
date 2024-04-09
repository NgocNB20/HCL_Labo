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
 * 予約配送フラグ：列挙型
 *
 * @author kaneda
 *
 */
@Getter
@AllArgsConstructor
public enum HTypeReservationDeliveryFlag implements EnumType {

    /** 通常配送 */
    OFF("通常配送", "0"),

    /** 予約配送 */
    ON("予約配送", "1");

    /** doma用ファクトリメソッド */
    public static HTypeReservationDeliveryFlag of(String value) {

        HTypeReservationDeliveryFlag hType = EnumTypeUtil.getEnumFromValue(HTypeReservationDeliveryFlag.class, value);

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