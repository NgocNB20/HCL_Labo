/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.presentation.sales;

import jp.co.itechh.quad.ddd.domain.pricecalculator.valueobject.SalesPriceConsumptionTax;
import jp.co.itechh.quad.ddd.domain.sales.entity.SalesSlipEntity;
import jp.co.itechh.quad.ddd.domain.sales.entity.SalesSlipForRevisionEntity;
import jp.co.itechh.quad.ddd.domain.sales.valueobject.AdjustmentAmount;
import jp.co.itechh.quad.ddd.domain.sales.valueobject.ApplyCoupon;
import jp.co.itechh.quad.ddd.domain.sales.valueobject.ItemPurchasePriceSubTotal;
import jp.co.itechh.quad.salesslip.presentation.api.param.AdjustmentAmountResponse;
import jp.co.itechh.quad.salesslip.presentation.api.param.GetSalesSlipForRevisionByTransactionRevisionIdResponse;
import jp.co.itechh.quad.salesslip.presentation.api.param.ItemPriceSubTotal;
import jp.co.itechh.quad.salesslip.presentation.api.param.SalesPriceConsumptionTaxResponse;
import jp.co.itechh.quad.salesslip.presentation.api.param.SalesSlipCouponApplyResponse;
import jp.co.itechh.quad.salesslip.presentation.api.param.SalesSlipResponse;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 売伝票エンドポイント ControllerのHelperクラス
 *
 * @author kaneda
 */
@Component
public class SalesSlipHelper {

    /**
     * 販売伝票エンティティから販売伝票取得レスポンスに変換
     *
     * @param salesSlipEntity 販売伝票エンティティ
     * @return 販売伝票レスポンス sales slip response
     */
    public SalesSlipResponse toSalesSlipResponse(SalesSlipEntity salesSlipEntity) {

        if (salesSlipEntity == null) {
            return null;
        }

        SalesSlipResponse response = new SalesSlipResponse();

        response.setSalesSlipId(salesSlipEntity.getSalesSlipId().getValue());
        response.setBillingAmount(salesSlipEntity.getBillingAmount());
        response.setCarriage(salesSlipEntity.getCarriage());
        response.setCommission(salesSlipEntity.getCommission());
        if (!StringUtils.isBlank(salesSlipEntity.getApplyCoupon().getCouponCode())) {
            response.setCouponCode(salesSlipEntity.getApplyCoupon().getCouponCode());
            response.setCouponSeq(salesSlipEntity.getApplyCoupon().getCouponSeq());
            response.setCouponVersionNo(salesSlipEntity.getApplyCoupon().getCouponVersionNo());
            response.setCouponName(salesSlipEntity.getApplyCoupon().getCouponName());
            response.setCouponPaymentPrice(salesSlipEntity.getApplyCoupon().getCouponPaymentPrice().getValue());
            response.setCouponUseFlag(salesSlipEntity.getApplyCoupon().isCouponUseFlag());
        }
        List<ItemPriceSubTotal> itemPriceSubTotalList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(salesSlipEntity.getItemPurchasePriceSubTotalList())) {
            for (ItemPurchasePriceSubTotal itemPurchasePriceSubTotal : salesSlipEntity.getItemPurchasePriceSubTotalList()) {

                ItemPriceSubTotal itemPriceSubTotal = new ItemPriceSubTotal();

                itemPriceSubTotal.setSalesItemSeq(itemPurchasePriceSubTotal.getSalesItemSeq());
                itemPriceSubTotal.setItemId(itemPurchasePriceSubTotal.getItemId());
                itemPriceSubTotal.setItemUnitPrice(itemPurchasePriceSubTotal.getItemUnitPrice());
                itemPriceSubTotal.setItemPriceSubTotal(itemPurchasePriceSubTotal.getItemPurchasePriceSubTotal());
                itemPriceSubTotal.setItemCount(itemPurchasePriceSubTotal.getItemCount());
                itemPriceSubTotal.setItemTaxRate(itemPurchasePriceSubTotal.getItemTaxRate());

                itemPriceSubTotalList.add(itemPriceSubTotal);
            }
        }
        response.setItemPriceSubTotalList(itemPriceSubTotalList);
        response.setItemSalesPriceTotal(salesSlipEntity.getItemPurchasePriceTotal());
        response.setReducedTax(salesSlipEntity.getSalesPriceConsumptionTax().getReducedTax());
        response.setReducedTaxTargetPrice(salesSlipEntity.getSalesPriceConsumptionTax().getReducedTaxTargetPrice());
        response.setStandardTax(salesSlipEntity.getSalesPriceConsumptionTax().getStandardTax());
        response.setStandardTaxTargetPrice(salesSlipEntity.getSalesPriceConsumptionTax().getStandardTaxTargetPrice());
        response.setAdjustmentAmountList(toAdjustmentAmountListResponse(salesSlipEntity.getAdjustmentAmountList()));

