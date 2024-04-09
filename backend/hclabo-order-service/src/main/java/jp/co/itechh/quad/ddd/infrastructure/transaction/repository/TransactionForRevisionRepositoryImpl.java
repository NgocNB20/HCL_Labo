package jp.co.itechh.quad.ddd.infrastructure.transaction.repository;

import jp.co.itechh.quad.ddd.domain.transaction.entity.TransactionForRevisionEntity;
import jp.co.itechh.quad.ddd.domain.transaction.repository.ITransactionForRevisionRepository;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.TransactionRevisionId;
import jp.co.itechh.quad.ddd.infrastructure.transaction.dao.TransactionForRevisionDao;
import jp.co.itechh.quad.ddd.infrastructure.transaction.dbentity.TransactionForRevisionDbEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 改訂用取引リポジトリ実装クラス
 */
@Component
public class TransactionForRevisionRepositoryImpl implements ITransactionForRevisionRepository {

    /** 改訂用取引Daoクラス */
    private final TransactionForRevisionDao transactionForRevisionDao;

    /** 改訂用取引Helperクラス */
    private final TransactionForRevisionRepositoryHelper transactionForRevisionRepositoryHelper;

    /**
     * コンストラクタ.
     *
     * @param transactionForRevisionDao              改訂用取引Daoクラス
     * @param transactionForRevisionRepositoryHelper 改訂用取引Helperクラス
     */
    @Autowired
    public TransactionForRevisionRepositoryImpl(TransactionForRevisionDao transactionForRevisionDao,
                                                TransactionForRevisionRepositoryHelper transactionForRevisionRepositoryHelper) {
        this.transactionForRevisionDao = transactionForRevisionDao;
        this.transactionForRevisionRepositoryHelper = transactionForRevisionRepositoryHelper;
    }

    /**
     * 取引取得
     *
     * @param transactionRevisionId 値オブジェクト
     * @return　TransactionForRevisionEntity 改訂用取引エンティティ
     */
    @Override
    public TransactionForRevisionEntity get(TransactionRevisionId transactionRevisionId) {

        TransactionForRevisionDbEntity transactionForRevisionDbEntity =
                        transactionForRevisionDao.getByTransactionRevisionId(transactionRevisionId.getValue());

        return transactionForRevisionRepositoryHelper.toTransactionForRevisionEntity(transactionForRevisionDbEntity);
    }

    /**
     * 取引登録
     *
     * @param transactionForRevisionEntity 改訂用取引エンティティ
     */
    @Override
    public int save(TransactionForRevisionEntity transactionForRevisionEntity) {
        TransactionForRevisionDbEntity transactionForRevisionDbEntity =
                        transactionForRevisionRepositoryHelper.copyPropertiesFromEntityToDbEntity(
                                        transactionForRevisionEntity);

        return transactionForRevisionDao.insert(transactionForRevisionDbEntity);
    }

    /**
     * 取引更新
     *
     * @param transactionForRevisionEntity 改訂用取引エンティティ
     * @return 更新件数
     */
    @Override
    public int update(TransactionForRevisionEntity transactionForRevisionEntity) {

        TransactionForRevisionDbEntity transactionForRevisionDbEntity =
                        transactionForRevisionRepositoryHelper.copyPropertiesFromEntityToDbEntity(
                                        transactionForRevisionEntity);

        return transactionForRevisionDao.update(transactionForRevisionDbEntity);
    }
}