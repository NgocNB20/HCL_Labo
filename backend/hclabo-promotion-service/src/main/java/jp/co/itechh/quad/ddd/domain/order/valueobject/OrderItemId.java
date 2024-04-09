/*
 * Copyright (C) 2023 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.order.valueobject;

import jp.co.itechh.quad.ddd.exception.AssertChecker;
import lombok.Getter;

import java.util.UUID;

/**
 * 注文商品ID 値オブジェクト
 */
@Getter
public class OrderItemId {

    /** 値 */
    private final String value;

    /**
     * コンストラクタ(ユニークなIDを新規採番)
     */
    public OrderItemId() {
        this.value = UUID.randomUUID().toString();
    }

    /**
     * コンストラクタ
     *
     * @param value
     */
    public OrderItemId(String value) {
        // アサートチェック
        AssertChecker.assertNotEmpty("OrderItemId value is empty", value);

        this.value = value;
    }

}