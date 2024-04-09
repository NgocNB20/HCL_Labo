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
 * 性別回答：列挙型
 *
 * @author kaneda
 *
 */
@Getter
@AllArgsConstructor
public enum HTypeSearchSex implements EnumType {

    /** 全て */
    ALL("全て", ""),

    /** 男性のみ */
    ONLY_MALE("男性のみ", "1"),

    /** 女性のみ */
    ONLY_FEMALE("女性のみ", "2");

    /** doma用ファクトリメソッド */
    public static HTypeSearchSex of(String value) {

        HTypeSearchSex hType = EnumTypeUtil.getEnumFromValue(HTypeSearchSex.class, value);

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