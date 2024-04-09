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
 * メール送信要否
 *
 * @author kaneda
 *
 */
@Getter
@AllArgsConstructor
public enum HTypeMailRequired implements EnumType {

    /** 送信不要 */
    NO_NEED("送信不要", "0"),

    /** 送信する */
    REQUIRED("送信する", "1");

    /** doma用ファクトリメソッド */
    public static HTypeMailRequired of(String value) {

        HTypeMailRequired hType = EnumTypeUtil.getEnumFromValue(HTypeMailRequired.class, value);

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