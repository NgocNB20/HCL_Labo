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
 * 送り先注意フラグ：列挙型
 *
 * @author kaneda
 */
@Getter
@AllArgsConstructor
public enum HTypeResultSendAlertFlag implements EnumType {

    /** 送り先注意フラグOFF */
    OFF("－", "0"),

    /** 送り先注意フラグON */
    ON("○", "1");

    /** doma用ファクトリメソッド */
    public static HTypeResultSendAlertFlag of(String value) {

        HTypeResultSendAlertFlag hType = EnumTypeUtil.getEnumFromValue(HTypeResultSendAlertFlag.class, value);

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