/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.order.details;

import jp.co.itechh.quad.admin.base.util.common.DiffUtil;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.ConversionUtility;
import jp.co.itechh.quad.admin.base.utility.DateUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeBillStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeBillType;
import jp.co.itechh.quad.admin.constant.type.HTypeInvoiceAttachmentFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeMailRequired;
import jp.co.itechh.quad.admin.constant.type.HTypeOrderAgeType;
import jp.co.itechh.quad.admin.constant.type.HTypeOrderSex;
import jp.co.itechh.quad.admin.constant.type.HTypeSend;
import jp.co.itechh.quad.admin.entity.multipayment.MulPayBillEntity;
import jp.co.itechh.quad.admin.entity.order.OrderSummaryEntity;
import jp.co.itechh.quad.admin.entity.order.additional.OrderAdditionalChargeEntity;
import jp.co.itechh.quad.admin.entity.order.bill.OrderBillEntity;
import jp.co.itechh.quad.admin.entity.order.delivery.OrderDeliveryEntity;
import jp.co.itechh.quad.admin.entity.order.goods.OrderGoodsEntity;
import jp.co.itechh.quad.admin.entity.order.index.OrderIndexEntity;
import jp.co.itechh.quad.admin.entity.order.memo.OrderMemoEntity;
import jp.co.itechh.quad.admin.entity.order.orderperson.OrderPersonEntity;
import jp.co.itechh.quad.admin.entity.order.settlement.OrderSettlementEntity;
import jp.co.itechh.quad.admin.entity.shop.delivery.DeliveryMethodEntity;
import jp.co.itechh.quad.admin.entity.shop.settlement.SettlementMethodEntity;
import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderSlipResponseItemList;
import jp.co.itechh.quad.salesslip.presentation.api.param.AdjustmentAmountResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static jp.co.itechh.quad.admin.pc.web.admin.order.details.AbstractOrderDetailsModel.CANCEL;

/**
 * 処理履歴詳細Helper
 *
 * @author kimura
 */
@Component
public class OrderHistoryDetailsHelper extends AbstractOrderDetailsHelper {

    /** 変換ユーティリティクラス */
    private final ConversionUtility conversionUtility;

    /** コンストラクタ */
    public OrderHistoryDetailsHelper(DateUtility dateUtility,
                                     ConversionUtility conversionUtility,
                                     ConversionUtility conversionUtility1) {
        super(dateUtility, conversionUtility);
        this.conversionUtility = conversionUtility1;
    }

    /************************************
     ** Diffスタイル用メソッド
     ************************************/

    /**
     * 項目変更チェック
     *
     * @param diffList          相違情報リスト
     * @param dataPath          データ項目名（パス形式）
     * @param styleValue        相違点が見つかった時のスタイル設定値
     * @param settingStyleValue HTMLに設定されたスタイル設定値
     * @return スタイル設定値
     */
    protected String checkDiff(List<String> diffList, String dataPath, String styleValue, String settingStyleValue) {
        if (diffList.contains(dataPath)) {
            if (settingStyleValue == null || settingStyleValue.isEmpty()) {
                return styleValue;
            } else if (styleValue == null || styleValue.isEmpty()) {
                return settingStyleValue;
            } else {
                return settingStyleValue + String.valueOf(styleValue.charAt(0)).toUpperCase() + styleValue.substring(1);
            }
        }
        return settingStyleValue;
    }

    /**
     * スタイルクラスを返す
     *
     * @param settingStyleValue HTML設定スタイルクラス
     * @param styleValue        変更スタイルクラス
     * @return styleClass
     */
    public String getClassValue(String settingStyleValue, String styleValue) {
        if (settingStyleValue == null || settingStyleValue.isEmpty()) {
            return styleValue;
        } else if (styleValue == null || styleValue.isEmpty()) {
            return settingStyleValue;
        } else {
            return settingStyleValue + String.valueOf(styleValue.charAt(0)).toUpperCase() + styleValue.substring(1);
        }
    }

