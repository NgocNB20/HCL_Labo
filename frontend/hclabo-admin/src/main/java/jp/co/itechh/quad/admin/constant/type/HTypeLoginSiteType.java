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
 * ログインサイト種別
 *
 * @author kaneda
 *
 */
@Getter
@AllArgsConstructor
public enum HTypeLoginSiteType implements EnumType {

    /** PC:0 */
    PC("PC", "0");

    /** doma用ファクトリメソッド */
    public static HTypeLoginSiteType of(String value) {

        HTypeLoginSiteType hType = EnumTypeUtil.getEnumFromValue(HTypeLoginSiteType.class, value);

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