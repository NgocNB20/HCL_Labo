/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.billing.valueobject;

import jp.co.itechh.quad.ddd.exception.AssertChecker;
import lombok.Getter;

import java.util.UUID;

/**
 * 請求先住所ID 値オブジェクト
 */
@Getter
public class BillingAddressId {

    /** 値 */
    private final String value;

    /**
     * コンストラクタ(ユニークなIDを新規採番)
     */
    public BillingAddressId() {
        this.value = UUID.randomUUID().toString();
    }

    /**
     * コンストラクタ
     *
     * @param value
     */
    public BillingAddressId(String value) {

        AssertChecker.assertNotEmpty("billingAddressId is empty", value);

        this.value = value;
    }

    /**
     * コンストラクタ
     *
     * @param value
     * @param customParams 案件用引数
     */
    public BillingAddressId(String value, Object... customParams) {
        this.value = value;
    }
}