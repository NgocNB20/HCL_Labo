package jp.co.itechh.quad.ddd.infrastructure.billing.dao;

import jp.co.itechh.quad.core.constant.type.HTypeGmoPaymentCancelStatus;
import jp.co.itechh.quad.ddd.domain.billing.entity.TargetSalesOrderPaymentDto;
import jp.co.itechh.quad.ddd.infrastructure.billing.dbentity.OrderPaymentDbEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * 注文決済Daoクラス
 */
@Dao
@ConfigAutowireable
public interface OrderPaymentDao {

    /**
     * インサート
     *
     * @param orderPaymentDbEntity 注文決済DbEntityクラス
     * @return 処理件数
     */
    @Insert(excludeNull = true)
    int insert(OrderPaymentDbEntity orderPaymentDbEntity);

    /**
     * アップデート
     *
     * @param orderPaymentDbEntity 注文決済DbEntityクラス
     * @return 処理件数
     */
    @Update
    int update(OrderPaymentDbEntity orderPaymentDbEntity);

    /**
     * 請求伝票IDから取得する
     *
     * @param billingSlipId 注文決済ID
     * @return 注文決済DbEntityクラス
     */
    @Select
    OrderPaymentDbEntity getByBillingSlipId(String billingSlipId);

    /**
     * デリート
     */
    @Delete(sqlFile = true)
    int clearOrderPayment(Timestamp deleteTime);

    /**
     * 対象受注番号一覧を取得
     *
     * @return 対象受注番号一覧
     */
    @Select
    List<TargetSalesOrderPaymentDto> getTargetSalesOrderNumberList();

    /**
     * 対象受注の注文決済に結果反映
     *
     * @param orderCode              受注番号
     * @param gmoPaymentCancelStatus GMO決済キャンセル状態
     * @return 処理件数
     */
    @Update(sqlFile = true)
    int updateGmoPaymentCancelStatus(String orderCode, HTypeGmoPaymentCancelStatus gmoPaymentCancelStatus);

    /**
     * 決済期限切れ対象受注一覧取得
     *
     * @param transactionId 取引ID
     * @param today         処理日
     * @return 受注サマリエンティティリスト
     */
    @Select
    Integer getReminderExpiredTransactionId(String transactionId, Date today);

    /**
     * 督促対象受注取得
     *
     * @param thresholdDate 支払期限日までの残日数
     * @param today         処理日付
     * @return 受注サマリエンティティリスト
     */
    @Select
    Integer getReminderTargetOrderByTransactionId(String transactionId, Date thresholdDate, Date today);

    /**
     * デリート
     *
     * @param transactionId 取引ID
     */
    @Delete(sqlFile = true)
    int deleteOrderPayment(String transactionId);
}