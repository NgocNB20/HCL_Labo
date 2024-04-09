/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.billing.valueobject;

import jp.co.itechh.quad.ddd.exception.AssertChecker;
import lombok.Getter;

import java.util.UUID;

/**
 * 改訂用注文決済ID 値オブジェクト
 */
@Getter
public class OrderPaymentRevisionId {

    /** 値 */
    private final String value;

    /**
     * コンストラクタ(ユニークなIDを新規採番)
     */
    public OrderPaymentRevisionId() {
        this.value = UUID.randomUUID().toString();
    }

    /**
     * コンストラクタ
     *
     * @param value
     */
    public OrderPaymentRevisionId(String value) {
        AssertChecker.assertNotNull("orderPaymentRevisionId is null", value);
        this.value = value;
    }
}
