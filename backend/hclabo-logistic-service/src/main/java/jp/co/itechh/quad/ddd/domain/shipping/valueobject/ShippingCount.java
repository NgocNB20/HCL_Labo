/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.shipping.valueobject;

import jp.co.itechh.quad.ddd.exception.AssertChecker;
import lombok.Getter;

/**
 * 配送数量 値オブジェクト
 */
@Getter
public class ShippingCount {

    /** 値(正の整数) */
    private final int value;

    /**
     * コンストラクタ
     *
     * @param value
     */
    public ShippingCount(int value) {

        // チェック
        AssertChecker.assertIntegerNotNegatives("shippingCount is null", value);

        // 設定
        this.value = value;
    }

}
