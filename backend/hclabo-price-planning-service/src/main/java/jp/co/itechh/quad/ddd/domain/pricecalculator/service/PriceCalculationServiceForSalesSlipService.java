/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.pricecalculator.service;

import jp.co.itechh.quad.ddd.domain.logistic.adapter.IAddressBookAdapter;
import jp.co.itechh.quad.ddd.domain.logistic.adapter.IShippingSlipAdapter;
import jp.co.itechh.quad.ddd.domain.logistic.adapter.model.ShippingSlip;
import jp.co.itechh.quad.ddd.domain.logistic.adapter.model.ShippingSlipForRevision;
import jp.co.itechh.quad.ddd.domain.payment.adapter.IBillingAdapter;
import jp.co.itechh.quad.ddd.domain.payment.adapter.model.BillingSlip;
import jp.co.itechh.quad.ddd.domain.payment.adapter.model.BillingSlipForRevision;
import jp.co.itechh.quad.ddd.domain.pricecalculator.valueobject.BillingAmount;
import jp.co.itechh.quad.ddd.domain.pricecalculator.valueobject.Carriage;
import jp.co.itechh.quad.ddd.domain.pricecalculator.valueobject.CarriageFactory;
import jp.co.itechh.quad.ddd.domain.pricecalculator.valueobject.Commission;
import jp.co.itechh.quad.ddd.domain.pricecalculator.valueobject.CommissionFactory;
import jp.co.itechh.quad.ddd.domain.pricecalculator.valueobject.CouponPaymentPrice;
import jp.co.itechh.quad.ddd.domain.pricecalculator.valueobject.CouponPaymentPriceFactory;
import jp.co.itechh.quad.ddd.domain.pricecalculator.valueobject.Item;
import jp.co.itechh.quad.ddd.domain.pricecalculator.valueobject.ItemSalesPriceSubTotal;
import jp.co.itechh.quad.ddd.domain.pricecalculator.valueobject.ItemSalesPriceTotal;
import jp.co.itechh.quad.ddd.domain.pricecalculator.valueobject.ItemSalesPriceTotalFactory;
import jp.co.itechh.quad.ddd.domain.pricecalculator.valueobject.ItemSalesPriceTotalFactoryParam;
import jp.co.itechh.quad.ddd.domain.pricecalculator.valueobject.SalesPriceConsumptionTax;
import jp.co.itechh.quad.ddd.domain.promotion.adapter.IOrderSlipAdapter;
import jp.co.itechh.quad.ddd.domain.promotion.adapter.model.OrderSlip;
import jp.co.itechh.quad.ddd.domain.promotion.adapter.model.OrderSlipForRevision;
import jp.co.itechh.quad.ddd.domain.promotion.adapter.model.OrderSlipItem;
import jp.co.itechh.quad.ddd.domain.sales.entity.SalesSlipEntity;
import jp.co.itechh.quad.ddd.domain.sales.entity.SalesSlipForRevisionEntity;
import jp.co.itechh.quad.ddd.domain.sales.valueobject.AdjustmentAmount;
import jp.co.itechh.quad.ddd.domain.sales.valueobject.ItemPurchasePriceSubTotal;
import jp.co.itechh.quad.ddd.exception.AssertChecker;
import jp.co.itechh.quad.ddd.exception.DomainException;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 販売伝票用一括金額計算ドメインサービス
 */
@Service
public class PriceCalculationServiceForSalesSlipService {

    /** 注文票アダプター */
    private final IOrderSlipAdapter orderSlipAdapter;

    /** 配送伝票アダプター */
    private final IShippingSlipAdapter shippingSlipAdapter;

    /** 住所録アダプター */
    private final IAddressBookAdapter addressBookAdapter;

    /** 請求伝票アダプター */
    private final IBillingAdapter billingSlipAdapter;

    /** 商品販売金額合計 ファクトリ */
    private final ItemSalesPriceTotalFactory itemSalesPriceTotalFactory;

    /** 送料 ファクトリ */
    private final CarriageFactory carriageFactory;

    /** クーポン支払い額 ファクトリ */
    private final CouponPaymentPriceFactory couponPaymentPriceFactory;

    /** 手数料 ファクトリ */
    private final CommissionFactory commissionFactory;