        return response;
    }

    /**
     * 改訂用取引IDに紐づく改訂用販売伝票取得レスポンスに変換
     *
     * @param salesSlipForRevisionEntity 改訂用販売伝票エンティティ
     * @return 改訂用取引IDに紐づく改訂用販売伝票取得レスポンス
     */
    public GetSalesSlipForRevisionByTransactionRevisionIdResponse toGetSalesSlipForRevisionByTransactionRevisionIdResponse(
                    SalesSlipForRevisionEntity salesSlipForRevisionEntity) {

        if (ObjectUtils.isEmpty(salesSlipForRevisionEntity)) {
            return null;
        }

        GetSalesSlipForRevisionByTransactionRevisionIdResponse response =
                        new GetSalesSlipForRevisionByTransactionRevisionIdResponse();

        if (salesSlipForRevisionEntity.getSalesSlipId() != null) {
            response.setSalesSlipId(salesSlipForRevisionEntity.getSalesSlipId().getValue());
        }
        if (salesSlipForRevisionEntity.getSalesStatus() != null) {
            response.setSalesStatus(salesSlipForRevisionEntity.getSalesStatus().name());
        }

        response.setBillingAmount(salesSlipForRevisionEntity.getBillingAmount());
        response.setItemPurchasePriceTotal(salesSlipForRevisionEntity.getItemPurchasePriceTotal());
        response.setCarriage(salesSlipForRevisionEntity.getCarriage());
        response.setCommission(salesSlipForRevisionEntity.getCommission());
        response.setTransactionId(salesSlipForRevisionEntity.getTransactionId());
        response.setCustomerId(salesSlipForRevisionEntity.getCustomerId());
        response.setRegistDate(salesSlipForRevisionEntity.getRegistDate());
        response.setSalesOpenDate(salesSlipForRevisionEntity.getSalesOpenDate());

        response.setApplyCouponResponse(toSalesSlipCouponApplyResponse(salesSlipForRevisionEntity.getApplyCoupon()));
        response.setItemPriceSubTotal(
                        toItemPriceSubTotalList(salesSlipForRevisionEntity.getItemPurchasePriceSubTotalList()));
        response.setSalesPriceConsumptionTaxResponse(
                        toSalesPriceConsumptionTaxResponse(salesSlipForRevisionEntity.getSalesPriceConsumptionTax()));
        response.setAdjustmentAmountListResponse(
                        toAdjustmentAmountListResponse(salesSlipForRevisionEntity.getAdjustmentAmountList()));

        if (salesSlipForRevisionEntity.getSalesSlipRevisionId() != null) {
            response.setSalesSlipRevisionId(salesSlipForRevisionEntity.getSalesSlipRevisionId().getValue());
        }

        response.setTransactionRevisionId(salesSlipForRevisionEntity.getTransactionRevisionId());

        return response;
    }

    /**
     * 商品金額小計リストレスポンスリストに変換
     *
     * @param itemPurchasePriceSubTotalList 商品購入価格小計 値オブジェクトリスト
     * @return 商品金額小計リスト
     */
    public List<ItemPriceSubTotal> toItemPriceSubTotalList(List<ItemPurchasePriceSubTotal> itemPurchasePriceSubTotalList) {

        if (CollectionUtils.isEmpty(itemPurchasePriceSubTotalList)) {
            return null;
        }

        List<ItemPriceSubTotal> responseList = new ArrayList<>();

        itemPurchasePriceSubTotalList.forEach(item -> {
            ItemPriceSubTotal response = new ItemPriceSubTotal();

            response.setSalesItemSeq(item.getSalesItemSeq());
            response.setItemPriceSubTotal(item.getItemPurchasePriceSubTotal());
            response.setItemId(item.getItemId());
            response.setItemUnitPrice(item.getItemUnitPrice());
            response.setItemCount(item.getItemCount());
            response.setItemTaxRate(item.getItemTaxRate());

            responseList.add(response);
        });

        return responseList;
    }

    /**
     * 販売金額消費税レスポンスに変換
     *
     * @param salesPriceConsumptionTax 販売金額消費税 値オブジェクト
     * @return 販売金額消費税レスポンス
     */
    public SalesPriceConsumptionTaxResponse toSalesPriceConsumptionTaxResponse(SalesPriceConsumptionTax salesPriceConsumptionTax) {

        if (ObjectUtils.isEmpty(salesPriceConsumptionTax)) {
            return null;
        }

        SalesPriceConsumptionTaxResponse response = new SalesPriceConsumptionTaxResponse();

        response.setStandardTaxTargetPrice(salesPriceConsumptionTax.getStandardTaxTargetPrice());
        response.setReducedTaxTargetPrice(salesPriceConsumptionTax.getReducedTaxTargetPrice());
        response.setStandardTax(salesPriceConsumptionTax.getStandardTax());
        response.setReducedTax(salesPriceConsumptionTax.getReducedTax());

        return response;
    }

    /**
     * クーポン 値オブジェクトリストに変換
     *
     * @param adjustmentAmountList クーポン 値オブジェクトリスト
     * @return クーポン 値オブジェクトリスト
     */
    public List<AdjustmentAmountResponse> toAdjustmentAmountListResponse(List<AdjustmentAmount> adjustmentAmountList) {

        if (CollectionUtils.isEmpty(adjustmentAmountList)) {
            return null;
        }

        List<AdjustmentAmountResponse> responseList = new ArrayList<>();

        adjustmentAmountList.forEach(item -> {
            AdjustmentAmountResponse response = new AdjustmentAmountResponse();

            response.setAdjustName(item.getAdjustName());
            response.setAdjustPrice(item.getAdjustPrice());

            responseList.add(response);
        });

        return responseList;
    }

    /**
     * クーポン適用レスポンスに変換
     *
     * @param applyCoupon クーポン 値オブジェクト
     * @return クーポン適用レスポンス
     */
    public SalesSlipCouponApplyResponse toSalesSlipCouponApplyResponse(ApplyCoupon applyCoupon) {

        if (ObjectUtils.isEmpty(applyCoupon)) {
            return null;
        }

        SalesSlipCouponApplyResponse response = new SalesSlipCouponApplyResponse();

        response.setCouponCode(applyCoupon.getCouponCode());
        response.setCouponSeq(applyCoupon.getCouponSeq());
        response.setCouponVersionNo(applyCoupon.getCouponVersionNo());
        response.setCouponName(applyCoupon.getCouponName());
        if (applyCoupon.getCouponPaymentPrice() != null) {
            response.setCouponPaymentPrice(applyCoupon.getCouponPaymentPrice().getValue());
        }
        response.setCouponUseFlag(applyCoupon.isCouponUseFlag());

        return response;
    }

}