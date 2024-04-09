/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.category;

import jp.co.itechh.quad.core.dto.goods.category.CategoryGoodsDetailsDto;
import jp.co.itechh.quad.core.dto.goods.category.CategoryGoodsSearchForDaoConditionDto;

import java.util.List;

/**
 *
 * カテゴリに紐づく商品ｸﾞﾙｰﾌﾟﾘｽﾄを取得
 *
 * @author kimura
 * @version $Revision: 1.4 $
 *
 */
public interface CategoryGetGoodsListLogic {

    /**
     * カテゴリに紐づく商品ｸﾞﾙｰﾌﾟﾘｽﾄを取得
     * @param conditionDto カテゴリ商品情報Dao用検索条件Dto
     * @return カテゴリ内商品詳細DTOﾘｽﾄ
     */
    List<CategoryGoodsDetailsDto> execute(CategoryGoodsSearchForDaoConditionDto conditionDto);

}