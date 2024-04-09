/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.goodsgroup;

import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsGroupEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品グループマップ取得(商品グループコード)
 *
 * @author hirata
 * @version $Revision: 1.1 $
 */
public interface GoodsGroupMapGetByCodeLogic {
    // LGP0021

    /**
     * 商品グループマップ取得
     * 商品グループエンティティを保持するマップを取得する。
     *
     * @param shopSeq ショップSEQ
     * @param goodsGroupCodeList 商品グループコードリスト
     * @return 商品グループエンティティMAP
     */
    Map<String, GoodsGroupEntity> execute(Integer shopSeq, List<String> goodsGroupCodeList);
}