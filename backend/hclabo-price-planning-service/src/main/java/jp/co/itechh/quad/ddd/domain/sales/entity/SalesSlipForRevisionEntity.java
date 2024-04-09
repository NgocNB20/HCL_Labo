/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.sales.entity;

import jp.co.itechh.quad.ddd.domain.pricecalculator.valueobject.CouponPaymentPrice;
import jp.co.itechh.quad.ddd.domain.pricecalculator.valueobject.SalesPriceConsumptionTax;
import jp.co.itechh.quad.ddd.domain.sales.valueobject.AdjustmentAmount;
import jp.co.itechh.quad.ddd.domain.sales.valueobject.AdjustmentAmountSeqFactory;
import jp.co.itechh.quad.ddd.domain.sales.valueobject.ApplyCoupon;
import jp.co.itechh.quad.ddd.domain.sales.valueobject.ItemPurchasePriceSubTotal;
import jp.co.itechh.quad.ddd.domain.sales.valueobject.SalesSlipId;
import jp.co.itechh.quad.ddd.domain.sales.valueobject.SalesSlipRevisionId;
import jp.co.itechh.quad.ddd.domain.sales.valueobject.SalesStatus;
import jp.co.itechh.quad.ddd.exception.AssertChecker;
import jp.co.itechh.quad.ddd.exception.DomainException;
import lombok.Getter;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 改訂用販売伝票エンティティ
 */
@Getter
public class SalesSlipForRevisionEntity extends SalesSlipEntity {

    /** 改訂用販売伝票ID */
    private SalesSlipRevisionId salesSlipRevisionId;

    /** 改訂用取取引ID */
    private String transactionRevisionId;

    /** 改訂前手数料適用フラグ */
    private boolean originCommissionApplyFlag;

    /** 改訂前送料適用フラグ */
    private boolean originCarriageApplyFlag;

    /**
     * コンストラクタ<br/>
     * 改訂用販売伝票発行
     *
     * @param originSalesSlipEntity
     * @param transactionRevisionId
     * @param registDate
     */
    public SalesSlipForRevisionEntity(SalesSlipEntity originSalesSlipEntity,
                                      String transactionRevisionId,
                                      Date registDate) {

        super();

        // アサートチェック
        AssertChecker.assertNotNull("originSalesSlipEntity is null", originSalesSlipEntity);
        AssertChecker.assertNotEmpty("transactionRevisionId is empty", transactionRevisionId);
        AssertChecker.assertNotNull("registDate is null", registDate);

        /* 設定 */
        // 元取引をコピー
        super.copyProperties(originSalesSlipEntity);
        // 改訂販売伝票用設定
        this.salesSlipRevisionId = new SalesSlipRevisionId();
        this.transactionRevisionId = transactionRevisionId;
        this.registDate = registDate;
    }

    /**
     * クーポン利用フラグ設定
     *
     * @param couponUseFlag
     */
    public void settingCouponUseFlag(boolean couponUseFlag) {

        // クーポン未適用で、クーポン利用をする場合はエラー
        if (StringUtils.isBlank(this.applyCoupon.getCouponCode()) && couponUseFlag) {
            throw new DomainException("PRICE-PLANNING-SAEV0001-E");
        }

        ApplyCoupon newApplyCoupon = new ApplyCoupon(this.applyCoupon.getCouponCode(), this.applyCoupon.getCouponSeq(),
                                                     this.applyCoupon.getCouponVersionNo(),
                                                     this.applyCoupon.getCouponName(), new CouponPaymentPrice(
                        this.applyCoupon.getCouponPaymentPrice().getValue()), couponUseFlag
        );
        super.settingCoupon(newApplyCoupon);
    }

    /**
     * 調整金額追加
     *
     * @param adjustName 調整項目名
     * @param adjustPrice 調整金額
     */
    public void addAdjustmentAmount(String adjustName, int adjustPrice) {

        // アサートチェック
        AssertChecker.assertNotEmpty("adjustName is empty", adjustName);

        // 調整金額リストがNullの場合、インスタンスを生成
        if (this.adjustmentAmountList == null) {
            this.adjustmentAmountList = new ArrayList<>();
        }

        // 調整金額をリストに追加
        AdjustmentAmount adjustmentAmount = new AdjustmentAmount(
                        AdjustmentAmountSeqFactory.constructAdjustmentAmountSeq(this.adjustmentAmountList), adjustName,
                        adjustPrice
        );
        this.adjustmentAmountList.add(adjustmentAmount);

    }

