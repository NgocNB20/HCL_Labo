/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.noveltypresent.impl;

import jp.co.itechh.quad.core.dto.shop.noveltypresent.NoveltyGoodsCountConditionDto;
import jp.co.itechh.quad.core.entity.goods.category.CategoryEntity;
import jp.co.itechh.quad.core.logic.goods.category.CategoryListGetLogic;
import jp.co.itechh.quad.core.logic.goods.goods.GoodsGetLogic;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.noveltypresent.NoveltyPresentSearchService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * ノベルティプレゼント条件検索サービス<br/>
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 *
 */
@Service
public class NoveltyPresentSearchServiceImpl extends AbstractShopService implements NoveltyPresentSearchService {

    /** カテゴリ取得ロジック */
    public CategoryListGetLogic categoryListGetLogic;

    /** 商品取得ロジック */
    public GoodsGetLogic goodsGetLogic;

    @Autowired
    public NoveltyPresentSearchServiceImpl(CategoryListGetLogic categoryListGetLogic, GoodsGetLogic goodsGetLogic) {
        this.categoryListGetLogic = categoryListGetLogic;
        this.goodsGetLogic = goodsGetLogic;
    }

    /**
     * 商品条件に一致する商品数を取得
     *
     * @param conditionDto ノベルティプレゼント商品数確認用検索条件
     * @return 商品数
     */
    @Override
    public int getTargetGoodsCount(NoveltyGoodsCountConditionDto conditionDto) {

        List<Integer> categorySeqList = null;

        // カテゴリIDからカテゴリSEQを取得
        if (conditionDto.getCategoryIdList() != null && !conditionDto.getCategoryIdList().isEmpty()) {
            // 共通情報チェック
            Integer shopSeq = 1001;
            List<CategoryEntity> entityList =
                            categoryListGetLogic.getEntityListByIdList(shopSeq, conditionDto.getCategoryIdList());
            if (ObjectUtils.isNotEmpty(entityList)) {
                categorySeqList = new ArrayList<>();
                for (CategoryEntity entity : entityList) {
                    categorySeqList.add(entity.getCategorySeq());
                }

                conditionDto.setCategorySeqList(categorySeqList);
            } else {
                return 0;
            }
        }

        int count = goodsGetLogic.getNoveltyGoodsCount(conditionDto);

        return count;
    }

}