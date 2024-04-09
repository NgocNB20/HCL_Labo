/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.billing.valueobject;

import lombok.Getter;

import java.util.UUID;

/**
 * 注文決済ID 値オブジェクト
 */
@Getter
public class OrderPaymentId {

    /** 値 */
    private final String value;

    /**
     * コンストラクタ(ユニークなIDを新規採番)
     */
    public OrderPaymentId() {
        this.value = UUID.randomUUID().toString();
    }

    /**
     * コンストラクタ
     *
     * @param value
     */
    public OrderPaymentId(String value) {
        this.value = value;
    }
}
