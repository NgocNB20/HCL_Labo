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
 * GMO連携解除フラグ
 *
 * @author kaneda
 *
 */
@Getter
@AllArgsConstructor
public enum HTypeGmoReleaseFlag implements EnumType {

    /** 正常 ※ラベル未使用 */
    NORMAL("", "0"),

    /** 連携解除 */
    RELEASE("GMO連携解除", "1");

    /** doma用ファクトリメソッド */
    public static HTypeGmoReleaseFlag of(String value) {

        HTypeGmoReleaseFlag hType = EnumTypeUtil.getEnumFromValue(HTypeGmoReleaseFlag.class, value);

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