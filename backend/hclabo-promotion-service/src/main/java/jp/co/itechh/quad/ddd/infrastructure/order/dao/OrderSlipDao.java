package jp.co.itechh.quad.ddd.infrastructure.order.dao;

import jp.co.itechh.quad.ddd.infrastructure.order.dbentity.OrderSlipDbEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;

import java.sql.Timestamp;
import java.util.List;

/**
 * 注文票Daoクラス<br/>
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Dao
@ConfigAutowireable
public interface OrderSlipDao {

    /**
     * インサート<br/>
     *
     * @param orderSlipDbEntity
     * @return 処理件数
     */
    @Insert(excludeNull = true)
    int insert(OrderSlipDbEntity orderSlipDbEntity);

    /**
     * アップデート<br/>
     *
     * @param orderSlipDbEntity
     * @return 処理件数
     */
    @Update
    int update(OrderSlipDbEntity orderSlipDbEntity);

    /**
     * アップデート【注文確定用】<br/>
     *
     * @param orderSlipDbEntity 注文票DBエンティティ
     * @param transactionId 取引ID
     * @return 処理件数
     */
    @Update(sqlFile = true)
    int updateForOpen(OrderSlipDbEntity orderSlipDbEntity, String transactionId);

    /**
     * デリート<br/>
     *
     * @param orderSlipId 注文票ID
     * @return 処理件数
     */
    @Delete(sqlFile = true)
    int deleteByOrderSlipId(String orderSlipId);

    /**
     * 注文票ID AND 注文状態で注文票取得<br/>
     *
     * @param orderSlipId 注文票ID
     * @param orderStatus 注文ステータス
     * @return 注文票
     */
    @Select
    OrderSlipDbEntity getByOrderSlipIdAndOrderStatus(String orderSlipId, String orderStatus);

    /**
     * 顧客ID AND 注文状態で注文票取得<br/>
     *
     * @param customerId 注文票ID
     * @param orderStatus 注文ステータス
     * @return 注文票
     */
    @Select
    OrderSlipDbEntity getByCustomerIdAndOrderStatus(String customerId, String orderStatus);

    /**
     * 注文番号 AND 注文状態で注文票取得<br/>
     *
     * @param transactionId 取引ID
     * @param orderStatus 注文ステータス
     * @return OrderSlipDbEntity
     */
    @Select
    OrderSlipDbEntity getByTransactionIdAndOrderStatus(String transactionId, String orderStatus);

    /**
     * 取引IDで注文票取得<br/>
     *
     * @param transactionId 取引ID
     * @return OrderSlipDbEntity
     */
    @Select
    OrderSlipDbEntity getByTransactionId(String transactionId);

    /**
     * 注文状態で注文票リスト取得<br/>
     *
     * @param orderStatus 注文ステータス
     * @return 注文票リスト
     */
    @Select
    List<OrderSlipDbEntity> getByOrderStatus(String orderStatus);

    /**
     * 注文商品のデータを削除（下書き状態のみ削除）
     *
     * @param customerId 顧客ID
     * @return 注文票の削除件数を返却する
     */
    @Delete(sqlFile = true)
    int deleteDraftOrderSlipByCustomerId(String customerId);

    /**
     * デリート
     */
    @Delete(sqlFile = true)
    int clearOrderSlip(Timestamp deleteTime);

    /**
     * デリート
     *
     * @param transactionId 取引ID
     */
    @Delete(sqlFile = true)
    int deleteOrderSlip(String transactionId);
}