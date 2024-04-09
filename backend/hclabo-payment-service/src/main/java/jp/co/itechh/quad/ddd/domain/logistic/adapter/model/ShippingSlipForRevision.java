/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.logistic.adapter.model;

import lombok.Data;

/**
 * 改訂用配送伝票<br/>
 */
@Data
public class ShippingSlipForRevision extends ShippingSlip {

    // TODO リファクタリングしたい
    // ・ SalesSlipのほうとアクセス修飾子が異なる（private⇔public）
    // ・改訂前から配送伝票IDを返却していたり、していなかったりでズレている（返すほうがいいかな）　これはmiroも修正

    /** 改訂用配送伝票ID */
    private String shippingSlipRevesionId;

    /** 取引ID */
    private String transactionId;
}