/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.category;

import jp.co.itechh.quad.core.entity.goods.category.CategoryConditionDetailEntity;

/**
 * カテゴリ条件詳細修正
 *
 * @author PHAM QUANG DIEU (VJP)
 */
public interface CategoryConditionDetailModifyLogic {

    /**
     *
     * カテゴリ条件詳細修正
     *
     * @param categoryConditionDetailEntity カテゴリ条件詳細エンティティクラス
     * @return 件数
     */
    int execute(CategoryConditionDetailEntity categoryConditionDetailEntity);
}