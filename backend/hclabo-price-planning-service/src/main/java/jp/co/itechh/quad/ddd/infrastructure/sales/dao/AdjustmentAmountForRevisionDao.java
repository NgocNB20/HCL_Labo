package jp.co.itechh.quad.ddd.infrastructure.sales.dao;

import jp.co.itechh.quad.ddd.infrastructure.sales.dbentity.AdjustmentAmountForRevisionDbEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;

import java.sql.Timestamp;
import java.util.List;

/**
 * 改訂用クーポンDaoクラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Dao
@ConfigAutowireable
public interface AdjustmentAmountForRevisionDao {

    /**
     * インサート
     *
     * @param adjustmentAmountForRevisionDbEntity 調整金額DbEntityクラス
     * @return 処理件数
     */
    @Insert(excludeNull = true)
    int insert(AdjustmentAmountForRevisionDbEntity adjustmentAmountForRevisionDbEntity);

    /**
     * アップデート
     *
     * @param adjustmentAmountForRevisionDbEntity 調整金額DbEntityクラス
     * @return 更新した数
     */
    @Update
    int update(AdjustmentAmountForRevisionDbEntity adjustmentAmountForRevisionDbEntity);

    /**
     * インサートorアップデート
     *
     * @param adjustmentAmountForRevisionDbEntity 調整金額DbEntityクラス
     * @return 処理件数
     */
    @Insert(sqlFile = true)
    int insertOrUpdate(AdjustmentAmountForRevisionDbEntity adjustmentAmountForRevisionDbEntity);

    /**
     * アップデート
     *
     * @param adjustmentAmountForRevisionDbEntity 調整金額DbEntityクラス
     * @return 処理件数
     */
    @Delete
    int delete(AdjustmentAmountForRevisionDbEntity adjustmentAmountForRevisionDbEntity);

    /**
     * 調整金額DbEntityリスト取得.
     *
     * @param salesSlipRevisionId 販売伝票ID
     * @return 調整金額DbEntityリスト
     */
    @Select
    List<AdjustmentAmountForRevisionDbEntity> getBySalesSlipRevisionId(String salesSlipRevisionId);

    /**
     * デリート
     */
    @Delete(sqlFile = true)
    int clearAdjustmentAmountForRevision(Timestamp deleteTime);

}
