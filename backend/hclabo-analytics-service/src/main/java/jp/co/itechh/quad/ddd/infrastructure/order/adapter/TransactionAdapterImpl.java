package jp.co.itechh.quad.ddd.infrastructure.order.adapter;

import jp.co.itechh.quad.core.web.HeaderParamsUtility;
import jp.co.itechh.quad.ddd.domain.order.adapter.ITransactionAdapter;
import jp.co.itechh.quad.ddd.domain.order.adapter.model.TransactionForRevision;
import jp.co.itechh.quad.transaction.presentation.api.TransactionApi;
import jp.co.itechh.quad.transaction.presentation.api.param.GetTransactionForRevisionRequest;
import jp.co.itechh.quad.transaction.presentation.api.param.TransactionForRevisionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 取引エアダプター実装クラス
 */
@Component
public class TransactionAdapterImpl implements ITransactionAdapter {

    /**
     * 取引エAPI
     */
    private final TransactionApi transactionApi;

    /**
     * 取引エアダプターHelperクラス
     */
    private final TransactionAdapterHelper transactionAdapterHelper;

    /**
     * コンストラクタ
     *
     * @param transactionApi           取引エAPI
     * @param transactionAdapterHelper 取引エアダプターHelperクラス
     * @param headerParamsUtil         ヘッダパラメーターユーティル
     */
    @Autowired
    public TransactionAdapterImpl(TransactionApi transactionApi,
                                  TransactionAdapterHelper transactionAdapterHelper,
                                  HeaderParamsUtility headerParamsUtil) {
        this.transactionApi = transactionApi;
        this.transactionAdapterHelper = transactionAdapterHelper;

        // サービス間のAPI呼び出す時、ヘッダーにパラメーターを設定する
        headerParamsUtil.setHeader(this.transactionApi.getApiClient());
    }

    /**
     * 改訂用取引取得
     *
     * @param transactionRevisionId 改訂用取引ID
     * @return 改訂用取引
     */
    @Override
    public TransactionForRevision getTransactionForRevision(String transactionRevisionId) {
        GetTransactionForRevisionRequest transactionForRevisionRequest = new GetTransactionForRevisionRequest();
        transactionForRevisionRequest.setTransactionRevisionId(transactionRevisionId);

        TransactionForRevisionResponse transactionForRevisionResponse =
                        transactionApi.getTransactionForRevision(transactionForRevisionRequest);

        return transactionAdapterHelper.toTransactionForRevision(transactionForRevisionResponse);
    }
}