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
 * 配信希望形式種別
 *
 * @author kaneda
 */
@Getter
@AllArgsConstructor
public enum HTypeMagazineType implements EnumType {

    /** PC-テキスト */
    PC_TEXT("テキスト版", "0"),

    /** PC-HTML */
    PC_HTML("HTML版", "1");

    /** doma用ファクトリメソッド */
    public static HTypeMagazineType of(String value) {

        HTypeMagazineType hType = EnumTypeUtil.getEnumFromValue(HTypeMagazineType.class, value);

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