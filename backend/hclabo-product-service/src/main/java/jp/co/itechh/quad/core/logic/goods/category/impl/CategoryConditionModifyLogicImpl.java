/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.category.impl;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.dao.goods.category.CategoryConditionDao;
import jp.co.itechh.quad.core.entity.goods.category.CategoryConditionEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.goods.category.CategoryConditionModifyLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * カカテゴリ条件修正
 *
 * @author PHAM QUANG DIEU (VJP)
 */
@Component
public class CategoryConditionModifyLogicImpl extends AbstractShopLogic implements CategoryConditionModifyLogic {

    /** カテゴリ条件Daoクラス */
    private final CategoryConditionDao categoryConditionDao;

    @Autowired
    public CategoryConditionModifyLogicImpl(CategoryConditionDao categoryConditionDao) {
        this.categoryConditionDao = categoryConditionDao;
    }

    /**
     *
     * カカテゴリ条件修正
     *
     * @param categoryConditionEntity カテゴリ条件エンティティクラス
     * @return 件数
     */
    @Override
    public int execute(CategoryConditionEntity categoryConditionEntity) {
        // 日付関連Helper取得
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);
        categoryConditionEntity.setUpdateTime(dateUtility.getCurrentTime());
        return categoryConditionDao.update(categoryConditionEntity);
    }
}
