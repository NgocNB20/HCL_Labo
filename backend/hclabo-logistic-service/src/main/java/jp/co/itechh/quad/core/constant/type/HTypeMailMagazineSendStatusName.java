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
 * メールマガ配信状態
 *
 * @author Doan Thang (VJP)
 *
 */
@Domain(valueType = String.class, factoryMethod = "of")
@Getter
@AllArgsConstructor
public enum HTypeMailMagazineSendStatusName implements EnumType {

    /** 配信中 */
    SENDING("配信中", "0"),

    /** 配信停止 */
    SEND_STOP(" 配信停止", "1"),

    /** 配信解除 */
    REMOVE("配信解除", "2");

    /** doma用ファクトリメソッド */
    public static HTypeMailMagazineSendStatusName of(String value) {

        HTypeMailMagazineSendStatusName hType =
                        EnumTypeUtil.getEnumFromValue(HTypeMailMagazineSendStatusName.class, value);

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