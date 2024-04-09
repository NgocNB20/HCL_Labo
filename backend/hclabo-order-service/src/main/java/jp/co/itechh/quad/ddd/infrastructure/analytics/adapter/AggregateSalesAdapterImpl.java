/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.infrastructure.analytics.adapter;

import jp.co.itechh.quad.core.web.HeaderParamsUtility;
import jp.co.itechh.quad.ddd.domain.analytics.adapter.IAggregateSalesAdapter;
import jp.co.itechh.quad.reports.presentation.api.ReportsApi;
import jp.co.itechh.quad.reports.presentation.api.param.ReportRegistRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 集計用販売アダプター
 *
 * @author PHAM QUANG DIEU (VJP)
 */
@Component
public class AggregateSalesAdapterImpl implements IAggregateSalesAdapter {

    /** 受注検索API */
    private final ReportsApi reportsApi;

    /** コンストラクタ */
    @Autowired
    public AggregateSalesAdapterImpl(ReportsApi reportsApi, HeaderParamsUtility headerParamsUtil) {
        this.reportsApi = reportsApi;

        // サービス間のAPI呼び出す時、ヘッダーにパラメーターを設定する
        headerParamsUtil.setHeader(this.reportsApi.getApiClient());
    }

    /**
     * 分析マイクロサービス<br/>
     * 集計用販売データ登録
     *
     * @param revisedTransactionId 改訂後取引ID
     * @param transactionIdBeforeRevision 改訂前取引ID
     */
    @Override
    public void aggregateSalesData(String revisedTransactionId, String transactionIdBeforeRevision) {
        ReportRegistRequest reportRegistRequest = new ReportRegistRequest();
        reportRegistRequest.setTransactionRevisionId(revisedTransactionId);
        reportRegistRequest.setTransactionId(transactionIdBeforeRevision);

        reportsApi.registReportData(reportRegistRequest);
    }
}