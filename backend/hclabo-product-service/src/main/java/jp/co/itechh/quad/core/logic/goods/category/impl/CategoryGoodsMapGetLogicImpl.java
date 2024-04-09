/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.category.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.dao.goods.category.CategoryGoodsDao;
import jp.co.itechh.quad.core.entity.goods.category.CategoryGoodsEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.goods.category.CategoryGoodsMapGetLogic;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * カテゴリ登録商品マップ取得ロジック
 *
 * @author hirata
 * @version $Revision: 1.2 $
 *
 */
@Component
public class CategoryGoodsMapGetLogicImpl extends AbstractShopLogic implements CategoryGoodsMapGetLogic {

    /** カテゴリ登録商品DAO */
    private final CategoryGoodsDao categoryGoodsDao;

    @Autowired
    public CategoryGoodsMapGetLogicImpl(CategoryGoodsDao categoryGoodsDao) {
        this.categoryGoodsDao = categoryGoodsDao;
    }

    /**
     *
     * 実行メソッド
     *
     * @param goodsGroupSeqList 商品グループSEQリスト
     * @return カテゴリ登録商品マップ
     */
    @Override
    public Map<Integer, List<CategoryGoodsEntity>> execute(List<Integer> goodsGroupSeqList) {
        // (1)パラメータチェック
        ArgumentCheckUtil.assertNotNull("goodsGroupSeqList", goodsGroupSeqList);

        // (2)商品検索結果リスト取得処理
        List<CategoryGoodsEntity> categoryGoodsEntityList = categoryGoodsDao.getCategoryGoodsList(goodsGroupSeqList);

        // (3) （戻り値用）カテゴリ登録商品マップの編集
        Integer tmpGoodsGroupSeq = null;
        List<CategoryGoodsEntity> tmpCategoryGoodsEntityList = new ArrayList<>();
        Map<Integer, List<CategoryGoodsEntity>> categoryGoodsMap = new HashMap<>();

        for (CategoryGoodsEntity categoryGoodsEntity : categoryGoodsEntityList) {
            if (tmpGoodsGroupSeq != null && !tmpGoodsGroupSeq.equals(categoryGoodsEntity.getGoodsGroupSeq())) {
                categoryGoodsMap.put(tmpGoodsGroupSeq, tmpCategoryGoodsEntityList);
                tmpCategoryGoodsEntityList = new ArrayList<>();
                tmpGoodsGroupSeq = categoryGoodsEntity.getGoodsGroupSeq();
            }
            if (tmpGoodsGroupSeq == null) {
                tmpGoodsGroupSeq = categoryGoodsEntity.getGoodsGroupSeq();
            }
            tmpCategoryGoodsEntityList.add(categoryGoodsEntity);
        }

        if (tmpGoodsGroupSeq != null) {
            categoryGoodsMap.put(tmpGoodsGroupSeq, tmpCategoryGoodsEntityList);
        }

        return categoryGoodsMap;
    }

    /**
     * カテゴリ登録商品のリストを取得する
     *
     * @param categorySeq カテゴリSEQ
     * @param goodsGroupSeqList 商品管理SEQのリスト
     * @return カテゴリ登録商品のリスト
     */
    @Override
    public List<CategoryGoodsEntity> getCategoryGoodsListByCategorySeq(Integer categorySeq,
                                                                       List<Integer> goodsGroupSeqList) {
        // 全商品取得
        if (CollectionUtils.isEmpty(goodsGroupSeqList)) {
            return categoryGoodsDao.getCategoryGoodsListByCategorySeq(categorySeq);
        }
        // 個別商品取得
        else {
            List<CategoryGoodsEntity> dbCategoryGoodsEntityList =
                            categoryGoodsDao.getCategoryGoodsListByCategorySeq(categorySeq);
            List<CategoryGoodsEntity> categoryGoodsEntityList = new ArrayList<>();
            for (CategoryGoodsEntity entity : dbCategoryGoodsEntityList) {
                if (goodsGroupSeqList.contains(entity.getGoodsGroupSeq())) {
                    categoryGoodsEntityList.add(entity);
                }
            }

            if (CollectionUtils.isNotEmpty(categoryGoodsEntityList)) {
                return categoryGoodsEntityList;
            }

        }
        return null;
    }
}