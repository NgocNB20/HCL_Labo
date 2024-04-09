package jp.co.itechh.quad.ddd.infrastructure.sales.dao;

import jp.co.itechh.quad.ddd.infrastructure.sales.dbentity.SalesSlipForRevisionDbEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;

import java.sql.Timestamp;

/**
 * 販売伝票エンドポイントDaoクラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Dao
@ConfigAutowireable
public interface SalesSlipForRevisionDao {

    /**
     * インサート
     *
     * @param salesSlipForRevisionDbEntity 改訂用販売伝票DbEntityクラス
     * @return 処理件数
     */
    @Insert(excludeNull = true)
    int insert(SalesSlipForRevisionDbEntity salesSlipForRevisionDbEntity);

    /**
     * アップデート
     *
     * @param salesSlipForRevisionDbEntity 改訂用販売伝票DbEntityクラス
     * @return 更新した数
     */
    @Update
    int update(SalesSlipForRevisionDbEntity salesSlipForRevisionDbEntity);

    /**
     * 改訂用販売伝票DbEntity取得
     *
     * @param transactionRevisionId 改訂用取引ID
     * @return 改訂用販売伝票DbEntity
     */
    @Select
    SalesSlipForRevisionDbEntity getByTransactionRevisionId(String transactionRevisionId);

    /**
     * 改訂用販売伝票DbEntity取得
     *
     * @param salesSlipRevisionId 改訂用販売伝票ID
     * @return 改訂用販売伝票DbEntity
     */
    @Select
    SalesSlipForRevisionDbEntity getBySalesSlipRevisionId(String salesSlipRevisionId);

    /**
     * デリート
     */
    @Delete(sqlFile = true)
    int clearSalesSlipForRevision(Timestamp deleteTime);

}
