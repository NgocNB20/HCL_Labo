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
 * 手動表示フラグ
 *
 * @author kaneda
 *
 */
@Domain(valueType = String.class, factoryMethod = "of")
@Getter
@AllArgsConstructor
public enum HTypeManualDisplayFlag implements EnumType {

    /** 手動表示しない */
    OFF("手動表示しない", "0"),

    /** 手動表示する */
    ON("手動表示する", "1");

    /** doma用ファクトリメソッド */
    public static HTypeManualDisplayFlag of(String value) {

        HTypeManualDisplayFlag hType = EnumTypeUtil.getEnumFromValue(HTypeManualDisplayFlag.class, value);

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