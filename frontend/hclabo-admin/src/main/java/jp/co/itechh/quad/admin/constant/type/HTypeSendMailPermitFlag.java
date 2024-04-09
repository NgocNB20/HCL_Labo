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
 * メール配信希望フラグ：列挙型
 *
 * @author kaneda
 *
 */
@Getter
@AllArgsConstructor
public enum HTypeSendMailPermitFlag implements EnumType {

    /** 希望しない */
    OFF("希望しない", "0"),

    /** 希望する */
    ON("希望する", "1");

    /** doma用ファクトリメソッド */
    public static HTypeSendMailPermitFlag of(String value) {

        HTypeSendMailPermitFlag hType = EnumTypeUtil.getEnumFromValue(HTypeSendMailPermitFlag.class, value);

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