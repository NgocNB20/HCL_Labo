/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.stock.adapter;

import jp.co.itechh.quad.core.dto.goods.stock.StockDto;

import java.util.List;

/**
 * 商品アダプター
 *
 * @author yt23807
 */
public interface IStockAdapter {

    /**
     * 物流マイクロサービス<br/>
     * 在庫詳細情報リスト取得
     *  GET /stock/details : 在庫詳細一覧取得（getDetails）を呼び出す
     *
     * @param productIdList 商品IDリスト
     * @return 商品詳細リスト
     */
    List<StockDto> getDetails(List<String> productIdList);
}
