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
 * カテゴリ種別
 *
 * @author Pham Quang Dieu (VJP)
 */

@Getter
@AllArgsConstructor
public enum HTypeSortByType implements EnumType {

    /** 人気順 */
    POPULARITY("人気順", "popularityCount"),

    /** おすすめ順 */
    RECOMMEND("おすすめ順", "normal"),

    /** 新着順 */
    NEW("新着順", "whatsnewdate"),

    /** 価格順 */
    PRICE("価格順", "goodsGroupMinPrice");

    /** doma用ファクトリメソッド */
    public static HTypeSortByType of(String value) {

        HTypeSortByType hType = EnumTypeUtil.getEnumFromValue(HTypeSortByType.class, value);

        if (hType == null) {
            throw new IllegalArgumentException(value);
        } else {
            return hType;
        }
    }

    /** ラベル */
    private String label;

    /** 区分値 */
    private String value;
}