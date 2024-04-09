/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dao.goods.goods;

import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsStockDisplayEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.boot.ConfigAutowireable;

import java.util.List;

/**
 * 商品在庫Daoクラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Dao
@ConfigAutowireable
public interface GoodsStockDisplayDao {

    /**
     * 商品在庫表示リスト取得
     *
     * @param goodsSeqList 商品SEQリスト
     * @return 商品在庫表示
     */
    @Select
    List<GoodsStockDisplayEntity> getGoodsStockDisplayList(List<Integer> goodsSeqList);

    /**
     * 在庫情報アップサート
     *
     * @param goodsStockDisplayEntity    商品在庫表示
     * @return 件数
     */
    @Insert(sqlFile = true)
    int upsertStock(GoodsStockDisplayEntity goodsStockDisplayEntity);
}