    /**
     * 変更箇所の表示スタイル設定処理<br/>
     *
     * @param orderHistorydetailsModel Model
     */
    public void setDiff(OrderHistoryDetailsModel orderHistorydetailsModel) {

        OrderDetailsCommonModel modified = orderHistorydetailsModel.getModifiedOrderDetailsCommonModel();
        OrderDetailsCommonModel original = orderHistorydetailsModel.getOriginalOrderDetailsCommonModel();

        /** 受注サマリー */
        setOrderSummaryDiff(orderHistorydetailsModel, modified, original);

        /** 受注お客様 */
        setOrderPersonDiff(orderHistorydetailsModel, modified, original);

        /** 受注配送/受注商品 */
        setOrderDeliveryDiff(orderHistorydetailsModel, modified, original);

        /** 受注決済 */
        setOrderSettlementDiff(orderHistorydetailsModel, modified, original);

        /** その他料金 */
        setAddtionalChargeDiff(orderHistorydetailsModel, modified.getSalesSlipResponse().getAdjustmentAmountList(),
                               original.getSalesSlipResponse().getAdjustmentAmountList()
                              );

        /** 受注請求 */
        setOrderBillDiff(orderHistorydetailsModel, modified, original);

        /** 受注メモ */
        setOrderMemoDiff(orderHistorydetailsModel, modified, original);

        /** 受注インデックス */
        setOrderIndexDiff(orderHistorydetailsModel, modified, original);

        /** 受注決済詳細 */
        setOrderSettlementDetailDiff(orderHistorydetailsModel, modified, original);

        /** マルペイ請求 */
        setMulPayBillDiff(orderHistorydetailsModel, modified, original);

        /** 請求先 ※フェーズ２の新規項目 */
        setBillingAddressDiff(orderHistorydetailsModel, modified, original);
    }

    /**
     * 受注サマリの変更箇所の差分セット<br/>
     *
     * @param orderHistorydetailsModel Model
     * @param modified                 修正後のOrderDetailsCommonModel
     * @param original                 修正前のOrderDetailsCommonModel
     */
    protected void setOrderSummaryDiff(OrderHistoryDetailsModel orderHistorydetailsModel,
                                       OrderDetailsCommonModel modified,
                                       OrderDetailsCommonModel original) {

        orderHistorydetailsModel.setModifiedOrderSummaryList(
                        DiffUtil.diff(toOrderSummaryEntity(original), toOrderSummaryEntity(modified)));
    }

    /**
     * 受注お客様の変更箇所の差分セット<br/>
     *
     * @param orderHistorydetailsModel Model
     * @param modified                 修正後のOrderDetailsCommonModel
     * @param original                 修正前のOrderDetailsCommonModel
     */
    protected void setOrderPersonDiff(OrderHistoryDetailsModel orderHistorydetailsModel,
                                      OrderDetailsCommonModel modified,
                                      OrderDetailsCommonModel original) {

        orderHistorydetailsModel.setModifiedOrderPersonList(
                        DiffUtil.diff(toOrderPersonEntity(original), toOrderPersonEntity(modified)));
    }

    /**
     * 受注配送の変更箇所の差分セット<br/>
     *
     * @param orderHistorydetailsModel Model
     * @param modified                 修正後のOrderDetailsCommonModel
     * @param original                 修正前のOrderDetailsCommonModel
     */
    protected void setOrderDeliveryDiff(OrderHistoryDetailsModel orderHistorydetailsModel,
                                        OrderDetailsCommonModel modified,
                                        OrderDetailsCommonModel original) {

        OrderReceiverUpdateItem orderReceiverUpdateItem = orderHistorydetailsModel.getOrderReceiverItem();

        orderHistorydetailsModel.setModifiedOrderDeliveryList(
                        DiffUtil.diff(toOrderDeliveryEntity(original), toOrderDeliveryEntity(modified)));
        orderHistorydetailsModel.setModifiedDeliveryMethod(
                        DiffUtil.diff(toDeliveryMethodEntity(original), toDeliveryMethodEntity(modified)));

        // 受注商品の変更箇所の表示スタイル設定
        setGoodsDiff(orderHistorydetailsModel, orderReceiverUpdateItem, modified, original);
    }

