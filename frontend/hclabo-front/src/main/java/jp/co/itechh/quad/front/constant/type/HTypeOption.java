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
 * オプション （集計）：列挙型
 *
 * @author kaneda
 *
 */
@Getter
@AllArgsConstructor
public enum HTypeOption implements EnumType {

    /** キャンセル・返品・変更を含まない */
    EXCLUSION("キャンセル・返品・変更を含まない", "0"),

    /** キャンセル・返品・変更分も含めて表示 */
    ALL("キャンセル・返品・変更分も含めて表示", "1");

    /** doma用ファクトリメソッド */
    public static HTypeOption of(String value) {

        HTypeOption hType = EnumTypeUtil.getEnumFromValue(HTypeOption.class, value);

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