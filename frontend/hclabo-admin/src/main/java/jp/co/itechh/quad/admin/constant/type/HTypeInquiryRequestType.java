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
 * 問い合わせ発信者種別
 *
 * @author kaneda
 *
 */
@Getter
@AllArgsConstructor
public enum HTypeInquiryRequestType implements EnumType {

    /** お客様 */
    CONSUMER("お客様", "0"),

    /** 運用者 */
    OPERATOR("運用者", "1");

    /** doma用ファクトリメソッド */
    public static HTypeInquiryRequestType of(String value) {

        HTypeInquiryRequestType hType = EnumTypeUtil.getEnumFromValue(HTypeInquiryRequestType.class, value);

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