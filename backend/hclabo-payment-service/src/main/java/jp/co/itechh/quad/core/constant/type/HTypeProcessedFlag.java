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
 * 入金処理済みフラグ：列挙型
 *
 * @author kaneda
 *
 */
@Domain(valueType = String.class, factoryMethod = "of")
@Getter
@AllArgsConstructor
public enum HTypeProcessedFlag implements EnumType {

    /** 要処理 ※ラベル未使用 */
    PROCESSING_REQUIRED("", "0"),

    /** 処理済/処理不要 ※ラベル未使用 */
    PROCESSED("", "1"),

    /** 請求決済エラー ※ラベル未使用 */
    BILL_SETTLEMENT_ERROR("", "9");

    /** doma用ファクトリメソッド */
    public static HTypeProcessedFlag of(String value) {

        HTypeProcessedFlag hType = EnumTypeUtil.getEnumFromValue(HTypeProcessedFlag.class, value);

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
