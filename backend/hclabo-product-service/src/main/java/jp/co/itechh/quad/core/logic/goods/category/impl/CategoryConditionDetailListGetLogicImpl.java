/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.category.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.dao.goods.category.CategoryConditionDetailDao;
import jp.co.itechh.quad.core.entity.goods.category.CategoryConditionDetailEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.goods.category.CategoryConditionDetailListGetLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * カテゴリ条件詳細リスト取得
 *
 * @author PHAM QUANG DIEU (VJP)
 *
 */
@Component
public class CategoryConditionDetailListGetLogicImpl extends AbstractShopLogic
                implements CategoryConditionDetailListGetLogic {

    /** カテゴリ条件詳細Daoクラス */
    private final CategoryConditionDetailDao categoryConditionDetailDao;

    @Autowired
    public CategoryConditionDetailListGetLogicImpl(CategoryConditionDetailDao categoryConditionDetailDao) {
        this.categoryConditionDetailDao = categoryConditionDetailDao;
    }

    /**
     * カテゴリ条件詳細を取得する
     *
     * @param categorySeq カテゴリSEQ
     * @return カテゴリ条件詳細リスト
     */
    @Override
    public List<CategoryConditionDetailEntity> execute(Integer categorySeq) {

        ArgumentCheckUtil.assertNotNull("categorySeq", categorySeq);

        List<CategoryConditionDetailEntity> categoryConditionDetailList =
                        categoryConditionDetailDao.getEntityListByCategorySeq(categorySeq);
        ;

        return categoryConditionDetailList;
    }

}