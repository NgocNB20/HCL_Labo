/*
 * Copyright (C) 2023 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.sales;

import jp.co.itechh.quad.ddd.domain.sales.repository.ISalesSlipRepository;
import jp.co.itechh.quad.ddd.exception.AssertChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 販売伝票削除ユースケース
 */
@Service
public class DeleteSalesSlipUseCase {

    /** 注文票リポジトリ */
    private final ISalesSlipRepository salesSlipRepository;

    /** コンストラクタ */
    @Autowired
    public DeleteSalesSlipUseCase(ISalesSlipRepository salesSlipRepository) {
        this.salesSlipRepository = salesSlipRepository;
    }

    /**
     * 販売伝票票除する
     *
     * @param transactionId 取引ID
     */
    public void deleteUnnecessaryByTransactionId(String transactionId) {

        AssertChecker.assertNotEmpty("transactionId is empty", transactionId);

        // 配送伝票削除
        this.salesSlipRepository.deleteUnnecessaryByTransactionId(transactionId);
    }
}
