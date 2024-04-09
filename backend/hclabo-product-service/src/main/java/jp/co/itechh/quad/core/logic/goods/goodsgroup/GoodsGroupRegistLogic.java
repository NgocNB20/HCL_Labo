/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.goodsgroup;

import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsGroupEntity;

/**
 * 商品グループ登録
 *
 * @author hirata
 * @version $Revision: 1.2 $
 */
public interface GoodsGroupRegistLogic {
    // LGP0004

    /**
     *
     * 商品グループ登録
     *
     * @param goodsGroupEntity 商品グループエンティティ
     * @return 登録した件数
     */
    int execute(GoodsGroupEntity goodsGroupEntity);
}