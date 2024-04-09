/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.transaction.valueobject;

import jp.co.itechh.quad.ddd.exception.AssertChecker;
import lombok.Getter;

import java.util.UUID;

/**
 * 取引ID 値オブジェクト
 */
@Getter
public class TransactionId {

    /** 値 */
    private final String value;

    /**
     * コンストラクタ(ユニークなIDを新規採番)
     */
    public TransactionId() {
        this.value = UUID.randomUUID().toString();
    }

    /**
     * コンストラクタ
     *
     * @param value
     */
    public TransactionId(String value) {
        // アサートチェック
        AssertChecker.assertNotEmpty("TransactionId value is empty", value);

        this.value = value;
    }

    /**
     * コンストラクタ
     * ※最新取引ID用
     */
    public TransactionId(String value, Object... customParams) {
        this.value = value;
    }

}