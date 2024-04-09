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
 * お届け希望日指定フラグ：列挙型
 *
 * @author kaneda
 *
 */
@Domain(valueType = String.class, factoryMethod = "of")
@Getter
@AllArgsConstructor
public enum HTypeReceiverDateDesignationFlag implements EnumType {

    /** 指定不可 */
    OFF("指定不可", "0"),

    /** 指定可 */
    ON("指定可", "1");

    /** doma用ファクトリメソッド */
    public static HTypeReceiverDateDesignationFlag of(String value) {

        HTypeReceiverDateDesignationFlag hType =
                        EnumTypeUtil.getEnumFromValue(HTypeReceiverDateDesignationFlag.class, value);

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