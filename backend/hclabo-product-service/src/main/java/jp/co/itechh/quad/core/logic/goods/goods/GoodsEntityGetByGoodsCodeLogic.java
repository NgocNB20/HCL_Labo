/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.goods;

import jp.co.itechh.quad.core.entity.goods.goods.GoodsEntity;

/**
 * 商品エンティティ取得（商品コード）
 * 商品コード、ショップSEQをキーに商品エンティティを取得する。
 *
 * @author MN7017
 * @version $Revision: 1.1 $
 *
 */
public interface GoodsEntityGetByGoodsCodeLogic {

    /**
     * 取得処理実行
     *
     * @param shopSeq   ショップSEQ
     * @param goodsCode 商品コード
     * @return 商品エンティティ
     */
    GoodsEntity execute(Integer shopSeq, String goodsCode);

}