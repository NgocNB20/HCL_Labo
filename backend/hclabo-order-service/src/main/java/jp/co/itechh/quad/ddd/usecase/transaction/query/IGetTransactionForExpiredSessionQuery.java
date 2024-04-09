package jp.co.itechh.quad.ddd.usecase.transaction.query;

/**
 * GMOからのOrderCodeを使用して取引IDを取得するクエリ
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
public interface IGetTransactionForExpiredSessionQuery {

    /**
     * GMOからのOrderCodeを使用して取引IDを取得する
     *
     * @param orderCode 受注番号
     * @return transactionId 取引ID
     */
    String getTransactionIdByOrderCode(String orderCode);
}
