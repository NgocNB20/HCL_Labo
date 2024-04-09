/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.sales.valueobject;

import jp.co.itechh.quad.ddd.exception.AssertChecker;
import lombok.Getter;

/**
 * 調整金額 値オブジェクト
 */
@Getter
public class AdjustmentAmount {

    /** 調整金額連番 */
    private final AdjustmentAmountSeq adjustmentAmountSeq;

    /** 調整項目名 */
    private final String adjustName;

    /** 調整金額 */
    private final int adjustPrice;

    /** コンストラクタ */
    public AdjustmentAmount(AdjustmentAmountSeq adjustmentAmountSeq, String adjustName, int adjustPrice) {
        // チェック
        AssertChecker.assertNotNull("adjustName is null", adjustmentAmountSeq);
        AssertChecker.assertNotEmpty("adjustName is empty", adjustName);

        // 設定
        this.adjustmentAmountSeq = adjustmentAmountSeq;
        this.adjustName = adjustName;
        this.adjustPrice = adjustPrice;

    }

}