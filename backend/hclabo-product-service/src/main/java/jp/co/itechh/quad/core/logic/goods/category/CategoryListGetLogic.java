/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.category;

import jp.co.itechh.quad.core.dto.goods.category.CategorySearchForDaoConditionDto;
import jp.co.itechh.quad.core.entity.goods.category.CategoryEntity;

import java.util.List;

/**
 *
 * カテゴリ情報リスト取得ロジック
 *
 * @author ozaki
 * @version $Revision: 1.1 $
 *
 */
public interface CategoryListGetLogic {

    /**
     *
     * カテゴリ情報リストを取得する。
     *
     * @param conditionDto カテゴリ情報Dao用検索条件DTO
     * @return カテゴリエンティティリスト
     */
    List<CategoryEntity> execute(CategorySearchForDaoConditionDto conditionDto);

    /**
     * 商品コードの一覧からエンティティを取得
     *
     * @param shopSeq ショップSEQ
     * @param categoryIdList カテゴリIDのリスト
     * @return エンティティのリスト
     */
    List<CategoryEntity> getEntityListByIdList(Integer shopSeq, List<String> categoryIdList);

    /**
     * 商品コードからエンティティのリストを取得する
     *
     * @param goodsGroupCode 商品コード
     * @return エンティティのリスト
     */
    List<CategoryEntity> getEntityListByGoodsGroupCode(String goodsGroupCode);

}