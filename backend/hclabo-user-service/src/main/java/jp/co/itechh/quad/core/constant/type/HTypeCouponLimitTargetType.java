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
 * クーポン利用制限対象種別
 *
 * @author kaneda
 *
 */
@Domain(valueType = String.class, factoryMethod = "of")
@Getter
@AllArgsConstructor
public enum HTypeCouponLimitTargetType implements EnumType {

    /** 対象外:0 */
    OFF("無効", "0"),

    /** 対象:1 */
    ON("有効", "1"),

    /** 対象(チェック済み）:2 */
    ON_CHECKED("有効", "2");

    /** doma用ファクトリメソッド */
    public static HTypeCouponLimitTargetType of(String value) {

        HTypeCouponLimitTargetType hType = EnumTypeUtil.getEnumFromValue(HTypeCouponLimitTargetType.class, value);

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