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
 * 顧客区分（集計）：列挙型
 *
 * @author kaneda
 *
 */
@Getter
@AllArgsConstructor
public enum HTypeRepeatPurchase implements EnumType {

    /** 会員 */
    MEMBER("会員", "1"),

    /** ゲスト */
    GUEST("ゲスト", "0");

    /** doma用ファクトリメソッド */
    public static HTypeRepeatPurchase of(String value) {

        HTypeRepeatPurchase hType = EnumTypeUtil.getEnumFromValue(HTypeRepeatPurchase.class, value);

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