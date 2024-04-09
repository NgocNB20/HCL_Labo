/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.infrastructure.sales.repository;

import jp.co.itechh.quad.ddd.domain.sales.entity.SalesSlipEntity;
import jp.co.itechh.quad.ddd.domain.sales.valueobject.AdjustmentAmount;
import jp.co.itechh.quad.ddd.domain.sales.valueobject.AdjustmentAmountSeq;
import jp.co.itechh.quad.ddd.domain.sales.valueobject.ItemPurchasePriceSubTotal;
import jp.co.itechh.quad.ddd.infrastructure.sales.dbentity.AdjustmentAmountDbEntity;
import jp.co.itechh.quad.ddd.infrastructure.sales.dbentity.ItemPurchasePriceSubTotalDbEntity;
import jp.co.itechh.quad.ddd.infrastructure.sales.dbentity.SalesSlipDbEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 販売伝票リポジトリHelperクラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class SalesSlipRepositoryHelper {

    /**
     * SalesSlipDbエンティティへ<br />
     *
     * @param salesSlipEntity
     */
    public SalesSlipDbEntity toSalesSlipDbEntity(SalesSlipEntity salesSlipEntity) {

        if (salesSlipEntity == null) {
            return null;
        }
        SalesSlipDbEntity salesSlipDbEntity = new SalesSlipDbEntity();
        salesSlipDbEntity.setSalesSlipId(salesSlipEntity.getSalesSlipId().getValue());
        salesSlipDbEntity.setSalesStatus(salesSlipEntity.getSalesStatus().name());

        // クーポン
        if (salesSlipEntity.getApplyCoupon() != null) {
            salesSlipDbEntity.setCouponCode(salesSlipEntity.getApplyCoupon().getCouponCode());
            salesSlipDbEntity.setCouponSeq(salesSlipEntity.getApplyCoupon().getCouponSeq());
            salesSlipDbEntity.setCouponName(salesSlipEntity.getApplyCoupon().getCouponName());
            salesSlipDbEntity.setCouponVersionNo(salesSlipEntity.getApplyCoupon().getCouponVersionNo());
            salesSlipDbEntity.setCouponUseFlag(salesSlipEntity.getApplyCoupon().isCouponUseFlag());
            if (salesSlipEntity.getApplyCoupon().getCouponPaymentPrice() != null) {
                salesSlipDbEntity.setCouponPaymentPrice(
                                salesSlipEntity.getApplyCoupon().getCouponPaymentPrice().getValue());
            }
        }

        salesSlipDbEntity.setBillingAmount(salesSlipEntity.getBillingAmount());
        salesSlipDbEntity.setItemPurchasePriceTotal(salesSlipEntity.getItemPurchasePriceTotal());
        salesSlipDbEntity.setCarriage(salesSlipEntity.getCarriage());
        salesSlipDbEntity.setCommission(salesSlipEntity.getCommission());

        // 消費税
        if (salesSlipEntity.getSalesPriceConsumptionTax() != null) {
            salesSlipDbEntity.setStandardTaxTargetPrice(
                            salesSlipEntity.getSalesPriceConsumptionTax().getStandardTaxTargetPrice());
            salesSlipDbEntity.setReducedTaxTargetPrice(
                            salesSlipEntity.getSalesPriceConsumptionTax().getReducedTaxTargetPrice());
            salesSlipDbEntity.setStandardTax(salesSlipEntity.getSalesPriceConsumptionTax().getStandardTax());
            salesSlipDbEntity.setReducedTax(salesSlipEntity.getSalesPriceConsumptionTax().getReducedTax());
        }

        salesSlipDbEntity.setTransactionId(salesSlipEntity.getTransactionId());
        salesSlipDbEntity.setCustomerId(salesSlipEntity.getCustomerId());
        salesSlipDbEntity.setOrderCode(salesSlipEntity.getOrderCode());
        salesSlipDbEntity.setRegistDate(salesSlipEntity.getRegistDate());
        salesSlipDbEntity.setSalesOpenDate(salesSlipEntity.getSalesOpenDate());

        return salesSlipDbEntity;
    }

    /**
     * アイテム購入価格小計Dbエンティティリストへ<br />
     *
     * @param salesSlipEntity 販売伝票エンティティ
     * @return アイテム購入価格小計Dbエンティティリストへ
     */
    public List<ItemPurchasePriceSubTotalDbEntity> toItemPurchasePriceSubTotalDbEntityList(SalesSlipEntity salesSlipEntity) {

        List<ItemPurchasePriceSubTotal> itemPurchasePriceSubTotalList =
                        salesSlipEntity.getItemPurchasePriceSubTotalList();
        if (CollectionUtils.isEmpty(itemPurchasePriceSubTotalList)) {
            return new ArrayList<>();
        }

        return itemPurchasePriceSubTotalList.stream().map(itemPurchasePriceSubTotal -> {
            ItemPurchasePriceSubTotalDbEntity itemPurchasePriceSubTotalDbEntity =
                            new ItemPurchasePriceSubTotalDbEntity();
            itemPurchasePriceSubTotalDbEntity.setItemId(itemPurchasePriceSubTotal.getItemId());
            itemPurchasePriceSubTotalDbEntity.setItemUnitPrice(itemPurchasePriceSubTotal.getItemUnitPrice());
            itemPurchasePriceSubTotalDbEntity.setItemPurchasePriceSubTotal(
                            itemPurchasePriceSubTotal.getItemPurchasePriceSubTotal());
            itemPurchasePriceSubTotalDbEntity.setItemCount(itemPurchasePriceSubTotal.getItemCount());
            itemPurchasePriceSubTotalDbEntity.setItemTaxRate(itemPurchasePriceSubTotal.getItemTaxRate());
            itemPurchasePriceSubTotalDbEntity.setFreeCarriageItemFlag(
                            itemPurchasePriceSubTotal.isFreeCarriageItemFlag());
            itemPurchasePriceSubTotalDbEntity.setSalesSlipId(salesSlipEntity.getSalesSlipId().getValue());
            itemPurchasePriceSubTotalDbEntity.setOrderItemSeq(itemPurchasePriceSubTotal.getSalesItemSeq());
            return itemPurchasePriceSubTotalDbEntity;

        }).collect(Collectors.toList());

    }

    /**
     * アイテム購入価格小計エンティティリストへ
     *
     * @param itemPurchasePriceSubTotalDbList カテゴリ詳細DTO
     * @return アイテム購入価格小計エンティティリストへ
     */
    public List<ItemPurchasePriceSubTotal> toItemPurchasePriceSubTotalEntityList(List<ItemPurchasePriceSubTotalDbEntity> itemPurchasePriceSubTotalDbList) {

        if (CollectionUtils.isEmpty(itemPurchasePriceSubTotalDbList)) {
            return null;
        }

        List<ItemPurchasePriceSubTotal> itemPurchasePriceSubTotalList = new ArrayList<>();

        itemPurchasePriceSubTotalDbList.forEach(itemDb -> {
            ItemPurchasePriceSubTotal itemPurchasePriceSubTotal =
                            new ItemPurchasePriceSubTotal(itemDb.getOrderItemSeq(),
                                                          itemDb.getItemPurchasePriceSubTotal(), itemDb.getItemId(),
                                                          itemDb.getItemUnitPrice(), itemDb.getItemCount(),
                                                          itemDb.getItemTaxRate(), itemDb.isFreeCarriageItemFlag()
                            );

            itemPurchasePriceSubTotalList.add(itemPurchasePriceSubTotal);
        });

        return itemPurchasePriceSubTotalList;
    }

    /**
     * 調整金額 値オブジェクトリストに変換
     *
     * @param adjustmentAmountDbEntityList クーポン DbEntityリスト
     * @return 調整金額値オブジェクトリスト
     */
    public List<AdjustmentAmount> toAdjustmentAmountList(List<AdjustmentAmountDbEntity> adjustmentAmountDbEntityList) {

        if (CollectionUtils.isEmpty(adjustmentAmountDbEntityList)) {
            return null;
        }

        List<AdjustmentAmount> adjustmentAmountList = new ArrayList<>();

        adjustmentAmountDbEntityList.forEach(item -> {
            AdjustmentAmount adjustmentAmount =
                            new AdjustmentAmount(new AdjustmentAmountSeq(item.getAdjustmentAmountSeq()),
                                                 item.getAdjustName(), item.getAdjustPrice()
                            );

            adjustmentAmountList.add(adjustmentAmount);
        });

        return adjustmentAmountList;
    }

    /**
     * 調整金額Dbエンティティリストへ<br />
     *
     * @param salesSlipEntity 販売伝票エンティティ
     * @return アイテム購入価格小計Dbエンティティリストへ
     */
    public List<AdjustmentAmountDbEntity> toAdjustmentAmountDbEntityList(SalesSlipEntity salesSlipEntity) {

        List<AdjustmentAmount> adjustmentAmountList = salesSlipEntity.getAdjustmentAmountList();
        if (CollectionUtils.isEmpty(adjustmentAmountList)) {
            return new ArrayList<>();
        }

        return adjustmentAmountList.stream().map(adjustmentAmount -> {

            AdjustmentAmountDbEntity adjustmentAmountDbEntity = new AdjustmentAmountDbEntity();
            adjustmentAmountDbEntity.setSalesSlipId(salesSlipEntity.getSalesSlipId().getValue());
            adjustmentAmountDbEntity.setAdjustmentAmountSeq(adjustmentAmount.getAdjustmentAmountSeq().getValue());
            adjustmentAmountDbEntity.setAdjustName(adjustmentAmount.getAdjustName());
            adjustmentAmountDbEntity.setAdjustPrice(adjustmentAmount.getAdjustPrice());

            return adjustmentAmountDbEntity;

        }).collect(Collectors.toList());

    }

}