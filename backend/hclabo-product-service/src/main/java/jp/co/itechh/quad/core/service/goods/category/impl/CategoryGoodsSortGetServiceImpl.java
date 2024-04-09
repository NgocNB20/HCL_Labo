/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.goods.category.impl;

import jp.co.itechh.quad.core.base.util.seasar.AssertionUtil;
import jp.co.itechh.quad.core.entity.goods.category.CategoryGoodsSortEntity;
import jp.co.itechh.quad.core.logic.goods.category.CategoryGoodsSortGetLogic;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.goods.category.CategoryGoodsSortGetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * カテゴリ登録商品並び順取得
 *
 * @author PHAM QUANG DIEU (VJP)
 */
@Service
public class CategoryGoodsSortGetServiceImpl extends AbstractShopService implements CategoryGoodsSortGetService {

    /**
     * カテゴリ登録商品並び順取得
     */
    private final CategoryGoodsSortGetLogic categoryGoodsSortGetLogic;

    @Autowired
    public CategoryGoodsSortGetServiceImpl(CategoryGoodsSortGetLogic categoryGoodsSortGetLogic) {
        this.categoryGoodsSortGetLogic = categoryGoodsSortGetLogic;
    }

    /**
     * カテゴリ登録商品並び順取得
     *
     * @param categoryId カテゴリID
     * @return カテゴリ登録商品並び順
     */
    @Override
    public CategoryGoodsSortEntity execute(String categoryId) {

        AssertionUtil.assertNotNull("categoryId", categoryId);

        CategoryGoodsSortEntity categoryGoodsSortEntity = categoryGoodsSortGetLogic.execute(categoryId);

        return categoryGoodsSortEntity;
    }

}
