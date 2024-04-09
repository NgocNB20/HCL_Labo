/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.sales.valueobject;

import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 調整金額連番 値オブジェクト ファクトリ
 */
public class AdjustmentAmountSeqFactory {

    /**
     * 調整金額連番 値オブジェクト 生成
     *
     * @param adjustmentAmountList
     */
    public static AdjustmentAmountSeq constructAdjustmentAmountSeq(List<AdjustmentAmount> adjustmentAmountList) {

        int adjustmentAmountSeqVal = 0;

        if (CollectionUtils.isEmpty(adjustmentAmountList)) {
            return new AdjustmentAmountSeq(adjustmentAmountSeqVal);
        }

        // 調整金額連番の最大値を設定
        for (AdjustmentAmount AdjustmentAmount : adjustmentAmountList) {
            if (adjustmentAmountSeqVal < AdjustmentAmount.getAdjustmentAmountSeq().getValue()) {
                adjustmentAmountSeqVal = AdjustmentAmount.getAdjustmentAmountSeq().getValue();
            }
        }
        return new AdjustmentAmountSeq(++adjustmentAmountSeqVal);
    }
}