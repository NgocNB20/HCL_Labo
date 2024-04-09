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
 * 管理者状態
 *
 * @author kaneda
 */
@Getter
@AllArgsConstructor
public enum HTypeAdministratorStatus implements EnumType {

    /** 使用中:0 */
    USE("使用中", "0"),

    /** 停止中:1 */
    STOP("停止中", "1");

    /** doma用ファクトリメソッド */
    public static HTypeAdministratorStatus of(String value) {

        HTypeAdministratorStatus hType = EnumTypeUtil.getEnumFromValue(HTypeAdministratorStatus.class, value);

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
