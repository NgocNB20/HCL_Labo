/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.category;

import jp.co.itechh.quad.core.entity.goods.category.CategoryEntity;

/**
 *
 * カテゴリ情報修正
 *
 * @author kimura
 * @version $Revision: 1.1 $
 *
 */
public interface CategoryModifyLogic {

    /**
     *
     * カテゴリ情報を修正
     *
     * @param categoryEntity カテゴリ情報
     * @return 件数
     */
    int execute(CategoryEntity categoryEntity);
}