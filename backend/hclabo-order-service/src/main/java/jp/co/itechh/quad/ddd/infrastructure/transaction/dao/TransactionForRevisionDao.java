package jp.co.itechh.quad.ddd.infrastructure.transaction.dao;

import jp.co.itechh.quad.ddd.infrastructure.transaction.dbentity.TransactionForRevisionDbEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;

import java.sql.Timestamp;
import java.util.stream.Stream;

/**
 * 改訂用取引Daoクラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Dao
@ConfigAutowireable
public interface TransactionForRevisionDao {

    /**
     * インサート
     *
     * @param transactionForRevisionDbEntity 改訂用取引DbEntityクラス
     * @return 処理件数
     */
    @Insert(excludeNull = true)
    int insert(TransactionForRevisionDbEntity transactionForRevisionDbEntity);

    /**
     * アップデート
     *
     * @param transactionForRevisionDbEntity 改訂用取引DbEntityクラス
     * @return 処理件数
     */
    @Update
    int update(TransactionForRevisionDbEntity transactionForRevisionDbEntity);

    /**
     * 取引DbEntity取得
     *
     * @param transactionRevisionId 取引ID
     * @return 改訂用取引ID
     */
    @Select
    TransactionForRevisionDbEntity getByTransactionRevisionId(String transactionRevisionId);

    /**
     * 改訂用取引データ（未確定）取得
     *
     * @param periodTimeCancelUnopenBatch 対象期間from
     * @return 改訂用取引IDリスト
     */
    @Select
    Stream<String> getTransactionForRevisionUnconfirmedList(Timestamp periodTimeCancelUnopenBatch);

    /**
     * デリート
     *
     * @param transactionRevisionId 取引ID
     */
    @Delete(sqlFile = true)
    int deleteTransactionForRevision(String transactionRevisionId);

    /**
     * デリート
     *
     * @param periodTimeCancelUnopenBatch 取引ID
     */
    @Delete(sqlFile = true)
    int deleteTransactionForRevisionConfirmed(Timestamp periodTimeCancelUnopenBatch);

}
