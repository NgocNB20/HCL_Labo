/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.category.impl;

import jp.co.itechh.quad.core.constant.type.HTypeCategoryType;
import jp.co.itechh.quad.core.constant.type.HTypeOpenStatus;
import jp.co.itechh.quad.core.constant.type.HTypeSiteType;
import jp.co.itechh.quad.core.dao.goods.category.CategoryDao;
import jp.co.itechh.quad.core.dto.goods.category.CategoryDetailsDto;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.goods.category.CategoryDetailsGetLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * カテゴリ情報DTO取得
 *
 * @author ozaki
 * @version $Revision: 1.1 $
 *
 */
@Component
public class CategoryDetailsGetLogicImpl extends AbstractShopLogic implements CategoryDetailsGetLogic {

    /** カテゴリ分類リスト用valueカラム名 */
    protected static final String VALUE_COLNAME = "categoryid";
    /** カテゴリ分類リスト用ラベルカラム名 */
    protected static final String LABEL_COLNAME = "categoryname";

    /** カテゴリDAO */
    private final CategoryDao categoryDao;

    @Autowired
    public CategoryDetailsGetLogicImpl(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    /**
     * カテゴリ詳細Dtoクラスリストを取得する
     *
     * @param categoryIdList カテゴリIDリスト
     * @param siteType  サイト
     * @return カテゴリ詳細Dtoクラスリスト
     */
    @Override
    public List<CategoryDetailsDto> getCategoryDetailsDtoList(List<String> categoryIdList, HTypeSiteType siteType) {

        Integer shopSeq = 1001;
        List<CategoryDetailsDto> categoryDetailsDtoList =
                        categoryDao.getCategoryDetailsDtoListByCategoryId(categoryIdList, shopSeq, siteType,
                                                                          HTypeOpenStatus.OPEN
                                                                         );

        List<CategoryDetailsDto> resultCategoryDetailsDtoList = new ArrayList<>();
        for (String categoryId : categoryIdList) {
            for (CategoryDetailsDto categoryDetailsDto : categoryDetailsDtoList) {
                if (categoryId.equals(categoryDetailsDto.getCategoryId())) {
                    resultCategoryDetailsDtoList.add(categoryDetailsDto);
                    break;
                }
            }
        }

        return resultCategoryDetailsDtoList;
    }

    /**
     * カテゴリSEQのリストを取得する
     *
     * @param categoryType カテゴリ種別
     * @return カテゴリSEQのリスト
     */
    @Override
    public List<Integer> getCategorySeqListByCategoryType(HTypeCategoryType categoryType) {
        return categoryDao.getCategorySeqListByCategoryType(categoryType);
    }
}