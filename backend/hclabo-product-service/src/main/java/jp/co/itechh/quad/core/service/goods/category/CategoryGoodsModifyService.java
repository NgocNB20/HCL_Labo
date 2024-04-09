/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2023 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.goods.category;

import java.util.List;

/**
 * カテゴリ商品修正
 *
 * @author PHAM QUANG DIEU (VJP)
 */
public interface CategoryGoodsModifyService {

    /**
     * カテゴリ商品の修正
     *
     * @param categorySeq    カテゴリーSEQ
     * @param goodsGroupList 商品グループリスト
     * @return 件数
     */
    int execute(Integer categorySeq, List<Integer> goodsGroupList);
}