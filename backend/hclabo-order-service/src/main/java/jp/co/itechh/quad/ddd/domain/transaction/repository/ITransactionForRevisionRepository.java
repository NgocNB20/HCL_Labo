/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.transaction.repository;

import jp.co.itechh.quad.ddd.domain.transaction.entity.TransactionForRevisionEntity;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.TransactionRevisionId;

/**
 * 改訂用取引リポジトリ
 */
public interface ITransactionForRevisionRepository {

    /**
     * 改訂用取引取得
     *
     * @param transactionRevisionId
     * @return TransactionForRevisionEntity
     */
    TransactionForRevisionEntity get(TransactionRevisionId transactionRevisionId);

    /**
     * 改訂用取引登録
     *
     * @param transactionForRevisionEntity
     */
    int save(TransactionForRevisionEntity transactionForRevisionEntity);

    /**
     * 改訂用取引更新
     *
     * @param transactionForRevisionEntity
     * @return 更新件数
     */
    int update(TransactionForRevisionEntity transactionForRevisionEntity);

}