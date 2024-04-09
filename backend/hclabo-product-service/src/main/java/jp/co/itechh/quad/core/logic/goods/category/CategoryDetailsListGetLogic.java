/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.category;

import jp.co.itechh.quad.core.dto.goods.category.CategoryDetailsDto;
import jp.co.itechh.quad.core.dto.goods.category.CategorySearchForDaoConditionDto;

import java.util.List;

/**
 *
 * カテゴリ詳細情報リスト取得ロジック
 *
 * @author ozaki
 * @version $Revision: 1.1 $
 *
 */
public interface CategoryDetailsListGetLogic {

    /**
     *
     * カテゴリ詳細情報リストを取得する。
     *
     * @param conditionDto カテゴリ情報Dao用検索条件DTO
     * @return カテゴリ詳細DTOリスト
     */
    List<CategoryDetailsDto> execute(CategorySearchForDaoConditionDto conditionDto);

}