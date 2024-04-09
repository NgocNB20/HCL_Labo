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
 * アンケート回答必須フラグ：列挙型
 *
 * @author kaneda
 *
 */
@Getter
@AllArgsConstructor
public enum HTypeReplyRequiredFlag implements EnumType {

    /** 任意 */
    OPTIONAL("任意", "0"),

    /** 必須 */
    REQUIRED("必須", "1");

    /** doma用ファクトリメソッド */
    public static HTypeReplyRequiredFlag of(String value) {

        HTypeReplyRequiredFlag hType = EnumTypeUtil.getEnumFromValue(HTypeReplyRequiredFlag.class, value);

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