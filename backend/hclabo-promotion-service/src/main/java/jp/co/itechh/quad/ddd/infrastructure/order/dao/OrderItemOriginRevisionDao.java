package jp.co.itechh.quad.ddd.infrastructure.order.dao;

import jp.co.itechh.quad.ddd.infrastructure.order.dbentity.OrderItemOriginRevisionDbEntity;
import org.seasar.doma.BatchInsert;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Select;
import org.seasar.doma.boot.ConfigAutowireable;

import java.sql.Timestamp;
import java.util.List;

/**
 * 改訂元注文商品Daoクラス<br/>
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Dao
@ConfigAutowireable
public interface OrderItemOriginRevisionDao {

    /**
     * インサート<br/>
     *
     * @param orderItemRevisionDbEntityList
     * @return 処理件数
     */
    @BatchInsert
    int[] insert(List<OrderItemOriginRevisionDbEntity> orderItemRevisionDbEntityList);

    /**
     * 一括削除
     *
     * @param orderItemRevisionDbEntity
     * @return 処理件数
     */
    @Delete
    int delete(OrderItemOriginRevisionDbEntity orderItemRevisionDbEntity);

    /**
     * 改訂元注文商品DbEntityリスト取得<br/>
     *
     * @param orderSlipRevisionId 改訂用注文票ID
     * @return 注文票リスト
     */
    @Select
    List<OrderItemOriginRevisionDbEntity> getByOrderSlipRevisionId(String orderSlipRevisionId);

    /**
     * デリート
     */
    @Delete(sqlFile = true)
    int clearOrderItemOriginRevision(Timestamp deleteTime);
}
