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
 * 納品書添付フラグ
 *
 * @author kaneda
 *
 */
@Getter
@AllArgsConstructor
public enum HTypeInvoiceAttachmentFlag implements EnumType {

    /** 納品書不要 */
    OFF("不要", "0"),

    /** 納品書必要 */
    ON("必要", "1");

    /** doma用ファクトリメソッド */
    public static HTypeInvoiceAttachmentFlag of(String value) {

        HTypeInvoiceAttachmentFlag hType = EnumTypeUtil.getEnumFromValue(HTypeInvoiceAttachmentFlag.class, value);

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