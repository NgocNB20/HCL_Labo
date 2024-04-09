/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.goods;

import jp.co.itechh.quad.core.dto.goods.goods.GoodsSearchForBackDaoConditionDto;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsSearchResultDto;

import java.util.List;

/**
 *
 * 商品検索結果リスト取得
 *
 * @author hirata
 * @version $Revision: 1.2 $
 *
 */
public interface GoodsSearchResultListGetLogic {

    // LGG0006

    /**
     *
     * 実行メソッド
     *
     * @param searchCondition 商品Dao用検索条件（管理機能）Dto
     * @return 商品検索結果DTOリスト
     */
    List<GoodsSearchResultDto> execute(GoodsSearchForBackDaoConditionDto searchCondition);
}