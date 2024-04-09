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
 * 会員出力データタイプ
 *
 * @author kaneda
 *
 */
@Getter
@AllArgsConstructor
public enum HTypeMemberInfoOutData implements EnumType {

    /** 会員CSV */
    MEMBER_CSV("会員CSV", "6");

    /** doma用ファクトリメソッド */
    public static HTypeMemberInfoOutData of(String value) {

        HTypeMemberInfoOutData hType = EnumTypeUtil.getEnumFromValue(HTypeMemberInfoOutData.class, value);

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