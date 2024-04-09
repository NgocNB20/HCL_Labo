package jp.co.itechh.quad.ddd.infrastructure.sales.dao;

import jp.co.itechh.quad.ddd.infrastructure.sales.dbentity.SalesSlipDbEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;

import java.sql.Timestamp;

/**
 * 販売伝票Daoクラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Dao
@ConfigAutowireable
public interface SalesSlipDao {

    /**
     * インサート
     *
     * @param salesSlipDbEntity 販売伝票DbEntityクラス
     * @return 処理件数
     */
    @Insert(excludeNull = true)
    int insert(SalesSlipDbEntity salesSlipDbEntity);

    /**
     * アップデート
     *
     * @param salesSlipDbEntity 販売伝票DbEntityクラス
     * @return 更新した数
     */
    @Update
    int update(SalesSlipDbEntity salesSlipDbEntity);

    /**
     * 販売伝票DbEntity取得
     *
     * @param transactionId 取引ID
     * @return 販売伝票DbEntity
     */
    @Select
    SalesSlipDbEntity getByTransactionId(String transactionId);

    /**
     * 販売伝票DbEntity取得.
     *
     * @param salesSlipId 販売伝票ID
     * @return 販売伝票DbEntity
     */
    @Select
    SalesSlipDbEntity getBySalesSlipId(String salesSlipId);

    /**
     * クーポン利用済回数取得
     *
     * @param customerId      顧客ID
     * @param couponSeq       クーポンSEQ
     * @param couponStartTime クーポン利用開始日時
     * @return クーポン理容済回数
     */
    @Select
    int getCouponCountByCustomerId(String customerId, int couponSeq, Timestamp couponStartTime);

    /**
     * デリート
     */
    @Delete(sqlFile = true)
    int clearSaleSlip(Timestamp deleteTime);

    /**
     * デリート
     *
     * @param transactionId 取引ID
     */
    @Delete(sqlFile = true)
    int deleteSaleSlip(String transactionId);
}