    /**
     * 受注商品の変更箇所の差分セット<br/>
     *
     * @param orderHistorydetailsModel Model
     * @param orderReceiverUpdateItem  お届け先表示情報Item
     * @param modified                 修正後のOrderDetailsCommonModel
     * @param original                 修正前のOrderDetailsCommonModel
     */
    protected void setGoodsDiff(OrderHistoryDetailsModel orderHistorydetailsModel,
                                OrderReceiverUpdateItem orderReceiverUpdateItem,
                                OrderDetailsCommonModel modified,
                                OrderDetailsCommonModel original) {

        // 初期化
        orderHistorydetailsModel.setModifiedOrderGoodsList(new ArrayList<>());

        // 対象Entity名
        String ordergoods = orderHistorydetailsModel.diffObjectNameOrderGoods + DiffUtil.SEPARATOR;

        List<OrderGoodsUpdateItem> orderGoodsUpdateItems = orderReceiverUpdateItem.getOrderGoodsUpdateItems();
        Iterator<OrderGoodsUpdateItem> orderGoodsUpdateIte = orderGoodsUpdateItems.iterator();

        List<OrderSlipResponseItemList> modifiedItemList = modified.getOrderSlipResponse().getItemList();
        Iterator<OrderSlipResponseItemList> modifiedItemIte = modifiedItemList.iterator();
        List<OrderSlipResponseItemList> originalItemList = original.getOrderSlipResponse().getItemList();
        Iterator<OrderSlipResponseItemList> originalItemIte = originalItemList.iterator();
        while (orderGoodsUpdateIte.hasNext()) {
            orderGoodsUpdateIte.next();
            if (modifiedItemIte.hasNext()) {
                OrderGoodsEntity modifiedGoods = toOrderGoodsEntity(modifiedItemIte.next());
                if (originalItemIte.hasNext()) {
                    OrderGoodsEntity originalGoods = toOrderGoodsEntity(originalItemIte.next());
                    List<String> goodsDiffList = DiffUtil.diff(originalGoods, modifiedGoods);
                    orderHistorydetailsModel.getModifiedOrderGoodsList().add(goodsDiffList);
                } else {
                    // 商品を追加した場合
                    List<String> goodsDiffList = new ArrayList<>();
                    goodsDiffList.add(ordergoods + "goodsCount");
                    orderHistorydetailsModel.getModifiedOrderGoodsList().add(goodsDiffList);
                }
            }
        }
    }

    /**
     * 受注決済の変更箇所の差分セット<br/>
     *
     * @param orderHistorydetailsModel Model
     * @param modified                 修正後のOrderDetailsCommonModel
     * @param original                 修正前のOrderDetailsCommonModel
     */
    protected void setOrderSettlementDiff(OrderHistoryDetailsModel orderHistorydetailsModel,
                                          OrderDetailsCommonModel modified,
                                          OrderDetailsCommonModel original) {

        orderHistorydetailsModel.setModifiedOrderSettlementList(
                        DiffUtil.diff(toOrderSettlementEntity(original), toOrderSettlementEntity(modified)));
    }

