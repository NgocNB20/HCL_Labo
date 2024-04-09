/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.pricecalculator;

import lombok.Data;

import java.util.List;

/**
 * 商品販売金額計算結果 ユースケースDto
 */
@Data
public class CalculationItemSalesPriceUseCaseDto {

    /** 商品販売金額リスト */
    private List<ItemSalesPriceUseCaseDto> itemSalesPriceUseCaseDtoList;

    /** 商品販売金額合計 */
    private int itemSalesPriceTotal;

    /** 税込商品販売金額合計 */
    private int itemSalesPriceTotalInTax;
}
