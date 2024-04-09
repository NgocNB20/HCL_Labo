/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.category;

import jp.co.itechh.quad.core.entity.goods.category.CategoryConditionEntity;

/**
 * カカテゴリ条件修正
 *
 * @author PHAM QUANG DIEU (VJP)
 */
public interface CategoryConditionModifyLogic {

    /**
     *
     * カカテゴリ条件修正
     *
     * @param categoryConditionEntity カテゴリ条件エンティティクラス
     * @return 件数
     */
    int execute(CategoryConditionEntity categoryConditionEntity);
}