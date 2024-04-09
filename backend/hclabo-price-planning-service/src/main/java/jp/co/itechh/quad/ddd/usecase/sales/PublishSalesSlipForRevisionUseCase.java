/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.sales;

import jp.co.itechh.quad.ddd.domain.sales.entity.SalesSlipEntity;
import jp.co.itechh.quad.ddd.domain.sales.entity.SalesSlipForRevisionEntity;
import jp.co.itechh.quad.ddd.domain.sales.repository.ISalesSlipForRevisionRepository;
import jp.co.itechh.quad.ddd.domain.sales.repository.ISalesSlipRepository;
import jp.co.itechh.quad.ddd.exception.DomainException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 改訂用販売伝票発行 ユースケース
 */
@Service
public class PublishSalesSlipForRevisionUseCase {

    /** 販売伝票集約リポジトリ */
    private final ISalesSlipRepository salesSlipRepository;

    /** 改訂用販売伝票集約リポジトリ */
    private final ISalesSlipForRevisionRepository salesSlipForRevisionRepository;

    /**
     * コンストラクタ
     *
     * @param salesSlipRepository
     * @param salesSlipForRevisionRepository
     */
    @Autowired
    public PublishSalesSlipForRevisionUseCase(ISalesSlipRepository salesSlipRepository,
                                              ISalesSlipForRevisionRepository salesSlipForRevisionRepository) {
        this.salesSlipRepository = salesSlipRepository;
        this.salesSlipForRevisionRepository = salesSlipForRevisionRepository;
    }

    /**
     * 改訂用販売伝票を発行する
     *
     * @param transactionId
     * @param transactionRevisionId
     */
    public void publishSalesSlip(String transactionId, String transactionRevisionId) {

        // 取引IDに紐づく販売伝票を取得する
        SalesSlipEntity salesSlipEntity = salesSlipRepository.getByTransactionId(transactionId);
        // 販売伝票が存在しない場合はエラー
        if (salesSlipEntity == null) {
            throw new DomainException("PRICE-PLANNING-SASE0001-E", new String[] {transactionId});
        }

        // 改訂用販売伝票発行
        SalesSlipForRevisionEntity salesSlipForRevisionEntity =
                        new SalesSlipForRevisionEntity(salesSlipEntity, transactionRevisionId, new Date());

        // 改訂用販売伝票を登録する
        salesSlipForRevisionRepository.save(salesSlipForRevisionEntity);
    }
}