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
 *
 * カテゴリ商品手動ソート
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 *
 */
@Getter
@AllArgsConstructor
public enum HTypeCategoryGoodsManualSort implements EnumType {

    TOP("最上位", "0"),

    BOTTOM("最下位", "1"),

    POSITION_SPECIFICATION("位置指定", "2");

    /** doma用ファクトリメソッド */
    public static HTypeCategoryGoodsManualSort of(String value) {

        HTypeCategoryGoodsManualSort hType = EnumTypeUtil.getEnumFromValue(HTypeCategoryGoodsManualSort.class, value);

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