/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.goods;

import jp.co.itechh.quad.core.dto.goods.goods.GoodsDetailsDto;

import java.util.List;
import java.util.Map;

/**
 * 商品詳細情報MAP取得
 * 商品詳細Dtoを保持するマップを取得する。
 *
 * @author ozaki
 *
 */
public interface GoodsDetailsMapGetLogic {

    /**
     * 共通情報から商品詳細情報MAP取得
     * 商品詳細Dtoを保持するマップを取得する。
     *
     * @param goodsSeqList 商品シーケンスリスト
     * @return 商品詳細情報MAP
     */
    Map<Integer, GoodsDetailsDto> execute(List<Integer> goodsSeqList);

}