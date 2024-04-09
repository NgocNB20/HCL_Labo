/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.transaction.repository;

import jp.co.itechh.quad.ddd.domain.transaction.entity.TransactionEntity;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.TransactionId;
import jp.co.itechh.quad.ddd.infrastructure.transaction.dto.GetByDraftStatusResultDto;

import java.util.List;

/**
 * 取引リポジトリ
 */
public interface ITransactionRepository {

    /**
     * 取引登録
     *
     * @param transactionEntity 取引
     */
    void save(TransactionEntity transactionEntity);

    /**
     * 取引更新
     *
     * @param transactionEntity 取引
     * @return 更新件数
     */
    int update(TransactionEntity transactionEntity);

    /**
     * 取引取得
     *
     * @param transactionId 取引ID
     * @return TransactionEntity 取引
     */
    TransactionEntity get(TransactionId transactionId);

    /**
     * 督促対象受注一覧
     *
     * @return OrderPaymentDtoリスト
     */
    List<GetByDraftStatusResultDto> getReminderPayment();

    /**
     * 期限切れ対象受注一覧
     *
     * @return 支払期限切れDTOリスト
     */
    List<GetByDraftStatusResultDto> getExpiredPayment();

    /**
     * 未入金取引一覧取得
     *
     * @return 取引一覧
     */
    List<TransactionEntity> getUnpaidTransactionList();

    /**
     * ノベルティに関しての取引取得
     *
     * @param transactionId 取引ID
     * @return TransactionEntity 取引
     */
    TransactionEntity getForNovelty(TransactionId transactionId);

}
