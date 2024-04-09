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
 * バッチタイプ状態
 *
 * @author kaneda
 *
 */
@Getter
@AllArgsConstructor
public enum HTypeBatchDerive implements EnumType {

    /** オンライン */
    ONLINE("オンライン", "1"),

    /** オフライン */
    OFFLINE("オフライン", "0");

    /** doma用ファクトリメソッド */
    public static HTypeBatchDerive of(String value) {

        HTypeBatchDerive hType = EnumTypeUtil.getEnumFromValue(HTypeBatchDerive.class, value);

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
