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
 * 改訂用販売伝票に調整金額を追加 ユースケース
 */
@Service
public class AddAdjustmentAmountToSalesSlipForRevisionUseCase {

    /** 改訂用販売伝票集約リポジトリ */
    private final ISalesSlipForRevisionRepository salesSlipForRevisionRepository;

    /** コンストラクタ */
    @Autowired
    public AddAdjustmentAmountToSalesSlipForRevisionUseCase(ISalesSlipForRevisionRepository salesSlipForRevisionRepository) {
        this.salesSlipForRevisionRepository = salesSlipForRevisionRepository;
    }

    /**
     * 改訂用販売伝票に調整金額を追加
     *
     * @param transactionRevisionId
     * @param adjustName
     * @param adjustPrice
     */
    public void addAdjustmentAmountToSalesSlipForRevision(String transactionRevisionId,
                                                          String adjustName,
                                                          int adjustPrice) {

        // 改訂用取引IDに紐づく改訂用販売伝票を取得する
        SalesSlipForRevisionEntity salesSlipForRevisionEntity =
                        salesSlipForRevisionRepository.getByTransactionRevisionId(transactionRevisionId);
        if (salesSlipForRevisionEntity == null) {
            throw new DomainException("PRICE-PLANNING-SEVE0001-E", new String[] {transactionRevisionId});
        }

        // 調整金額追加
        salesSlipForRevisionEntity.addAdjustmentAmount(adjustName, adjustPrice);

        // 販売伝票を更新する
        salesSlipForRevisionRepository.update(salesSlipForRevisionEntity);
    }
}