/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.billing.valueobject;

import lombok.Getter;

import java.util.UUID;

/**
 * 請求伝票ID 値オブジェクト
 */
@Getter
public class BillingSlipId {

    /** 値 */
    private final String value;

    /**
     * コンストラクタ(ユニークなIDを新規採番)
     */
    public BillingSlipId() {
        this.value = UUID.randomUUID().toString();
    }

    /**
     * コンストラクタ
     *
     * @param value
     */
    public BillingSlipId(String value) {
        this.value = value;
    }
}