    /**
     * その他追加料金の変更箇所の差分セット<br/>
     *
     * @param orderHistorydetailsModel Model
     * @param modified                 修正後のOrderDetailsCommonModel
     * @param original                 修正前のOrderDetailsCommonModel
     */
    protected void setAddtionalChargeDiff(OrderHistoryDetailsModel orderHistorydetailsModel,
                                          List<AdjustmentAmountResponse> modified,
                                          List<AdjustmentAmountResponse> original) {

        if (CollectionUtils.isEmpty(modified)) {
            modified = new ArrayList<>();
        }
        if (CollectionUtils.isEmpty(original)) {
            original = new ArrayList<>();
        }

        List<OrderAdditionalChargeItem> orderAdditionalChargeItems =
                        orderHistorydetailsModel.getOrderAdditionalChargeItems();
        Iterator<OrderAdditionalChargeItem> orderAdditionalChargeIte = orderAdditionalChargeItems.iterator();
        Iterator<AdjustmentAmountResponse> modifiedAddChargeIte = modified.iterator();
        Iterator<AdjustmentAmountResponse> originalAddChargeIte = original.iterator();

        orderHistorydetailsModel.setModifiedAdditionalChargeList(new ArrayList<>());

        while (orderAdditionalChargeIte.hasNext()) {
            orderAdditionalChargeIte.next();
            if (modifiedAddChargeIte.hasNext()) {
                AdjustmentAmountResponse modifiedAddCharge = modifiedAddChargeIte.next();
                if (originalAddChargeIte.hasNext()) {
                    AdjustmentAmountResponse originalAddCharge = originalAddChargeIte.next();
                    orderHistorydetailsModel.getModifiedAdditionalChargeList()
                                            .add(DiffUtil.diff(toOrderAdditionalChargeEntity(originalAddCharge),
                                                               toOrderAdditionalChargeEntity(modifiedAddCharge)
                                                              ));
                } else {
                    List<String> diff = new ArrayList<>();
                    diff.add("OrderAdditionalChargeEntity.additionalDetailsName");
                    diff.add("OrderAdditionalChargeEntity.additionalDetailsPrice");
                    orderHistorydetailsModel.getModifiedAdditionalChargeList().add(diff);
                }
            }
        }
    }

    /**
     * 受注請求の変更箇所の差分セット<br/>
     *
     * @param orderHistorydetailsModel Model
     * @param modified                 修正後のOrderDetailsCommonModel
     * @param original                 修正前のOrderDetailsCommonModel
     */
    protected void setOrderBillDiff(OrderHistoryDetailsModel orderHistorydetailsModel,
                                    OrderDetailsCommonModel modified,
                                    OrderDetailsCommonModel original) {

        orderHistorydetailsModel.setModifiedOrderBillList(
                        DiffUtil.diff(toOrderBillEntity(original), toOrderBillEntity(modified)));
    }

    /**
     * 受注メモの変更箇所の差分セット<br/>
     *
     * @param orderHistorydetailsModel Model
     * @param modified                 修正後のOrderDetailsCommonModel
     * @param original                 修正前のOrderDetailsCommonModel
     */
    protected void setOrderMemoDiff(OrderHistoryDetailsModel orderHistorydetailsModel,
                                    OrderDetailsCommonModel modified,
                                    OrderDetailsCommonModel original) {

        orderHistorydetailsModel.setModifiedMemoList(
                        DiffUtil.diff(toOrderMemoEntity(original), toOrderMemoEntity(modified)));
    }

    /**
     * 受注インデックスの変更箇所の差分セット<br/>
     *
     * @param orderHistorydetailsModel Model
     * @param modified                 修正後のOrderDetailsCommonModel
     * @param original                 修正前のOrderDetailsCommonModel
     */
    protected void setOrderIndexDiff(OrderHistoryDetailsModel orderHistorydetailsModel,
                                     OrderDetailsCommonModel modified,
                                     OrderDetailsCommonModel original) {

        orderHistorydetailsModel.setModifiedOrderIndexList(
                        DiffUtil.diff(toOrderIndexEntity(original), toOrderIndexEntity(modified)));
    }

    /**
     * 受注決済詳細の変更箇所の差分セット<br/>
     *
     * @param orderHistorydetailsModel Model
     * @param modified                 修正後のOrderDetailsCommonModel
     * @param original                 修正前のOrderDetailsCommonModel
     */
    protected void setOrderSettlementDetailDiff(OrderHistoryDetailsModel orderHistorydetailsModel,
                                                OrderDetailsCommonModel modified,
                                                OrderDetailsCommonModel original) {

        orderHistorydetailsModel.setModifiedSettlementMethodList(
                        DiffUtil.diff(toSettlementMethodEntity(original), toSettlementMethodEntity(modified)));
    }

