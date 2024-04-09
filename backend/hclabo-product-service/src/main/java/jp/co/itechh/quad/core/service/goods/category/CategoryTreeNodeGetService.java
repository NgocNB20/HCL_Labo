/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.goods.category;

import jp.co.itechh.quad.core.dto.goods.category.CategorySearchForDaoConditionDto;
import jp.co.itechh.quad.core.dto.goods.category.CategoryTreeDto;

/**
 * カテゴリ木構造取得
 *
 * @author PHAM QUANG DIEU (VJP)
 */
public interface CategoryTreeNodeGetService {

    /**
     * カテゴリー木構造Dtoクラス取得。
     *
     * @param conditionDto      カテゴリ情報検索条件DTO
     * @return カテゴリ情報DTO
     */
    CategoryTreeDto execute(CategorySearchForDaoConditionDto conditionDto);
}
