/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.billing.valueobject;

/**
 * 注文決済ステータス 値オブジェクト(enum)
 */
public enum OrderPaymentStatus {

    /** 下書き */
    DRAFT,

    /** 決済途中 */
    UNDER_SETTLEMENT,

    /** 取消 */
    CANCEL,

    /** 確定 */
    OPEN

}