    /**
     * お支払方法詳細（マルペイ請求）の変更箇所の差分セット<br/>
     *
     * @param orderHistorydetailsModel Model
     * @param modified                 修正後のOrderDetailsCommonModel
     * @param original                 修正前のOrderDetailsCommonModel
     */
    protected void setMulPayBillDiff(OrderHistoryDetailsModel orderHistorydetailsModel,
                                     OrderDetailsCommonModel modified,
                                     OrderDetailsCommonModel original) {

        orderHistorydetailsModel.setModifiedMulPayBillList(
                        DiffUtil.diff(toMulPayBillEntity(original), toMulPayBillEntity(modified)));
    }

    /**
     * 請求先の変更箇所の差分セット<br/>
     *
     * @param orderHistorydetailsModel Model
     * @param modified                 修正後のOrderDetailsCommonModel
     * @param original                 修正前のOrderDetailsCommonModel
     */
    protected void setBillingAddressDiff(OrderHistoryDetailsModel orderHistorydetailsModel,
                                         OrderDetailsCommonModel modified,
                                         OrderDetailsCommonModel original) {

        orderHistorydetailsModel.setModifiedBillingAddressList(
                        DiffUtil.diff(toOrderDeliveryEntityForBillingAddress(original),
                                      toOrderDeliveryEntityForBillingAddress(modified)
                                     ));
    }

    /**
     * 差分項目判定用にフェーズ１エンティティクラスに変換<br/>
     * 該当エンティティの必要な項目を上から列挙
     *
     * @param model
     * @return OrderSummaryEntity
     */
    private OrderSummaryEntity toOrderSummaryEntity(OrderDetailsCommonModel model) {

        OrderSummaryEntity entity = ApplicationContextUtility.getBean(OrderSummaryEntity.class);
        // TODO キャンセル時間が処理時間になっている
        entity.setCancelTime(CANCEL.equals(model.getOrderReceivedResponse().getOrderStatus()) ?
                                             this.conversionUtility.toTimestamp(
                                                             model.getOrderReceivedResponse().getProcessTime()) :
                                             null);
        entity.setNoveltyPresentJudgmentStatus(model.getOrderReceivedResponse().getNoveltyPresentJudgmentStatus());

        return entity;
    }

    /**
     * 差分項目判定用にフェーズ１エンティティクラスに変換<br/>
     * 該当エンティティの必要な項目を上から列挙
     *
     * @param model
     * @return OrderPersonEntity
     */
    private OrderPersonEntity toOrderPersonEntity(OrderDetailsCommonModel model) {

        OrderPersonEntity entity = ApplicationContextUtility.getBean(OrderPersonEntity.class);
        entity.setOrderFirstName(model.getCustomerResponse().getMemberInfoFirstName());
        entity.setOrderLastName(model.getCustomerResponse().getMemberInfoLastName());
        entity.setOrderFirstKana(model.getCustomerResponse().getMemberInfoFirstKana());
        entity.setOrderLastKana(model.getCustomerResponse().getMemberInfoLastKana());
        entity.setOrderZipCode(model.getCustomerAddressResponse().getZipCode());
        entity.setOrderPrefecture(model.getCustomerAddressResponse().getPrefecture());
        entity.setOrderAddress1(model.getCustomerAddressResponse().getAddress1());
        entity.setOrderAddress2(model.getCustomerAddressResponse().getAddress2());
        entity.setOrderAddress3(model.getCustomerAddressResponse().getAddress3());
        entity.setOrderTel(model.getCustomerResponse().getMemberInfoTel());
        entity.setOrderMail(model.getCustomerResponse().getMemberInfoMail());
        entity.setOrderBirthday(
                        this.conversionUtility.toTimestamp(model.getCustomerResponse().getMemberInfoBirthday()));
        entity.setOrderAgeType(HTypeOrderAgeType.getType(
                        this.conversionUtility.toTimestamp(model.getCustomerResponse().getMemberInfoBirthday())));
        entity.setOrderSex(EnumTypeUtil.getEnumFromValue(HTypeOrderSex.class,
                                                         model.getCustomerResponse().getMemberInfoSex()
                                                        ));
        return entity;
    }

