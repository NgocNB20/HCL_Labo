/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.hclabo.core.constant.type;

import jp.co.itechh.quad.hclabo.core.util.common.EnumTypeUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.seasar.doma.Domain;

/**
 * 検査状態
 *
 */
@Domain(valueType = String.class, factoryMethod = "of")
@Getter
@AllArgsConstructor
public enum HTypeExamStatus implements EnumType {

    /** 返送待ち */
    WAITING_RETURN("返送待ち","W"),

    /** 受領済み */
    RECEIVED("受領済み","R"),

    /** 一部検査完了 */
    MIDDLE("一部検査完了", "M"),

    /** 検査完了 */
    END("検査完了", "E");

    /** doma用ファクトリメソッド */
    public static HTypeExamStatus of(String value) {

        HTypeExamStatus hType = EnumTypeUtil.getEnumFromValue(HTypeExamStatus.class, value);

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
