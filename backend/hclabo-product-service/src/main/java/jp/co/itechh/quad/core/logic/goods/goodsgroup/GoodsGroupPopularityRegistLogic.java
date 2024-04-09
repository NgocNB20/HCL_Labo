/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.goodsgroup;

import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsGroupPopularityEntity;

/**
 * 商品グループ人気登録
 *
 * @author hirata
 * @version $Revision: 1.1 $
 */
public interface GoodsGroupPopularityRegistLogic {
    // LGP0015

    /**
     *
     * 商品グループ人気登録
     *
     * @param goodsGroupPopularityEntity 商品グループ人気エンティティ
     * @return 登録した件数
     */
    int execute(GoodsGroupPopularityEntity goodsGroupPopularityEntity);
}