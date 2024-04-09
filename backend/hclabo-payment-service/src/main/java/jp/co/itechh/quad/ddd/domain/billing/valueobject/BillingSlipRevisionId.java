/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.billing.valueobject;

import lombok.Getter;

import java.util.UUID;

/**
 * 改訂用請求伝票ID 値オブジェクト
 */
@Getter
public class BillingSlipRevisionId {

    /** 値 */
    private final String value;

    /**
     * コンストラクタ(ユニークなIDを新規採番)
     */
    public BillingSlipRevisionId() {
        this.value = UUID.randomUUID().toString();
    }

    /**
     * コンストラクタ
     *
     * @param value
     */
    public BillingSlipRevisionId(String value) {
        this.value = value;
    }
}
