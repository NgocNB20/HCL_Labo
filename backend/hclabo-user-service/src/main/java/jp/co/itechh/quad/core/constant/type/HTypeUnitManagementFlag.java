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
 * 規格管理フラグ：列挙型
 *
 * @author kaneda
 *
 */
@Domain(valueType = String.class, factoryMethod = "of")
@Getter
@AllArgsConstructor
public enum HTypeUnitManagementFlag implements EnumType {

    /** 規格管理する  */
    ON("規格表示あり", "1"),

    /** 規格管理しない */
    OFF("規格表示なし", "0");

    /** doma用ファクトリメソッド */
    public static HTypeUnitManagementFlag of(String value) {

        HTypeUnitManagementFlag hType = EnumTypeUtil.getEnumFromValue(HTypeUnitManagementFlag.class, value);

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