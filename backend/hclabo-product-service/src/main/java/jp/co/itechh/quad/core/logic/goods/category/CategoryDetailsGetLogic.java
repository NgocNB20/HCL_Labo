/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.category;

import jp.co.itechh.quad.core.constant.type.HTypeCategoryType;
import jp.co.itechh.quad.core.constant.type.HTypeSiteType;
import jp.co.itechh.quad.core.dto.goods.category.CategoryDetailsDto;

import java.util.List;

/**
 *
 * カテゴリ情報DTO取得
 *
 * @author ozaki
 * @version $Revision: 1.1 $
 *
 */
public interface CategoryDetailsGetLogic {

    /**
     * カテゴリ詳細Dtoクラスリストを取得する
     *
     * @param categoryIdList カテゴリIDリスト
     * @param siteType  サイト
     * @return カテゴリ詳細Dtoクラスリスト
     */
    List<CategoryDetailsDto> getCategoryDetailsDtoList(List<String> categoryIdList, HTypeSiteType siteType);

    /**
     * カテゴリSEQのリストを取得する
     *
     * @param categoryType カテゴリ種別
     * @return カテゴリSEQのリスト
     */
    List<Integer> getCategorySeqListByCategoryType(HTypeCategoryType categoryType);
}