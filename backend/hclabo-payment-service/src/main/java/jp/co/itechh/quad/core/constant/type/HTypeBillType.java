/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.constant.type;

import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.seasar.doma.Domain;

/**
 * 請求種別
 *
 * @author kaneda
 *
 */
@Domain(valueType = String.class, factoryMethod = "of")
@Getter
@AllArgsConstructor
public enum HTypeBillType implements EnumType {

    /** 前請求 */
    PRE_CLAIM("前請求", "0"),

    /** 後請求 */
    POST_CLAIM("後請求", "1");

    /** doma用ファクトリメソッド */
    public static HTypeBillType of(String value) {

        HTypeBillType hType = EnumTypeUtil.getEnumFromValue(HTypeBillType.class, value);

        if (hType == null) {
            throw new IllegalArgumentException(value);
        } else {
            return hType;
        }
    }

    /**
     * ラベル
     */
    private String label;

    /**
     * 区分値
     */
    private String value;
}
