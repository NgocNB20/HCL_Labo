package jp.co.itechh.quad.ddd.infrastructure.sales.dao;

import jp.co.itechh.quad.ddd.infrastructure.sales.dbentity.ItemPurchasePriceSubTotalDbEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;

import java.sql.Timestamp;
import java.util.List;

/**
 * 商品購入価格小計 Daoクラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Dao
@ConfigAutowireable
public interface ItemPurchasePriceSubTotalDao {

    /**
     * インサート
     *
     * @param itemPurchasePriceSubTotalDbEntity 商品購入価格小計 DbEntityクラス
     * @return 処理件数
     */
    @Insert(excludeNull = true)
    int insert(ItemPurchasePriceSubTotalDbEntity itemPurchasePriceSubTotalDbEntity);

    /**
     * アップデート
     * @param itemPurchasePriceSubTotalDbEntity 商品購入価格小計 DbEntityクラス
     * @return 更新した数
     */
    @Update
    int update(ItemPurchasePriceSubTotalDbEntity itemPurchasePriceSubTotalDbEntity);

    /**
     * インサートorアップデート
     *
     * @param itemPurchasePriceSubTotalDbEntity 商品購入価格小計 DbEntityクラス
     * @return 処理件数
     */
    @Insert(sqlFile = true)
    int insertOrUpdate(ItemPurchasePriceSubTotalDbEntity itemPurchasePriceSubTotalDbEntity);

    /**
     * アップデート
     *
     * @param itemPurchasePriceSubTotalDbEntity 商品購入価格小計 DbEntityクラス
     * @return 処理件数
     */
    @Delete
    int delete(ItemPurchasePriceSubTotalDbEntity itemPurchasePriceSubTotalDbEntity);

    /**
     * 商品購入価格小計DbEntityリスト取得.
     *
     * @param salesSlipId 販売伝票ID
     * @return 商品購入価格小計DbEntityリスト
     */
    @Select
    List<ItemPurchasePriceSubTotalDbEntity> getBySalesSlipId(String salesSlipId);

    /**
     * デリート
     */
    @Delete(sqlFile = true)
    int clearItemPurchasePriceSubTotal(Timestamp deleteTime);

    /**
     * デリート
     *
     * @param transactionId 取引ID
     */
    @Delete(sqlFile = true)
    int deleteItemPurchasePriceSubTotal(String transactionId);

    /**
     * デリート
     *
     * @param salesSlipId 販売伝票ID
     */
    @Delete(sqlFile = true)
    int deleteBySalesSlipId(String salesSlipId);
}