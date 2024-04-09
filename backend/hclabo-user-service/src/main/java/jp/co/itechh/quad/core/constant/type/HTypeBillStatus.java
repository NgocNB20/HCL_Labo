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
 * 請求状態
 *
 * @author kaneda
 *
 */
@Domain(valueType = String.class, factoryMethod = "of")
@Getter
@AllArgsConstructor
public enum HTypeBillStatus implements EnumType {

    /** 未請求 0 */
    BILL_NO_CLAIM("未請求", "0"),

    /** 請求済み 1 */
    BILL_CLAIM("請求済み", "1");

    /** doma用ファクトリメソッド */
    public static HTypeBillStatus of(String value) {

        HTypeBillStatus hType = EnumTypeUtil.getEnumFromValue(HTypeBillStatus.class, value);

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