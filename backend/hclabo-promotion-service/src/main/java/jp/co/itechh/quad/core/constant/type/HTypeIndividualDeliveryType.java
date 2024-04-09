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
 * 個別配送種別
 *
 * @author kaneda
 */
@Domain(valueType = String.class, factoryMethod = "of")
@Getter
@AllArgsConstructor
public enum HTypeIndividualDeliveryType implements EnumType {

    /**
     *  個別配送商品
     */
    ON("個別配送する", "1"),

    /**
     *  個別配送商品ではない
     */
    OFF("個別配送しない", "0");

    /** doma用ファクトリメソッド */
    public static HTypeIndividualDeliveryType of(String value) {

        HTypeIndividualDeliveryType hType = EnumTypeUtil.getEnumFromValue(HTypeIndividualDeliveryType.class, value);

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
