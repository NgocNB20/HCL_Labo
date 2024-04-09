/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.category.impl;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.dao.goods.category.CategoryDisplayDao;
import jp.co.itechh.quad.core.entity.goods.category.CategoryDisplayEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.goods.category.CategoryDisplayRegistLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * カテゴリ表示情報登録
 *
 * @author kimura
 * @author Kaneko (itec) 2012/08/21 UtilからHelperへ変更　Util使用箇所をgetComponentで取得するように変更
 *
 */
@Component
public class CategoryDisplayRegistLogicImpl extends AbstractShopLogic implements CategoryDisplayRegistLogic {

    /** カテゴリ表示情報登録DAO */
    private final CategoryDisplayDao categoryDisplayDao;

    @Autowired
    public CategoryDisplayRegistLogicImpl(CategoryDisplayDao categoryDisplayDao) {
        this.categoryDisplayDao = categoryDisplayDao;
    }

    /**
     *
     * カテゴリ表示情報登録
     *
     * @param categoryDisplayEntity カテゴリ表示エンティティ
     * @return 件数
     */
    @Override
    public int execute(CategoryDisplayEntity categoryDisplayEntity) {

        // 日付関連Helper取得
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);

        Timestamp currentTime = dateUtility.getCurrentTime();
        categoryDisplayEntity.setRegistTime(currentTime);
        categoryDisplayEntity.setUpdateTime(currentTime);
        return categoryDisplayDao.insert(categoryDisplayEntity);
    }
}