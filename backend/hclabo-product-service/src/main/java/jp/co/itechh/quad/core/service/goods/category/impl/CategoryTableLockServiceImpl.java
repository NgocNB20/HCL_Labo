/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.goods.category.impl;

import jp.co.itechh.quad.core.logic.goods.category.CategoryTableLockLogic;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.goods.category.CategoryTableLockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * カテゴリテーブルロック
 *
 * @author kimura
 * @version $Revision: 1.1 $
 */
@Service
public class CategoryTableLockServiceImpl extends AbstractShopService implements CategoryTableLockService {

    /**
     * カテゴリテーブルロック
     */
    private final CategoryTableLockLogic categoryTableLockLogic;

    @Autowired
    public CategoryTableLockServiceImpl(CategoryTableLockLogic categoryTableLockLogic) {
        this.categoryTableLockLogic = categoryTableLockLogic;
    }

    /**
     * カテゴリテーブルロック
     */
    @Override
    public void execute() {

        // カテゴリテーブルロック処理
        categoryTableLockLogic.execute();
    }

}
