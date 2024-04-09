/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.category.impl;

import jp.co.itechh.quad.core.dao.goods.category.CategoryGoodsDao;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.goods.category.CategoryGoodsTableLockLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * カテゴリ登録商品テーブルロック取得ロジック
 *
 * @author hirata
 * @version $Revision: 1.2 $
 *
 */
@Component
public class CategoryGoodsTableLockLogicImpl extends AbstractShopLogic implements CategoryGoodsTableLockLogic {

    /** カテゴリ登録商品DAO */
    private final CategoryGoodsDao categoryGoodsDao;

    @Autowired
    public CategoryGoodsTableLockLogicImpl(CategoryGoodsDao categoryGoodsDao) {
        this.categoryGoodsDao = categoryGoodsDao;
    }

    /**
     * カテゴリ登録商品テーブルロック
     * カテゴリ登録商品テーブルのレコードを排他取得する。
     *
     */
    @Override
    public void execute() {

        // (1) カテゴリ登録商品テーブル排他取得
        categoryGoodsDao.updateLockTableShareModeNowait();
    }
}
