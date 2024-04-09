/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.transaction;

import jp.co.itechh.quad.ddd.domain.transaction.entity.TransactionForRevisionEntity;
import jp.co.itechh.quad.ddd.domain.transaction.repository.ITransactionForRevisionRepository;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.TransactionRevisionId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 改訂用取引取得ユースケース
 */
@Service
public class GetTransactionForRevisionUseCase {

    /** 改訂用取引リポジトリ */
    private final ITransactionForRevisionRepository transactionForRevisionRepository;

    /** コンストラクタ */
    @Autowired
    public GetTransactionForRevisionUseCase(ITransactionForRevisionRepository transactionForRevisionRepository) {
        this.transactionForRevisionRepository = transactionForRevisionRepository;
    }

    /**
     * 改訂用取引取得
     *
     * @param transactionRevisionId 改訂用取引ID
     * @return TransactionForRevisionEntity
     */
    public TransactionForRevisionEntity getTransactionForRevision(String transactionRevisionId) {

        // 改訂用取引を取得
        return transactionForRevisionRepository.get(new TransactionRevisionId(transactionRevisionId));
    }

}
