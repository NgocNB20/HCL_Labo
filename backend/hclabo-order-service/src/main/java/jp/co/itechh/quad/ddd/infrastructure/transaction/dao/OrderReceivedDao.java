package jp.co.itechh.quad.ddd.infrastructure.transaction.dao;

import jp.co.itechh.quad.ddd.infrastructure.transaction.dbentity.OrderReceivedDbEntity;
import jp.co.itechh.quad.ddd.infrastructure.transaction.dto.CustomerOrderHistoryBaseInfoDto;
import jp.co.itechh.quad.ddd.infrastructure.transaction.dto.OrderProcessHistoryDto;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.SelectOptions;

import java.sql.Timestamp;
import java.util.List;

/**
 * 受注Daoクラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Dao
@ConfigAutowireable
public interface OrderReceivedDao {

    /**
     * インサート
     *
     * @param orderReceivedDbEntity 受注Dbエンティティ
     * @return 処理件数
     */
    @Insert(excludeNull = true)
    int insert(OrderReceivedDbEntity orderReceivedDbEntity);

    /**
     * アップデート（改訂元取引IDチェック）
     *
     * @param orderReceivedDbEntity 受注Dbエンティティ
     * @param originTransactionId   改訂元取引ID
     * @return 処理件数
     */
    @Update(sqlFile = true)
    int updateWithDbTranCheck(OrderReceivedDbEntity orderReceivedDbEntity, String originTransactionId);

    /**
     * 受注番号用連番取得
     *
     * @return 受注番号用連番
     */
    @Select
    int getOrderCodeSeq();

    /**
     * 受注Dbエンティティ取得
     *
     * @param orderReceivedId 受注ID
     * @return 受注Dbエンティティ
     */
    @Select
    OrderReceivedDbEntity getByOrderReceivedId(String orderReceivedId);

    /**
     * 受注Dbエンティティ取得
     *
     * @param latestTransactionId 取引ID
     * @return 受注Dbエンティティ
     */
    @Select
    OrderReceivedDbEntity getByLatestTransactionId(String latestTransactionId);

    /**
     * 受注Dbエンティティ取得
     *
     * @param orderCode 受注番号
     * @return 受注Dbエンティティ
     */
    @Select
    OrderReceivedDbEntity getByOrderCode(String orderCode);

    /**
     * 受注処理履歴一覧取得
     *
     * @param orderCode
     * @return List<OrderProcessHistoryDto>
     */
    @Select
    List<OrderProcessHistoryDto> getOrderProcessHistoryList(String orderCode);

    /**
     * 顧客注文履歴一覧取得
     *
     * @param customerId
     * @return List<CustomerOrderHistoryBaseInfoDto>
     */
    @Select
    List<CustomerOrderHistoryBaseInfoDto> getCustomerOrderHistoryList(String customerId, SelectOptions selectOptions);

    /**
     * 顧客ごとの受注件数取得
     *
     * @param customerId
     * @return 顧客ごとの受注件数
     */
    @Select
    int getOrderReceivedCountByCustomerId(String customerId);

    /**
     * デリート
     */
    @Delete(sqlFile = true)
    int clearOrderReceived(Timestamp deleteTime);

    /**
     * GMOからのOrderCodeを使用して取引IDを取得する
     *
     * @param orderCode 受注番号
     * @return transactionId 取引ID
     */
    @Select
    String getTransactionIdByOrderCodeExpiredSession(String orderCode);

    /**
     * デリート
     *
     * @param transactionId 受注ID
     */
    @Delete(sqlFile = true)
    int deleteOrderReceived(String transactionId);
}
