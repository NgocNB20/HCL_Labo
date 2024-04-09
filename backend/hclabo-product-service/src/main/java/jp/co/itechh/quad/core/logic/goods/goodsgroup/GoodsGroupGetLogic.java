/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.goodsgroup;

import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsGroupEntity;

import java.util.List;

/**
 *  商品グループ取得Logic
 *
 * @author Tanizaki (Itec) 2013/09/04
 *
 */
public interface GoodsGroupGetLogic {

    /**
     * 商品管理番号のリストからエンティティを取得
     *
     * @param shopSeq ショップSEQ
     * @param goodsGroupCodeList 商品管理番号のリスト
     * @return エンティティのリスト
     */
    List<GoodsGroupEntity> getEntityListByGoodsGroupCodeList(Integer shopSeq, List<String> goodsGroupCodeList);
}
