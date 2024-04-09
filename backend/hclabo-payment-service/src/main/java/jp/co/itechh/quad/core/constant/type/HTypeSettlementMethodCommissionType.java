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
 * 決済方法手数料種別：列挙型
 *
 * @author kaneda
 *
 */
@Domain(valueType = String.class, factoryMethod = "of")
@Getter
@AllArgsConstructor
public enum HTypeSettlementMethodCommissionType implements EnumType {

    /** 一律（円） */
    FLAT_YEN("一律（円）", "0"),

    /** 金額別（円） */
    EACH_AMOUNT_YEN("金額別（円）", "1");

    /** doma用ファクトリメソッド */
    public static HTypeSettlementMethodCommissionType of(String value) {

        HTypeSettlementMethodCommissionType hType =
                        EnumTypeUtil.getEnumFromValue(HTypeSettlementMethodCommissionType.class, value);

        if (hType == null) {
            throw new IllegalArgumentException(value);
        } else {
            return hType;
        }
    }

    /**
     * ラベル
     */
    private String label;

    /**
     * 区分値
     */
    private String value;
}