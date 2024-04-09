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
 * カテゴリ情報削除
 *
 * @author kimura
 * @version $Revision: 1.1 $
 *
 */
public interface CategoryRemoveLogic {

    /**
     *
     * カテゴリ情報を削除
     *
     * @param categoryEntity カテゴリエンティティ
     * @return int 件数
     */
    int execute(CategoryEntity categoryEntity);
}