/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2023 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.goods.category;

import jp.co.itechh.quad.core.entity.goods.category.CategoryGoodsSortEntity;

/**
 * カテゴリ登録商品並び順削除
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 *
 */
public interface CategoryGoodsSortRemoveService {

    /**
     * カテゴリ登録商品並び順削除
     *
     * @param categoryGoodsSortEntity カテゴリ登録商品並び順エンティティクラス
     * @return int 件数
     */
    int execute(CategoryGoodsSortEntity categoryGoodsSortEntity);

}