    /**
     * 差分項目判定用にフェーズ１エンティティクラスに変換<br/>
     * 該当エンティティの必要な項目を上から列挙
     *
     * @param model
     * @return OrderDeliveryEntity
     */
    private OrderDeliveryEntity toOrderDeliveryEntity(OrderDetailsCommonModel model) {

        OrderDeliveryEntity entity = ApplicationContextUtility.getBean(OrderDeliveryEntity.class);
        entity.setShipmentDate(
                        this.conversionUtility.toTimestamp(model.getShippingSlipResponse().getCompleteShipmentDate()));
        entity.setDeliveryCode(model.getShippingSlipResponse().getShipmentStatusConfirmCode());
        entity.setInvoiceAttachmentFlag(Boolean.TRUE.equals(model.getShippingSlipResponse().getInvoiceNecessaryFlag()) ?
                                                        HTypeInvoiceAttachmentFlag.ON :
                                                        HTypeInvoiceAttachmentFlag.OFF);
        entity.setReceiverLastName(model.getShippingAddressResponse().getLastName());
        entity.setReceiverFirstName(model.getShippingAddressResponse().getFirstName());
        entity.setReceiverLastKana(model.getShippingAddressResponse().getLastKana());
        entity.setReceiverFirstKana(model.getShippingAddressResponse().getFirstKana());
        entity.setReceiverTel(model.getShippingAddressResponse().getTel());
        entity.setReceiverZipCode(model.getShippingAddressResponse().getZipCode());
        entity.setReceiverPrefecture(model.getShippingAddressResponse().getPrefecture());
        entity.setReceiverAddress1(model.getShippingAddressResponse().getAddress1());
        entity.setReceiverAddress2(model.getShippingAddressResponse().getAddress2());
        entity.setReceiverAddress3(model.getShippingAddressResponse().getAddress3());
        entity.setReceiverDate(this.conversionUtility.toTimestamp(model.getShippingSlipResponse().getReceiverDate()));
        entity.setReceiverTimeZone(model.getShippingSlipResponse().getReceiverTimeZone());
        entity.setDeliveryNote(model.getShippingAddressResponse().getShippingMemo());
        return entity;
    }

    /**
     * 差分項目判定用にフェーズ１エンティティクラスに変換<br/>
     * 該当エンティティの必要な項目を上から列挙
     *
     * @param model
     * @return DeliveryMethodEntity
     */
    private DeliveryMethodEntity toDeliveryMethodEntity(OrderDetailsCommonModel model) {

        DeliveryMethodEntity entity = ApplicationContextUtility.getBean(DeliveryMethodEntity.class);
        entity.setDeliveryMethodName(model.getShippingSlipResponse().getShippingMethodName());
        return entity;
    }

    /**
     * 差分項目判定用にフェーズ１エンティティクラスに変換<br/>
     * 該当エンティティの必要な項目を上から列挙
     *
     * @param item 注文商品
     * @return OrderGoodsEntity
     */
    private OrderGoodsEntity toOrderGoodsEntity(OrderSlipResponseItemList item) {

        OrderGoodsEntity entity = ApplicationContextUtility.getBean(OrderGoodsEntity.class);
        entity.setGoodsCount(new BigDecimal(item.getItemCount() == null ? -1 : item.getItemCount()));
        return entity;
    }

