/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.sales.valueobject;

/**
 * 販売ステータス 値オブジェクト(enum)
 */
public enum SalesStatus {

    /** 下書き */
    DRAFT,

    /** 確定 */
    OPEN,

    /** 取消 */
    CANCEL,

    /** 終了 */
    CLOSE
}
