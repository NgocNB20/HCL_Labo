package jp.co.itechh.quad.ddd.infrastructure.order.dao;

import jp.co.itechh.quad.ddd.infrastructure.order.dbentity.OrderItemRevisionDbEntity;
import org.seasar.doma.BatchInsert;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
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
public interface OrderItemRevisionDao {

    /**
     * インサート<br/>
     *
     * @param orderItemRevisionDbEntityList
     * @return 処理件数
     */
    @BatchInsert
    int[] insert(List<OrderItemRevisionDbEntity> orderItemRevisionDbEntityList);

    /**
     * インサートorアップデート<br/>
     *
     * @param orderItemRevisionDbEntity
     * @return 処理件数
     */
    @Insert(sqlFile = true)
    int insertOrUpdate(OrderItemRevisionDbEntity orderItemRevisionDbEntity);

    /**
     * デリート<br/>
     *
     * @param orderItemRevisionDbEntity 注文商品DbEntity
     * @return 処理件数
     */
    @Delete
    int delete(OrderItemRevisionDbEntity orderItemRevisionDbEntity);

    /**
     * 改訂用注文票IDで注文商品取得<br/>
     *
     * @param orderSlipRevisionId 改訂用注文票ID
     * @return 注文商品リスト
     */
    @Select
    List<OrderItemRevisionDbEntity> getByOrderSlipRevisionId(String orderSlipRevisionId);

    /**
     * デリート
     */
    @Delete(sqlFile = true)
    int clearOrderItemRevision(Timestamp deleteTime);
}
