package jp.co.itechh.quad.ddd.infrastructure.order.dao;

import jp.co.itechh.quad.ddd.infrastructure.order.dbentity.OrderItemDbEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;

import java.sql.Timestamp;
import java.util.List;

/**
 * 注文商品Daoクラス<br/>
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Dao
@ConfigAutowireable
public interface OrderItemDao {

    /**
     * インサート<br/>
     *
     * @param orderItemDbEntity
     * @return 処理件数
     */
    @Insert(excludeNull = true)
    int insert(OrderItemDbEntity orderItemDbEntity);

    /**
     * アップデート<br/>
     *
     * @param orderItemDbEntity
     * @return 処理件数
     */
    @Update
    int update(OrderItemDbEntity orderItemDbEntity);

    /**
     * インサートorアップデート
     *
     * @param orderItemDbEntity 注文商品DbEntityクラス
     * @return 処理件数
     */
    @Insert(sqlFile = true)
    int insertOrUpdate(OrderItemDbEntity orderItemDbEntity);

    /**
     * デリート<br/>
     *
     * @param orderItemDbEntity
     * @return 処理件数
     */
    @Delete
    int delete(OrderItemDbEntity orderItemDbEntity);

    /**
     * 注文票IDで注文商品取得<br/>
     *
     * @param orderSlipId 注文票ID
     * @return 注文商品リスト
     */
    @Select
    List<OrderItemDbEntity> getByOrderSlipId(String orderSlipId);

    /**
     * デリート
     */
    @Delete(sqlFile = true)
    int clearOrderItem(Timestamp deleteTime);

    /**
     * 注文票のデータを削除（下書き状態のみ削除）
     *
     * @param customerId 顧客ID
     * @return 注文票の削除件数を返却する
     */
    @Delete(sqlFile = true)
    int deleteDraftOrderItemByCustomerId(String customerId);

    /**
     * デリート
     *
     * @param transactionId 取引ID
     */
    @Delete(sqlFile = true)
    int deleteOrderItem(String transactionId);

    /**
     * デリート
     *
     * @param orderSlipId 注文票ID
     */
    @Delete(sqlFile = true)
    int deleteByOrderSlipId(String orderSlipId);
}