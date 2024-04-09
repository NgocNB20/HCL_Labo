/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.category;

import jp.co.itechh.quad.core.entity.goods.category.CategoryGoodsEntity;

import java.util.List;

/**
 * カテゴリに紐づくカテゴリ登録商品を取得
 *
 * @author kimura
 * @version $Revision: 1.1 $
 *
 */
public interface CategoryGetGoodsOrderListLogic {

    /**
     * カテゴリに紐づくカテゴリ登録商品を取得
     * @param categorySeq カテゴリSEQ
     * @param goodsGroupSeq 商品グループSEQ
     * @param fromOrderDisplay From並び順
     * @param toOrderDisplay To並び順
     * @param orderBy 昇順、降順
     * @return list カテゴリ登録商品リスト
     */
    List<CategoryGoodsEntity> execute(Integer categorySeq,
                                      Integer goodsGroupSeq,
                                      Integer fromOrderDisplay,
                                      Integer toOrderDisplay,
                                      boolean orderBy);

}