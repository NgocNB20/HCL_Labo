/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.category.impl;

import jp.co.itechh.quad.core.dao.goods.category.CategoryDao;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.goods.category.CategoryTableLockLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * カテゴリテーブルロック取得ロジック
 *
 * @author hirata
 * @version $Revision: 1.1 $
 *
 */
@Component
public class CategoryTableLockLogicImpl extends AbstractShopLogic implements CategoryTableLockLogic {

    /** カテゴリ情報DAO */
    private final CategoryDao categoryDao;

    @Autowired
    public CategoryTableLockLogicImpl(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    /**
     * カテゴリテーブルロック
     * カテゴリテーブルのテーブルを排他取得する。
     *
     */
    @Override
    public void execute() {

        // (1) カテゴリテーブル排他取得
        categoryDao.updateLockTableShareModeNowait();
    }
}
