/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.billing.valueobject.billingstatus;

/**
 * 後請求ステータス 値オブジェクト(enum)
 */
public enum PostClaimBillingStatus implements IBillingStatus {

    /** 下書き */
    DRAFT,

    /** オーソリ済 */
    AUTHORIZED,

    /** 取消 */
    CANCEL,

    /** 一時停止 */
    SUSPENDED,

    /** 売上確定 */
    SALES,

    /** 返金 */
    REFUND

}
