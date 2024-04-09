/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.goodsgroup;

import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsGroupEntity;

/**
 * 商品グループ更新
 *
 * @author hirata
 * @version $Revision: 1.3 $
 */
public interface GoodsGroupUpdateLogic {
    // LGP0005

    /**
     *
     * 商品グループ更新
     *
     * @param goodsGroupEntity 商品グループエンティティ
     * @return 更新した件数
     */
    int execute(GoodsGroupEntity goodsGroupEntity);
}