/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.constant.type;

import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.seasar.doma.Domain;

/**
 * GMO決済キャンセル状態
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Domain(valueType = String.class, factoryMethod = "of")
@Getter
@AllArgsConstructor
public enum HTypeGmoPaymentCancelStatus implements EnumType {

    /**
     * 未判定:0
     */
    UNDECIDED("未判定", "0"),

    /**
     * 未キャンセル:1
     */
    UNCANCELLED("未キャンセル", "1"),

    /**
     * キャンセル済み:2
     */
    CANCELLED("キャンセル済み", "2");

    /** doma用ファクトリメソッド */
    public static HTypeGmoPaymentCancelStatus of(String value) {

        HTypeGmoPaymentCancelStatus hType = EnumTypeUtil.getEnumFromValue(HTypeGmoPaymentCancelStatus.class, value);

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