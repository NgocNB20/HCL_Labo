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
 * サイトマップ出力フラグ：列挙型
 *
 * @author kaneda
 *
 */
@Domain(valueType = String.class, factoryMethod = "of")
@Getter
@AllArgsConstructor
public enum HTypeSiteMapFlag implements EnumType {

    /** 対象外 */
    OFF("対象外", "0"),

    /** 対象 */
    ON("対象", "1");

    /** doma用ファクトリメソッド */
    public static HTypeSiteMapFlag of(String value) {

        HTypeSiteMapFlag hType = EnumTypeUtil.getEnumFromValue(HTypeSiteMapFlag.class, value);

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