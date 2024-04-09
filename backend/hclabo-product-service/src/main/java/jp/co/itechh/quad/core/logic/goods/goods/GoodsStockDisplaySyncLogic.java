/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.core.logic.goods.goods;

import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsStockDisplayEntity;

import java.util.List;

/**
 * 商品在庫表示ロジック
 *
 * @author kimura
 */
public interface GoodsStockDisplaySyncLogic {

    /**
     * 在庫情報アップサート
     *
     * @param goodsStockDisplayEntityList 商品在庫表示リスト
     * @return 件数
     */
    int syncUpsertStock(List<GoodsStockDisplayEntity> goodsStockDisplayEntityList);

}
