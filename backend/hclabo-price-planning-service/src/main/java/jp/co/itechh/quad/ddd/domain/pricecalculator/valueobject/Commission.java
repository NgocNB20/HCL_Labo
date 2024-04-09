/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.pricecalculator.valueobject;

import lombok.Getter;

/**
 * 手数料 値オブジェクト
 */
@Getter
public class Commission {

    /** 値 */
    private final int value;

    /**
     * ファクトリ用コンストラクタ
     *
     * @param value
     */
    public Commission(int value) {
        this.value = value;
    }

}
