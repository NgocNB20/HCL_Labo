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
 * 注文種別：列挙型
 *
 * @author kaneda
 *
 */
@Getter
@AllArgsConstructor
public enum HTypeOrderType implements EnumType {

    /** 通常注文 */
    NORMAL("通常注文", "0"),

    /** 予約注文 */
    RESERVATION("予約注文", "2");

    /** doma用ファクトリメソッド */
    public static HTypeOrderType of(String value) {

        HTypeOrderType hType = EnumTypeUtil.getEnumFromValue(HTypeOrderType.class, value);

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