    /** コンストラクタ */
    @Autowired
    public PriceCalculationServiceForSalesSlipService(IOrderSlipAdapter orderSlipAdapter,
                                                      IShippingSlipAdapter shippingSlipAdapter,
                                                      IAddressBookAdapter addressBookAdapter,
                                                      IBillingAdapter billingSlipAdapter,
                                                      ItemSalesPriceTotalFactory itemSalesPriceTotalFactory,
                                                      CarriageFactory carriageFactory,
                                                      CouponPaymentPriceFactory couponPaymentPriceFactory,
                                                      CommissionFactory commissionFactory) {
        this.orderSlipAdapter = orderSlipAdapter;
        this.shippingSlipAdapter = shippingSlipAdapter;
        this.addressBookAdapter = addressBookAdapter;
        this.billingSlipAdapter = billingSlipAdapter;
        this.itemSalesPriceTotalFactory = itemSalesPriceTotalFactory;
        this.carriageFactory = carriageFactory;
        this.couponPaymentPriceFactory = couponPaymentPriceFactory;
        this.commissionFactory = commissionFactory;
    }

    /**
     * 一括金額計算（フロントサイト注文用）<br/>
     * ※取引IDから各種伝票を取得し、商品金額合計、送料、クーポン支払い額、手数料、消費税を一括で取得する
     *
     * @param transactionId 取引ID
     * @param salesSlipEntity 販売伝票
     * @return 販売伝票用一括金額計算結果
     */
    public PriceCalculationForSalesSlipDto bundlePriceCalculation(String transactionId,
                                                                  SalesSlipEntity salesSlipEntity) {

        // チェック
        AssertChecker.assertNotEmpty("transactionId is empty", transactionId);

        // 戻り値Model生成
        PriceCalculationForSalesSlipDto calcDto = new PriceCalculationForSalesSlipDto();

        /* カート商品金額計算 */
        // 下書き注文票を取得する
        OrderSlip orderSlip = orderSlipAdapter.getDraftOrderSlipByTransactionId(transactionId);
        // 取得できない場合またはカートに商品がない場合は計算せずに終了
        if (orderSlip == null || CollectionUtils.isEmpty(orderSlip.getItemList())) {
            return calcDto;
        }

        // 注文票から商品購入価格リスト、商品販売金額合計を計算して戻り値Modelへ設定
        ItemSalesPriceTotal itemSalesPriceTotal = calculationOrderSlipPriceAndSetDto(orderSlip, calcDto);

        /* 送料計算 */
        // 配送伝票を取得する
        ShippingSlip shippingSlip = shippingSlipAdapter.getShippingSlip(transactionId);
        // 取得できない場合または配送方法、住所が未設定の場合は終了
        if (shippingSlip == null || StringUtils.isBlank(shippingSlip.getShippingMethodId()) || StringUtils.isBlank(
                        shippingSlip.getShippingAddressId())) {
            return calcDto;
        }

        // 配送伝票から送料を計算して戻り値Modelへ設定する
        Carriage carriage = calculationShippingSlipPriceAndSetDto(shippingSlip, itemSalesPriceTotal, calcDto);

        /* クーポン支払い額計算 */
        CouponPaymentPrice couponPaymentPrice =
                        couponPaymentPriceFactory.constructCouponPaymentPrice(salesSlipEntity.getApplyCoupon(),
                                                                              itemSalesPriceTotal, carriage, null
                                                                             );
        calcDto.setCouponPaymentPrice(couponPaymentPrice);

        /* 決済手数料計算 */
        // 請求伝票を取得する
        BillingSlip billingSlip = billingSlipAdapter.getBillingSlip(transactionId);
        // 取得できない場合
        if (billingSlip == null || StringUtils.isBlank(billingSlip.getPaymentMethodId())) {
            return calcDto;
        }

        Commission commission =
                        commissionFactory.constructCommission(billingSlip.getPaymentMethodId(), itemSalesPriceTotal,
                                                              carriage, null, couponPaymentPrice
                                                             );
        // 取得できない場合 ※金額別手数料の該当区間なしなど
        if (commission == null) {
            return calcDto;
        }
        calcDto.setCommission(commission.getValue());

        /* 消費税計算 */
        SalesPriceConsumptionTax salesPriceConsumptionTax =
                        new SalesPriceConsumptionTax(itemSalesPriceTotal, carriage, commission, null);
        calcDto.setSalesPriceConsumptionTax(salesPriceConsumptionTax);

        /* 請求金額計算 */
        BillingAmount billingAmount = new BillingAmount(couponPaymentPrice, salesPriceConsumptionTax);
        calcDto.setBillingAmount(billingAmount.getValue());

        return calcDto;
    }

