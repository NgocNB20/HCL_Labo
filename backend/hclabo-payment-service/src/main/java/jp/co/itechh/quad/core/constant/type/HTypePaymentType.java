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
 * 支払い種別：列挙型
 *
 * @author kaneda
 *
 */
@Domain(valueType = String.class, factoryMethod = "of")
@Getter
@AllArgsConstructor
public enum HTypePaymentType implements EnumType {

    /** 一括 */
    SINGLE("一括", "1"),

    /** 分割 */
    INSTALLMENT("分割", "2"),

    /** リボ */
    REVOLVING("リボ", "5");

    /** doma用ファクトリメソッド */
    public static HTypePaymentType of(String value) {

        HTypePaymentType hType = EnumTypeUtil.getEnumFromValue(HTypePaymentType.class, value);

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