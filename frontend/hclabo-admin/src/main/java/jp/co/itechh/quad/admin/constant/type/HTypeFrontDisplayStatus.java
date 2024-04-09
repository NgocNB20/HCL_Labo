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
 * フロント公開状態
 *
 * @author kimura
 *
 */
@Getter
@AllArgsConstructor
public enum HTypeFrontDisplayStatus implements EnumType {

    /** 非表示 */
    NO_OPEN("非表示", "0"),

    /** 表示中 */
    OPEN("表示中", "1");

    /** doma用ファクトリメソッド */
    public static HTypeFrontDisplayStatus of(String value) {

        HTypeFrontDisplayStatus hType = EnumTypeUtil.getEnumFromValue(HTypeFrontDisplayStatus.class, value);

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
