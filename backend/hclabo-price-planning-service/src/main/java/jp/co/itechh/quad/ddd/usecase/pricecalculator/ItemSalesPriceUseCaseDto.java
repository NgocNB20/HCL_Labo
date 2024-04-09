/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.pricecalculator;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 商品販売金額 ユースケースDto
 */
@Data
public class ItemSalesPriceUseCaseDto {

    /** 注文商品連番 */
    private Integer orderItemSeq;

    /** 商品ID（商品SEQ） */
    private String itemId;

    /** 商品単価 */
    private int itemUnitPrice;

    /** 税込商品単価 */
    private int itemUnitPriceInTax;

    /** 商品金額小計 */
    private int itemSalesPriceSubTotal;

    /** 税込商品金額小計 */
    private int itemSalesPriceSubTotalInTax;

    /** 商品税率 */
    private BigDecimal itemTaxRate;

}
