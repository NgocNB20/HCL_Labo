/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.constant.type;

import jp.co.itechh.quad.front.util.common.EnumTypeUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 会員種別
 *
 * @author kaneda
 *
 */
@Getter
@AllArgsConstructor
public enum HTypeMemberInfoType implements EnumType {

    /** 個人 ※ラベル未使用 */
    INDIVIDUAL("", "0"),

    /** 法人 ※ラベル未使用 */
    CORPORATION("", "1");

    /** doma用ファクトリメソッド */
    public static HTypeMemberInfoType of(String value) {

        HTypeMemberInfoType hType = EnumTypeUtil.getEnumFromValue(HTypeMemberInfoType.class, value);

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