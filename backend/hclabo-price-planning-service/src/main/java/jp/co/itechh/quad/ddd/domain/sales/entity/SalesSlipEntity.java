/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.sales.entity;

import jp.co.itechh.quad.ddd.domain.pricecalculator.service.PriceCalculationForSalesSlipDto;
import jp.co.itechh.quad.ddd.domain.pricecalculator.valueobject.SalesPriceConsumptionTax;
import jp.co.itechh.quad.ddd.domain.sales.valueobject.AdjustmentAmount;
import jp.co.itechh.quad.ddd.domain.sales.valueobject.ApplyCoupon;
import jp.co.itechh.quad.ddd.domain.sales.valueobject.ItemPurchasePriceSubTotal;
import jp.co.itechh.quad.ddd.domain.sales.valueobject.SalesItemSeqFactory;
import jp.co.itechh.quad.ddd.domain.sales.valueobject.SalesSlipId;
import jp.co.itechh.quad.ddd.domain.sales.valueobject.SalesStatus;
import jp.co.itechh.quad.ddd.exception.AssertChecker;
import jp.co.itechh.quad.ddd.exception.DomainException;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * 販売伝票エンティティ
 */
@Getter
public class SalesSlipEntity {

    /** 販売伝票ID */
    protected SalesSlipId salesSlipId;

    /** 販売ステータス */
    protected SalesStatus salesStatus;

    /** 適用クーポン */
    protected ApplyCoupon applyCoupon;

    /** 請求金額 */
    protected Integer billingAmount;

    /** 商品購入価格小計リスト */
    protected List<ItemPurchasePriceSubTotal> itemPurchasePriceSubTotalList;

    /** 商品購入価格合計 */
    protected Integer itemPurchasePriceTotal;

    /** 送料 */
    protected Integer carriage;

    /** 手数料 */
    protected Integer commission;

    /** 販売金額消費税 */
    protected SalesPriceConsumptionTax salesPriceConsumptionTax;

    /** 調整金額リスト */
    protected List<AdjustmentAmount> adjustmentAmountList;

    /** 取引ID */
    protected String transactionId;

    /** 顧客ID */
    protected String customerId;

    /** 受注番号（確定時に設定される） */
    protected String orderCode;

    /** 登録日時 */
    protected Date registDate;

    /** 販売確定日時 */
    protected Date salesOpenDate;

    /**
     * コンストラクタ
     * 販売伝票発行
     *
     * @param transactionId
     * @param customerId
     * @param registDate
     */
    public SalesSlipEntity(String transactionId, String customerId, Date registDate) {

        // アサートチェック
        AssertChecker.assertNotEmpty("transactionId is empty", transactionId);
        AssertChecker.assertNotEmpty("customerId is empty", customerId);
        AssertChecker.assertNotNull("registDate is null", registDate);

        // 設定
        this.salesSlipId = new SalesSlipId();
        this.salesStatus = SalesStatus.DRAFT;
        this.transactionId = transactionId;
        this.customerId = customerId;
        this.registDate = registDate;
        // 初期化
        this.itemPurchasePriceSubTotalList = new ArrayList<>();
    }

    /**
     * コンストラクタ<br/>
     * 販売伝票改訂(改訂用販売伝票から生成)
     *
     * @param salesSlipForRevisionEntity
     * @param registDate
     */
    public SalesSlipEntity(SalesSlipForRevisionEntity salesSlipForRevisionEntity, Date registDate) {

        // アサートチェック
        AssertChecker.assertNotNull("salesSlipForRevisionEntity is null", salesSlipForRevisionEntity);
        AssertChecker.assertNotNull("registDate is null", registDate);

        /* 設定 */
        // 改訂用販売伝票をコピー
        copyProperties(salesSlipForRevisionEntity);
        // 改訂用販売伝票IDを引き継いで、販売伝票IDに設定
        this.salesSlipId = new SalesSlipId(salesSlipForRevisionEntity.getSalesSlipRevisionId().getValue());
        // 改訂用取引IDを引き継いで、取引IDに設定
        this.transactionId = salesSlipForRevisionEntity.getTransactionRevisionId();
        this.registDate = registDate;
    }

