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

    /**
     * 送料
     */
    private Integer carriage;

    /**
     * クーポンSEQ
     */
    private Integer couponSeq;

    /**
     * クーポン枝番
     */
    private Integer couponVersionNo;

    /**
     * クーポン支払い額
     */
    private Integer couponPaymentPrice;

    /**
     * クーポン支払い額
     */
    private Boolean couponUseFlag;

    /**
     * 手数料
     */
    private Integer commission;

    /**
     * 請求金額
     */
    private Integer billingAmount;

    /**
     * 標準税率対象金額
     */
    private Integer standardTaxTargetPrice;

    /**
     * 軽減税率対象金額
     */
    private Integer reducedTaxTargetPrice;

    /**
     * 標準税額
     */
    private Integer standardTax;

    /**
     * 軽減税額
     */
    private Integer reducedTax;

    /**
     * 調整金額リスト
     */
    private List<AdjustmentAmount> adjustmentAmountList;

    /**
     * 商品金額リスト
     */
    private List<ItemPrice> itemPriceList;

    /**
     * 商品金額合計
     */
    private Integer goodsPriceTotal;

    /**
     * クーポン名
     */
    private String couponName;

}
