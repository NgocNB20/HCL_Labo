/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.category.impl;

import jp.co.itechh.quad.core.dao.goods.category.CategoryDao;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.goods.category.NewCategorySeqGetLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * カテゴリSEQ採番
 *
 * @author kimura
 * @version $Revision: 1.1 $
 *
 */
@Component
public class NewCategorySeqGetLogicImpl extends AbstractShopLogic implements NewCategorySeqGetLogic {

    /** カテゴリDao */
    private final CategoryDao categoryDao;

    @Autowired
    public NewCategorySeqGetLogicImpl(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    /**
     * 実行メソッド
     *
     * @return カテゴリSEQ
     */
    @Override
    public Integer execute() {
        return categoryDao.getCategorySeqNextVal();
    }
}
