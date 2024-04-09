/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.sales;

import jp.co.itechh.quad.ddd.domain.sales.entity.SalesSlipEntity;
import jp.co.itechh.quad.ddd.domain.sales.repository.ISalesSlipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 取引IDに紐づく販売伝票取得 ユースケース
 */
@Service
public class GetSalesSlipByTransactionIdUseCase {

    /** 販売伝票集約リポジトリ */
    private final ISalesSlipRepository salesSlipRepository;

    /**
     * コンストラクタ
     *
     * @param salesSlipRepository
     */
    @Autowired
    public GetSalesSlipByTransactionIdUseCase(ISalesSlipRepository salesSlipRepository) {
        this.salesSlipRepository = salesSlipRepository;
    }

    /**
     * 取引IDに紐づく販売伝票を取得する
     *
     * @param transactionId
     * @return 存在する ... shippingSlipEntity / 存在しない ... null
     */
    public SalesSlipEntity getSalesSlipByTransactionId(String transactionId) {

        // 取引IDに紐づく販売伝票を取得する
        SalesSlipEntity salesSlipEntity = salesSlipRepository.getByTransactionId(transactionId);

        return salesSlipEntity;
    }

}
