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
 * 無料配送フラグ
 *
 * @author kaneda
 *
 */
@Getter
@AllArgsConstructor
public enum HTypeFreeDeliveryFlag implements EnumType {

    /** 有料 */
    OFF("通常送料", "0"),

    /** 無料 */
    ON("送料込み", "1");

    /** doma用ファクトリメソッド */
    public static HTypeFreeDeliveryFlag of(String value) {

        HTypeFreeDeliveryFlag hType = EnumTypeUtil.getEnumFromValue(HTypeFreeDeliveryFlag.class, value);

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