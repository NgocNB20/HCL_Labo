/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.hclabo.core.constant;

/**
 * 受注状態(ENUM) 値オブジェクト
 */
public enum OrderStatus {

    /** 商品準備中 */
    ITEM_PREPARING,

    /** 入金待ち */
    PAYMENT_CONFIRMING,

    /** 出荷完了 */
    SHIPMENT_COMPLETION,

    /** キャンセル */
    CANCEL,

    /** 請求決済エラー */
    PAYMENT_ERROR;

}
