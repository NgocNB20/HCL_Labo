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
 * HTypeSex + 未回答項目：列挙型
 *
 * 注意！！ 未回答有りから未回答無しへの変更は不可
 *
 * @author kaneda
 *
 */
@Domain(valueType = String.class, factoryMethod = "of")
@Getter
@AllArgsConstructor
public enum HTypeSexUnnecessaryAnswer implements EnumType {

    /** 未選択 */
    UNKNOWN("未選択", "0"),

    /** 男(HTypeSex) */
    MALE("男性", "1"),

    /** 女(HTypeSex) */
    FEMALE("女性", "2");

    /** doma用ファクトリメソッド */
    public static HTypeSexUnnecessaryAnswer of(String value) {

        HTypeSexUnnecessaryAnswer hType = EnumTypeUtil.getEnumFromValue(HTypeSexUnnecessaryAnswer.class, value);

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