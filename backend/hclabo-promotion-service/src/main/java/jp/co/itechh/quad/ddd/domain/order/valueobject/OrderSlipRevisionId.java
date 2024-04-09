/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.order.valueobject;

import lombok.Getter;

import java.util.UUID;

/**
 * 改訂用注文票ID 値オブジェクト
 */
@Getter
public class OrderSlipRevisionId {

    /** 値 */
    private final String value;

    /**
     * コンストラクタ(ユニークなIDを新規採番)
     */
    public OrderSlipRevisionId() {
        this.value = UUID.randomUUID().toString();
    }

    /**
     * コンストラクタ
     * @param value
     */
    public OrderSlipRevisionId(String value) {
        this.value = value;
    }
}
