/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.category.impl;

import jp.co.itechh.quad.core.dao.goods.category.CategoryDao;
import jp.co.itechh.quad.core.entity.goods.category.CategoryEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.goods.category.CategoryRemoveLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * カテゴリ情報削除
 *
 * @author kimura
 * @version $Revision: 1.1 $
 *
 */
@Component
public class CategoryRemoveLogicImpl extends AbstractShopLogic implements CategoryRemoveLogic {

    /** カテゴリ情報削除DAO */
    private final CategoryDao categoryDao;

    @Autowired
    public CategoryRemoveLogicImpl(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    /**
     *
     * カテゴリ情報削除
     *
     * @param categoryEntity カテゴリエンティティ
     * @return 件数
     */
    @Override
    public int execute(CategoryEntity categoryEntity) {
        return categoryDao.delete(categoryEntity);
    }
}
