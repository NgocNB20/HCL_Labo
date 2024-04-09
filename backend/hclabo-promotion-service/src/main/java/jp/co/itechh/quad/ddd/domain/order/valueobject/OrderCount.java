/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.order.valueobject;

import jp.co.itechh.quad.ddd.exception.AssertChecker;
import lombok.Getter;

/**
 * 注文数量 値オブジェクト
 */
@Getter
public class OrderCount {

    /** 値(正の整数) */
    private final int value;

    /**
     * コンストラクタ
     * @param value
     */
    public OrderCount(int value) {

        // チェック
        AssertChecker.assertIntegerNotNegative("orderCount", value);

        // 設定
        this.value = value;
    }

}
