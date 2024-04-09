/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.sales.valueobject;

import lombok.Getter;

import java.util.UUID;

/**
 * 販売伝票ID 値オブジェクト
 */
@Getter
public class SalesSlipId {

    /** 値 */
    private final String value;

    /**
     * コンストラクタ(ユニークなIDを新規採番)
     */
    public SalesSlipId() {
        this.value = UUID.randomUUID().toString();
    }

    /**
     * コンストラクタ
     * @param value
     */
    public SalesSlipId(String value) {
        this.value = value;
    }
}