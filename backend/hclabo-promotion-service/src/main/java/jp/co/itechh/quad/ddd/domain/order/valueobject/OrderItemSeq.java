/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.order.valueobject;

import jp.co.itechh.quad.ddd.exception.AssertChecker;
import lombok.Getter;

/**
 * 注文商品連番 値オブジェクト
 */
@Getter
public class OrderItemSeq {

    /** 値(正の整数) */
    private final int value;

    /**
     * コンストラクタ
     * @param value
     */
    public OrderItemSeq(int value) {

        // チェック
        AssertChecker.assertIntegerNotNegative("OrderItemSeq", value);

        // 設定
        this.value = value;
    }

}
