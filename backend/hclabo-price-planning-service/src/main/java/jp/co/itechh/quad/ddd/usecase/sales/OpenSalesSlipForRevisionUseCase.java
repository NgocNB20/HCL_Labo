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
 * 改訂用販売伝票を確定する ユースケース
 */
@Service
public class OpenSalesSlipForRevisionUseCase {

    /** 改訂用販売伝票集約リポジトリ */
    private final ISalesSlipForRevisionRepository salesSlipForRevisionRepository;

    /** 販売伝票集約リポジトリ */
    private final ISalesSlipRepository salesSlipRepository;

    /** コンストラクタ */
    @Autowired
    public OpenSalesSlipForRevisionUseCase(ISalesSlipForRevisionRepository salesSlipForRevisionRepository,
                                           ISalesSlipRepository salesSlipRepository) {
        this.salesSlipForRevisionRepository = salesSlipForRevisionRepository;
        this.salesSlipRepository = salesSlipRepository;
    }

    /**
     * 改訂用販売伝票を確定する
     *
     * @param transactionRevisionId
     */
    public void openSalesSlipForRevision(String transactionRevisionId) {

        // 改訂用取引IDに紐づく改訂用販売伝票を取得する
        SalesSlipForRevisionEntity salesSlipForRevisionEntity =
                        salesSlipForRevisionRepository.getByTransactionRevisionId(transactionRevisionId);
        if (salesSlipForRevisionEntity == null) {
            throw new DomainException("PRICE-PLANNING-SEVE0001-E", new String[] {transactionRevisionId});
        }

        // 販売伝票改訂
        SalesSlipEntity salesSlipEntity = new SalesSlipEntity(salesSlipForRevisionEntity, new Date());

        // 改訂済み販売伝票を登録
        salesSlipRepository.save(salesSlipEntity);
    }
}