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
 * 性別：列挙型
 *
 * @author kaneda
 *
 */
@Domain(valueType = String.class, factoryMethod = "of")
@Getter
@AllArgsConstructor
public enum HTypeSex implements EnumType {

    /** 不明 */
    UNKNOWN("未選択", "0"),

    /** 男 */
    MALE("男性", "1"),

    /** 女 */
    FEMALE("女性", "2");

    /** doma用ファクトリメソッド */
    public static HTypeSex of(String value) {

        HTypeSex hType = EnumTypeUtil.getEnumFromValue(HTypeSex.class, value);

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