    /**
     * 改訂用一括金額計算<br/>
     * ※改訂用取引IDから各改訂伝票を取得し、商品金額合計、送料、クーポン支払い額、手数料、消費税、請求金額を一括で取得する
     *
     * @param transactionRevisionId      改訂用取引ID
     * @param salesSlipForRevisionEntity 改訂用販売伝票
     * @param salesSlipEntity            改訂元販売伝票
     * @return 販売伝票用一括金額計算結果
     */
    public PriceCalculationForSalesSlipDto bundlePriceCalculationForRevision(String transactionRevisionId,
                                                                             SalesSlipForRevisionEntity salesSlipForRevisionEntity,
                                                                             SalesSlipEntity salesSlipEntity) {

        // チェック
        AssertChecker.assertNotEmpty("transactionRevisionId is empty", transactionRevisionId);

        // 戻り値Model生成
        PriceCalculationForSalesSlipDto calcDto = new PriceCalculationForSalesSlipDto();

        /* カート商品金額計算 */
        // 改訂用注文票を取得する
        OrderSlipForRevision orderSlipForRevision =
                        orderSlipAdapter.getOrderSlipByTransactionRevisionId(transactionRevisionId);
        // 取得できない場合またはカートに商品がない場合は計算せずに終了
        if (orderSlipForRevision == null || CollectionUtils.isEmpty(orderSlipForRevision.getItemList())) {
            return calcDto;
        }

        // 改訂用注文票から商品販売金額合計を計算して戻り値Modelへ設定
        ItemSalesPriceTotal itemSalesPriceTotal =
                        calculationOrderSlipPriceForRevisionAndSetDto(orderSlipForRevision, salesSlipForRevisionEntity,
                                                                      salesSlipEntity, calcDto
                                                                     );

        /* 送料計算 */
        Carriage carriage;
        if (salesSlipForRevisionEntity.isOriginCarriageApplyFlag()) {

            // 改訂前送料適用フラグが立っている場合
            carriage = new Carriage(salesSlipEntity.getCarriage());
            calcDto.setCarriage(carriage.getValue());

        } else {

            // 改訂用配送伝票を取得する
            ShippingSlipForRevision shippingSlipForRevision =
                            shippingSlipAdapter.getShippingSlipForRevision(transactionRevisionId);
            // 取得できない場合または配送方法、住所が未設定の場合は終了
            if (shippingSlipForRevision == null || StringUtils.isBlank(shippingSlipForRevision.getShippingMethodId())
                || StringUtils.isBlank(shippingSlipForRevision.getShippingAddressId())) {
                return calcDto;
            }

            // 改訂用配送伝票から送料を計算して戻り値Modelへ設定する
            carriage = calculationShippingSlipPriceAndSetDto(shippingSlipForRevision, itemSalesPriceTotal, calcDto);
        }

        /* クーポン支払い額計算 */
        List<AdjustmentAmount> adjustmentAmountList = salesSlipForRevisionEntity.getAdjustmentAmountList();
        CouponPaymentPrice couponPaymentPrice = couponPaymentPriceFactory.constructCouponPaymentPrice(
                        salesSlipForRevisionEntity.getApplyCoupon(), itemSalesPriceTotal, carriage,
                        adjustmentAmountList
                                                                                                     );
        calcDto.setCouponPaymentPrice(couponPaymentPrice);

        /* 決済手数料計算 */
        Commission commission;
        if (salesSlipForRevisionEntity.isOriginCommissionApplyFlag()) {

            // 改訂前手数料適用フラグが立っている場合
            commission = new Commission(salesSlipEntity.getCommission());

        } else {

            // 改訂用請求伝票を取得する
            BillingSlipForRevision billingSlipForRevision =
                            billingSlipAdapter.getBillingSlipForRevision(transactionRevisionId);

            // 取得できない場合
            if (billingSlipForRevision == null || StringUtils.isBlank(billingSlipForRevision.getPaymentMethodId())) {
                return calcDto;
            }

            commission = commissionFactory.constructCommission(billingSlipForRevision.getPaymentMethodId(),
                                                               itemSalesPriceTotal, carriage, adjustmentAmountList,
                                                               couponPaymentPrice
                                                              );
            // 取得できない場合 ※金額別手数料の該当区間なしなど
            if (commission == null) {
                return calcDto;
            }
        }
        calcDto.setCommission(commission.getValue());

        /* 消費税計算 */
        SalesPriceConsumptionTax salesPriceConsumptionTax =
                        new SalesPriceConsumptionTax(itemSalesPriceTotal, carriage, commission, adjustmentAmountList);
        calcDto.setSalesPriceConsumptionTax(salesPriceConsumptionTax);

        /* 請求金額計算 */
        BillingAmount billingAmount = new BillingAmount(couponPaymentPrice, salesPriceConsumptionTax);
        calcDto.setBillingAmount(billingAmount.getValue());

        return calcDto;
    }

