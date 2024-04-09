/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 20022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */
package jp.co.itechh.quad.core.logic.goods.category;

import jp.co.itechh.quad.core.entity.goods.category.CategoryConditionDetailEntity;

import java.util.List;

/**
 * カテゴリ条件詳細登録
 *
 * @author Pham Quang Dieu (VJP)
 */
public interface CategoryConditionDetailRegistLogic {

    /**
     * カテゴリ条件詳細登録
     *
     * @param categoryConditionDetailEntityList カテゴリ条件詳細エンティティリスト
     * @return 件数
     */
    int[] execute(List<CategoryConditionDetailEntity> categoryConditionDetailEntityList);
}
