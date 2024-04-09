/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.goods;

import jp.co.itechh.quad.core.dto.goods.goods.GoodsDto;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsSearchForDaoConditionDto;

import java.util.List;

/**
 *
 * 商品リスト取得
 *
 * @author ozaki
 * @version $Revision: 1.2 $
 *
 */
public interface GoodsListGetLogic {

    /**
     *
     * 商品リスト取得
     * 商品情報リストを取得する。
     *
     * @param goodsSearchForDaoConditionDto 商品Dao用検索条件DTO
     * @return 商品DTOリスト
     */
    List<GoodsDto> execute(GoodsSearchForDaoConditionDto goodsSearchForDaoConditionDto);
}