/*
 * Copyright (C) 2023 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.hclabo.ddd.domain.examination.valueobject;

import jp.co.itechh.quad.hclabo.ddd.exception.AssertChecker;
import lombok.Getter;

/**
 * 表示順 値オブジェクト
 */
@Getter
public class OrderDisplay {

    /** 値(正の整数) */
    private final int value;

    /**
     * コンストラクタ
     *
     * @param value 表示順バリュー
     */
    public OrderDisplay(int value) {

        // チェック
        AssertChecker.assertIntegerNotNegatives("orderDisplay is null", value);

        // 設定
        this.value = value;
    }

}
