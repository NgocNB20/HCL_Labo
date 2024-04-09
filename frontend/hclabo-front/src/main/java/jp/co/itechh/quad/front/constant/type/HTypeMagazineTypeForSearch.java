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
 * 検索用：配信希望形式種別：列挙型
 *
 * @author kaneda
 */
@Getter
@AllArgsConstructor
public enum HTypeMagazineTypeForSearch implements EnumType {

    /** PC-テキスト */
    PC_TEXT("テキスト版", "0"),

    /** テキスト版(HTML版を除く) */
    ONLY_TEXT("テキスト版(HTML版を除く)", "9"),

    /** PC-HTML */
    PC_HTML("HTML版", "1");

    /** doma用ファクトリメソッド */
    public static HTypeMagazineTypeForSearch of(String value) {

        HTypeMagazineTypeForSearch hType = EnumTypeUtil.getEnumFromValue(HTypeMagazineTypeForSearch.class, value);

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