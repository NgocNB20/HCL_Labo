package jp.co.itechh.quad.ddd.infrastructure.sales.dao;

import jp.co.itechh.quad.ddd.infrastructure.sales.dbentity.ItemPurchasePriceSubTotalForRevisionDbEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;

import java.sql.Timestamp;
import java.util.List;

/**
 * 改訂用商品購入価格小計 Daoクラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Dao
@ConfigAutowireable
public interface ItemPurchasePriceSubTotalForRevisionDao {

    /**
     * インサート
     *
     * @param itemPurchasePriceSubTotalForRevisionDbEntity 改訂用商品購入価格小計 DbEntityクラス
     * @return 処理件数
     */
    @Insert(excludeNull = true)
    int insert(ItemPurchasePriceSubTotalForRevisionDbEntity itemPurchasePriceSubTotalForRevisionDbEntity);

    /**
     * アップデート
     * @param itemPurchasePriceSubTotalForRevisionDbEntity 改訂用商品購入価格小計 DbEntityクラス
     * @return 更新した数
     */
    @Update
    int update(ItemPurchasePriceSubTotalForRevisionDbEntity itemPurchasePriceSubTotalForRevisionDbEntity);

    /**
     * アップデート
     *
     * @param itemPurchasePriceSubTotalForRevisionDbEntity 改訂用商品購入価格小計 DbEntityクラス
     * @return 処理件数
     */
    @Delete
    int delete(ItemPurchasePriceSubTotalForRevisionDbEntity itemPurchasePriceSubTotalForRevisionDbEntity);

    /**
     * インサートorアップデート
     *
     * @param itemPurchasePriceSubTotalForRevisionDbEntity 改訂用商品購入価格小計 DbEntityクラス
     * @return 処理件数
     */
    @Insert(sqlFile = true)
    int insertOrUpdate(ItemPurchasePriceSubTotalForRevisionDbEntity itemPurchasePriceSubTotalForRevisionDbEntity);

    /**
     * 商品購入価格小計DbEntityリスト取得.
     *
     * @param salesSlipRevisionId 改訂用販売伝票ID
     * @return 商品購入価格小計DbEntityリスト
     */
    @Select
    List<ItemPurchasePriceSubTotalForRevisionDbEntity> getBySalesSlipForRevisionId(String salesSlipRevisionId);

    /**
     * デリート
     */
    @Delete(sqlFile = true)
    int clearItemPurchasePriceSubTotalForRevision(Timestamp deleteTime);
}