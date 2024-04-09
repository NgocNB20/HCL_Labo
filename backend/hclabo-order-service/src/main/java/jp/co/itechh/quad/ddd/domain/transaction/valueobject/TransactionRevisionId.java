/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.transaction.valueobject;

import jp.co.itechh.quad.ddd.exception.AssertChecker;
import lombok.Getter;

import java.util.UUID;

/**
 * 改訂用取引ID 値オブジェクト
 */
@Getter
public class TransactionRevisionId {

    /** 値 */
    private final String value;

    /**
     * コンストラクタ(ユニークなIDを新規採番)
     */
    public TransactionRevisionId() {
        this.value = UUID.randomUUID().toString();
    }

    /**
     * コンストラクタ
     *
     * @param value
     */
    public TransactionRevisionId(String value) {
        // アサートチェック
        AssertChecker.assertNotEmpty("TransactionRevisionId value is empty", value);

        this.value = value;
    }

}