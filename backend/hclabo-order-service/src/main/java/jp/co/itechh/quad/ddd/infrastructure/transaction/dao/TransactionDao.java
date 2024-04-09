package jp.co.itechh.quad.ddd.infrastructure.transaction.dao;

import jp.co.itechh.quad.ddd.infrastructure.transaction.dbentity.TransactionDbEntity;
import jp.co.itechh.quad.ddd.infrastructure.transaction.dto.GetByDraftStatusResultDto;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Stream;

/**
 * 取引Daoクラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Dao
@ConfigAutowireable
public interface TransactionDao {

    /**
     * インサート
     *
     * @param transactionDbEntity 取引DbEntity
     * @return 処理件数
     */
    @Insert(excludeNull = true)
    int insert(TransactionDbEntity transactionDbEntity);

    /**
     * アップデート
     *
     * @param transactionDbEntity 取引DbEntity
     * @return 処理件数
     */
    @Update
    int update(TransactionDbEntity transactionDbEntity);

    /**
     * 取引IDで取引DbEntity取得<br/>
     *
     * @param transactionId 取引ID
     * @return 取引DbEntity
     */
    @Select
    TransactionDbEntity getByTransactionId(String transactionId);

    /**
     * 未入金取引DbEntity一覧取得<br/>
     *
     * @return 取引DbEntity 一覧
     */
    @Select
    List<TransactionDbEntity> getUnpaidTransactionList();

    /**
     * デリート
     */
    @Delete(sqlFile = true)
    int clearTransaction(Timestamp deleteTime);

    /**
     * 支払督促リスト
     *
     * @return 支払督促DTOリスト
     */
    @Select
    List<GetByDraftStatusResultDto> getReminderPayment();

    /**
     * 支払期限切れリスト
     *
     * @return　支払期限切れDTOリスト
     */
    @Select
    List<GetByDraftStatusResultDto> getExpiredPayment();

    /**
     * 取引IDでノベルティに関しての取引DbEntity取得<br/>
     *
     * @param transactionId 取引ID
     * @return 取引DbEntity
     */
    @Select
    TransactionDbEntity getByTransactionIdForNovelty(String transactionId);

    /**
     * 指定日時以前の下書き状態の取引データ取得<br/>
     *
     * @param periodTimeCancelUnopenBatch 取引ID
     * @return
     */
    @Select
    Stream<GetByDraftStatusResultDto> getByDraftStatusList(Timestamp periodTimeCancelUnopenBatch);

    /**
     * デリート
     *
     * @param transactionId 取引ID
     */
    @Delete(sqlFile = true)
    int deleteTransaction(String transactionId);

}