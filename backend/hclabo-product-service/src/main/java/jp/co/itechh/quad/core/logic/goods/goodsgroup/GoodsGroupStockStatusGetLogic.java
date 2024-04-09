/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.goodsgroup;

import jp.co.itechh.quad.core.constant.type.HTypeStockStatusType;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsDto;

import java.util.List;

/**
 * 商品グループの在庫状況取得ロジック
 *
 * @author Kaneko　2013/03/01
 */
public interface GoodsGroupStockStatusGetLogic {

    /**
     * 商品グループの在庫状況の設定
     *
     * @param goodsDtoList 商品DTOリスト
     * @return 在庫状況
     */
    HTypeStockStatusType execute(List<GoodsDto> goodsDtoList);

}