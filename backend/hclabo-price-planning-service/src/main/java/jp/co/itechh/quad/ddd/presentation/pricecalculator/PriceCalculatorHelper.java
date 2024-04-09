/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.presentation.pricecalculator;

import jp.co.itechh.quad.ddd.usecase.pricecalculator.CalculationItemSalesPriceUseCaseDto;
import jp.co.itechh.quad.ddd.usecase.pricecalculator.ItemSalesPriceUseCaseDto;
import jp.co.itechh.quad.pricecalculator.presentation.api.param.ItemPriceSubTotalDisp;
import jp.co.itechh.quad.pricecalculator.presentation.api.param.ItemSalesPriceCalculateResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 金額計算エンドポイント ControllerのHelperクラス
 *
 * @author kaneda
 */
@Component
public class PriceCalculatorHelper {

    /**
     * 商品販売金額計算結果 ユースケースDtoから商品販売金額計算レスポンスに変換
     *
     * @param calculationItemSalesPriceUseCaseDto
     * @return ItemSalesPriceCalculateResponse
     */
    public ItemSalesPriceCalculateResponse toItemSalesPriceCalculateResponse(CalculationItemSalesPriceUseCaseDto calculationItemSalesPriceUseCaseDto) {

        if (calculationItemSalesPriceUseCaseDto == null || CollectionUtils.isEmpty(
                        calculationItemSalesPriceUseCaseDto.getItemSalesPriceUseCaseDtoList())) {
            return null;
        }

        ItemSalesPriceCalculateResponse response = new ItemSalesPriceCalculateResponse();
        List<ItemPriceSubTotalDisp> itemSubTotalPriceList = new ArrayList<>();

        // 商品金額小計リスト
        for (ItemSalesPriceUseCaseDto curDto : calculationItemSalesPriceUseCaseDto.getItemSalesPriceUseCaseDtoList()) {

            ItemPriceSubTotalDisp orderItem = new ItemPriceSubTotalDisp();

            orderItem.setItemId(curDto.getItemId());
            orderItem.setItemTaxRate(curDto.getItemTaxRate());
            orderItem.setItemUnitPrice(curDto.getItemUnitPrice());
            orderItem.setItemUnitPriceInTax(curDto.getItemUnitPriceInTax());
            orderItem.setItemPriceSubTotal(curDto.getItemSalesPriceSubTotal());
            orderItem.setItemPriceSubTotalInTax(curDto.getItemSalesPriceSubTotalInTax());

            itemSubTotalPriceList.add(orderItem);
        }

        // レスポンスへ設定
        response.setItemSubTotalPriceList(itemSubTotalPriceList);
        response.setItemSalesPriceTotal(calculationItemSalesPriceUseCaseDto.getItemSalesPriceTotal());
        response.setItemSalesPriceTotalInTax(calculationItemSalesPriceUseCaseDto.getItemSalesPriceTotalInTax());

        return response;
    }

}