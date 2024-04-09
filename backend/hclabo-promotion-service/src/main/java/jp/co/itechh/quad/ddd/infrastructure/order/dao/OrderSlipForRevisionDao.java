package jp.co.itechh.quad.ddd.infrastructure.order.dao;

import jp.co.itechh.quad.ddd.infrastructure.order.dbentity.OrderSlipForRevisionDbEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;

import java.sql.Timestamp;

/**
 * 改訂用注文票Daoクラス<br/>
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Dao
@ConfigAutowireable
public interface OrderSlipForRevisionDao {

    /**
     * インサート<br/>
     *
     * @param orderSlipForRevisionDbEntity 改訂用注文票DbEntityクラス
     * @return 処理件数
     */
    @Insert(excludeNull = true)
    int insert(OrderSlipForRevisionDbEntity orderSlipForRevisionDbEntity);

    /**
     * アップデート
     *
     * @param orderSlipForRevisionDbEntity 改訂用注文票DbEntityクラス
     * @return 更新件数
     */
    @Update
    int update(OrderSlipForRevisionDbEntity orderSlipForRevisionDbEntity);

    /**
     * 改訂用注文票DbEntity取得<br/>
     *
     * @param orderSlipRevisionId 改訂用注文票ID
     * @return 改訂用注文票DbEntity
     */
    @Select
    OrderSlipForRevisionDbEntity getByOrderSlipRevisionId(String orderSlipRevisionId);

    /**
     * 改訂用注文票DbEntity取得<br/>
     *
     * @param transactionRevisionId 改訂用取引ID
     * @return 改訂用注文票DbEntity
     */
    @Select
    OrderSlipForRevisionDbEntity getByTransactionRevisionId(String transactionRevisionId);

    /**
     * デリート
     */
    @Delete(sqlFile = true)
    int clearOrderSlipRevision(Timestamp deleteTime);
}
