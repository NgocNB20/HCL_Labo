/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.inventory.valueobject;

import jp.co.itechh.quad.ddd.exception.AssertChecker;
import lombok.Getter;

/**
 * 商品数量 値オブジェクト
 */
@Getter
public class ItemCount {

    /** 値(正の整数) */
    private final int value;

    /**
     * コンストラクタ
     *
     * @param value
     */
    public ItemCount(int value) {

        // チェック
        AssertChecker.assertIntegerNotNegatives("ItemCount not true", value);

        // 設定
        this.value = value;
    }

}