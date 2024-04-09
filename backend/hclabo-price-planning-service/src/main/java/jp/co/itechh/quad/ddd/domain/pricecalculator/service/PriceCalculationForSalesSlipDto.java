/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.pricecalculator.service;

import jp.co.itechh.quad.ddd.domain.pricecalculator.valueobject.CouponPaymentPrice;
import jp.co.itechh.quad.ddd.domain.pricecalculator.valueobject.SalesPriceConsumptionTax;
import jp.co.itechh.quad.ddd.domain.sales.valueobject.ItemPurchasePriceSubTotal;
import lombok.Data;

import java.util.List;

/**
 * 販売伝票用一括金額計算結果 モデル
 */
@Data
public class PriceCalculationForSalesSlipDto {

    /** 商品購入価格小計 値オブジェクト リスト */
    private List<ItemPurchasePriceSubTotal> itemPurchasePriceSubTotalList;

    /** 商品販売金額合計 */
    private Integer itemPurchasePriceTotal;

    /** 送料 */
    private Integer carriage;

    /** クーポン支払い額 値オブジェクト */
    private CouponPaymentPrice couponPaymentPrice;

    /** 手数料 */
    private Integer commission;

    /** 販売金額消費税 値オブジェクト */
    private SalesPriceConsumptionTax salesPriceConsumptionTax;

    /** 請求金額 */
    private Integer billingAmount;

}
