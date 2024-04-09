/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.transaction.valueobject;

import jp.co.itechh.quad.ddd.exception.AssertChecker;
import lombok.Getter;

import java.util.UUID;

/**
 * 受注ID 値オブジェクト
 */
@Getter
public class OrderReceivedId {

    /** 値 */
    private final String value;

    /**
     * コンストラクタ(ユニークなIDを新規採番)
     */
    public OrderReceivedId() {
        this.value = UUID.randomUUID().toString();
    }

    /**
     * コンストラクタ
     *
     * @param value
     */
    public OrderReceivedId(String value) {
        // アサートチェック
        AssertChecker.assertNotEmpty("OrderReceivedId value is empty", value);

        this.value = value;
    }

    /**
     * コンストラクタ
     *
     * @param value
     * @param customParams
     */
    public OrderReceivedId(String value, Object... customParams) {
        this.value = value;
    }
}