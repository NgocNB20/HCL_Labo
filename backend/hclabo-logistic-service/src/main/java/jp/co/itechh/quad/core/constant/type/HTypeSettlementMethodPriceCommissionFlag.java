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
 * 決済方法金額別手数料フラグ：列挙型
 *
 * @author kaneda
 *
 */
@Domain(valueType = String.class, factoryMethod = "of")
@Getter
@AllArgsConstructor
public enum HTypeSettlementMethodPriceCommissionFlag implements EnumType {

    /** 一律 */
    FLAT("一律", "0"),

    /** 金額別 */
    EACH_AMOUNT("金額別", "1");

    /** doma用ファクトリメソッド */
    public static HTypeSettlementMethodPriceCommissionFlag of(String value) {

        HTypeSettlementMethodPriceCommissionFlag hType =
                        EnumTypeUtil.getEnumFromValue(HTypeSettlementMethodPriceCommissionFlag.class, value);

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