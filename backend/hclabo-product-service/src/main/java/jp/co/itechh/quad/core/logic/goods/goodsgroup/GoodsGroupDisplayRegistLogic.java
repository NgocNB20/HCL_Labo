/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.goodsgroup;

import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsGroupDisplayEntity;

/**
 * 商品グループ表示登録
 *
 * @author hirata
 * @version $Revision: 1.2 $
 */
public interface GoodsGroupDisplayRegistLogic {
    // LGP0008

    /**
     *
     * 商品グループ表示登録
     *
     * @param goodsGroupDisplayEntity 商品グループ表示エンティティ
     * @return 登録した件数
     */
    int execute(GoodsGroupDisplayEntity goodsGroupDisplayEntity);
}