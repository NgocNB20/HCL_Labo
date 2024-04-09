/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.category;

import jp.co.itechh.quad.core.entity.goods.category.CategoryConditionEntity;

/**
 *
 * カテゴリ条件取得
 *
 * @author PHAM QUANG DIEU (VJP)
 *
 */
public interface CategoryConditionGetLogic {

    /**
     * カテゴリ条件取得。
     *
     * @param categorySeq カテゴリSEQ
     * @return カテゴリ条件クラス
     */
    CategoryConditionEntity execute(Integer categorySeq);

}
