/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.transaction;

import jp.co.itechh.quad.ddd.domain.priceplanning.adapter.ISalesSlipAdapter;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.TransactionRevisionId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 改訂用取引のクーポンを無効化する ユースケース
 */
@Service
public class DisableCouponOfTransactionForRevisionUseCase {

    /** 販売伝票アダプター */
    private final ISalesSlipAdapter salesSlipAdapter;

    /** コンストラクタ */
    @Autowired
    public DisableCouponOfTransactionForRevisionUseCase(ISalesSlipAdapter salesSlipAdapter) {
        this.salesSlipAdapter = salesSlipAdapter;
    }

    /**
     * 改訂用取引のクーポンを無効化する
     *
     * @param transactionRevisionId 改訂用取引ID
     */
    public void disableCouponOfTransactionForRevision(String transactionRevisionId) {

        // 改訂用販売伝票のクーポン利用フラグを無効化
        salesSlipAdapter.updateCouponUseFlagOfSalesSlipForRevision(
                        new TransactionRevisionId(transactionRevisionId), false);

    }
}