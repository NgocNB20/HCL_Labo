/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.category.impl;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.dao.goods.category.CategoryGoodsSortDao;
import jp.co.itechh.quad.core.entity.goods.category.CategoryGoodsSortEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.goods.category.CategoryGoodsSortModifyLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * カテゴリ登録商品並び順修正
 *
 * @author PHAM QUANG DIEU (VJP)
 */
@Component
public class CategoryGoodsSortModifyLogicImpl extends AbstractShopLogic implements CategoryGoodsSortModifyLogic {

    /** カテゴリ条件Daoクラス */
    private final CategoryGoodsSortDao categoryGoodsSortDao;

    @Autowired
    public CategoryGoodsSortModifyLogicImpl(CategoryGoodsSortDao categoryGoodsSortDao) {
        this.categoryGoodsSortDao = categoryGoodsSortDao;
    }

    /**
     *
     * カテゴリ登録商品並び順修正
     *
     * @param categoryGoodsSortEntity カテゴリ登録商品並び順エンティティクラス
     * @return 件数
     */
    @Override
    public int execute(CategoryGoodsSortEntity categoryGoodsSortEntity) {
        // 日付関連Helper取得
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);
        categoryGoodsSortEntity.setUpdateTime(dateUtility.getCurrentTime());
        return categoryGoodsSortDao.update(categoryGoodsSortEntity);
    }
}
