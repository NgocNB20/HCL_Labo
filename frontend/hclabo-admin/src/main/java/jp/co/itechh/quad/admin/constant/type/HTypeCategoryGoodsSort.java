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
 * 並び順設定
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Getter
@AllArgsConstructor
public enum HTypeCategoryGoodsSort implements EnumType {

    POPULARITY("人気順", "0"),

    NEW_ITEM("新着順", "1"),

    OLD_ITEM("古い順", "2"),

    HIGH_PRICE("価格の高い順", "3"),

    LOW_PRICE("価格の安い順", "4"),

    MANUAL("手動並び替え", "5");

    /** doma用ファクトリメソッド */
    public static HTypeCategoryGoodsSort of(String value) {

        HTypeCategoryGoodsSort hType = EnumTypeUtil.getEnumFromValue(HTypeCategoryGoodsSort.class, value);

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