/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.sales;

import jp.co.itechh.quad.ddd.domain.sales.entity.SalesSlipForRevisionEntity;
import jp.co.itechh.quad.ddd.domain.sales.repository.ISalesSlipForRevisionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 改訂用取引IDに紐づく改訂用販売伝票取得 ユースケース
 */
@Service
public class GetSalesSlipForRevisionByTransactionRevisionIdUseCase {

    /** 改訂用販売伝票集約リポジトリ */
    private final ISalesSlipForRevisionRepository salesSlipForRevisionRepository;

    /** コンストラクタ */
    @Autowired
    public GetSalesSlipForRevisionByTransactionRevisionIdUseCase(ISalesSlipForRevisionRepository salesSlipForRevisionRepository) {
        this.salesSlipForRevisionRepository = salesSlipForRevisionRepository;
    }

    /**
     * 改訂用取引IDに紐づく改訂用販売伝票取得
     *
     * @param transactionRevisionId
     * @return 存在する ... SalesSlipForRevisionEntity / 存在しない ... null
     */
    public SalesSlipForRevisionEntity getSalesSlipByTransactionId(String transactionRevisionId) {

        // 改訂用取引IDに紐づく改訂用販売伝票を取得する
        SalesSlipForRevisionEntity salesSlipForRevisionEntity =
                        salesSlipForRevisionRepository.getByTransactionRevisionId(transactionRevisionId);

        return salesSlipForRevisionEntity;
    }

}