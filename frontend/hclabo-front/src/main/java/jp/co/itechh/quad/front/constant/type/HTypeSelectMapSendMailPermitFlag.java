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
 * メール配信希望フラグ：列挙型
 *
 * @author kaneda
 *
 */
@Getter
@AllArgsConstructor
public enum HTypeSelectMapSendMailPermitFlag implements EnumType {

    /** 希望するのみ */
    ON("希望するのみ", "1");

    /** doma用ファクトリメソッド */
    public static HTypeSelectMapSendMailPermitFlag of(String value) {

        HTypeSelectMapSendMailPermitFlag hType =
                        EnumTypeUtil.getEnumFromValue(HTypeSelectMapSendMailPermitFlag.class, value);

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