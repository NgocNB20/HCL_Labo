package jp.co.itechh.quad.ddd.domain.order.adapter;

import jp.co.itechh.quad.ddd.domain.order.adapter.model.TransactionForRevision;

/**
 * 取引エアダプター
 */
public interface ITransactionAdapter {

    /**
     * 改訂用取引取得
     *
     * @param transactionRevisionId 改訂用取引ID
     * @return
     */
    TransactionForRevision getTransactionForRevision(String transactionRevisionId);

}
