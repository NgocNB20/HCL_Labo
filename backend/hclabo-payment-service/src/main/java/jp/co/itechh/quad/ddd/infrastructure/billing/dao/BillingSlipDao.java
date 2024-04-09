package jp.co.itechh.quad.ddd.infrastructure.billing.dao;

import jp.co.itechh.quad.ddd.infrastructure.billing.dbentity.BillingSlipDbEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;

import java.sql.Timestamp;

/**
 * 請求伝票Daoクラス
 */
@Dao
@ConfigAutowireable
public interface BillingSlipDao {

    /**
     * インサート
     *
     * @param billingSlipDbEntity 請求伝票DbEntityクラス
     * @return 処理件数
     */
    @Insert(excludeNull = true)
    int insert(BillingSlipDbEntity billingSlipDbEntity);

    /**
     * アップデート
     *
     * @param billingSlipDbEntity 請求伝票DbEntityクラス
     * @return 処理件数
     */
    @Update
    int update(BillingSlipDbEntity billingSlipDbEntity);

    /**
     * 取引IDで請求伝票取得
     *
     * @param transactionId 取引ID
     * @return 請求伝票DbEntityクラス
     */
    @Select
    BillingSlipDbEntity getByTransactionId(String transactionId);

    /**
     * 注文決済IDで請求伝票取得
     *
     * @param orderPaymentId 注文決済ID
     * @return 請求伝票DbEntityクラス
     */
    @Select
    BillingSlipDbEntity getByOrderPaymentId(String orderPaymentId);

    /**
     * デリート
     */
    @Delete(sqlFile = true)
    int clearBillingSlip(Timestamp deleteTime);

    /**
     * デリート
     *
     * @param transactionId 取引ID
     */
    @Delete(sqlFile = true)
    int deleteBillingSlip(String transactionId);
}
