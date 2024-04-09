/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.goods.category;

import jp.co.itechh.quad.core.entity.goods.category.CategoryEntity;

/**
 * カテゴリ修正(カテゴリエンティティのみ更新)
 *
 * @author kimura
 * @version $Revision: 1.1 $
 */
public interface CategorySimpleModifyService {

    /**
     * カテゴリの修正
     *
     * @param categoryEntity カテゴリエンティティ
     * @return 件数
     */
    int execute(CategoryEntity categoryEntity);

}
