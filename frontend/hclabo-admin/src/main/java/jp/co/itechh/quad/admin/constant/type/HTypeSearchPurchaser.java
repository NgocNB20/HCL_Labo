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
 * 購入者検索 購入者：列挙型
 *
 * @author kaneda
 *
 */
@Getter
@AllArgsConstructor
public enum HTypeSearchPurchaser implements EnumType {

    /** 会員とゲスト */
    MEMBERANDGUEST("会員とゲスト", ""),

    /** 会員のみ */
    MEMBER("会員のみ", "1"),

    /** ゲストのみ */
    GUEST("ゲストのみ", "2");

    /** doma用ファクトリメソッド */
    public static HTypeSearchPurchaser of(String value) {

        HTypeSearchPurchaser hType = EnumTypeUtil.getEnumFromValue(HTypeSearchPurchaser.class, value);

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