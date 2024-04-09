/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.category.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.dao.goods.category.CategoryDisplayDao;
import jp.co.itechh.quad.core.entity.goods.category.CategoryDisplayEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.goods.category.CategoryDisplayGetLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * カテゴリ表示取得
 *
 * @author PHAM QUANG DIEU (VJP)
 *
 */
@Component
public class CategoryDisplayGetLogicImpl extends AbstractShopLogic implements CategoryDisplayGetLogic {

    /** カテゴリ表示Daoクラス */
    private final CategoryDisplayDao categoryDisplayDao;

    @Autowired
    public CategoryDisplayGetLogicImpl(CategoryDisplayDao categoryDisplayDao) {
        this.categoryDisplayDao = categoryDisplayDao;
    }

    /**
     *
     * カテゴリ表示取得
     *
     * @param categorySeq カテゴリSEQ
     * @return カテゴリ表示クラス
     */
    @Override
    public CategoryDisplayEntity execute(Integer categorySeq) {

        ArgumentCheckUtil.assertNotNull("categorySeq", categorySeq);

        CategoryDisplayEntity categoryDisplayEntity = categoryDisplayDao.getEntity(categorySeq);

        return categoryDisplayEntity;
    }

}
