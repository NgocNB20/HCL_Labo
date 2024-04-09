/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.goods.category;

import jp.co.itechh.quad.core.dto.goods.category.CategoryDto;
import jp.co.itechh.quad.core.entity.goods.category.CategoryConditionDetailEntity;

import java.util.List;

/**
 * カテゴリ修正
 *
 * @author kimura
 * @version $Revision: 1.1 $
 */
public interface CategoryModifyService {

    /**
     * カテゴリの修正
     *
     * @param originalCategoryDto カテゴリ情報DTO
     * @param modifyCategoryDto   カテゴリ情報DTO
     * @return 件数
     */
    int execute(CategoryDto originalCategoryDto, CategoryDto modifyCategoryDto);

    /**
     * 項目変更チェック
     *
     * @param originalCategoryConditionDetailEntityList カテゴリ条件詳細エンティティリスト
     * @param modifyCategoryConditionDetailEntityList カテゴリ条件詳細エンティティリスト
     * @return 変更チェック
     */
    boolean checkChangeCategoryConditionDetailEntityList(List<CategoryConditionDetailEntity> originalCategoryConditionDetailEntityList,
                                                         List<CategoryConditionDetailEntity> modifyCategoryConditionDetailEntityList);
}