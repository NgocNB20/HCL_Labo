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
 * 出荷メール送信区分：列挙型
 *
 * @author kaneda
 *
 */
@Getter
@AllArgsConstructor
public enum HTypeShipmentCompleteMail implements EnumType {

    /** 送信 ※ラベル未使用 */
    MAIL_SEND("", "1"),

    /** 未送信 ※ラベル未使用 */
    MAIL_NOT_SEND("", "0");

    /** doma用ファクトリメソッド */
    public static HTypeShipmentCompleteMail of(String value) {

        HTypeShipmentCompleteMail hType = EnumTypeUtil.getEnumFromValue(HTypeShipmentCompleteMail.class, value);

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