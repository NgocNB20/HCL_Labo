/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.category;

import jp.co.itechh.quad.core.entity.goods.category.CategoryGoodsSortEntity;

/**
 * カテゴリ登録商品並び順修正
 *
 * @author PHAM QUANG DIEU (VJP)
 */
public interface CategoryGoodsSortModifyLogic {

    /**
     *
     * カテゴリ表示情報を修正
     *
     * @param categoryGoodsSortEntity カテゴリ登録商品並び順エンティティクラス
     * @return 件数
     */
    int execute(CategoryGoodsSortEntity categoryGoodsSortEntity);
}