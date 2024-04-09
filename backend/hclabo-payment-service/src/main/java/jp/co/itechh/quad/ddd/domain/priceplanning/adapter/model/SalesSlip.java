/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.priceplanning.adapter.model;

import lombok.Data;

import java.util.List;

/**
 * 販売伝票
 */
@Data
public class SalesSlip {

    private List<ItemPrice> itemPriceList;

    private Integer itemSalesPriceTotal;

    private Integer carriage;

    private Integer couponPaymentPrice;

    private Integer commission;

    private Integer billingAmount;

    private Integer standardTaxTargetPrice;

    private Integer reducedTaxTargetPrice;

    private Integer standardTax;

    private Integer reducedTax;

}
