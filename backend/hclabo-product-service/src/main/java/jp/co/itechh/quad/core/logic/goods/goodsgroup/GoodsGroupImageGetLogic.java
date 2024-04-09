/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.goodsgroup;

import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsGroupImageEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品グループ画像取得
 *
 * @author ozaki
 * @version $Revision: 1.1 $
 */
public interface GoodsGroupImageGetLogic {
    // LGP0010

    /**
     *
     * 商品グループ画像取得
     * 商品グループSEQリストを元に商品グループ画像マップを取得する。
     *
     * @param goodsGroupSeqList 商品グループSEQリスト
     * @return 商品グループ画像マップ
     */
    Map<Integer, List<GoodsGroupImageEntity>> execute(List<Integer> goodsGroupSeqList);
}