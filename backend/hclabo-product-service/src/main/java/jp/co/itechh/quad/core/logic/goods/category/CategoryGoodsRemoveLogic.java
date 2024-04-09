/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.category;

import jp.co.itechh.quad.core.entity.goods.category.CategoryGoodsEntity;

/**
 *
 * カテゴリ登録商品情報削除
 *
 * @author hirata
 * @version $Revision: 1.1 $
 *
 */
public interface CategoryGoodsRemoveLogic {

    /**
     * カテゴリ登録商品情報を削除
     *
     * @param categorySeq カテゴリSEQ
     * @return int 件数
     */
    int execute(Integer categorySeq);

    /**
     * カテゴリ登録商品情報を削除
     *
     * @param categoryGoodsEntity カテゴリ登録商品クラス
     * @return int 件数
     */
    int execute(CategoryGoodsEntity categoryGoodsEntity);
}