    /**
     * 差分項目判定用にフェーズ１エンティティクラスに変換<br/>
     * 該当エンティティの必要な項目を上から列挙
     *
     * @param model
     * @return OrderSettlementEntity
     */
    private OrderSettlementEntity toOrderSettlementEntity(OrderDetailsCommonModel model) {

        OrderSettlementEntity entity = ApplicationContextUtility.getBean(OrderSettlementEntity.class);
        entity.setTaxPrice(new BigDecimal((model.getSalesSlipResponse().getStandardTax() != null ?
                        model.getSalesSlipResponse().getStandardTax() :
                        0) + (model.getSalesSlipResponse().getReducedTax() != null ?
                        model.getSalesSlipResponse().getReducedTax() :
                        0)));
        if (model.getSalesSlipResponse().getStandardTax() != null) {
            entity.setStandardTaxPrice(new BigDecimal(model.getSalesSlipResponse().getStandardTax()));
        }
        if (model.getSalesSlipResponse().getReducedTax() != null) {
            entity.setReducedTaxPrice(new BigDecimal(model.getSalesSlipResponse().getReducedTax()));
        }
        if (model.getSalesSlipResponse().getStandardTaxTargetPrice() != null) {
            entity.setStandardTaxTargetPrice(new BigDecimal(model.getSalesSlipResponse().getStandardTaxTargetPrice()));
        }
        if (model.getSalesSlipResponse().getReducedTaxTargetPrice() != null) {
            entity.setReducedTaxTargetPrice(new BigDecimal(model.getSalesSlipResponse().getReducedTaxTargetPrice()));
        }
        if (model.getSalesSlipResponse().getItemSalesPriceTotal() != null) {
            entity.setGoodsPriceTotal(new BigDecimal(model.getSalesSlipResponse().getItemSalesPriceTotal()));
        }
        if (model.getSalesSlipResponse().getCommission() != null) {
            entity.setSettlementCommission(new BigDecimal(model.getSalesSlipResponse().getCommission()));
        }
        if (model.getSalesSlipResponse().getCarriage() != null) {
            entity.setCarriage(new BigDecimal(model.getSalesSlipResponse().getCarriage()));
        }
        if (model.getSalesSlipResponse().getCouponPaymentPrice() != null) {
            entity.setCouponDiscountPrice(new BigDecimal(model.getSalesSlipResponse().getCouponPaymentPrice()));
        }
        return entity;
    }

    /**
     * 差分項目判定用にフェーズ１エンティティクラスに変換<br/>
     * 該当エンティティの必要な項目を上から列挙
     *
     * @param model
     * @return OrderAdditionalChargeEntity
     */
    private OrderAdditionalChargeEntity toOrderAdditionalChargeEntity(AdjustmentAmountResponse model) {

        OrderAdditionalChargeEntity entity = ApplicationContextUtility.getBean(OrderAdditionalChargeEntity.class);
        entity.setAdditionalDetailsName(model.getAdjustName());
        entity.setAdditionalDetailsPrice(new BigDecimal(model.getAdjustPrice()));
        return entity;
    }

    /**
     * 差分項目判定用にフェーズ１エンティティクラスに変換<br/>
     * 該当エンティティの必要な項目を上から列挙
     *
     * @param model
     * @return OrderBillEntity
     */
    private OrderBillEntity toOrderBillEntity(OrderDetailsCommonModel model) {

        OrderBillEntity entity = ApplicationContextUtility.getBean(OrderBillEntity.class);
        entity.setBillPrice(new BigDecimal(model.getBillingSlipResponse().getBillingPrice()));
        entity.setMoneyReceiptTime(model.getBillingSlipResponse().getMoneyReceiptTime());

        return entity;
    }

    /**
     * 差分項目判定用にフェーズ１エンティティクラスに変換<br/>
     * 該当エンティティの必要な項目を上から列挙
     *
     * @param model
     * @return OrderMemoEntity
     */
    private OrderMemoEntity toOrderMemoEntity(OrderDetailsCommonModel model) {

        OrderMemoEntity entity = ApplicationContextUtility.getBean(OrderMemoEntity.class);
        entity.setMemo(model.getOrderReceivedResponse().getAdminMemo());
        return entity;
    }

    /**
     * 差分項目判定用にフェーズ１エンティティクラスに変換<br/>
     * 該当エンティティの必要な項目を上から列挙
     *
     * @param model
     * @return OrderMemoEntity
     */
    private OrderIndexEntity toOrderIndexEntity(OrderDetailsCommonModel model) {

        OrderIndexEntity entity = ApplicationContextUtility.getBean(OrderIndexEntity.class);
        entity.setSettlementMailRequired(Boolean.TRUE.equals(model.getOrderReceivedResponse().getNotificationFlag()) ?
                                                         HTypeMailRequired.REQUIRED :
                                                         HTypeMailRequired.NO_NEED);
        entity.setReminderSentFlag(Boolean.TRUE.equals(model.getOrderReceivedResponse().getReminderSentFlag()) ?
                                                   HTypeSend.SENT :
                                                   HTypeSend.UNSENT);
        entity.setExpiredSentFlag(Boolean.TRUE.equals(model.getOrderReceivedResponse().getExpiredSentFlag()) ?
                                                  HTypeSend.SENT :
                                                  HTypeSend.UNSENT);

        String billStatus = HTypeBillStatus.BILL_NO_CLAIM.getValue();
        if (HTypeBillType.PRE_CLAIM.getValue().equals(model.getPaymentMethodResponse().getBillType())
            || Boolean.TRUE.equals(model.getOrderReceivedResponse().getShippedFlag())) {
            billStatus = HTypeBillStatus.BILL_CLAIM.getValue();
        }
        entity.setBillStatus(billStatus);

        return entity;
    }