    /**
     * 注文票の金額計算
     * ※注文票から商品購入価格リスト、商品販売金額合計を計算して戻り値Modelへ設定する
     *
     * @param orderSlip 注文票
     * @param calcDto 販売伝票用一括金額計算結果
     * @return 商品販売金額合計
     */
    private ItemSalesPriceTotal calculationOrderSlipPriceAndSetDto(OrderSlip orderSlip,
                                                                   PriceCalculationForSalesSlipDto calcDto) {

        // 商品販売金額合計 値オブジェクトを生成 パラメータ作成
        List<ItemSalesPriceTotalFactoryParam> paramList = new ArrayList<>();

        for (OrderSlipItem orderSlipItem : orderSlip.getItemList()) {
            ItemSalesPriceTotalFactoryParam itemSalesPriceTotalFactoryParam = new ItemSalesPriceTotalFactoryParam();
            itemSalesPriceTotalFactoryParam.setOrderItemId(orderSlipItem.getItemId());
            itemSalesPriceTotalFactoryParam.setOrderItemCount(orderSlipItem.getItemCount());
            itemSalesPriceTotalFactoryParam.setOrderItemSeq(orderSlipItem.getOrderItemSeq());
            paramList.add(itemSalesPriceTotalFactoryParam);
        }

        // 商品販売金額合計 値オブジェクトを生成
        ItemSalesPriceTotal itemSalesPriceTotal = itemSalesPriceTotalFactory.constructItemSalesPriceTotal(paramList);

        // 商品購入価格リストを計算
        List<ItemPurchasePriceSubTotal> purchasedItemList = new ArrayList<>();
        for (ItemSalesPriceSubTotal itemSalesPriceSubTotal : itemSalesPriceTotal.getItemSalesPriceSubTotalList()) {
            ItemPurchasePriceSubTotal itemPurchasePriceSubTotal =
                            new ItemPurchasePriceSubTotal(itemSalesPriceSubTotal.getOrderItemSeq(),
                                                          itemSalesPriceSubTotal.getItemSalesPriceSubTotal(),
                                                          itemSalesPriceSubTotal.getOrderItem().getItemId(),
                                                          itemSalesPriceSubTotal.getOrderItem().getItemUnitPrice(),
                                                          itemSalesPriceSubTotal.getOrderItemCount(),
                                                          itemSalesPriceSubTotal.getOrderItem().getTaxRate(),
                                                          itemSalesPriceSubTotal.getOrderItem().isFreeCarriageItemFlag()
                            );
            purchasedItemList.add(itemPurchasePriceSubTotal);
        }

        // 商品購入価格リスト、商品販売金額合計をcalculationBillingAmountDtoに設定
        calcDto.setItemPurchasePriceSubTotalList(purchasedItemList);
        calcDto.setItemPurchasePriceTotal(itemSalesPriceTotal.getItemPriceTotal());

        return itemSalesPriceTotal;
    }

    /**
     * 配送伝票の金額計算
     * ※配送伝票から送料を計算して、戻り値Modelへ設定する
     *
     * @param shippingSlip 配送伝票
     * @param itemSalesPriceTotal 商品販売金額合計
     * @param calcDto 販売伝票用一括金額計算結果
     * @return 送料
     */
    private Carriage calculationShippingSlipPriceAndSetDto(ShippingSlip shippingSlip,
                                                           ItemSalesPriceTotal itemSalesPriceTotal,
                                                           PriceCalculationForSalesSlipDto calcDto) {

        // zipcodeを取得する
        String zipcode = addressBookAdapter.getZipcode(shippingSlip.getShippingAddressId());

        if (zipcode == null) {
            throw new DomainException("PRICE-PLANNING-ADDB0001-E", new String[] {shippingSlip.getShippingAddressId()});
        }

        // 送料 値オブジェクトを生成
        Carriage carriage = carriageFactory.constructCarriage(shippingSlip.getShippingMethodId(), zipcode,
                                                              itemSalesPriceTotal
                                                             );

        // 送料をcalculationBillingAmountDtoに設定
        calcDto.setCarriage(carriage.getValue());

        return carriage;
    }

