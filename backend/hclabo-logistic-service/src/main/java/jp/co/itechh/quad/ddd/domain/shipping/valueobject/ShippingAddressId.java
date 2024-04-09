/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.shipping.valueobject;

import jp.co.itechh.quad.ddd.exception.AssertChecker;
import lombok.Getter;

import java.util.UUID;

/**
 * 配送先住所ID 値オブジェクト
 */
@Getter
public class ShippingAddressId {

    /** 値 */
    private final String value;

    /** コンストラクタ(ユニークなIDを新規採番) */
    public ShippingAddressId() {
        this.value = UUID.randomUUID().toString();
    }

    /**
     * コンストラクタ
     *
     * @param value
     */
    public ShippingAddressId(String value) {

        AssertChecker.assertNotEmpty("ShippingAddressId is null", value);

        this.value = value;
    }

}