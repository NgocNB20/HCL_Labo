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
 * 問い合わせ種別
 *
 * @author kaneda
 *
 */
@Getter
@AllArgsConstructor
public enum HTypeInquiryType implements EnumType {

    /** 一般 */
    GENERAL("一般", "0"),

    /** 注文 */
    ORDER("注文", "1");

    /** doma用ファクトリメソッド */
    public static HTypeInquiryType of(String value) {

        HTypeInquiryType hType = EnumTypeUtil.getEnumFromValue(HTypeInquiryType.class, value);

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