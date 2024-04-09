/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.addressbook.valueobject;

import lombok.Getter;

import java.util.UUID;

/**
 * 住所ID 値オブジェクト
 */
@Getter
public class AddressId {

    /** 値 */
    private final String value;

    /**
     * コンストラクタ(ユニークなIDを新規採番)
     */
    public AddressId() {
        this.value = UUID.randomUUID().toString();
    }

    /**
     * コンストラクタ
     * @param value
     */
    public AddressId(String value) {
        this.value = value;
    }
}
