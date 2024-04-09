/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.category;

import jp.co.itechh.quad.core.entity.goods.category.CategoryGoodsEntity;

import java.util.List;
import java.util.Map;

/**
 *
 * カテゴリ登録商品マップ取得
 *
 * @author hirata
 * @version $Revision: 1.2 $
 *
 */
public interface CategoryGoodsMapGetLogic {

    // LGC0016

    /**
     *
     * 実行メソッド
     *
     * @param goodsGroupSeqList 商品グループSEQリスト
     * @return カテゴリ登録商品マップ
     */
    Map<Integer, List<CategoryGoodsEntity>> execute(List<Integer> goodsGroupSeqList);

    /**
     * カテゴリ登録商品のリストを取得する
     *
     * @param categorySeq   カテゴリSEQ
     * @param goodsGroupSeqList 商品管理SEQのリスト
     * @return カテゴリ登録商品のリスト
     */
    List<CategoryGoodsEntity> getCategoryGoodsListByCategorySeq(Integer categorySeq, List<Integer> goodsGroupSeqList);
}