    /**
     * 差分項目判定用にフェーズ１エンティティクラスに変換<br/>
     * 該当エンティティの必要な項目を上から列挙
     *
     * @param model
     * @return SettlementMethodEntity
     */
    private SettlementMethodEntity toSettlementMethodEntity(OrderDetailsCommonModel model) {

        SettlementMethodEntity entity = ApplicationContextUtility.getBean(SettlementMethodEntity.class);
        entity.setSettlementMethodName(model.getBillingSlipResponse().getPaymentMethodName());
        return entity;
    }

    /**
     * 差分項目判定用にフェーズ１エンティティクラスに変換<br/>
     * 該当エンティティの必要な項目を上から列挙
     *
     * @param model
     * @return MulPayBillEntity
     */
    private MulPayBillEntity toMulPayBillEntity(OrderDetailsCommonModel model) {

        if (model.getMulPayBillResponse() == null) {
            return null;
        }
        MulPayBillEntity entity = ApplicationContextUtility.getBean(MulPayBillEntity.class);
        entity.setOrderId(model.getMulPayBillResponse().getOrderId());
        entity.setJobCd(model.getMulPayBillResponse().getJobCd());
        entity.setMethod(model.getMulPayBillResponse().getMethod());
        entity.setPayTimes(model.getMulPayBillResponse().getPayTimes());
        entity.setTranId(model.getMulPayBillResponse().getTranId());
        entity.setConvenience(model.getMulPayBillResponse().getConvenience());
        entity.setConfNo(model.getMulPayBillResponse().getConfNo());
        entity.setReceiptNo(model.getMulPayBillResponse().getReceiptNo());
        entity.setCustId(model.getMulPayBillResponse().getCustId());
        entity.setBkCode(model.getMulPayBillResponse().getBkCode());
        entity.setErrCode(model.getMulPayBillResponse().getErrCode());
        entity.setErrInfo(model.getMulPayBillResponse().getErrInfo());
        return entity;
    }

    /**
     * 差分項目判定用にフェーズ１エンティティクラスに変換<br/>
     * 該当エンティティの必要な項目を上から列挙
     *
     * @param model
     * @return OrderDeliveryEntity
     */
    private OrderDeliveryEntity toOrderDeliveryEntityForBillingAddress(OrderDetailsCommonModel model) {

        OrderDeliveryEntity entity = ApplicationContextUtility.getBean(OrderDeliveryEntity.class);
        entity.setReceiverLastName(model.getBillingAddressResponse().getLastName());
        entity.setReceiverFirstName(model.getBillingAddressResponse().getFirstName());
        entity.setReceiverLastKana(model.getBillingAddressResponse().getLastKana());
        entity.setReceiverFirstKana(model.getBillingAddressResponse().getFirstKana());
        entity.setReceiverTel(model.getBillingAddressResponse().getTel());
        entity.setReceiverZipCode(model.getBillingAddressResponse().getZipCode());
        entity.setReceiverPrefecture(model.getBillingAddressResponse().getPrefecture());
        entity.setReceiverAddress1(model.getBillingAddressResponse().getAddress1());
        entity.setReceiverAddress2(model.getBillingAddressResponse().getAddress2());
        entity.setReceiverAddress3(model.getBillingAddressResponse().getAddress3());
        entity.setDeliveryNote(model.getBillingAddressResponse().getShippingMemo());
        return entity;
    }

}