    /**
     * 金額設定
     * ※販売伝票の各種金額を全て設定する
     *
     * @param bundlePriceCalculationDto
     */
    public void settingPrices(PriceCalculationForSalesSlipDto bundlePriceCalculationDto) {

        // アサートチェック
        AssertChecker.assertNotNull("bundlePriceCalculationDto is null", bundlePriceCalculationDto);

        /* 金額設定 */
        // 商品購入価格小計リスト
        this.itemPurchasePriceSubTotalList = bundlePriceCalculationDto.getItemPurchasePriceSubTotalList();
        // 商品購入価格合計
        this.itemPurchasePriceTotal = bundlePriceCalculationDto.getItemPurchasePriceTotal();
        // 送料
        this.carriage = bundlePriceCalculationDto.getCarriage();
        // クーポン
        if (this.applyCoupon != null && !StringUtils.isBlank(this.applyCoupon.getCouponCode())) {
            ApplyCoupon newApplyCoupon =
                            new ApplyCoupon(this.applyCoupon.getCouponCode(), this.applyCoupon.getCouponSeq(),
                                            this.applyCoupon.getCouponVersionNo(), this.applyCoupon.getCouponName(),
                                            bundlePriceCalculationDto.getCouponPaymentPrice(),
                                            this.applyCoupon.isCouponUseFlag()
                            );
            this.applyCoupon = newApplyCoupon;
        }
        // 手数料
        this.commission = bundlePriceCalculationDto.getCommission();
        // 販売金額消費税
        this.salesPriceConsumptionTax = bundlePriceCalculationDto.getSalesPriceConsumptionTax();
        // 請求金額
        this.billingAmount = bundlePriceCalculationDto.getBillingAmount();

    }

    /**
     * クーポン設定
     * ※ドメインサービス用 パッケージプライベート
     *
     * @param coupon
     */
    void settingCoupon(ApplyCoupon coupon) {

        // アサートチェック
        AssertChecker.assertNotNull("coupon is null", coupon);

        this.applyCoupon = coupon;
    }

    /**
     * クーポン取消
     */
    public void cancelCoupon() {

        // チェック
        // 下書き状態でないならエラー
        if (this.salesStatus != SalesStatus.DRAFT) {
            throw new DomainException("PRICE-PLANNING-SALS0001-E");
        }

        this.applyCoupon = null;
    }

    /**
     * 販売伝票確定
     *
     * @param salesOpenDate
     */
    public void openSlip(String orderCode, Date salesOpenDate) {

        // チェック
        // アサートチェック
        AssertChecker.assertNotEmpty("orderCode is blank", orderCode);
        // 下書き状態でないならエラー
        if (this.salesStatus != SalesStatus.DRAFT) {
            throw new DomainException("PRICE-PLANNING-SALS0001-E");
        }

        // 販売ステータス確定
        this.salesStatus = SalesStatus.OPEN;
        this.salesOpenDate = salesOpenDate;
        this.orderCode = orderCode;
    }

    /**
     * 伝票終了
     */
    public void closeSlip() {

        // チェック
        // 確定状態でないならエラー
        if (this.salesStatus != SalesStatus.OPEN) {
            throw new DomainException("PRICE-PLANNING-SALS0001-E");
        }
        // 販売ステータス終了
        this.salesStatus = SalesStatus.CLOSE;
    }

    void setItemPurchasePriceSubTotalList(List<ItemPurchasePriceSubTotal> itemPurchasePriceSubTotalList) {
        this.itemPurchasePriceSubTotalList = itemPurchasePriceSubTotalList;
    }

    /**
     *  商品リストを個数単位で分割し、数量をすべて1にする
     */
    public void itemListDivision() {
        List<ItemPurchasePriceSubTotal> itemListDivision = new ArrayList<>();
        ItemPurchasePriceSubTotal itemPurchasePriceSubTotalNew;

        // 分割前は商品連番順にソートされている想定だ
        this.getItemPurchasePriceSubTotalList().sort(Comparator.comparing(ItemPurchasePriceSubTotal::getSalesItemSeq));

        for (ItemPurchasePriceSubTotal itemPurchasePriceSubTotal : this.getItemPurchasePriceSubTotalList()) {

            int count = itemPurchasePriceSubTotal.getItemCount();
            int salesItemSeqCur = SalesItemSeqFactory.constructSalesItemSeq(itemListDivision);

            // 数量が1の場合は、変更後商品リストの末尾に追加する
            if (count == 1) {
                itemPurchasePriceSubTotalNew = new ItemPurchasePriceSubTotal(
                    salesItemSeqCur,
                    itemPurchasePriceSubTotal.getItemPurchasePriceSubTotal(),
                    itemPurchasePriceSubTotal.getItemId(),
                    itemPurchasePriceSubTotal.getItemUnitPrice(),
                    1,
                    itemPurchasePriceSubTotal.getItemTaxRate(),
                    itemPurchasePriceSubTotal.isFreeCarriageItemFlag()
                );
                itemListDivision.add(itemPurchasePriceSubTotalNew);
            }
            // 数量が2以上の場合は、注文数量が1つずつになるように変更後商品リストの末尾に追加する
            else if (count > 1) {
                int itemPurchasePriceSubTotalVal = itemPurchasePriceSubTotal.getItemPurchasePriceSubTotal() / count;
                for (int index = 0; index < count; index++) {
                    itemPurchasePriceSubTotalNew = new ItemPurchasePriceSubTotal(
                        salesItemSeqCur++,
                        itemPurchasePriceSubTotalVal,
                        itemPurchasePriceSubTotal.getItemId(),
                        itemPurchasePriceSubTotal.getItemUnitPrice(),
                        1,
                        itemPurchasePriceSubTotal.getItemTaxRate(),
                        itemPurchasePriceSubTotal.isFreeCarriageItemFlag()
                    );

                    itemListDivision.add(itemPurchasePriceSubTotalNew);
                }
            }

        }

        this.setItemPurchasePriceSubTotalList(itemListDivision);
    }