    /**
     * 改訂用注文票の金額計算
     * ※改訂用注文票から商品購入価格リスト、商品販売金額合計を計算して戻り値Modelへ設定する
     *
     * @param orderSlipForRevision 改訂用注文票
     * @param salesSlipForRevisionEntity 改訂用販売伝票
     * @param salesSlipEntity 改訂元販売伝票
     * @param calcDto 販売伝票用一括金額計算結果
     * @return 商品販売金額合計
     */
    private ItemSalesPriceTotal calculationOrderSlipPriceForRevisionAndSetDto(OrderSlipForRevision orderSlipForRevision,
                                                                              SalesSlipForRevisionEntity salesSlipForRevisionEntity,
                                                                              SalesSlipEntity salesSlipEntity,
                                                                              PriceCalculationForSalesSlipDto calcDto) {

        // 商品販売金額合計 値オブジェクトを生成用パラメータ定義
        List<ItemSalesPriceTotalFactoryParam> paramList = new ArrayList<>();

        // 改訂用注文票ループして、パラメータ作成
        for (OrderSlipItem orderSlipRevisionItem : orderSlipForRevision.getOrderItemRevisionList()) {

            ItemSalesPriceTotalFactoryParam itemSalesPriceTotalFactoryParam = new ItemSalesPriceTotalFactoryParam();
            itemSalesPriceTotalFactoryParam.setOrderItemId(orderSlipRevisionItem.getItemId());
            itemSalesPriceTotalFactoryParam.setOrderItemCount(orderSlipRevisionItem.getItemCount());
            itemSalesPriceTotalFactoryParam.setOrderItemSeq(orderSlipRevisionItem.getOrderItemSeq());

            // 改訂前から存在する商品の場合、既存の商品購入価格小計を取得し、既存商品情報を設定
            ItemPurchasePriceSubTotal existPurchasePriceSubTotal = salesSlipEntity.getItemPurchasePriceSubTotalList()
                                                                                  .stream()
                                                                                  .filter(itemPurchasePriceSubTotal ->
                                                                                                          itemPurchasePriceSubTotal.getSalesItemSeq()
                                                                                                          == orderSlipRevisionItem.getOrderItemSeq())
                                                                                  .findFirst()
                                                                                  .orElse(null);
            if (existPurchasePriceSubTotal != null) {
                Item item = new Item(existPurchasePriceSubTotal.getItemId(),
                                     existPurchasePriceSubTotal.getItemUnitPrice(),
                                     existPurchasePriceSubTotal.getItemTaxRate(),
                                     existPurchasePriceSubTotal.isFreeCarriageItemFlag()
                );
                itemSalesPriceTotalFactoryParam.setApplyExistItem(item);
            }

            paramList.add(itemSalesPriceTotalFactoryParam);
        }

        // 商品販売金額合計 値オブジェクトを生成
        ItemSalesPriceTotal itemSalesPriceTotal = itemSalesPriceTotalFactory.constructItemSalesPriceTotal(paramList);

        // 商品購入価格リストを計算
        List<ItemPurchasePriceSubTotal> purchasedItemList = new ArrayList<>();
        for (ItemSalesPriceSubTotal itemSalesPriceSubTotal : itemSalesPriceTotal.getItemSalesPriceSubTotalList()) {
            ItemPurchasePriceSubTotal itemPurchasePriceSubTotal =
                            new ItemPurchasePriceSubTotal(itemSalesPriceSubTotal.getOrderItemSeq(),
                                                          itemSalesPriceSubTotal.getItemSalesPriceSubTotal(),
                                                          itemSalesPriceSubTotal.getOrderItem().getItemId(),
                                                          itemSalesPriceSubTotal.getOrderItem().getItemUnitPrice(),
                                                          itemSalesPriceSubTotal.getOrderItemCount(),
                                                          itemSalesPriceSubTotal.getOrderItem().getTaxRate(),
                                                          itemSalesPriceSubTotal.getOrderItem().isFreeCarriageItemFlag()
                            );
            purchasedItemList.add(itemPurchasePriceSubTotal);
        }

        // 商品購入価格リスト、商品販売金額合計をcalculationBillingAmountDtoに設定
        calcDto.setItemPurchasePriceSubTotalList(purchasedItemList);
        calcDto.setItemPurchasePriceTotal(itemSalesPriceTotal.getItemPriceTotal());

        return itemSalesPriceTotal;
    }

}