    /**
     * 伝票取消
     */
    public void cancelSlip() {

        // チェック
        // 確定状態でないならエラー
        if (this.salesStatus != SalesStatus.OPEN) {
            throw new DomainException("PRICE-PLANNING-SALS0001-E");
        }

        // 販売ステータス取消
        this.salesStatus = SalesStatus.CANCEL;

        /** キャンセル処理 */
        super.settingCoupon(new ApplyCoupon(this.applyCoupon.getCouponCode(), this.applyCoupon.getCouponSeq(),
                                            this.applyCoupon.getCouponVersionNo(), this.applyCoupon.getCouponName(),
                                            new CouponPaymentPrice(0), this.applyCoupon.isCouponUseFlag()
        ));
        // 請求金額
        this.billingAmount = 0;
        // 商品購入価格小計リスト
        List<ItemPurchasePriceSubTotal> newItemPurchasePriceSubTotalList = new ArrayList<>();
        for (ItemPurchasePriceSubTotal curItemPurchasePriceSubTotal : this.itemPurchasePriceSubTotalList) {
            ItemPurchasePriceSubTotal newItemPurchasePriceSubTotal =
                            new ItemPurchasePriceSubTotal(curItemPurchasePriceSubTotal.getSalesItemSeq(), 0,
                                                          curItemPurchasePriceSubTotal.getItemId(), 0, 0,
                                                          curItemPurchasePriceSubTotal.getItemTaxRate(),
                                                          curItemPurchasePriceSubTotal.isFreeCarriageItemFlag()
                            );
            newItemPurchasePriceSubTotalList.add(newItemPurchasePriceSubTotal);
        }
        this.itemPurchasePriceSubTotalList = newItemPurchasePriceSubTotalList;
        // 商品購入価格合計
        this.carriage = 0;
        // 手数料
        this.commission = 0;
        // 商品購入価格合計
        this.itemPurchasePriceTotal = 0;
        // 販売金額消費税
        this.salesPriceConsumptionTax = new SalesPriceConsumptionTax(0, 0, 0, 0);
        // 調整金額リスト
        if (!CollectionUtils.isEmpty(this.adjustmentAmountList)) {
            List<AdjustmentAmount> newAdjustmentAmountList = new ArrayList<>();
            for (AdjustmentAmount curAdjustmentAmount : this.adjustmentAmountList) {
                AdjustmentAmount newAdjustmentAmount =
                                new AdjustmentAmount(curAdjustmentAmount.getAdjustmentAmountSeq(),
                                                     curAdjustmentAmount.getAdjustName(), 0
                                );
                newAdjustmentAmountList.add(newAdjustmentAmount);
            }
            this.adjustmentAmountList = newAdjustmentAmountList;
        }
    }

    /**
     * 改訂前手数料適用フラグsetter
     * ※パッケージプライベート(ドメインサービスを利用)
     *
     * @param originCommissionApplyFlag
     */
    void setOriginCommissionApplyFlag(boolean originCommissionApplyFlag) {
        this.originCommissionApplyFlag = originCommissionApplyFlag;
    }

    /**
     * 改訂前送料適用フラグsetter
     * ※パッケージプライベート(ドメインサービスを利用)
     *
     * @param originCarriageApplyFlag
     */
    void setOriginCarriageApplyFlag(boolean originCarriageApplyFlag) {

        this.originCarriageApplyFlag = originCarriageApplyFlag;
    }

    /**
     * DB取得値設定用コンストラクタ
     * ※ユースケース層での使用禁止
     */
    public SalesSlipForRevisionEntity(SalesSlipId salesSlipId,
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
                                      Date salesOpenDate,
                                      SalesSlipRevisionId salesSlipRevisionId,
                                      String transactionRevisionId,
                                      boolean originCommissionApplyFlag,
                                      boolean originCarriageApplyFlag) {
        super(salesSlipId, salesStatus, applyCoupon, billingAmount, itemPurchasePriceSubTotalList,
              itemPurchasePriceTotal, carriage, commission, salesPriceConsumptionTax, adjustmentAmountList,
              transactionId, customerId, orderCode, registDate, salesOpenDate
             );
        this.salesSlipRevisionId = salesSlipRevisionId;
        this.transactionRevisionId = transactionRevisionId;
        this.originCommissionApplyFlag = originCommissionApplyFlag;
        this.originCarriageApplyFlag = originCarriageApplyFlag;
    }
}