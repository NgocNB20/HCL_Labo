/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.category.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.dao.goods.category.CategoryConditionDao;
import jp.co.itechh.quad.core.entity.goods.category.CategoryConditionEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.goods.category.CategoryConditionGetLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * カテゴリ条件取得
 *
 * @author PHAM QUANG DIEU (VJP)
 *
 */
@Component
public class CategoryConditionGetLogicImpl extends AbstractShopLogic implements CategoryConditionGetLogic {

    /** カテゴリ条件Daoクラス */
    private final CategoryConditionDao categoryConditionDao;

    @Autowired
    public CategoryConditionGetLogicImpl(CategoryConditionDao categoryConditionDao) {
        this.categoryConditionDao = categoryConditionDao;
    }

    /**
     * カテゴリ条件取得
     *
     * @param categorySeq カテゴリSEQ
     * @return カテゴリ条件クラス
     */
    @Override
    public CategoryConditionEntity execute(Integer categorySeq) {

        ArgumentCheckUtil.assertNotNull("categorySeq", categorySeq);

        CategoryConditionEntity categoryConditionEntity = categoryConditionDao.getEntity(categorySeq);

        return categoryConditionEntity;
    }

}
