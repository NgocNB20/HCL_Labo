/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.sales.valueobject;

import jp.co.itechh.quad.ddd.exception.AssertChecker;
import lombok.Getter;

import java.util.UUID;

/**
 * 改訂用販売伝票ID 値オブジェクト
 */
@Getter
public class SalesSlipRevisionId {

    /** 値 */
    private final String value;

    /**
     * コンストラクタ(ユニークなIDを新規採番)
     */
    public SalesSlipRevisionId() {
        this.value = UUID.randomUUID().toString();
    }

    /**
     * コンストラクタ
     *
     * @param value
     */
    public SalesSlipRevisionId(String value) {

        AssertChecker.assertNotEmpty("SalesSlipRevisionId is empty", value);

        this.value = value;
    }

}
