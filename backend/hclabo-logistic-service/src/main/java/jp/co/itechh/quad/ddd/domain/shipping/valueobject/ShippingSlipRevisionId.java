/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.shipping.valueobject;

import lombok.Getter;

import java.util.UUID;

/**
 * 改訂用配送伝票ID 値オブジェクト
 */
@Getter
public class ShippingSlipRevisionId {

    /** 値 */
    private final String value;

    /** コンストラクタ(ユニークなIDを新規採番) */
    public ShippingSlipRevisionId() {
        this.value = UUID.randomUUID().toString();
    }

    /**
     * コンストラクタ
     *
     * @param value
     */
    public ShippingSlipRevisionId(String value) {
        this.value = value;
    }
}
