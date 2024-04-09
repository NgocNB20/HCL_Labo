/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */
package jp.co.itechh.quad.core.logic.goods.category;

import jp.co.itechh.quad.core.entity.goods.category.CategoryGoodsSortEntity;

/**
 * カテゴリ登録商品並び順を登録
 *
 * @author Pham Quang Dieu (VJP)
 */
public interface CategoryGoodsSortRegistLogic {

    /**
     *
     * カテゴリ登録商品並び順を登録
     *
     * @param categoryGoodsSortEntity カテゴリ登録商品並び順エンティティ
     * @return 件数
     */
    int execute(CategoryGoodsSortEntity categoryGoodsSortEntity);
}
