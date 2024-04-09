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
 * コンビニ利用フラグ：列挙型
 *
 * @author kaneda
 *
 */
@Domain(valueType = String.class, factoryMethod = "of")
@Getter
@AllArgsConstructor
public enum HTypeUseConveni implements EnumType {

    /** 使わない ※ラベル未使用 */
    OFF("", "0"),

    /** 使う ※ラベル未使用 */
    ON("", "1");

    /** doma用ファクトリメソッド */
    public static HTypeUseConveni of(String value) {

        HTypeUseConveni hType = EnumTypeUtil.getEnumFromValue(HTypeUseConveni.class, value);

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