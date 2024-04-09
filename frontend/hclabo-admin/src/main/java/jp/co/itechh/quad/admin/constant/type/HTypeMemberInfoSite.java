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
 * 会員登録サイト種別：列挙型
 *
 * @author kaneda
 *
 */
@Getter
@AllArgsConstructor
public enum HTypeMemberInfoSite implements EnumType {

    /** PC */
    PC("PC", "0"),

    /** 管理者登録 */
    ADMIN("管理者登録", "3");

    /** doma用ファクトリメソッド */
    public static HTypeMemberInfoSite of(String value) {

        HTypeMemberInfoSite hType = EnumTypeUtil.getEnumFromValue(HTypeMemberInfoSite.class, value);

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