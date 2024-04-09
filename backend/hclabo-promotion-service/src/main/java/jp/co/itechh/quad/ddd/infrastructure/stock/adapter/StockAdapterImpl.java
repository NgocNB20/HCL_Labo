/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.infrastructure.stock.adapter;

import jp.co.itechh.quad.core.dto.goods.stock.StockDto;
import jp.co.itechh.quad.core.web.HeaderParamsUtility;
import jp.co.itechh.quad.ddd.domain.stock.adapter.IStockAdapter;
import jp.co.itechh.quad.stock.presentation.api.StockApi;
import jp.co.itechh.quad.stock.presentation.api.param.StockDetailListGetRequest;
import jp.co.itechh.quad.stock.presentation.api.param.StockDetailListResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 在庫アダプター実装クラス
 *
 * @author yt23807
 */
@Component
public class StockAdapterImpl implements IStockAdapter {

    /** 在庫API */
    private final StockApi stockApi;

    /** 在庫アダプターHelperクラス */
    private final StockAdapterHelper stockAdapterHelper;

    /**
     * コンストラクタ
     *
     * @param stockApi
     * @param stockAdapterHelper
     * @param headerParamsUtil ヘッダパラメーターユーティル
     */
    @Autowired
    public StockAdapterImpl(StockApi stockApi,
                            StockAdapterHelper stockAdapterHelper,
                            HeaderParamsUtility headerParamsUtil) {
        this.stockApi = stockApi;
        this.stockAdapterHelper = stockAdapterHelper;

        // サービス間のAPI呼び出す時、ヘッダーにパラメーターを設定する
        headerParamsUtil.setHeader(this.stockApi.getApiClient());
    }

    /**
     * 物流マイクロサービス<br/>
     * 在庫詳細情報リスト取得
     *  GET /stock/details : 在庫詳細一覧取得（getDetails）を呼び出す
     *
     * @param productIdList 商品IDリスト
     * @return 在庫詳細リスト
     */
    @Override
    public List<StockDto> getDetails(List<String> productIdList) {

        StockDetailListGetRequest stockDetailListGetRequest = new StockDetailListGetRequest();
        stockDetailListGetRequest.setGoodsSeqList(stockAdapterHelper.toIntegerList(productIdList));

        StockDetailListResponse stockDetailListResponse = stockApi.getDetails(stockDetailListGetRequest);

        return stockAdapterHelper.toStockList(stockDetailListResponse);
    }
}