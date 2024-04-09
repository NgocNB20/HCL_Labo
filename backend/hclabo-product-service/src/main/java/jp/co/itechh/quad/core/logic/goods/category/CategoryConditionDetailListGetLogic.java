/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.category;

import jp.co.itechh.quad.core.entity.goods.category.CategoryConditionDetailEntity;

import java.util.List;

/**
 * カテゴリ条件詳細リスト取得
 *
 * @author PHAM QUANG DIEU (VJP)
 */
public interface CategoryConditionDetailListGetLogic {

    /**
     * カテゴリ条件詳細を取得する。
     *
     * @param categorySeq カテゴリSEQ
     * @return カテゴリ条件詳細リスト
     */
    List<CategoryConditionDetailEntity> execute(Integer categorySeq);

}
