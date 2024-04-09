/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.shipping.valueobject;

/**
 * 配送ステータス 値オブジェクト(enum)
 */
public enum ShippingStatus {

    /** 下書き */
    DRAFT,

    /** 在庫確保 */
    SECURED_INVENTORY,

    /** 取消 */
    CANCEL,

    /** 確定 */
    OPEN,

    /** 出荷完了 */
    SHIPMENT_COMPLETED,

    /** 返品 */
    RETURN;
}
