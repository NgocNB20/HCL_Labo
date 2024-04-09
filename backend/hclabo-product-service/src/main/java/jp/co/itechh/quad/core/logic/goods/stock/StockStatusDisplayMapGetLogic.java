/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.stock;

import jp.co.itechh.quad.core.entity.goods.goodsgroup.stock.StockStatusDisplayEntity;

import java.util.List;
import java.util.Map;

/**
 * 在庫状態表示Map取得ロジック
 *
 * @author Kaneko
 *
 */
public interface StockStatusDisplayMapGetLogic {

    /**
     * 商品グループ在庫表示Map取得
     *
     * @param goodsGroupSeqList 商品グループシーケンスリスト
     * @return 商品グループ在庫表示Map
     */
    Map<Integer, StockStatusDisplayEntity> execute(List<Integer> goodsGroupSeqList);

}