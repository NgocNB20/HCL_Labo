/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.transaction.valueobject;

import lombok.Getter;

/**
 * 受注番号 値オブジェクト
 */
@Getter
public class OrderCode {

    /** 値 */
    private final String value;

    /**
     * コンストラクタ
     * ※ファクトリ、DB取得値設定用
     *
     * @param value
     */
    public OrderCode(String value) {
        this.value = value;
    }
}
