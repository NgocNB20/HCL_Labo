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
 * 削除状態付き公開状態
 *
 * @author kaneda
 *
 */
@Domain(valueType = String.class, factoryMethod = "of")
@Getter
@AllArgsConstructor
public enum HTypeOpenDeleteStatus implements EnumType {

    /** 非公開 */
    NO_OPEN("非公開", "0"),

    /** 公開中 */
    OPEN("公開中", "1"),

    /** 削除 */
    DELETED("削除", "9");

    /** doma用ファクトリメソッド */
    public static HTypeOpenDeleteStatus of(String value) {

        HTypeOpenDeleteStatus hType = EnumTypeUtil.getEnumFromValue(HTypeOpenDeleteStatus.class, value);

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
