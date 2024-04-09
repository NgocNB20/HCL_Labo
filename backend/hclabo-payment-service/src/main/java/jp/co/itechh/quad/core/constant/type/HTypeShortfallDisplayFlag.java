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
 * 不足金額表示フラグ：列挙型
 *
 * @author kaneda
 *
 */
@Domain(valueType = String.class, factoryMethod = "of")
@Getter
@AllArgsConstructor
public enum HTypeShortfallDisplayFlag implements EnumType {

    /** 表示しない */
    OFF("表示しない", "0"),

    /**  表示する */
    ON("表示する", "1");

    /** doma用ファクトリメソッド */
    public static HTypeShortfallDisplayFlag of(String value) {

        HTypeShortfallDisplayFlag hType = EnumTypeUtil.getEnumFromValue(HTypeShortfallDisplayFlag.class, value);

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
