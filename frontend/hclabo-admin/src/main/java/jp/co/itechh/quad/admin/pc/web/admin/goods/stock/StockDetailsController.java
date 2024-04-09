/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.goods.stock;

import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.ConversionUtility;
import jp.co.itechh.quad.admin.entity.goods.stock.StockResultEntity;
import jp.co.itechh.quad.admin.web.AbstractController;
import jp.co.itechh.quad.admin.web.PageInfoModule;
import jp.co.itechh.quad.inventory.presentation.api.InventoryApi;
import jp.co.itechh.quad.inventory.presentation.api.param.InventoryResultListGetRequest;
import jp.co.itechh.quad.inventory.presentation.api.param.InventoryResultListResponse;
import jp.co.itechh.quad.inventory.presentation.api.param.PageInfoRequest;
import jp.co.itechh.quad.product.presentation.api.ProductApi;
import jp.co.itechh.quad.product.presentation.api.param.ProductDetailsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.util.List;
import java.util.Optional;

/**
 * 在庫詳細画面コントロール
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@RequestMapping("goods/stock/details")
@Controller
@SessionAttributes(value = "stockDetailsModel")
@PreAuthorize("hasAnyAuthority('GOODS:4')")
public class StockDetailsController extends AbstractController {
    /**
     * Logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(StockDetailsController.class);

    /**
     * 在庫詳細Helper
     */
    private final StockDetailsHelper stockDetailsHelper;

    /**
     * 在庫情報Api
     */
    private final InventoryApi inventoryApi;

    /**
     * 商品API
     */
    private final ProductApi productApi;

    /**
     * 在庫検索：デフォルト：ソート項目
     */
    private static final String DEFAULT_STOCKSEARCH_ORDER_FIELD = "stockResultSeq";

    /**
     * 在庫検索：デフォルト：ソート条件(昇順/降順)
     */
    private static final boolean DEFAULT_STOCKSEARCH_ORDER_ASC = false;

    /**
     * 在庫検索：デフォルト：最大表示件数
     */
    private static final int DEFAULT_STOCKSEARCH_LIMIT = 100;

    /**
     * 変換ユーティリティクラス
     */
    private final ConversionUtility conversionUtility;

    /**
     * コンストラクター
     *
     * @param stockDetailsHelper
     * @param inventoryApi
     * @param conversionUtility
     * @param productApi 商品API
     */
    @Autowired
    public StockDetailsController(StockDetailsHelper stockDetailsHelper,
                                  InventoryApi inventoryApi,
                                  ConversionUtility conversionUtility,
                                  ProductApi productApi) {
        this.stockDetailsHelper = stockDetailsHelper;
        this.inventoryApi = inventoryApi;
        this.conversionUtility = conversionUtility;
        this.productApi = productApi;
    }

    /**
     * 在庫詳細ダイアログ表示 （Ajax）<br/>
     *
     * @param goodsSeq
     * @return 在庫詳細ダイアログ
     */
    @GetMapping(value = "/ajax")
    @ResponseBody
    public ResponseEntity<StockDetailsModel> doLoadIndex(@RequestParam(required = false) Optional<String> goodsSeq) {
        StockDetailsModel stockDetailsModel = new StockDetailsModel();
        // トップ画面アラートから遷移したとき、商品SEQを取得する。
        if (!goodsSeq.isPresent()) {
            return ResponseEntity.ok(stockDetailsModel);
        }
        String requestGoodsSeq = goodsSeq.get();
        // 変換Helper取得
        stockDetailsModel.setGoodsSeq(conversionUtility.toInteger(requestGoodsSeq));
        // 在庫詳細情報取得実行
        getStockDetailsInformationAjax(stockDetailsModel);
        return ResponseEntity.ok(stockDetailsModel);
    }

    /**
     * 在庫詳細情報取得<br/>
     *
     * @param stockDetailsModel
     */
    private void getStockDetailsInformationAjax(StockDetailsModel stockDetailsModel) {
        try {
            // 在庫詳細情報取得サービス実行
            ProductDetailsResponse productDetailsResponse =
                            productApi.getDetailsByGoodsSeq(stockDetailsModel.getGoodsSeq());
            if (productDetailsResponse == null) {
                return;
            }

            // ページング検索セットアップ
            PageInfoRequest pageInfoRequest = new PageInfoRequest();
            // ページング検索セットアップ
            PageInfoModule pageInfoHelper = ApplicationContextUtility.getBean(PageInfoModule.class);
            pageInfoHelper.setupPageRequest(pageInfoRequest, null, DEFAULT_STOCKSEARCH_LIMIT,
                                            DEFAULT_STOCKSEARCH_ORDER_FIELD, DEFAULT_STOCKSEARCH_ORDER_ASC
                                           );

            // 入庫実績リスト取得サービスの実行。
            InventoryResultListGetRequest inventoryResultListGetRequest =
                            stockDetailsHelper.toInventoryResultListGetRequestFromStockDetailsModel(stockDetailsModel);

            InventoryResultListResponse inventoryResultListResponse =
                            inventoryApi.get(inventoryResultListGetRequest, pageInfoRequest);

            List<StockResultEntity> stockResultEntityList =
                            stockDetailsHelper.toStockResultEntitiesFromInventoryResultListResponse(
                                            inventoryResultListResponse);

            // 取得した入庫実績エンティティリストDTOからPageへ設定
            stockDetailsHelper.toPageForStockResult(stockResultEntityList, productDetailsResponse, stockDetailsModel);

        } catch (HttpServerErrorException se) {
            LOGGER.error("例外処理が発生しました", se);
            handleServerError(se.getResponseBodyAsString());
        } catch (HttpClientErrorException ce) {
            LOGGER.error("在庫詳細情報取得", ce);
        }
    }
}