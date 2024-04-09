package jp.co.itechh.quad.ddd.infrastructure.sales.dao;

import jp.co.itechh.quad.ddd.infrastructure.sales.dbentity.AdjustmentAmountDbEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;

import java.sql.Timestamp;
import java.util.List;

/**
 * クーポンDaoクラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Dao
@ConfigAutowireable
public interface AdjustmentAmountDao {

    /**
     * インサート
     *
     * @param adjustmentAmountDbEntity 調整金額DbEntityクラス
     * @return 処理件数
     */
    @Insert(excludeNull = true)
    int insert(AdjustmentAmountDbEntity adjustmentAmountDbEntity);

    /**
     * アップデート
     *
     * @param adjustmentAmountDbEntity 調整金額DbEntityクラス
     * @return 更新した数
     */
    @Update
    int update(AdjustmentAmountDbEntity adjustmentAmountDbEntity);

    /**
     * インサートorアップデート
     *
     * @param adjustmentAmountDbEntity 調整金額DbEntityクラス
     * @return 処理件数
     */
    @Insert(sqlFile = true)
    int insertOrUpdate(AdjustmentAmountDbEntity adjustmentAmountDbEntity);

    /**
     * アップデート
     *
     * @param adjustmentAmountDbEntity 調整金額DbEntityクラス
     * @return 処理件数
     */
    @Delete
    int delete(AdjustmentAmountDbEntity adjustmentAmountDbEntity);

    /**
     * 調整金額DbEntityリスト取得.
     *
     * @param salesSlipId 販売伝票ID
     * @return 調整金額DbEntityリスト
     */
    @Select
    List<AdjustmentAmountDbEntity> getBySalesSlipId(String salesSlipId);

    /**
     * デリート
     */
    @Delete(sqlFile = true)
    int clearAdjustmentAmount(Timestamp deleteTime);

    /**
     * デリート
     *
     * @param transactionId 取引ID
     */
    @Delete(sqlFile = true)
    int deleteAdjustmentAmount(String transactionId);

}
