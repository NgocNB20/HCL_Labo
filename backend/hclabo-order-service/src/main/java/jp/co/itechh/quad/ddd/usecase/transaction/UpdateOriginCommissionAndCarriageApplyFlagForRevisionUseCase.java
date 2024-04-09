/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.transaction;

import jp.co.itechh.quad.ddd.domain.priceplanning.adapter.ISalesSlipAdapter;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.TransactionRevisionId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 改訂用取引の改訂前手数料/送料適用フラグ更新 ユースケース
 */
@Service
public class UpdateOriginCommissionAndCarriageApplyFlagForRevisionUseCase {

    /** 販売伝票アダプター */
    private final ISalesSlipAdapter salesSlipAdapter;

    /** コンストラクタ */
    @Autowired
    public UpdateOriginCommissionAndCarriageApplyFlagForRevisionUseCase(ISalesSlipAdapter salesSlipAdapter) {
        this.salesSlipAdapter = salesSlipAdapter;
    }

    /**
     * 改訂前手数料/送料適用フラグを更新
     *
     * @param transactionRevisionId
     * @param originCommissionApplyFlag
     * @param originCarriageApplyFlag
     */
    public void updateCouponUseFlagOfSalesSlipForRevision(String transactionRevisionId,
                                                          boolean originCommissionApplyFlag,
                                                          boolean originCarriageApplyFlag) {

        salesSlipAdapter.updateOriginCommissionAndCarriageApplyFlagForRevisionUseCase(
                        new TransactionRevisionId(transactionRevisionId), originCommissionApplyFlag,
                        originCarriageApplyFlag
                                                                                     );
    }
}