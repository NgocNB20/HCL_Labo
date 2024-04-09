/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.analytics.adapter;

/**
 * 集計用販売アダプター
 *
 * @author PHAM QUANG DIEU (VJP)
 */
public interface IAggregateSalesAdapter {

    /**
     * 分析マイクロサービス<br/>
     * 集計用販売データ登録
     *
     * @param revisedTransactionId 改訂後取引ID
     * @param transactionIdBeforeRevision 改訂前取引ID
     */
    void aggregateSalesData(String revisedTransactionId, String transactionIdBeforeRevision);
}
