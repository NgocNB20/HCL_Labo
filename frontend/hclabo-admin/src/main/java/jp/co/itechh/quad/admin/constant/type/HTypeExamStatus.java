/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.constant.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 検査状態
 *
 */
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

    /**
     * ラベル
     */
    private String label;

    /**
     * 区分値
     */
    private String value;
}
