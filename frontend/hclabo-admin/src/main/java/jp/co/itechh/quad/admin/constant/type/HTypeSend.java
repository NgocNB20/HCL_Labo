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
 * 送信状態：列挙型
 *
 * @author kaneda
 *
 */
@Getter
@AllArgsConstructor
public enum HTypeSend implements EnumType {

    /** 未送信 0 */
    UNSENT("未送信", "0"),

    /** 送信済 1 */
    SENT("送信済", "1");

    /** doma用ファクトリメソッド */
    public static HTypeSend of(String value) {

        HTypeSend hType = EnumTypeUtil.getEnumFromValue(HTypeSend.class, value);

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