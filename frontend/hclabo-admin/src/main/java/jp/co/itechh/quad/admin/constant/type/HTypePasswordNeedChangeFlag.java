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
 * パスワード変更要求フラグ：列挙型
 *
 * @author kaneda
 *
 */
@Getter
@AllArgsConstructor
public enum HTypePasswordNeedChangeFlag implements EnumType {

    /** 要求しない */
    OFF("要求しない", "0"),

    /** 要求する */
    ON("要求する", "1");

    /**
     * boolean に対応するフラグを返します。<br/>
     *
     * @param bool true, false
     * @return 引数がtrue:ON false:OFF
     */
    public static HTypePasswordNeedChangeFlag getFlagByBoolean(boolean bool) {
        return bool ? ON : OFF;
    }

    /** doma用ファクトリメソッド */
    public static HTypePasswordNeedChangeFlag of(String value) {

        HTypePasswordNeedChangeFlag hType = EnumTypeUtil.getEnumFromValue(HTypePasswordNeedChangeFlag.class, value);

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
