package jp.co.itechh.quad.ddd.infrastructure.sales.repository;

import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.ddd.domain.pricecalculator.valueobject.CouponPaymentPrice;
import jp.co.itechh.quad.ddd.domain.pricecalculator.valueobject.SalesPriceConsumptionTax;
import jp.co.itechh.quad.ddd.domain.sales.entity.SalesSlipForRevisionEntity;
import jp.co.itechh.quad.ddd.domain.sales.valueobject.AdjustmentAmount;
import jp.co.itechh.quad.ddd.domain.sales.valueobject.AdjustmentAmountSeq;
import jp.co.itechh.quad.ddd.domain.sales.valueobject.ApplyCoupon;
import jp.co.itechh.quad.ddd.domain.sales.valueobject.ItemPurchasePriceSubTotal;
import jp.co.itechh.quad.ddd.domain.sales.valueobject.SalesSlipId;
import jp.co.itechh.quad.ddd.domain.sales.valueobject.SalesSlipRevisionId;
import jp.co.itechh.quad.ddd.domain.sales.valueobject.SalesStatus;
import jp.co.itechh.quad.ddd.infrastructure.sales.dbentity.AdjustmentAmountDbEntity;
import jp.co.itechh.quad.ddd.infrastructure.sales.dbentity.AdjustmentAmountForRevisionDbEntity;
import jp.co.itechh.quad.ddd.infrastructure.sales.dbentity.ItemPurchasePriceSubTotalDbEntity;
import jp.co.itechh.quad.ddd.infrastructure.sales.dbentity.ItemPurchasePriceSubTotalForRevisionDbEntity;
import jp.co.itechh.quad.ddd.infrastructure.sales.dbentity.SalesSlipForRevisionDbEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 改訂用販売伝票 リポジトリHelperクラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class SalesSlipForRevisionRepositoryHelper {

    /**
     * 改訂用販売伝票エンティティに変換
     *
     * @param salesSlipForRevisionDbEntity 改訂用販売伝票DbEntityクラス
     * @param itemSubTotalForRevDbEntityList 改訂用商品購入価格小計 DbEntityクラス
     * @param adjustmentForRevDbEntityList 改訂用クーポンDbEntityクラス
     * @return 改訂用販売伝票エンティティ
     */
    public SalesSlipForRevisionEntity toSalesSlipForRevisionEntity(
        SalesSlipForRevisionDbEntity salesSlipForRevisionDbEntity,
        List<ItemPurchasePriceSubTotalForRevisionDbEntity> itemSubTotalForRevDbEntityList,
        List<AdjustmentAmountForRevisionDbEntity> adjustmentForRevDbEntityList) {

        if (salesSlipForRevisionDbEntity == null) {
            return null;
        }

        SalesStatus salesStatus =
                        EnumTypeUtil.getEnum(SalesStatus.class, salesSlipForRevisionDbEntity.getSalesStatus());

        ApplyCoupon applyCoupon = new ApplyCoupon(salesSlipForRevisionDbEntity.getCouponCode(),
                                                  salesSlipForRevisionDbEntity.getCouponSeq(),
                                                  salesSlipForRevisionDbEntity.getCouponVersionNo(),
                                                  salesSlipForRevisionDbEntity.getCouponName(), new CouponPaymentPrice(
                        salesSlipForRevisionDbEntity.getCouponPaymentPrice()),
                                                  salesSlipForRevisionDbEntity.isCouponUseFlag()
        );

        SalesPriceConsumptionTax salesPriceConsumptionTax =
                        new SalesPriceConsumptionTax(salesSlipForRevisionDbEntity.getStandardTaxTargetPrice(),
                                                     salesSlipForRevisionDbEntity.getReducedTaxTargetPrice(),
                                                     salesSlipForRevisionDbEntity.getStandardTax(),
                                                     salesSlipForRevisionDbEntity.getReducedTax()
                        );

        List<ItemPurchasePriceSubTotal> itemPurchasePriceSubTotalList =
            this.toItemPurchasePriceSubTotalList(itemSubTotalForRevDbEntityList);

        List<AdjustmentAmount> adjustmentAmountList =
            this.toAdjustmentAmountList(adjustmentForRevDbEntityList);

        return new SalesSlipForRevisionEntity(new SalesSlipId(salesSlipForRevisionDbEntity.getSalesSlipId()),
                                              salesStatus, applyCoupon, salesSlipForRevisionDbEntity.getBillingAmount(),
                                              itemPurchasePriceSubTotalList,
                                              salesSlipForRevisionDbEntity.getItemPurchasePriceTotal(),
                                              salesSlipForRevisionDbEntity.getCarriage(),
                                              salesSlipForRevisionDbEntity.getCommission(), salesPriceConsumptionTax,
                                              adjustmentAmountList, salesSlipForRevisionDbEntity.getTransactionId(),
                                              salesSlipForRevisionDbEntity.getCustomerId(),
                                              salesSlipForRevisionDbEntity.getOrderCode(),
                                              salesSlipForRevisionDbEntity.getRegistDate(),
                                              salesSlipForRevisionDbEntity.getSalesOpenDate(), new SalesSlipRevisionId(
                        salesSlipForRevisionDbEntity.getSalesSlipRevisionId()),
                                              salesSlipForRevisionDbEntity.getTransactionRevisionId(),
                                              salesSlipForRevisionDbEntity.isOriginCommissionApplyFlag(),
                                              salesSlipForRevisionDbEntity.isOriginCarriageApplyFlag()
        );
    }

    /**
     * 商品購入価格小計 値オブジェクトリストに変換
     *
     * @param itemPurchasePriceSubTotalDbEntityList 商品購入価格小計 DbEntityリスト
     * @return 商品購入価格小計 値オブジェクトリスト
     */
    public List<ItemPurchasePriceSubTotal> toItemPurchasePriceSubTotalList(List<ItemPurchasePriceSubTotalForRevisionDbEntity> itemPurchasePriceSubTotalDbEntityList) {

        if (CollectionUtils.isEmpty(itemPurchasePriceSubTotalDbEntityList)) {
            return null;
        }

        List<ItemPurchasePriceSubTotal> itemPurchasePriceSubTotalList = new ArrayList<>();

        itemPurchasePriceSubTotalDbEntityList.forEach(itemDb -> {
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
     * クーポン 値オブジェクトリストに変換
     *
     * @param adjustmentAmountDbEntityList クーポン DbEntityリスト
     * @return クーポン 値オブジェクトリスト
     */
    public List<AdjustmentAmount> toAdjustmentAmountList(List<AdjustmentAmountForRevisionDbEntity> adjustmentAmountDbEntityList) {

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
     * 改訂用販売伝票DbEntityクラスに変換
     *
     * @param salesSlipForRevisionEntity 改訂用販売伝票エンティティ
     * @return 改訂用販売伝票DbEntityクラス
     */
    public SalesSlipForRevisionDbEntity toSalesSlipForRevisionDbEntity(SalesSlipForRevisionEntity salesSlipForRevisionEntity) {

        SalesSlipForRevisionDbEntity salesSlipForRevisionDbEntity = new SalesSlipForRevisionDbEntity();

        if (salesSlipForRevisionEntity.getSalesSlipRevisionId() != null) {
            salesSlipForRevisionDbEntity.setSalesSlipRevisionId(
                            salesSlipForRevisionEntity.getSalesSlipRevisionId().getValue());
        }

        salesSlipForRevisionDbEntity.setTransactionRevisionId(salesSlipForRevisionEntity.getTransactionRevisionId());

        if (salesSlipForRevisionEntity.getSalesSlipRevisionId() != null) {
            salesSlipForRevisionDbEntity.setSalesSlipRevisionId(
                            salesSlipForRevisionEntity.getSalesSlipRevisionId().getValue());
        }

        salesSlipForRevisionDbEntity.setTransactionRevisionId(salesSlipForRevisionEntity.getTransactionRevisionId());

        if (salesSlipForRevisionEntity.getSalesSlipId() != null) {
            salesSlipForRevisionDbEntity.setSalesSlipId(salesSlipForRevisionEntity.getSalesSlipId().getValue());
        }

        salesSlipForRevisionDbEntity.setSalesSlipId(salesSlipForRevisionEntity.getSalesSlipId().getValue());
        salesSlipForRevisionDbEntity.setSalesStatus(salesSlipForRevisionEntity.getSalesStatus().name());
        salesSlipForRevisionDbEntity.setCouponCode(salesSlipForRevisionEntity.getApplyCoupon().getCouponCode());
        salesSlipForRevisionDbEntity.setCouponName(salesSlipForRevisionEntity.getApplyCoupon().getCouponName());
        salesSlipForRevisionDbEntity.setCouponSeq(salesSlipForRevisionEntity.getApplyCoupon().getCouponSeq());
        salesSlipForRevisionDbEntity.setCouponVersionNo(
                        salesSlipForRevisionEntity.getApplyCoupon().getCouponVersionNo());

        if (salesSlipForRevisionEntity.getApplyCoupon() != null) {

            if (salesSlipForRevisionEntity.getApplyCoupon().getCouponPaymentPrice() != null) {
                salesSlipForRevisionDbEntity.setCouponPaymentPrice(
                                salesSlipForRevisionEntity.getApplyCoupon().getCouponPaymentPrice().getValue());
            }

            salesSlipForRevisionDbEntity.setCouponUseFlag(
                            salesSlipForRevisionEntity.getApplyCoupon().isCouponUseFlag());
            salesSlipForRevisionDbEntity.setBillingAmount(salesSlipForRevisionEntity.getBillingAmount());
            salesSlipForRevisionDbEntity.setItemPurchasePriceTotal(
                            salesSlipForRevisionEntity.getItemPurchasePriceTotal());

        }

        salesSlipForRevisionDbEntity.setCarriage(salesSlipForRevisionEntity.getCarriage());
        salesSlipForRevisionDbEntity.setCommission(salesSlipForRevisionEntity.getCommission());

        if (salesSlipForRevisionEntity.getSalesPriceConsumptionTax() != null) {
            salesSlipForRevisionDbEntity.setStandardTaxTargetPrice(
                            salesSlipForRevisionEntity.getSalesPriceConsumptionTax().getStandardTaxTargetPrice());
            salesSlipForRevisionDbEntity.setReducedTaxTargetPrice(
                            salesSlipForRevisionEntity.getSalesPriceConsumptionTax().getReducedTaxTargetPrice());
            salesSlipForRevisionDbEntity.setStandardTax(
                            salesSlipForRevisionEntity.getSalesPriceConsumptionTax().getStandardTax());
            salesSlipForRevisionDbEntity.setReducedTax(
                            salesSlipForRevisionEntity.getSalesPriceConsumptionTax().getReducedTax());
        }

        salesSlipForRevisionDbEntity.setTransactionId(salesSlipForRevisionEntity.getTransactionId());
        salesSlipForRevisionDbEntity.setCustomerId(salesSlipForRevisionEntity.getCustomerId());
        salesSlipForRevisionDbEntity.setOrderCode(salesSlipForRevisionEntity.getOrderCode());
        salesSlipForRevisionDbEntity.setRegistDate(salesSlipForRevisionEntity.getRegistDate());
        salesSlipForRevisionDbEntity.setSalesOpenDate(salesSlipForRevisionEntity.getSalesOpenDate());
        salesSlipForRevisionDbEntity.setOriginCommissionApplyFlag(
                        salesSlipForRevisionEntity.isOriginCommissionApplyFlag());
        salesSlipForRevisionDbEntity.setOriginCarriageApplyFlag(salesSlipForRevisionEntity.isOriginCarriageApplyFlag());

        return salesSlipForRevisionDbEntity;
    }

    /**
     * クーポン DbEntityリストに変換
     *
     * @param adjustmentAmountList クーポン 値オブジェクトリスト
     * @param salesSlipId          販売伝票ID
     * @return クーポン DbEntityリスト
     */
    public List<AdjustmentAmountDbEntity> toAdjustmentAmountDbEntityList(List<AdjustmentAmount> adjustmentAmountList,
                                                                         String salesSlipId) {

        if (CollectionUtils.isEmpty(adjustmentAmountList)) {
            return null;
        }

        List<AdjustmentAmountDbEntity> adjustmentAmountDbEntityList = new ArrayList<>();

        adjustmentAmountList.forEach(item -> {
            AdjustmentAmountDbEntity adjustmentAmountDbEntity = new AdjustmentAmountDbEntity();

            adjustmentAmountDbEntity.setAdjustName(item.getAdjustName());
            adjustmentAmountDbEntity.setAdjustPrice(item.getAdjustPrice());
            adjustmentAmountDbEntity.setSalesSlipId(salesSlipId);

            adjustmentAmountDbEntityList.add(adjustmentAmountDbEntity);
        });

        return adjustmentAmountDbEntityList;
    }

    /**
     * 商品購入価格小計DbEntityリストに変換
     *
     * @param itemPurchasePriceSubTotalList 商品購入価格小計 値オブジェクトリスト
     * @param salesSlipId                   販売伝票ID
     * @return 商品購入価格小計DbEntityリスト
     */
    public List<ItemPurchasePriceSubTotalDbEntity> toItemPurchasePriceSubTotalDbEntityList(List<ItemPurchasePriceSubTotal> itemPurchasePriceSubTotalList,
                                                                                           String salesSlipId) {

        if (CollectionUtils.isEmpty(itemPurchasePriceSubTotalList)) {
            return null;
        }

        List<ItemPurchasePriceSubTotalDbEntity> itemPurchasePriceSubTotalDbEntityList = new ArrayList<>();

        itemPurchasePriceSubTotalList.forEach(item -> {
            ItemPurchasePriceSubTotalDbEntity itemPurchasePriceSubTotalDbEntity =
                            new ItemPurchasePriceSubTotalDbEntity();

            itemPurchasePriceSubTotalDbEntity.setOrderItemSeq(item.getSalesItemSeq());
            itemPurchasePriceSubTotalDbEntity.setItemPurchasePriceSubTotal(item.getItemPurchasePriceSubTotal());
            itemPurchasePriceSubTotalDbEntity.setItemId(item.getItemId());
            itemPurchasePriceSubTotalDbEntity.setItemUnitPrice(item.getItemUnitPrice());
            itemPurchasePriceSubTotalDbEntity.setItemCount(item.getItemCount());
            itemPurchasePriceSubTotalDbEntity.setFreeCarriageItemFlag(item.isFreeCarriageItemFlag());
            itemPurchasePriceSubTotalDbEntity.setSalesSlipId(salesSlipId);

            itemPurchasePriceSubTotalDbEntityList.add(itemPurchasePriceSubTotalDbEntity);
        });

        return itemPurchasePriceSubTotalDbEntityList;
    }

    /**
     * 改訂用商品購入価格小計 DbEntityリストに変換
     *
     * @param itemPurchasePriceSubTotalList 商品購入価格小計 値オブジェクトリスト
     * @param salesSlipRevisionId           改訂用販売伝票ID
     * @return 改訂用商品購入価格小計 DbEntityリスト
     */
    public List<ItemPurchasePriceSubTotalForRevisionDbEntity> toItemPurchasePriceSubTotalForRevisionDbEntityList(List<ItemPurchasePriceSubTotal> itemPurchasePriceSubTotalList,
                                                                                                                 String salesSlipRevisionId) {

        if (CollectionUtils.isEmpty(itemPurchasePriceSubTotalList)) {
            return null;
        }

        List<ItemPurchasePriceSubTotalForRevisionDbEntity> itemPurchasePriceSubTotalForRevisionDbEntityList =
                        new ArrayList<>();

        itemPurchasePriceSubTotalList.forEach(itemDomain -> {
            ItemPurchasePriceSubTotalForRevisionDbEntity itemPurchasePriceSubTotalForRevisionDbEntity =
                            new ItemPurchasePriceSubTotalForRevisionDbEntity();

            if (itemDomain.getSalesItemSeq() != null) {
                itemPurchasePriceSubTotalForRevisionDbEntity.setOrderItemSeq(itemDomain.getSalesItemSeq());
            }
            itemPurchasePriceSubTotalForRevisionDbEntity.setItemPurchasePriceSubTotal(
                            itemDomain.getItemPurchasePriceSubTotal());
            itemPurchasePriceSubTotalForRevisionDbEntity.setItemId(itemDomain.getItemId());
            itemPurchasePriceSubTotalForRevisionDbEntity.setItemUnitPrice(itemDomain.getItemUnitPrice());
            itemPurchasePriceSubTotalForRevisionDbEntity.setItemCount(itemDomain.getItemCount());
            itemPurchasePriceSubTotalForRevisionDbEntity.setItemTaxRate(itemDomain.getItemTaxRate());
            itemPurchasePriceSubTotalForRevisionDbEntity.setFreeCarriageItemFlag(itemDomain.isFreeCarriageItemFlag());
            itemPurchasePriceSubTotalForRevisionDbEntity.setSalesSlipRevisionId(salesSlipRevisionId);

            itemPurchasePriceSubTotalForRevisionDbEntityList.add(itemPurchasePriceSubTotalForRevisionDbEntity);
        });

        return itemPurchasePriceSubTotalForRevisionDbEntityList;
    }

    public List<AdjustmentAmountForRevisionDbEntity> toAdjustmentAmountForRevisionDbEntityList(List<AdjustmentAmount> adjustmentAmountList,
                                                                                               String salesSlipRevisionId) {

        if (CollectionUtils.isEmpty(adjustmentAmountList)) {
            return null;
        }

        List<AdjustmentAmountForRevisionDbEntity> adjustmentAmountForRevisionDbEntityList = new ArrayList<>();

        adjustmentAmountList.forEach(item -> {
            AdjustmentAmountForRevisionDbEntity adjustmentAmountForRevisionDbEntity =
                            new AdjustmentAmountForRevisionDbEntity();

            adjustmentAmountForRevisionDbEntity.setAdjustmentAmountSeq(item.getAdjustmentAmountSeq().getValue());
            adjustmentAmountForRevisionDbEntity.setAdjustName(item.getAdjustName());
            adjustmentAmountForRevisionDbEntity.setAdjustPrice(item.getAdjustPrice());
            adjustmentAmountForRevisionDbEntity.setSalesSlipRevisionId(salesSlipRevisionId);

            adjustmentAmountForRevisionDbEntityList.add(adjustmentAmountForRevisionDbEntity);
        });

        return adjustmentAmountForRevisionDbEntityList;
    }

}