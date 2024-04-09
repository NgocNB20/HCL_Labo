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
 * 入金状態（受注検索）：列挙型
 *
 * @author kaneda
 */
//@Domain(valueType = String.class, factoryMethod = "of")
//end
@Getter
@AllArgsConstructor
public enum HTypePaymentStatus implements EnumType {

    /** 未入金 */
    NOTHING_MONEY("未入金", "1"),

    /** 入金済み */
    JUST_MONEY("入金済み", "2"),

    /** 入金不足 */
    INSUFFICIENT_MONEY("入金不足", "3"),

    /** 入金超過 */
    OVER_MONEY("入金超過", "4");

    /** doma用ファクトリメソッド */
    public static HTypePaymentStatus of(String value) {

        HTypePaymentStatus hType = EnumTypeUtil.getEnumFromValue(HTypePaymentStatus.class, value);

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