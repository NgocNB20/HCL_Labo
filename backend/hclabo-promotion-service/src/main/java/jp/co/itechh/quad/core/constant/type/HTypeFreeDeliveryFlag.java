/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.constant.type;

import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.seasar.doma.Domain;

/**
 * 無料配送フラグ
 *
 * @author kaneda
 *
 */
@Domain(valueType = String.class, factoryMethod = "of")
@Getter
@AllArgsConstructor
public enum HTypeFreeDeliveryFlag implements EnumType {

    /** 有料 */
    OFF("通常送料", "0"),

    /** 無料 */
    ON("送料込み", "1");

    /** doma用ファクトリメソッド */
    public static HTypeFreeDeliveryFlag of(String value) {

        HTypeFreeDeliveryFlag hType = EnumTypeUtil.getEnumFromValue(HTypeFreeDeliveryFlag.class, value);

        if (hType == null) {
            throw new IllegalArgumentException(value);
        } else {
            return hType;
        }
    }

    /**
     * ラベル
     */
    private String label;

    /**
     * 区分値
     */
    private String value;
}
