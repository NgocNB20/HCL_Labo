/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.billing.valueobject.billingstatus;

/**
 * 前請求ステータス 値オブジェクト(enum)
 */
public enum PreClaimBillingStatus implements IBillingStatus {

    /** 下書き */
    DRAFT,

    /** 入金待ち */
    DEPOSIT_WAITING,

    /** 取消 */
    CANCEL,

    /** 入金 */
    DEPOSITED,

    /** 返金 */
    REFUND

}
