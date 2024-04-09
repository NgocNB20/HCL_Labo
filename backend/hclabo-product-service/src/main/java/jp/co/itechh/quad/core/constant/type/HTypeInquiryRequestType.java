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
 * 問い合わせ発信者種別
 *
 * @author kaneda
 *
 */
@Domain(valueType = String.class, factoryMethod = "of")
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
     * ラベル
     */
    private String label;

    /**
     * 区分値
     */
    private String value;
}