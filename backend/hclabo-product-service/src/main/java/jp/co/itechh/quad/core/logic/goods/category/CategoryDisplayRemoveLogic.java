/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.category;

import jp.co.itechh.quad.core.entity.goods.category.CategoryDisplayEntity;

/**
 *
 * カテゴリ表示情報削除
 *
 * @author kimura
 * @version $Revision: 1.1 $
 *
 */
public interface CategoryDisplayRemoveLogic {

    /**
     *
     * カテゴリ表示情報を削除
     *
     * @param categoryDisplayEntity カテゴリ表示エンティティ
     * @return int 件数
     */
    int execute(CategoryDisplayEntity categoryDisplayEntity);
}