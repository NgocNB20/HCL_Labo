/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.category.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.dao.goods.category.CategoryGoodsSortDao;
import jp.co.itechh.quad.core.entity.goods.category.CategoryGoodsSortEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.goods.category.CategoryGoodsSortGetLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * カテゴリ登録商品並び順取得
 *
 * @author PHAM QUANG DIEU (VJP)
 *
 */
@Component
public class CategoryGoodsSortGetLogicImpl extends AbstractShopLogic implements CategoryGoodsSortGetLogic {

    /** カテゴリ登録商品並び順Daoクラス */
    private final CategoryGoodsSortDao categoryGoodsSortDao;

    @Autowired
    public CategoryGoodsSortGetLogicImpl(CategoryGoodsSortDao categoryGoodsSortDao) {
        this.categoryGoodsSortDao = categoryGoodsSortDao;
    }

    /**
     *
     * カテゴリ登録商品並び順取得
     *
     * @param categoryId カテゴリID
     * @return カテゴリ登録商品並び順クラス
     */
    @Override
    public CategoryGoodsSortEntity execute(String categoryId) {

        ArgumentCheckUtil.assertNotNull("categoryId", categoryId);

        CategoryGoodsSortEntity categoryGoodsSortEntity = categoryGoodsSortDao.getEntityByCategoryId(categoryId);

        return categoryGoodsSortEntity;
    }

}