    /**
     * コンストラクタ<br/>
     * ※改訂用
     */
    SalesSlipEntity() {
    }

    /**
     * DB取得値設定用コンストラクタ
     * ※ユースケース層での使用禁止
     */
    public SalesSlipEntity(SalesSlipId salesSlipId,
                           SalesStatus salesStatus,
                           ApplyCoupon applyCoupon,
                           Integer billingAmount,
                           List<ItemPurchasePriceSubTotal> itemPurchasePriceSubTotalList,
                           Integer itemPurchasePriceTotal,
                           Integer carriage,
                           Integer commission,
                           SalesPriceConsumptionTax salesPriceConsumptionTax,
                           List<AdjustmentAmount> adjustmentAmountList,
                           String transactionId,
                           String customerId,
                           String orderCode,
                           Date registDate,
                           Date salesOpenDate) {
        this.salesSlipId = salesSlipId;
        this.salesStatus = salesStatus;
        this.applyCoupon = applyCoupon;
        this.billingAmount = billingAmount;
        this.itemPurchasePriceSubTotalList = itemPurchasePriceSubTotalList;
        this.itemPurchasePriceTotal = itemPurchasePriceTotal;
        this.carriage = carriage;
        this.commission = commission;
        this.salesPriceConsumptionTax = salesPriceConsumptionTax;
        this.adjustmentAmountList = adjustmentAmountList;
        this.transactionId = transactionId;
        this.customerId = customerId;
        this.orderCode = orderCode;
        this.registDate = registDate;
        this.salesOpenDate = salesOpenDate;
    }

    /**
     * プロパティコピー<br/>
     * ※改訂取引用
     *
     * @param salesSlipEntity
     */
    protected void copyProperties(SalesSlipEntity salesSlipEntity) {

        this.salesSlipId = salesSlipEntity.getSalesSlipId();
        this.salesStatus = salesSlipEntity.getSalesStatus();
        this.applyCoupon = salesSlipEntity.getApplyCoupon();
        this.billingAmount = salesSlipEntity.getBillingAmount();
        this.itemPurchasePriceSubTotalList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(salesSlipEntity.getItemPurchasePriceSubTotalList())) {
            for (ItemPurchasePriceSubTotal originItemPurchasePriceSubTotal : salesSlipEntity.getItemPurchasePriceSubTotalList()) {
                this.itemPurchasePriceSubTotalList.add(originItemPurchasePriceSubTotal);
            }
        }
        this.itemPurchasePriceTotal = salesSlipEntity.getItemPurchasePriceTotal();
        this.carriage = salesSlipEntity.getCarriage();
        this.commission = salesSlipEntity.getCommission();
        this.salesPriceConsumptionTax = salesSlipEntity.getSalesPriceConsumptionTax();
        this.adjustmentAmountList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(salesSlipEntity.getAdjustmentAmountList())) {
            for (AdjustmentAmount originAdjustmentAmount : salesSlipEntity.getAdjustmentAmountList()) {
                this.adjustmentAmountList.add(originAdjustmentAmount);
            }
        }
        this.transactionId = salesSlipEntity.getTransactionId();
        this.customerId = salesSlipEntity.getCustomerId();
        this.orderCode = salesSlipEntity.getOrderCode();
        this.registDate = salesSlipEntity.getRegistDate();
        this.salesOpenDate = salesSlipEntity.getSalesOpenDate();
    }
}