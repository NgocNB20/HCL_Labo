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
 * 配信状態：列挙型
 *
 * @author kaneda
 *
 */
@Getter
@AllArgsConstructor
public enum HTypeSendStatus implements EnumType {

    /** 配信中 */
    SENDING("配信中", "0"),

    /** 配信停止 */
    SEND_STOP("配信停止", "1"),

    /** 配信解除 */
    REMOVE("解除", "2");

    /** doma用ファクトリメソッド */
    public static HTypeSendStatus of(String value) {

        HTypeSendStatus hType = EnumTypeUtil.getEnumFromValue(HTypeSendStatus.class, value);

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