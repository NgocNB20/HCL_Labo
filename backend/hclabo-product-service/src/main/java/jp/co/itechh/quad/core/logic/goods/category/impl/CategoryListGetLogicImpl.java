/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.category.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.dao.goods.category.CategoryDao;
import jp.co.itechh.quad.core.dto.goods.category.CategorySearchForDaoConditionDto;
import jp.co.itechh.quad.core.entity.goods.category.CategoryEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.goods.category.CategoryListGetLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * カテゴリ情報リスト取得ロジック
 *
 * @author ozaki
 * @version $Revision: 1.2 $
 *
 */
@Component
public class CategoryListGetLogicImpl extends AbstractShopLogic implements CategoryListGetLogic {

    /** カテゴリ情報DAO */
    private final CategoryDao categoryDao;

    @Autowired
    public CategoryListGetLogicImpl(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    /**
     *
     * カテゴリ情報リストを取得する。
     *
     * @param conditionDto カテゴリ情報Dao用検索条件DTO
     * @return カテゴリ情報エンティティリスト
     */
    @Override
    public List<CategoryEntity> execute(CategorySearchForDaoConditionDto conditionDto) {

        // (1) パラメータチェック
        // カテゴリ情報Dao用検索条件DTOが null でないかをチェック
        ArgumentCheckUtil.assertNotNull("conditionDto", conditionDto);

        // (2) カテゴリエンティティリスト取得
        // カテゴリDaoのカテゴリエンティティリスト取得処理を実行する。
        // DAO CategoryDao
        // メソッド List<カテゴリエンティティ> getCategoryList( （パラメータ）カテゴリ情報Dao用検索条件DTO)
        List<CategoryEntity> categoryEntityList = categoryDao.getCategoryList(conditionDto);

        // (3) 戻り値
        // 取得したカテゴリエンティティリストを返す。
        // （該当するカテゴリ情報がない場合は 空のリスト を返す）
        return categoryEntityList;

    }

    /**
     * 商品コードの一覧からエンティティを取得
     *
     * @param shopSeq ショップSEQ
     * @param categoryIdList カテゴリIDのリスト
     * @return エンティティのリスト
     */
    @Override
    public List<CategoryEntity> getEntityListByIdList(Integer shopSeq, List<String> categoryIdList) {
        ArgumentCheckUtil.assertNotNull("categoryIdList", categoryIdList);

        if (categoryIdList.isEmpty()) {
            return new ArrayList<>();
        }
        return categoryDao.getEntityListByIdList(shopSeq, categoryIdList);
    }

    /**
     * 商品コードからエンティティのリストを取得する
     *
     * @param goodsGroupCode 商品コード
     * @return エンティティのリスト
     */
    @Override
    public List<CategoryEntity> getEntityListByGoodsGroupCode(String goodsGroupCode) {

        ArgumentCheckUtil.assertNotNull("goodsGroupCode", goodsGroupCode);

        return categoryDao.getEntityListByGoodsGroupCode(goodsGroupCode);
    }
}