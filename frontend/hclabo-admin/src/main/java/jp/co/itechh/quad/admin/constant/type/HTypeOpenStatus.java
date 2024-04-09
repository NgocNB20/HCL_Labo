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
 * 公開状態
 *
 * @author kaneda
 *
 */
@Getter
@AllArgsConstructor
public enum HTypeOpenStatus implements EnumType {

    /** 非公開 */
    NO_OPEN("非公開", "0"),

    /** 公開中 */
    OPEN("公開中", "1");

    /** doma用ファクトリメソッド */
    public static HTypeOpenStatus of(String value) {

        HTypeOpenStatus hType = EnumTypeUtil.getEnumFromValue(HTypeOpenStatus.class, value);

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