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
 * コンビニ利用フラグ：列挙型
 *
 * @author kaneda
 *
 */
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
     * ラベル<br/>
     */
    private String label;

    /**
     * 区分値<br/>
     */
    private String value;
}