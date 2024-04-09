/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.category.impl;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.dao.goods.category.CategoryConditionDetailDao;
import jp.co.itechh.quad.core.entity.goods.category.CategoryConditionDetailEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.goods.category.CategoryConditionDetailModifyLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * カテゴリ条件詳細修正
 *
 * @author PHAM QUANG DIEU (VJP)
 */
@Component
public class CategoryConditionDetailModifyLogicImpl extends AbstractShopLogic
                implements CategoryConditionDetailModifyLogic {

    /** カテゴリ条件詳細Daoクラス */
    private final CategoryConditionDetailDao categoryConditionDetailDao;

    @Autowired
    public CategoryConditionDetailModifyLogicImpl(CategoryConditionDetailDao categoryConditionDetailDao) {
        this.categoryConditionDetailDao = categoryConditionDetailDao;
    }

    /**
     *
     * カテゴリ条件詳細修正
     *
     * @param categoryConditionDetailEntity カテゴリ条件詳細エンティティクラス
     * @return 件数
     */
    @Override
    public int execute(CategoryConditionDetailEntity categoryConditionDetailEntity) {
        // 日付関連Helper取得
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);
        categoryConditionDetailEntity.setRegistTime(dateUtility.getCurrentTime());
        categoryConditionDetailEntity.setUpdateTime(dateUtility.getCurrentTime());
        return categoryConditionDetailDao.insert(categoryConditionDetailEntity);
    }
}