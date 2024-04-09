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
 * カート内同一商品マージフラグ
 *
 * 使用用途については下記画面設計書参照
 *　\document\02_外部設計\03_画面設計書\P04_HM3_画面設計書_フロントPC_カート.xlsx
 * ・【補足】カート商品制御
 *
 * @author kaneda
 *
 */
@Domain(valueType = String.class, factoryMethod = "of")
@Getter
@AllArgsConstructor
public enum HTypeCartGoodsMergeFlag implements EnumType {

    /** カートマージする ※ラベル未使用 */
    CART_MERGE("", "1"),

    /** カートマージしない ※ラベル未使用 */
    NOT_CART__MERGE("", "0");

    /** doma用ファクトリメソッド */
    public static HTypeCartGoodsMergeFlag of(String value) {

        HTypeCartGoodsMergeFlag hType = EnumTypeUtil.getEnumFromValue(HTypeCartGoodsMergeFlag.class, value);

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