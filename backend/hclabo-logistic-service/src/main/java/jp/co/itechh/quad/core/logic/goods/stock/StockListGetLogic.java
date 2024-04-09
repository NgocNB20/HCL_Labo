/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.stock;

import jp.co.itechh.quad.core.dto.goods.stock.StockDto;

import java.util.List;

/**
 * 在庫情報リスト取得ロジック
 *
 * @author negishi
 * @version $Revision: 1.3 $
 *
 */
public interface StockListGetLogic {

    /**
     * 実行メソッド
     *
     * @param goodsSeqList 商品SEQリスト
     * @return 在庫情報DTOリスト
     */
    List<StockDto> execute(List<Integer> goodsSeqList);

}
