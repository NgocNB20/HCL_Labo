package jp.co.itechh.quad.ddd.infrastructure.order.adapter;

import jp.co.itechh.quad.ddd.domain.order.adapter.model.TransactionForRevision;
import jp.co.itechh.quad.transaction.presentation.api.param.TransactionForRevisionResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

/**
 * 取引エアダプターHelperクラス
 */
@Component
public class TransactionAdapterHelper {

    /**
     * 改訂用取引に変換
     *
     * @param transactionForRevisionResponse 改訂用取引取得レスポンス
     * @return 改訂用取引
     */
    public TransactionForRevision toTransactionForRevision(TransactionForRevisionResponse transactionForRevisionResponse) {
        if (ObjectUtils.isEmpty(transactionForRevisionResponse)) {
            return null;
        }

        TransactionForRevision transactionForRevision = new TransactionForRevision();

        transactionForRevision.setShippedFlag(transactionForRevisionResponse.getShippedFlag());
        transactionForRevision.setProcessTime(transactionForRevisionResponse.getRegistDate());

        return transactionForRevision;
    }

}