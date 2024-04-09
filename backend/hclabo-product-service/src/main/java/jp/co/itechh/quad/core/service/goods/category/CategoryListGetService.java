/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.goods.category;

import jp.co.itechh.quad.core.constant.type.HTypeSiteType;
import jp.co.itechh.quad.core.dto.goods.category.CategorySearchForDaoConditionDto;
import jp.co.itechh.quad.core.entity.goods.category.CategoryEntity;

import java.util.List;

/**
 * カテゴリ情報リスト取得
 *
 * @author MN7017
 * @version $Revision: 1.2 $
 */
public interface CategoryListGetService {

    /**
     * カテゴリDTOのリストを取得する
     *
     * @param categorySearchForDaoConditionDto 検索条件DTO
     * @return カテゴリエンティティリスト
     */
    List<CategoryEntity> execute(CategorySearchForDaoConditionDto categorySearchForDaoConditionDto,
                                 Integer shopSeq,
                                 HTypeSiteType siteType);

    /**
     * 商品コードの一覧からエンティティを取得
     *
     * @param categoryIdList カテゴリIDのリスト
     * @return エンティティのリスト
     */
    List<CategoryEntity> getEntityListByIdList(List<String> categoryIdList);

    /**
     * 商品コードからエンティティのリストを取得する
     *
     * @param goodsGroupCode 商品コード
     * @return エンティティのリスト
     */
    List<CategoryEntity> getEntityListByGoodsGroupCode(String goodsGroupCode);

}