/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.category.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.constant.type.HTypeOpenStatus;
import jp.co.itechh.quad.core.dao.goods.category.CategoryDao;
import jp.co.itechh.quad.core.entity.goods.category.CategoryEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.goods.category.CategoryGetLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * カテゴリ情報取得
 *
 * @author kimura
 * @version $Revision: 1.1 $
 *
 */
@Component
public class CategoryGetLogicImpl extends AbstractShopLogic implements CategoryGetLogic {

    /** カテゴリ情報DAO */
    private final CategoryDao categoryDao;

    @Autowired
    public CategoryGetLogicImpl(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    /**
     * カテゴリ情報を取得
     *
     * @param shopSeq ショップSEQ
     * @param categoryId カテゴリID
     * @return カテゴリ情報エンティティ
     */
    @Override
    public CategoryEntity execute(Integer shopSeq, String categoryId) {
        return execute(shopSeq, categoryId, null);
    }

    /**
     * カテゴリ情報を取得
     *
     * @param shopSeq ショップSEQ
     * @param categoryId カテゴリID
     * @param openStatus 公開状態
     * @return カテゴリ情報エンティティ
     */
    @Override
    public CategoryEntity execute(Integer shopSeq, String categoryId, HTypeOpenStatus openStatus) {
        return execute(shopSeq, categoryId, openStatus, null);
    }

    /**
     * カテゴリ情報を取得
     *
     * @param shopSeq ショップSEQ
     * @param categoryId カテゴリID
     * @param openStatus 公開状態
     * @param frontDisplayReferenceDate フロント表示基準日時（管理サイトからの場合未設定。プレビュー画面の制御用日時）
     * @return カテゴリ情報エンティティ
     */
    @Override
    public CategoryEntity execute(Integer shopSeq,
                                  String categoryId,
                                  HTypeOpenStatus openStatus,
                                  Timestamp frontDisplayReferenceDate) {

        ArgumentCheckUtil.assertNotNull("categoryId", categoryId);

        CategoryEntity categoryEntity =
                        categoryDao.getCategory(shopSeq, categoryId, openStatus, frontDisplayReferenceDate);

        return categoryEntity;
    }

    /**
     * 公開のみカテゴリ情報を取得する。
     *
     * @param shopSeq                   ショップSEQ
     * @param categoryId                カテゴリID
     * @param openStatus                公開状態
     * @param frontDisplayReferenceDate フロント表示基準日時（管理サイトからの場合未設定。プレビュー画面の制御用日時）
     * @return カテゴリ情報エンティティ
     */
    @Override
    public CategoryEntity getCategoryOpen(Integer shopSeq,
                                          String categoryId,
                                          HTypeOpenStatus openStatus,
                                          Timestamp frontDisplayReferenceDate) {
        ArgumentCheckUtil.assertNotNull("categoryId", categoryId);

        return categoryDao.getCategoryOpen(shopSeq, categoryId, openStatus, frontDisplayReferenceDate);
    }
}