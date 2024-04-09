/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.infrastructure.transaction.repository;

import jp.co.itechh.quad.ddd.domain.transaction.entity.TransactionEntity;
import jp.co.itechh.quad.ddd.domain.transaction.repository.ITransactionRepository;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.TransactionId;
import jp.co.itechh.quad.ddd.infrastructure.transaction.dao.TransactionDao;
import jp.co.itechh.quad.ddd.infrastructure.transaction.dbentity.TransactionDbEntity;
import jp.co.itechh.quad.ddd.infrastructure.transaction.dto.GetByDraftStatusResultDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 取引リポジトリ実装クラス
 *
 * @author kimura
 */
@Component
public class TransactionRepositoryImpl implements ITransactionRepository {

    /** 取引Daoクラス */
    private final TransactionDao transactionDao;

    /** 取引リポジトリHelperクラス */
    private final TransactionRepositoryHelper transactionRepositoryHelper;

    /** コンストラクタ */
    @Autowired
    public TransactionRepositoryImpl(TransactionDao transactionDao,
                                     TransactionRepositoryHelper transactionRepositoryHelper) {
        this.transactionDao = transactionDao;
        this.transactionRepositoryHelper = transactionRepositoryHelper;
    }

    /**
     * 取引登録
     *
     * @param transactionEntity 取引
     */
    @Override
    public void save(TransactionEntity transactionEntity) {
        TransactionDbEntity transactionDbEntity = transactionRepositoryHelper.toTransactionDbEntity(transactionEntity);
        transactionDao.insert(transactionDbEntity);
    }

    /**
     * 取引更新
     *
     * @param transactionEntity 取引
     * @return 更新件数
     */
    @Override
    public int update(TransactionEntity transactionEntity) {
        TransactionDbEntity transactionDbEntity = transactionRepositoryHelper.toTransactionDbEntity(transactionEntity);
        return transactionDao.update(transactionDbEntity);
    }

    /**
     * 取引取得
     *
     * @param transactionId 取引ID
     * @return　TransactionEntity 取引
     */
    @Override
    public TransactionEntity get(TransactionId transactionId) {
        if (transactionId == null) {
            return null;
        }

        TransactionDbEntity transactionDbEntity = transactionDao.getByTransactionId(transactionId.getValue());
        return transactionRepositoryHelper.toTransactionEntity(transactionDbEntity);
    }

    /**
     * 未入金取引一覧取得
     *
     * @return 取引一覧
     */
    @Override
    public List<TransactionEntity> getUnpaidTransactionList() {

        List<TransactionDbEntity> transactionDbEntityList = transactionDao.getUnpaidTransactionList();
        return transactionRepositoryHelper.toTransactionEntityList(transactionDbEntityList);
    }

    /**
     * 支払督促リスト
     *
     * @return　支払督促リスト
     */
    @Override
    public List<GetByDraftStatusResultDto> getReminderPayment() {
        return transactionDao.getReminderPayment();
    }

    /**
     * 支払期限切れリスト
     *
     * @return　支払督促DTOリスト
     */
    @Override
    public List<GetByDraftStatusResultDto> getExpiredPayment() {
        return transactionDao.getExpiredPayment();
    }

    /**
     * ノベルティに関しての取引取得
     *
     * @param transactionId 取引ID
     * @return TransactionEntity 取引
     */
    @Override
    public TransactionEntity getForNovelty(TransactionId transactionId) {
        if (transactionId == null) {
            return null;
        }

        TransactionDbEntity transactionDbEntity = transactionDao.getByTransactionIdForNovelty(transactionId.getValue());
        return transactionRepositoryHelper.toTransactionEntity(transactionDbEntity);
    }
}