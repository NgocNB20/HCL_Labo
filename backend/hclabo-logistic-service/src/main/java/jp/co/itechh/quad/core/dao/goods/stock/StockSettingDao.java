/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dao.goods.stock;

import jp.co.itechh.quad.core.entity.goods.stock.StockSettingEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Script;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;

import java.util.List;

/**
 * 在庫設定Dao
 *
 * @author thang
 * @version $Revision: 1.0 $
 */
@Dao
@ConfigAutowireable
public interface StockSettingDao {

    /**
     * インサート
     *
     * @param stockSettingEntity 在庫設定
     * @return 処理件数
     */
    @Insert(excludeNull = true)
    int insert(StockSettingEntity stockSettingEntity);

    /**
     * アップデート
     *
     * @param stockSettingEntity 在庫設定
     * @return 処理件数
     */
    @Update
    int update(StockSettingEntity stockSettingEntity);

    /**
     * デリート
     *
     * @param stockSettingEntity 在庫設定
     * @return 処理件数
     */
    @Delete
    int delete(StockSettingEntity stockSettingEntity);

    /**
     * エンティティ取得
     *
     * @param goodsSeq 商品SEQ
     * @return 在庫設定エンティティ
     */
    @Select
    StockSettingEntity getEntity(Integer goodsSeq);

    /**
     * 在庫設定エンティティリスト取得
     *
     * @param goodsSeq 商品SEQリスト
     * @return 在庫設定エンティティリスト
     */
    @Select
    List<StockSettingEntity> getStockSettingListByGoodsGroupSeq(List<Integer> goodsSeq);

    /**
     * 在庫設定テーブルロック
     */
    @Script
    void updateLockTableShareModeNowait();

    /**
     * 在庫管理フラグ更新
     *
     * @param goodsSeq       商品SEQ
     * @param stockManagementFlag 在庫管理フラグ
     * @return 更新件数
     */
    @Update(sqlFile = true)
    int updateStockManagementFlag(Integer goodsSeq, String stockManagementFlag);
}
