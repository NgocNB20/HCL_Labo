/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.category.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.dao.goods.category.CategoryDao;
import jp.co.itechh.quad.core.entity.goods.category.CategoryEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.goods.category.CategoryLockLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 *
 * カテゴリレコードロック取得ロジック
 *
 * @author hirata
 * @version $Revision: 1.2 $
 *
 */
@Component
public class CategoryLockLogicImpl extends AbstractShopLogic implements CategoryLockLogic {

    /** カテゴリ情報DAO */
    private final CategoryDao categoryDao;

    @Autowired
    public CategoryLockLogicImpl(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    /**
     * カテゴリレコードロック
     * カテゴリテーブルのレコードを排他取得する。
     *
     * @param categorySeqList カテゴリSEQリスト
     */
    @Override
    public void execute(List<Integer> categorySeqList) {

        // (1) パラメータチェック
        // カテゴリSEQリストが null または 0件 でないかをチェック
        ArgumentCheckUtil.assertNotEmpty("categorySeqList", categorySeqList);

        // (2) カテゴリのレコード排他取得
        List<CategoryEntity> categoryEntityList = categoryDao.getCategoryBySeqForUpdate(categorySeqList);
        // カテゴリSEQリストと取得件数が異なる場合
        if (categorySeqList.size() != categoryEntityList.size()) {
            // カテゴリ排他取得エラーを投げる。
            throwMessage(MSGCD_CATEGORY_SELECT_FOR_UPDATE_FAIL);
        }
    }
}