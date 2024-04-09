/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.pricecalculator.valueobject;

import lombok.Getter;

/**
 * 送料 値オブジェクト
 * ※ファクトリあり
 */
@Getter
public class Carriage {

    /** 値 */
    private final int value;

    /**
     * ファクトリ用コンストラクタ
     * ※パッケージプライベート
     *
     * @param value
     */
    public Carriage(int value) {
        this.value = value;
    }

}
