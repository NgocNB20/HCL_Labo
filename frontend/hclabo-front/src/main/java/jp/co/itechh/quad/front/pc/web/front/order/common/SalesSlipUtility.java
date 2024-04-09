/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.pc.web.front.order.common;

import jp.co.itechh.quad.coupon.presentation.api.CouponApi;
import jp.co.itechh.quad.salesslip.presentation.api.SalesSlipApi;
import jp.co.itechh.quad.salesslip.presentation.api.param.SalesSlipGetRequest;
import jp.co.itechh.quad.salesslip.presentation.api.param.SalesSlipResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.math.BigDecimal;

/**
 * 販売伝票Helper
 *
 * @author Pham Quang Dieu
 */

@Component
public class SalesSlipUtility {

    /** 販売伝票API */
    private SalesSlipApi salesSlipApi;

    /** クーポンエンドポイントAPI */
    private CouponApi couponApi;

    /**
     * コンストラク
     *
     * @param salesSlipApi
     * @param couponApi
     */
    @Autowired
    public SalesSlipUtility(SalesSlipApi salesSlipApi, CouponApi couponApi) {
        this.salesSlipApi = salesSlipApi;
        this.couponApi = couponApi;
    }

    /**
     * 販売伝票取得
     *
     * @param transactionId
     * @param model
     */
    public void getSaleSlips(String transactionId, Model model) {
        SalesSlipGetRequest salesSlipGetRequest = new SalesSlipGetRequest();
        salesSlipGetRequest.setTransactionId(transactionId);
        SalesSlipResponse salesSlipResponse = salesSlipApi.get(salesSlipGetRequest);
        SalesSlipModel salesSlipModel = convertToSalesSlipCommon(salesSlipResponse);
        model.addAttribute(SalesSlipModel.ATTRIBUTE_NAME_KEY, salesSlipModel);
    }

    /**
     * 販売伝票Modelに変換<br/>
     * 販売伝票が保持するトランザクションデータは伝票から取得して画面Modelに設定
     *
     * @param salesSlipResponse 販売伝票レスポンス
     * @return 販売伝票Model
     */
    public SalesSlipModel convertToSalesSlipCommon(SalesSlipResponse salesSlipResponse) {
        SalesSlipModel salesSlipModel = new SalesSlipModel();
        if (!ObjectUtils.isEmpty(salesSlipResponse)) {
            if (salesSlipResponse.getBillingAmount() != null) {
                salesSlipModel.setBillingAmount(new BigDecimal(salesSlipResponse.getBillingAmount()));
            }
            if (salesSlipResponse.getCommission() != null) {
                salesSlipModel.setCommission(new BigDecimal(salesSlipResponse.getCommission()));
            }
            if (salesSlipResponse.getItemSalesPriceTotal() != null) {
                salesSlipModel.setItemSalesPriceTotal(new BigDecimal(salesSlipResponse.getItemSalesPriceTotal()));
            }
            if (salesSlipResponse.getCarriage() != null) {
                salesSlipModel.setCarriage(new BigDecimal(salesSlipResponse.getCarriage()));
            }
            if (salesSlipResponse.getStandardTax() != null) {
                salesSlipModel.setStandardTax(new BigDecimal(salesSlipResponse.getStandardTax()));
            }
            if (salesSlipResponse.getStandardTaxTargetPrice() != null) {
                salesSlipModel.setStandardTaxTargetPrice(new BigDecimal(salesSlipResponse.getStandardTaxTargetPrice()));
            }
            if (salesSlipResponse.getReducedTaxTargetPrice() != null) {
                salesSlipModel.setReducedTaxTargetPrice(new BigDecimal(salesSlipResponse.getReducedTaxTargetPrice()));
            }
            if (salesSlipResponse.getReducedTax() != null) {
                salesSlipModel.setReducedTax(new BigDecimal(salesSlipResponse.getReducedTax()));
            }
            if (salesSlipResponse.getStandardTax() != null && salesSlipResponse.getReducedTax() != null) {
                salesSlipModel.setTaxPrice(
                                new BigDecimal(salesSlipResponse.getStandardTax() + salesSlipResponse.getReducedTax()));
            }
            if (salesSlipResponse.getCouponPaymentPrice() != null) {
                salesSlipModel.setCouponPaymentPrice(new BigDecimal(salesSlipResponse.getCouponPaymentPrice()));
            }
            if (salesSlipResponse.getCouponName() != null) {
                salesSlipModel.setCouponName(salesSlipResponse.getCouponName());
            }
        }

        return salesSlipModel;
    }
}