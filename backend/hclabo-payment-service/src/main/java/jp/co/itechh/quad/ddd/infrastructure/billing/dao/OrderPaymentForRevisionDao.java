package jp.co.itechh.quad.ddd.infrastructure.billing.dao;

import jp.co.itechh.quad.ddd.infrastructure.billing.dbentity.OrderPaymentForRevisionDbEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;

import java.sql.Timestamp;

/**
 * 改訂用注文決済エンティティDaoクラス
 */
@Dao
@ConfigAutowireable
public interface OrderPaymentForRevisionDao {

    /**
     * インサート
     *
     * @param orderPaymentForRevisionDbEntity 改訂用注文決済エンティティDbEntityクラス
     * @return 処理件数
     */
    @Insert(excludeNull = true)
    int insert(OrderPaymentForRevisionDbEntity orderPaymentForRevisionDbEntity);

    /**
     * アップデート
     *
     * @param orderPaymentForRevisionDbEntity 改訂用注文決済エンティティDbEntityクラス
     * @return 処理件数
     */
    @Update
    int update(OrderPaymentForRevisionDbEntity orderPaymentForRevisionDbEntity);

    /**
     * 改訂用請求伝票IDから取得する
     *
     * @param billingSlipRevisionId 改訂用請求伝票ID
     * @return 改訂用注文決済エンティティDbEntityクラス
     */
    @Select
    OrderPaymentForRevisionDbEntity getByBillingSlipRevisionId(String billingSlipRevisionId);

    /**
     * デリート
     */
    @Delete(sqlFile = true)
    int clearOrderPaymentForRevision(Timestamp deleteTime);

}
