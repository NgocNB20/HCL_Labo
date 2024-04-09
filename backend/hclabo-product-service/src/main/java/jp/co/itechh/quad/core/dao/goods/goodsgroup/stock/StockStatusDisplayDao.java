/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dao.goods.goodsgroup.stock;

import jp.co.itechh.quad.core.entity.goods.goodsgroup.stock.StockStatusDisplayEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.boot.ConfigAutowireable;

import java.util.List;

/**
 * 商品グループ在庫表示Daoクラス
 *
 * @author thang
 */
@Dao
@ConfigAutowireable
public interface StockStatusDisplayDao {
    /**
     * エンティティ取得
     *
     * @param goodsGroupSeq 商品グループSEQ
     * @return 商品グループ在庫表示エンティティDTO
     */
    @Select
    StockStatusDisplayEntity getEntity(Integer goodsGroupSeq);

    /**
     * 商品グループ在庫表示リスト取得
     *
     * @param goodsGroupSeqList 商品グループSEQリスト
     * @return 商品グループエンティティリスト
     */
    @Select
    List<StockStatusDisplayEntity> getStockStatusDisplayList(List<Integer> goodsGroupSeqList);

}
