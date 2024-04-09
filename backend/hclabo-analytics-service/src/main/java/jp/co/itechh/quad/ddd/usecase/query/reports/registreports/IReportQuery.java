package jp.co.itechh.quad.ddd.usecase.query.reports.registreports;

/**
 * 集計用販売データ登録
 */
public interface IReportQuery {

    /**
     * 集計用販売データ登録
     *
     * @param orderSalesQueryModel 集計用販売データ
     */
    void regist(ReportsQueryModel orderSalesQueryModel);

    /**
     * 「改訂前取引ID」で1つ前の集計用販売データを取得
     *
     * @param transactionBeforeRevisionId 改訂前取引ID
     * @return 集計用販売データ（集計機能共通用データ）
     */
    ReportsQueryModel getByBeforeTransactionId(String transactionBeforeRevisionId);
}
