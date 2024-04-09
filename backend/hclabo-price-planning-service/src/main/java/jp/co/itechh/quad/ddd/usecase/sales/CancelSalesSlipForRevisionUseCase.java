/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.sales;

import jp.co.itechh.quad.ddd.domain.sales.entity.SalesSlipForRevisionEntity;
import jp.co.itechh.quad.ddd.domain.sales.repository.ISalesSlipForRevisionRepository;
import jp.co.itechh.quad.ddd.exception.DomainException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 改訂用販売伝票取消ユースケース
 */
@Service
public class CancelSalesSlipForRevisionUseCase {

    /** 改訂用販売伝票リポジトリ */
    private final ISalesSlipForRevisionRepository salesSlipForRevisionRepository;

    /** コンストラクタ */
    @Autowired
    public CancelSalesSlipForRevisionUseCase(ISalesSlipForRevisionRepository salesSlipForRevisionRepository) {
        this.salesSlipForRevisionRepository = salesSlipForRevisionRepository;
    }

    /**
     * 改訂用販売伝票を取消する
     *
     * @param transactionRevisionId 改訂用取引ID
     */
    public void cancelSalesSlipForRevision(String transactionRevisionId) {

        // 改訂用取引IDに紐づく改訂用販売伝票を取得する
        SalesSlipForRevisionEntity salesSlipForRevisionEntity =
                        salesSlipForRevisionRepository.getByTransactionRevisionId(transactionRevisionId);
        if (salesSlipForRevisionEntity == null) {
            throw new DomainException("PRICE-PLANNING-SEVE0001-E", new String[] {transactionRevisionId});
        }

        // 改訂用伝票取消
        salesSlipForRevisionEntity.cancelSlip();

        // 販売伝票を更新する
        salesSlipForRevisionRepository.update(salesSlipForRevisionEntity);
    }

}