/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.pricecalculator.valueobject;

import lombok.Getter;

/**
 * クーポン支払い額 値オブジェクト
 * ※ファクトリあり
 */
@Getter
public class CouponPaymentPrice {

    /** 値 */
    private final int value;

    /**
     * ファクトリ用コンストラクタ
     *
     * @param value
     */
    public CouponPaymentPrice(int value) {
        this.value = value;
    }

}