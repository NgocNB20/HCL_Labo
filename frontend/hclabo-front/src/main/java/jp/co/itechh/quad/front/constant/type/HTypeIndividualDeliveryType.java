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
 * 個別配送種別
 *
 * @author kaneda
 */
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
     * ラベル<br/>
     */
    private String label;

    /**
     * 区分値<br/>
     */
    private String value;
}