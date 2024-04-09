/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.sales.valueobject;

import jp.co.itechh.quad.ddd.exception.AssertChecker;
import lombok.Getter;

/**
 * 調整金額連番 値オブジェクト
 */
@Getter
public class AdjustmentAmountSeq {

    /** 値(正の整数) */
    private final int value;

    /**
     * コンストラクタ
     * @param value
     */
    public AdjustmentAmountSeq(int value) {

        // チェック
        AssertChecker.assertIntegerNotNegative("AdjustmentAmountSeq", value);

        // 設定
        this.value = value;
    }
}