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
 * カード会社全件フラグ （集計）：列挙型
 *
 * @author kaneda
 *
 */
@Getter
@AllArgsConstructor
public enum HTypeCreditCompanyAllFlag implements EnumType {

    /** 全て */
    ALL("全て", "1"),

    /** カード会社別 */
    SELECT("カード会社別", "0");

    /** doma用ファクトリメソッド */
    public static HTypeCreditCompanyAllFlag of(String value) {

        HTypeCreditCompanyAllFlag hType = EnumTypeUtil.getEnumFromValue(HTypeCreditCompanyAllFlag.class, value);

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