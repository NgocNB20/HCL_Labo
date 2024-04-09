/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.goods.category;

import jp.co.itechh.quad.core.entity.goods.category.CategoryGoodsSortEntity;

/**
 * カテゴリ登録商品並び順取得
 *
 * @author PHAM QUANG DIEU (VJP)
 */
public interface CategoryGoodsSortGetService {

    /**
     * カテゴリ登録商品並び順取得
     *
     * @param categoryId カテゴリID
     * @return カテゴリ登録商品並び順
     */
    CategoryGoodsSortEntity execute(String categoryId);

}
