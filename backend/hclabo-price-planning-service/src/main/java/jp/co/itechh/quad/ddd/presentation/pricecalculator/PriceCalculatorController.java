/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.presentation.pricecalculator;

import io.swagger.annotations.ApiParam;
import jp.co.itechh.quad.core.web.AbstractController;
import jp.co.itechh.quad.ddd.usecase.pricecalculator.CalculationItemSalesPriceUseCase;
import jp.co.itechh.quad.ddd.usecase.pricecalculator.CalculationItemSalesPriceUseCaseDto;
import jp.co.itechh.quad.pricecalculator.presentation.api.PricePlanningApi;
import jp.co.itechh.quad.pricecalculator.presentation.api.param.ItemSalesPriceCalculateRequest;
import jp.co.itechh.quad.pricecalculator.presentation.api.param.ItemSalesPriceCalculateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 金額計算エンドポイント Controller
 */
@RestController
public class PriceCalculatorController extends AbstractController implements PricePlanningApi {

    /** 商品販売金額計算 ユースケース */
    private final CalculationItemSalesPriceUseCase calculationBillingAmountUseCase;

    /** Helperクラス */
    private final PriceCalculatorHelper priceCalculatorHelper;

    /**
     * コンストラクタ
     *
     * @param calculationBillingAmountUseCase 商品販売金額計算ユースケース
     * @param priceCalculatorHelper 金額計算エンドポイントControllerのHelper
     */
    @Autowired
    public PriceCalculatorController(CalculationItemSalesPriceUseCase calculationBillingAmountUseCase,
                                     PriceCalculatorHelper priceCalculatorHelper) {
        this.calculationBillingAmountUseCase = calculationBillingAmountUseCase;
        this.priceCalculatorHelper = priceCalculatorHelper;
    }

    /**
     * POST /price-planning/price-calculator/items-sales-price/calculate : 商品販売金額計算
     * カート（注文票）の各商品に対して、単価を取得し、小計及び商品合計金額を計算する
     *
     * @param itemSalesPriceCalculateRequest 商品販売金額計算リクエスト (required)
     * @return 商品販売金額計算レスポンス (status code 200)
     * or システムエラー (status code 200)
     */
    @Override
    public ResponseEntity<ItemSalesPriceCalculateResponse> calculateItemSalesPrice(
                    @ApiParam(value = "商品販売金額計算リクエスト", required = true) @Valid @RequestBody
                                    ItemSalesPriceCalculateRequest itemSalesPriceCalculateRequest) {

        // 商品販売金額計算ユースケース
        CalculationItemSalesPriceUseCaseDto calculationItemSalesPriceUseCaseDto =
                        calculationBillingAmountUseCase.calculationItemSalesPrice(
                                        itemSalesPriceCalculateRequest.getOrderSlipId());

        ItemSalesPriceCalculateResponse response =
                        priceCalculatorHelper.toItemSalesPriceCalculateResponse(calculationItemSalesPriceUseCaseDto);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
