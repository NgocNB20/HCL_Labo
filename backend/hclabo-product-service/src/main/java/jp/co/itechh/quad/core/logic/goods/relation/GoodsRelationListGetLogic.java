/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.relation;

import jp.co.itechh.quad.core.dto.goods.goodsgroup.GoodsRelationDto;
import jp.co.itechh.quad.core.dto.goods.goodsgroup.GoodsRelationSearchForDaoConditionDto;

import java.util.List;

/**
 *
 * 関連商品詳細情報リスト取得
 * 対象商品の関連商品リスト（商品Dtoリスト）を取得する。
 *
 * @author ozaki
 * @version $Revision: 1.2 $
 *
 */
public interface GoodsRelationListGetLogic {

    // LGR0001

    /**
     *
     * 関連商品詳細情報リスト取得
     *
     * @param goodsRelationSearchForDaoConditionDto 関連商品Dao用検索条件Dto
     * @return 関連商品情報リスト
     */
    List<GoodsRelationDto> execute(GoodsRelationSearchForDaoConditionDto goodsRelationSearchForDaoConditionDto);
}