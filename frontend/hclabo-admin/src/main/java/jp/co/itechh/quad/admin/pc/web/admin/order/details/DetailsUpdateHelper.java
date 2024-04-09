/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.order.details;

import jp.co.itechh.quad.addressbook.presentation.api.param.AddressBookAddressRegistRequest;
import jp.co.itechh.quad.addressbook.presentation.api.param.AddressBookAddressResponse;
import jp.co.itechh.quad.admin.base.util.common.CollectionUtil;
import jp.co.itechh.quad.admin.base.util.seasar.StringConversionUtil;
import jp.co.itechh.quad.admin.base.util.seasar.StringUtil;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.ConversionUtility;
import jp.co.itechh.quad.admin.base.utility.DateUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeAlcoholFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeBillStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeBillType;
import jp.co.itechh.quad.admin.constant.type.HTypeCancelFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeCarrierType;
import jp.co.itechh.quad.admin.constant.type.HTypeCouponLimitTargetType;
import jp.co.itechh.quad.admin.constant.type.HTypeCouponTargetType;
import jp.co.itechh.quad.admin.constant.type.HTypeEmergencyFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeFreeDeliveryFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeGmoReleaseFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeGoodsSaleStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeGoodsTaxType;
import jp.co.itechh.quad.admin.constant.type.HTypeIndividualDeliveryType;
import jp.co.itechh.quad.admin.constant.type.HTypeInvoiceAttachmentFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeNoveltyGoodsType;
import jp.co.itechh.quad.admin.constant.type.HTypeNoveltyPresentJudgmentStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeOrderStatus;
import jp.co.itechh.quad.admin.constant.type.HTypePaymentLinkType;
import jp.co.itechh.quad.admin.constant.type.HTypePaymentStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeReceiverDateDesignationFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeSend;
import jp.co.itechh.quad.admin.constant.type.HTypeSettlementMethodType;
import jp.co.itechh.quad.admin.constant.type.HTypeShipmentStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeSnsLinkFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeStockManagementFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeStockStatusType;
import jp.co.itechh.quad.admin.constant.type.HTypeUnitManagementFlag;
import jp.co.itechh.quad.admin.dto.goods.goods.GoodsDetailsDto;
import jp.co.itechh.quad.admin.dto.order.OrderMessageDto;
import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipForRevisionByTransactionRevisionIdResponse;
import jp.co.itechh.quad.convenience.presentation.api.param.ConvenienceListResponse;
import jp.co.itechh.quad.convenience.presentation.api.param.ConvenienceResponse;
import jp.co.itechh.quad.coupon.presentation.api.param.CouponResponse;
import jp.co.itechh.quad.customer.presentation.api.param.CustomerResponse;
import jp.co.itechh.quad.examination.presentation.api.param.ExamKitListResponse;
import jp.co.itechh.quad.examination.presentation.api.param.ExamKitResponse;
import jp.co.itechh.quad.method.presentation.api.param.DeliveryMethodResponse;
import jp.co.itechh.quad.method.presentation.api.param.PaymentMethodResponse;
import jp.co.itechh.quad.method.presentation.api.param.ShippingMethodResponse;
import jp.co.itechh.quad.mulpay.presentation.api.param.MulPayBillResponse;
import jp.co.itechh.quad.orderreceived.presentation.api.param.OrderReceivedResponse;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderItemResponse;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderItemRevisionResponse;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderSlipForRevisionResponse;
import jp.co.itechh.quad.product.presentation.api.param.GoodsDetailsResponse;
import jp.co.itechh.quad.product.presentation.api.param.ProductDetailListResponse;
import jp.co.itechh.quad.salesslip.presentation.api.param.AdjustmentAmountResponse;
import jp.co.itechh.quad.salesslip.presentation.api.param.GetSalesSlipForRevisionByTransactionRevisionIdResponse;
import jp.co.itechh.quad.salesslip.presentation.api.param.ItemPriceSubTotal;
import jp.co.itechh.quad.salesslip.presentation.api.param.SalesPriceConsumptionTaxResponse;
import jp.co.itechh.quad.salesslip.presentation.api.param.SalesSlipCouponApplyResponse;
import jp.co.itechh.quad.shippingslip.presentation.api.param.ShippingSlipResponse;
import jp.co.itechh.quad.transaction.presentation.api.param.BillingRequest;
import jp.co.itechh.quad.transaction.presentation.api.param.OrderItemCountRequest;
import jp.co.itechh.quad.transaction.presentation.api.param.ReceiverRequest;
import jp.co.itechh.quad.transaction.presentation.api.param.RegistInputContentToSuspendedTransactionForRevisionRequest;
import jp.co.itechh.quad.transaction.presentation.api.param.RegistInputContentToTransactionForRevisionRequest;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

/**
 * 受注修正修正・確認画面Helperクラス。
 */
@Component
public class DetailsUpdateHelper {

    /** 決済方法：コンビニ */
    protected static final String CONVENIENCE_TYPE = "3";

    /** 変換ユーティリティ */
    private final ConversionUtility conversionUtility;

    /** DateUtility */
    private final DateUtility dateUtility;

    private AbstractOrderDetailsHelper abstractOrderDetailsHelper;

    @Autowired
    public DetailsUpdateHelper(ConversionUtility conversionUtility,
                               DateUtility dateUtility,
                               AbstractOrderDetailsHelper abstractOrderDetailsHelper) {
        this.conversionUtility = conversionUtility;
        this.dateUtility = dateUtility;
        this.abstractOrderDetailsHelper = abstractOrderDetailsHelper;
    }

    /**
     * 受注情報詳細Modelのデータを画面Modelにセット
     *
     * @param detailsUpdateModel
     * @param orderDetailsRevisionResponseModel
     */
    public void toPage(DetailsUpdateModel detailsUpdateModel,
                       OrderDetailsRevisionResponseModel orderDetailsRevisionResponseModel) {

        // 受注概要情報をセット
        setOrderInfo(detailsUpdateModel, orderDetailsRevisionResponseModel.getOrderReceivedResponse());

        // お客様情報をセット
        setOrderCustomerInfo(detailsUpdateModel, orderDetailsRevisionResponseModel.getCustomerResponse());

        // 受注配送情報、受注商品情報の値をセット
        setOrderDeliveryInfo(detailsUpdateModel, orderDetailsRevisionResponseModel.getOrderSlipForRevisionResponse(),
                             orderDetailsRevisionResponseModel.getShippingSlipForRevisionResponse(),
                             orderDetailsRevisionResponseModel.getShippingMethodResponse(),
                             orderDetailsRevisionResponseModel.getShippingAddressResponse(),
                             orderDetailsRevisionResponseModel.getGoodsDtoList(),
                             orderDetailsRevisionResponseModel.getSalesSlipForRevisionResponse(),
                             orderDetailsRevisionResponseModel.getExamKitListResponse()
                            );

        // 受注請求決済入金情報をセット
        setOrderPaymentInfo(detailsUpdateModel, orderDetailsRevisionResponseModel.getBillingSlipForRevisionResponse(),
                            orderDetailsRevisionResponseModel.getBillingAddressResponse(),
                            orderDetailsRevisionResponseModel.getPaymentMethodResponse(),
                            orderDetailsRevisionResponseModel.getOrderReceivedResponse(),
                            orderDetailsRevisionResponseModel.getConvenienceListResponse(),
                            orderDetailsRevisionResponseModel.getMulPayBillResponse()
                           );

        // マルチペイメント情報をセット
        setMulPayInfo(detailsUpdateModel, orderDetailsRevisionResponseModel.getMulPayBillResponse());

        // 受注金額情報をセット
        setOrderAmountInfo(detailsUpdateModel, orderDetailsRevisionResponseModel.getSalesSlipForRevisionResponse(),
                           orderDetailsRevisionResponseModel.getCouponResponse()
                          );

        // ポップアップ用修正前項目セット
        if (orderDetailsRevisionResponseModel.getSalesSlipResponse().getCommission() != null) {
            detailsUpdateModel.setOrgCommissionDisp(
                            orderDetailsRevisionResponseModel.getSalesSlipResponse().getCommission().toString());
        }
        if (orderDetailsRevisionResponseModel.getSalesSlipResponse().getCarriage() != null) {
            detailsUpdateModel.setOrgCarriageDisp(
                            orderDetailsRevisionResponseModel.getSalesSlipResponse().getCarriage().toString());
        }

        OrderMessageDto orderMessageDto = detailsUpdateModel.getOrderMessageDto();
        orderMessageDto.setOrderMessageList(orderMessageDto.getOrderMessageList());

    }

    /**
     * 受注概要情報をセット
     *
     * @param detailsUpdateModel
     * @param orderReceivedResponse
     */
    private void setOrderInfo(DetailsUpdateModel detailsUpdateModel, OrderReceivedResponse orderReceivedResponse) {

        // 処理日時
        detailsUpdateModel.setProcessTime(conversionUtility.toTimestamp(orderReceivedResponse.getProcessTime()));
        // 受注日時
        detailsUpdateModel.setOrderTime(conversionUtility.toTimestamp(orderReceivedResponse.getOrderReceivedDate()));
        // キャンセルフラグ
        detailsUpdateModel.setCancelFlag("CANCEL".equals(orderReceivedResponse.getOrderStatus()) ?
                                                         HTypeCancelFlag.ON :
                                                         HTypeCancelFlag.OFF);
        // キャンセル日時
        detailsUpdateModel.setCancelTime(this.conversionUtility.toTimestamp(orderReceivedResponse.getCancelDate()));
        // 受注状態
        if ("ITEM_PREPARING".equals(orderReceivedResponse.getOrderStatus())) {
            // 商品準備中
            detailsUpdateModel.setOrderStatus(HTypeOrderStatus.GOODS_PREPARING);
        } else if ("PAYMENT_CONFIRMING".equals(orderReceivedResponse.getOrderStatus())) {
            // 入金確認中
            detailsUpdateModel.setOrderStatus(HTypeOrderStatus.PAYMENT_CONFIRMING);
        } else if ("SHIPMENT_COMPLETION".equals(orderReceivedResponse.getOrderStatus())) {
            // 出荷完了
            detailsUpdateModel.setOrderStatus(HTypeOrderStatus.SHIPMENT_COMPLETION);
        }
        // 出荷済みフラグ
        detailsUpdateModel.setShippedFlag(Boolean.TRUE.equals(orderReceivedResponse.getShippedFlag()));
        // 異常フラグ
        detailsUpdateModel.setEmergencyFlag("PAYMENT_ERROR".equals(orderReceivedResponse.getOrderStatus()) ?
                                                            HTypeEmergencyFlag.ON :
                                                            HTypeEmergencyFlag.OFF);

        // キャリア種別(常にPCを指定)
        detailsUpdateModel.setCarrierType(HTypeCarrierType.PC);

        // 受注メモをセット
        detailsUpdateModel.setMemo(orderReceivedResponse.getAdminMemo());

        // メール送信要否
        if (Boolean.TRUE.equals(orderReceivedResponse.getNotificationFlag())) {
            detailsUpdateModel.setUpdateMailRequired("true");
        } else {
            detailsUpdateModel.setUpdateMailRequired("false");
        }
        detailsUpdateModel.setReminderSentFlag(Boolean.TRUE.equals(orderReceivedResponse.getReminderSentFlag()) ?
                                                               HTypeSend.SENT :
                                                               HTypeSend.UNSENT);
        detailsUpdateModel.setExpiredSentFlag(Boolean.TRUE.equals(orderReceivedResponse.getExpiredSentFlag()) ?
                                                              HTypeSend.SENT :
                                                              HTypeSend.UNSENT);

        detailsUpdateModel.setNoveltyPresentJudgmentStatus(
                        EnumTypeUtil.getEnumFromValue(HTypeNoveltyPresentJudgmentStatus.class,
                                                      orderReceivedResponse.getNoveltyPresentJudgmentStatus()
                                                     ));
        detailsUpdateModel.setUpdateNoveltyPresentJudgmentStatus(
                        orderReceivedResponse.getNoveltyPresentJudgmentStatus());
    }

    /**
     * お客様情報をセット
     *
     * @param detailsUpdateModel
     * @param customerResponse
     */
    private void setOrderCustomerInfo(DetailsUpdateModel detailsUpdateModel, CustomerResponse customerResponse) {
        detailsUpdateModel.setMemberInfoSeq(customerResponse.getMemberInfoSeq());
        detailsUpdateModel.setOrderMail(customerResponse.getMemberInfoMail());
    }

    /**
     * 受注配送情報リストをセット<br/>
     * 配送情報内の商品情報リストをセット
     *
     * @param detailsUpdateModel
     * @param shippingSlipForRevisionResponse
     * @param shippingMethodResponse
     * @param shippingAddressResponse
     * @param goodsDtoList
     * @param salesSlipForRevisionResponse
     */
    private void setOrderDeliveryInfo(DetailsUpdateModel detailsUpdateModel,
                                      OrderSlipForRevisionResponse orderSlipForRevisionResponse,
                                      ShippingSlipResponse shippingSlipForRevisionResponse,
                                      ShippingMethodResponse shippingMethodResponse,
                                      AddressBookAddressResponse shippingAddressResponse,
                                      List<GoodsDetailsDto> goodsDtoList,
                                      GetSalesSlipForRevisionByTransactionRevisionIdResponse salesSlipForRevisionResponse,
                                      ExamKitListResponse examKitListResponse) {

        // 納品書添付フラグ
        detailsUpdateModel.setInvoiceAttachmentFlag(
                        Boolean.TRUE.equals(shippingSlipForRevisionResponse.getInvoiceNecessaryFlag()) ?
                                        HTypeInvoiceAttachmentFlag.ON :
                                        HTypeInvoiceAttachmentFlag.OFF);
        // 納品書更新用フラグ
        detailsUpdateModel.setUpdateInvoiceAttachmentFlag(detailsUpdateModel.getInvoiceAttachmentFlag().getValue());

        // お届け先アイテム
        OrderReceiverUpdateItem receiverItem =
                        toOrderReceiverItem(orderSlipForRevisionResponse, shippingSlipForRevisionResponse,
                                            shippingMethodResponse, shippingAddressResponse, goodsDtoList,
                                            salesSlipForRevisionResponse, examKitListResponse
                                           );
        detailsUpdateModel.setOrderReceiverItem(receiverItem);

        // 商品合計点数
        Integer originalTotalItemCount = 0;
        if (org.apache.commons.collections.CollectionUtils.isNotEmpty(
                        orderSlipForRevisionResponse.getOrderItemRevisionList())) {
            for (OrderItemRevisionResponse orderItemRevisionResponse : orderSlipForRevisionResponse.getOrderItemRevisionList()) {
                originalTotalItemCount += orderItemRevisionResponse.getOrderCount();
            }
        }
        detailsUpdateModel.setOrderGoodsCountTotal(BigDecimal.valueOf(originalTotalItemCount));
    }

    /**
     * 受注お届け先アイテムへ変換
     *
     * @param orderSlipForRevisionResponse
     * @param shippingSlipForRevisionResponse
     * @param shippingMethodResponse
     * @param shippingAddressResponse
     * @param salesSlipForRevisionResponse
     * @return お届け先アイテム
     */
    private OrderReceiverUpdateItem toOrderReceiverItem(OrderSlipForRevisionResponse orderSlipForRevisionResponse,
                                                        ShippingSlipResponse shippingSlipForRevisionResponse,
                                                        ShippingMethodResponse shippingMethodResponse,
                                                        AddressBookAddressResponse shippingAddressResponse,
                                                        List<GoodsDetailsDto> goodsDtoList,
                                                        GetSalesSlipForRevisionByTransactionRevisionIdResponse salesSlipForRevisionResponse,
                                                        ExamKitListResponse examKitListResponse) {

        OrderReceiverUpdateItem receiverItem = ApplicationContextUtility.getBean(OrderReceiverUpdateItem.class);

        // お届け先住所情報設定
        receiverItem.setAddressId(shippingAddressResponse.getAddressId());
        receiverItem.setReceiverLastName(shippingAddressResponse.getLastName());
        receiverItem.setReceiverFirstName(shippingAddressResponse.getFirstName());
        receiverItem.setReceiverLastKana(shippingAddressResponse.getLastKana());
        receiverItem.setReceiverFirstKana(shippingAddressResponse.getFirstKana());
        receiverItem.setReceiverTel(shippingAddressResponse.getTel());
        receiverItem.setReceiverZipCode(shippingAddressResponse.getZipCode());
        receiverItem.setReceiverPrefecture(shippingAddressResponse.getPrefecture());
        receiverItem.setReceiverAddress1(shippingAddressResponse.getAddress1());
        receiverItem.setReceiverAddress2(shippingAddressResponse.getAddress2());
        receiverItem.setReceiverAddress3(shippingAddressResponse.getAddress3());
        receiverItem.setDeliveryNote(shippingAddressResponse.getShippingMemo());

        // 配送伝票から取得
        receiverItem.setShipmentStatus(shippingSlipForRevisionResponse.getCompleteShipmentDate() != null ?
                                                       HTypeShipmentStatus.SHIPPED :
                                                       HTypeShipmentStatus.UNSHIPMENT);
        receiverItem.setShipmentDate(
                        conversionUtility.toTimestamp(shippingSlipForRevisionResponse.getCompleteShipmentDate()));
        receiverItem.setDeliveryCode(shippingSlipForRevisionResponse.getShipmentStatusConfirmCode());
        receiverItem.setReceiverDateDesignationFlag(
                        shippingMethodResponse.getDeliveryMethodResponse().getPossibleSelectDays() != null
                        && shippingMethodResponse.getDeliveryMethodResponse().getPossibleSelectDays() != 0 ?
                                        HTypeReceiverDateDesignationFlag.ON :
                                        HTypeReceiverDateDesignationFlag.OFF);
        receiverItem.setReceiverDate(conversionUtility.toTimestamp(shippingSlipForRevisionResponse.getReceiverDate()));
        receiverItem.setReceiverTimeZone(shippingSlipForRevisionResponse.getReceiverTimeZone());
        receiverItem.setDeliveryMethodSeq(
                        conversionUtility.toInteger(shippingSlipForRevisionResponse.getShippingMethodId()));
        receiverItem.setDeliveryMethodName(shippingSlipForRevisionResponse.getShippingMethodName());

        /* 修正用 */
        // 配送方法SEQ
        receiverItem.setUpdateDeliveryMethodSeq(StringConversionUtil.toString(receiverItem.getDeliveryMethodSeq()));
        // お届け希望日
        receiverItem.setUpdateReceiverDate(conversionUtility.toYmd(receiverItem.getReceiverDate()));
        // 配送メモ
        receiverItem.setReadOnlyDeliveryNote(receiverItem.getDeliveryNote());

        // 受注商品情報リストをセット
        List<OrderGoodsUpdateItem> orderGoodsItems = new ArrayList<>();

        List<OrderItemRevisionResponse> orderItemRevisionList = orderSlipForRevisionResponse.getOrderItemRevisionList();

        // 検査キットレスポンスMap＜注文商品ID、検査キットレスポンス＞
        Map<String, ExamKitResponse> examkitInformationMap = new HashMap<>();
        if (examKitListResponse != null && examKitListResponse.getExamKitList() != null) {
            for (ExamKitResponse examKitResponse : examKitListResponse.getExamKitList()) {
                examkitInformationMap.put(examKitResponse.getOrderItemId(), examKitResponse);
            }
        }

        // 注文商品 レスポンスMap＜注文商品連番、注文商品 レスポンス＞
        List<OrderItemResponse> orderItemResponseList = orderSlipForRevisionResponse.getOrderItemList();
        Map<Integer, OrderItemResponse> orderItemInformationMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(orderItemResponseList)) {
            for (OrderItemResponse orderItemResponse : orderItemResponseList) {
                orderItemInformationMap.put(orderItemResponse.getOrderItemSeq(), orderItemResponse);
            }
        }

        if (CollectionUtil.isNotEmpty(orderItemRevisionList)) {
            for (OrderItemRevisionResponse orderItemRevisionResponse : orderItemRevisionList) {

                // 販売伝票から単価を取得
                ItemPriceSubTotal salesSlipItemPrice = null;

                if (!ObjectUtils.isEmpty(salesSlipForRevisionResponse) && CollectionUtil.isNotEmpty(
                                salesSlipForRevisionResponse.getItemPriceSubTotal()) && CollectionUtil.isNotEmpty(
                                goodsDtoList)) {
                    salesSlipItemPrice = salesSlipForRevisionResponse.getItemPriceSubTotal()
                                                                     .stream()
                                                                     .filter(itemSubTotal -> itemSubTotal.getSalesItemSeq()
                                                                                             != null
                                                                                             && itemSubTotal.getSalesItemSeq()
                                                                                                            .equals(orderItemRevisionResponse.getOrderItemSeq()))
                                                                     .findFirst()
                                                                     .orElse(null);
                }
                // 商品マスタ情報取得
                GoodsDetailsDto goodsDetailsDto = goodsDtoList.stream()
                                                              .filter(goodsDto -> goodsDto.getGoodsSeq() != null
                                                                                  && goodsDto.getGoodsSeq()
                                                                                             .toString()
                                                                                             .equals(orderItemRevisionResponse.getItemId()))
                                                              .findFirst()
                                                              .orElse(null);
                Objects.requireNonNull(goodsDetailsDto);

                OrderGoodsUpdateItem goodsItem = ApplicationContextUtility.getBean(OrderGoodsUpdateItem.class);

                // 伝票が保持する商品情報をセット
                goodsItem.setGoodsGroupName(orderItemRevisionResponse.getItemName());
                goodsItem.setUnitValue1(orderItemRevisionResponse.getUnitValue1());
                goodsItem.setUnitValue2(orderItemRevisionResponse.getUnitValue2());
                goodsItem.setJanCode(orderItemRevisionResponse.getJanCode());
                goodsItem.setNoveltyGoodsType(EnumTypeUtil.getEnumFromValue(HTypeNoveltyGoodsType.class, orderItemRevisionResponse.getNoveltyGoodsType()));
                goodsItem.setGoodsCount(new BigDecimal(orderItemRevisionResponse.getOrderCount()));
                goodsItem.setOrderDisplay(orderItemRevisionResponse.getOrderItemSeq());
                // 販売伝票情報セット
                if (salesSlipItemPrice != null) {
                    goodsItem.setTaxRate(salesSlipItemPrice.getItemTaxRate());
                    goodsItem.setGoodsPrice(BigDecimal.valueOf(salesSlipItemPrice.getItemUnitPrice()));
                    goodsItem.setGoodsPriceInTax(BigDecimal.valueOf(salesSlipItemPrice.getItemUnitPrice()));
                    goodsItem.setPostTaxOrderGoodsPrice(BigDecimal.valueOf(salesSlipItemPrice.getItemPriceSubTotal()));
                } else {
                    goodsItem.setTaxRate(null);
                    goodsItem.setGoodsPrice(null);
                    goodsItem.setGoodsPriceInTax(null);
                    goodsItem.setPostTaxOrderGoodsPrice(null);
                }
                // 商品マスタ情報セット
                goodsItem.setFreeDeliveryFlag(goodsDetailsDto.getFreeDeliveryFlag());
                goodsItem.setIndividualDeliveryType(goodsDetailsDto.getIndividualDeliveryType());
                goodsItem.setGoodsGroupCode(goodsDetailsDto.getGoodsGroupCode());
                goodsItem.setGoodsSeq(goodsDetailsDto.getGoodsSeq());
                goodsItem.setGoodsCode(goodsDetailsDto.getGoodsCode());

                // 修正用
                goodsItem.setUpdateGoodsCount(String.valueOf(orderItemRevisionResponse.getOrderCount()));
                goodsItem.setOriginalUpdateGoodsCount(goodsItem.getUpdateGoodsCount());

                OrderItemResponse orderItemOrigin = orderItemInformationMap.get(orderItemRevisionResponse.getOrderItemSeq());
                if (orderItemOrigin != null) {
                    goodsItem.setOriginGoodsCount(new BigDecimal(orderItemOrigin.getOrderCount()));
                }

                // 検査キット情報の設定
                ExamKitResponse examKitResponse = examkitInformationMap.get(orderItemRevisionResponse.getOrderItemId());
                if (examKitResponse != null) {
                    goodsItem.setExamKitCode(examKitResponse.getExamKitCode());
                }

                orderGoodsItems.add(goodsItem);
            }
        }

        receiverItem.setOrderGoodsUpdateItems(orderGoodsItems);

        return receiverItem;
    }

    /**
     * 受注請求、決済、入金情報をセット
     *
     * @param detailsUpdateModel
     * @param billingAddressResponse
     * @param paymentMethodResponse
     * @param orderReceivedResponse
     * @param convenienceListResponse
     * @param mulPayBillResponse
     */
    private void setOrderPaymentInfo(DetailsUpdateModel detailsUpdateModel,
                                     BillingSlipForRevisionByTransactionRevisionIdResponse billingSlipRevisionResponse,
                                     AddressBookAddressResponse billingAddressResponse,
                                     PaymentMethodResponse paymentMethodResponse,
                                     OrderReceivedResponse orderReceivedResponse,
                                     ConvenienceListResponse convenienceListResponse,
                                     MulPayBillResponse mulPayBillResponse) {

        // 請求状態
        detailsUpdateModel.setBillStatus(HTypeBillType.PRE_CLAIM.getValue().equals(paymentMethodResponse.getBillType())
                                         || Boolean.TRUE.equals(orderReceivedResponse.getShippedFlag()) ?
                                                         HTypeBillStatus.BILL_CLAIM :
                                                         HTypeBillStatus.BILL_NO_CLAIM);
        // 決済代行連携解除
        detailsUpdateModel.setGmoReleaseFlag(
                        Boolean.TRUE.equals(billingSlipRevisionResponse.getPaymentAgencyReleaseFlag()) ?
                                        HTypeGmoReleaseFlag.RELEASE :
                                        HTypeGmoReleaseFlag.NORMAL);
        // 決済方法SEQ
        detailsUpdateModel.setSettlementMethodSeq(
                        this.conversionUtility.toInteger(billingSlipRevisionResponse.getPaymentMethodId()));
        // 修正用決済方法SEQ
        detailsUpdateModel.setUpdateSettlementMethodSeq(detailsUpdateModel.getSettlementMethodSeq().toString());
        // 決済方法名
        detailsUpdateModel.setSettlementMethodName(billingSlipRevisionResponse.getPaymentMethodName());
        // 入金日
        if (billingSlipRevisionResponse.getMoneyReceiptTime() != null) {
            detailsUpdateModel.setReceiptTime(
                            conversionUtility.toTimestamp(billingSlipRevisionResponse.getMoneyReceiptTime()));
        }
        // 決済種別
        if (HTypeSettlementMethodType.CREDIT.getValue().equals(paymentMethodResponse.getSettlementMethodType())) {
            detailsUpdateModel.setSettlementMethodType(HTypeSettlementMethodType.CREDIT);
        } else if (HTypeSettlementMethodType.LINK_PAYMENT.getValue()
                                                         .equals(paymentMethodResponse.getSettlementMethodType())) {

            detailsUpdateModel.setSettlementMethodType(HTypeSettlementMethodType.LINK_PAYMENT);
            detailsUpdateModel.setPayTypeName(billingSlipRevisionResponse.getPayTypeName());
            detailsUpdateModel.setBillTypeLink(EnumTypeUtil.getEnumFromValue(HTypePaymentLinkType.class,
                                                                             billingSlipRevisionResponse.getLinkPayType()
                                                                            ));
            detailsUpdateModel.setPayMethod(billingSlipRevisionResponse.getPaymethod());
            if (HTypePaymentLinkType.LATERDATEPAYMENT.getValue().equals(billingSlipRevisionResponse.getLinkPayType())) {
                detailsUpdateModel.setPaymentTimeLimitDate(
                                conversionUtility.toTimestamp(billingSlipRevisionResponse.getLaterDateLimit()));
                detailsUpdateModel.setCancelableDate(
                                conversionUtility.toTimestamp(billingSlipRevisionResponse.getCancelExpiredDate()));
            } else if (HTypePaymentLinkType.IMMEDIATEPAYMENT.getValue()
                                                            .equals(billingSlipRevisionResponse.getLinkPayType())) {
                detailsUpdateModel.setCancelLimit(
                                conversionUtility.toYmd(billingSlipRevisionResponse.getCancelLimit()));
            }

            if (CONVENIENCE_TYPE.equals(billingSlipRevisionResponse.getPayType())) {
                // 選択コンビニ名を設定
                setConveniName(detailsUpdateModel, convenienceListResponse, mulPayBillResponse);
            }

        }
        // 請求種別
        detailsUpdateModel.setBillType(HTypeBillType.PRE_CLAIM.getValue().equals(paymentMethodResponse.getBillType()) ?
                                                       HTypeBillType.PRE_CLAIM :
                                                       HTypeBillType.POST_CLAIM);

        // 入金状態
        if (orderReceivedResponse.getPaymentStatusDetail() != null) {
            detailsUpdateModel.setPaymentStatus(
                            HTypePaymentStatus.valueOf(orderReceivedResponse.getPaymentStatusDetail()).getValue());
        }

        // 請求先情報をセット
        detailsUpdateModel.setOrderBillingAddressId(billingAddressResponse.getAddressId());
        detailsUpdateModel.setOrderBillingLastName(billingAddressResponse.getLastName());
        detailsUpdateModel.setOrderBillingFirstName(billingAddressResponse.getFirstName());
        detailsUpdateModel.setOrderBillingLastKana(billingAddressResponse.getLastKana());
        detailsUpdateModel.setOrderBillingFirstKana(billingAddressResponse.getFirstKana());
        detailsUpdateModel.setOrderBillingTel(billingAddressResponse.getTel());
        detailsUpdateModel.setOrderBillingPrefecture(billingAddressResponse.getPrefecture());
        detailsUpdateModel.setOrderBillingPrefecture(billingAddressResponse.getPrefecture());
        detailsUpdateModel.setOrderBillingZipCode(billingAddressResponse.getZipCode());
        detailsUpdateModel.setOrderBillingAddress1(billingAddressResponse.getAddress1());
        detailsUpdateModel.setOrderBillingAddress2(billingAddressResponse.getAddress2());
        detailsUpdateModel.setOrderBillingAddress3(billingAddressResponse.getAddress3());

    }

    /**
     * マルチペイメント情報をセット
     *
     * @param detailsUpdateModel
     * @param mulPayBillResponse
     */
    private void setMulPayInfo(DetailsUpdateModel detailsUpdateModel, MulPayBillResponse mulPayBillResponse) {

        if (mulPayBillResponse != null) {

            detailsUpdateModel.setMulPayBillSeq(mulPayBillResponse.getMulPayBillSeq());
            detailsUpdateModel.setPayType(mulPayBillResponse.getPayType());
            detailsUpdateModel.setTranType(mulPayBillResponse.getTranType());
            detailsUpdateModel.setOrderId(mulPayBillResponse.getOrderId());
            detailsUpdateModel.setAccessId(mulPayBillResponse.getAccessId());
            detailsUpdateModel.setAccessPass(mulPayBillResponse.getAccessPass());
            detailsUpdateModel.setJobCd(mulPayBillResponse.getJobCd());
            detailsUpdateModel.setMethod(mulPayBillResponse.getMethod());
            detailsUpdateModel.setPayTimes(mulPayBillResponse.getPayTimes());
            detailsUpdateModel.setSeqMode(mulPayBillResponse.getSeqMode());
            detailsUpdateModel.setCardSeq(mulPayBillResponse.getCardSeq());
            detailsUpdateModel.setAmount(mulPayBillResponse.getAmount());
            detailsUpdateModel.setTax(mulPayBillResponse.getTax());
            detailsUpdateModel.setTdFlag(mulPayBillResponse.getTdFlag());
            detailsUpdateModel.setAcs(mulPayBillResponse.getMethod());
            detailsUpdateModel.setForward(mulPayBillResponse.getForward());
            detailsUpdateModel.setApprove(mulPayBillResponse.getApprove());
            detailsUpdateModel.setTranId(mulPayBillResponse.getTranId());
            detailsUpdateModel.setBankName(mulPayBillResponse.getBankName());
            detailsUpdateModel.setBranchName(mulPayBillResponse.getBranchName());
            detailsUpdateModel.setAccountType(
                            abstractOrderDetailsHelper.getAccountTypeLabel(mulPayBillResponse.getAccountType()));
            detailsUpdateModel.setAccountNumber(mulPayBillResponse.getAccountNumber());
            detailsUpdateModel.setExprireDate(
                            abstractOrderDetailsHelper.formatIntDate(mulPayBillResponse.getExprireDate()));
            detailsUpdateModel.setTranDate(mulPayBillResponse.getTranDate());
            detailsUpdateModel.setConvenience(mulPayBillResponse.getConvenience());
            detailsUpdateModel.setConfNo(mulPayBillResponse.getConfNo());
            detailsUpdateModel.setReceiptNo(mulPayBillResponse.getReceiptNo());
            detailsUpdateModel.setPaymentTerm(mulPayBillResponse.getPaymentTerm());
            detailsUpdateModel.setCustId(mulPayBillResponse.getCustId());
            detailsUpdateModel.setBkCode(mulPayBillResponse.getBkCode());
            detailsUpdateModel.setEncryptReceiptNo(mulPayBillResponse.getEncryptReceiptNo());
            detailsUpdateModel.setErrInfo(mulPayBillResponse.getErrInfo());
        }
    }

    /**
     * 金額情報をセット
     *
     * @param detailsUpdateModel
     * @param salesSlipForRevisionResponse
     * @param couponResponse
     */
    private void setOrderAmountInfo(DetailsUpdateModel detailsUpdateModel,
                                    GetSalesSlipForRevisionByTransactionRevisionIdResponse salesSlipForRevisionResponse,
                                    CouponResponse couponResponse) {

        if (salesSlipForRevisionResponse.getBillingAmount() != null) {
            detailsUpdateModel.setOrderPrice(new BigDecimal(salesSlipForRevisionResponse.getBillingAmount()));
        }
        if (salesSlipForRevisionResponse.getItemPurchasePriceTotal() != null) {
            detailsUpdateModel.setGoodsPriceTotal(
                            new BigDecimal(salesSlipForRevisionResponse.getItemPurchasePriceTotal()));
            detailsUpdateModel.setPostTaxGoodsPriceTotal(
                            new BigDecimal(salesSlipForRevisionResponse.getItemPurchasePriceTotal()));
        }
        if (salesSlipForRevisionResponse.getCarriage() != null) {
            detailsUpdateModel.setCarriage(new BigDecimal(salesSlipForRevisionResponse.getCarriage()));
        }
        if (salesSlipForRevisionResponse.getCommission() != null) {
            detailsUpdateModel.setSettlementCommission(new BigDecimal(salesSlipForRevisionResponse.getCommission()));
        }
        if (salesSlipForRevisionResponse.getApplyCouponResponse() != null
            && salesSlipForRevisionResponse.getApplyCouponResponse().getCouponPaymentPrice() != null) {
            detailsUpdateModel.setCouponDiscountPrice(Boolean.TRUE.equals(
                            salesSlipForRevisionResponse.getApplyCouponResponse().getCouponUseFlag()) ?
                                                                      new BigDecimal(salesSlipForRevisionResponse.getApplyCouponResponse()
                                                                                                                 .getCouponPaymentPrice()) :
                                                                      BigDecimal.ZERO);
        }

        // 消費税
        if (salesSlipForRevisionResponse.getSalesPriceConsumptionTaxResponse() != null) {

            SalesPriceConsumptionTaxResponse taxResponse =
                            salesSlipForRevisionResponse.getSalesPriceConsumptionTaxResponse();

            // 消費税合計
            Integer TaxPriceTotal = 0;

            // 標準消費税額
            if (taxResponse.getStandardTax() != null) {
                detailsUpdateModel.setStandardTaxPrice(new BigDecimal(taxResponse.getStandardTax()));

                TaxPriceTotal += taxResponse.getStandardTax();
            }
            // 軽減消費税額
            if (taxResponse.getReducedTax() != null) {
                detailsUpdateModel.setReducedTaxPrice(new BigDecimal(taxResponse.getReducedTax()));

                TaxPriceTotal += taxResponse.getReducedTax();
            }
            // 標準税率対象
            if (taxResponse.getStandardTaxTargetPrice() != null) {
                detailsUpdateModel.setStandardTaxTargetPrice(new BigDecimal(taxResponse.getStandardTaxTargetPrice()));
            }
            // 軽減税率対象
            if (taxResponse.getReducedTaxTargetPrice() != null) {
                detailsUpdateModel.setReducedTaxTargetPrice(new BigDecimal(taxResponse.getReducedTaxTargetPrice()));
            }

            // 消費税合計
            detailsUpdateModel.setTaxPrice(BigDecimal.valueOf(TaxPriceTotal));
        }

        if (couponResponse != null && salesSlipForRevisionResponse.getApplyCouponResponse() != null) {
            // クーポンが適用されている場合のみクーポン情報を画面に反映する
            // クーポン名
            detailsUpdateModel.setCouponName(salesSlipForRevisionResponse.getApplyCouponResponse().getCouponName());
            // クーポン割引額 画面上マイナス表示
            detailsUpdateModel.setCouponDiscountPrice(
                            new BigDecimal(salesSlipForRevisionResponse.getApplyCouponResponse()
                                                                       .getCouponPaymentPrice()).negate());
            // 適用クーポン名
            detailsUpdateModel.setApplyCouponName(
                            salesSlipForRevisionResponse.getApplyCouponResponse().getCouponName());
            // 適用クーポンID
            detailsUpdateModel.setApplyCouponId(couponResponse.getCouponId());
            // 適用クーポンSEQ
            detailsUpdateModel.setCouponSeq(couponResponse.getCouponSeq());
            // 適用クーポン枝番
            detailsUpdateModel.setCouponVersionNo(couponResponse.getCouponVersionNo());
            // クーポン利用フラグ
            detailsUpdateModel.setCouponLimitTargetTypeValue(Boolean.TRUE.equals(
                            salesSlipForRevisionResponse.getApplyCouponResponse().getCouponUseFlag()) ?
                                                                             HTypeCouponLimitTargetType.ON.getValue() :
                                                                             HTypeCouponLimitTargetType.OFF.getValue());
            // 商品にクーポン対象フラグ設定
            setCouponTargetGoodsFlg(detailsUpdateModel, couponResponse);
            // ポップアップ用クーポン適用金額
            detailsUpdateModel.setCouponDiscountLowerOrderPriceDisp(couponResponse.getDiscountLowerOrderPrice());
            // ポップアップ用クーポン割引金額
            detailsUpdateModel.setCouponDiscountPriceDisp(couponResponse.getDiscountPrice());
        } else {
            // クーポンが適用されていない場合はクーポン情報を初期化する
            detailsUpdateModel.setCouponDiscountPrice(BigDecimal.ZERO);
        }

        // 受注追加料金をセット
        setOrderAdditionalChargeDtoList(detailsUpdateModel, salesSlipForRevisionResponse);
    }

    /**
     * 商品にクーポン対象フラグ設定<br/>
     *
     * @param model          ページクラス
     * @param couponResponse クーポンエンティティ
     */
    private void setCouponTargetGoodsFlg(DetailsUpdateModel model, CouponResponse couponResponse) {

        // クーポン対象商品が入力されていない場合は判定しない
        String targetGoods = couponResponse.getTargetGoods();
        if (StringUtils.isBlank(targetGoods)) {
            return;
        }

        // クーポン対象商品にアイコンを付与
        List<String> targetGoodsList = Arrays.asList(conversionUtility.toDivArray(targetGoods));
        boolean isExclude = HTypeCouponTargetType.EXCLUDE_TARGET.getValue().equals(couponResponse.getTargetGoodsType());
        for (OrderGoodsUpdateItem orderGoodsUpdateItem : model.getOrderReceiverItem().getOrderGoodsUpdateItems()) {
            // 適用するクーポンが対象商品指定の場合は、該当する商品のみに付与
            // 適用するクーポンが除外商品指定の場合は、該当する商品以外に付与
            boolean isContain = targetGoodsList.contains(orderGoodsUpdateItem.getGoodsCode());
            if (isExclude != isContain) {
                orderGoodsUpdateItem.setCouponTargetGoodsFlg(true);
            }
        }
    }

    /**
     * 商品合計金額がクーポン適用金額に満たない場合のダイアログ表示用金額のセット
     * <pre>
     *   クーポン対象商品のみの合計金額となる。
     *   全商品の合計金額ではないため、注意。※Ver.4独自仕様
     * </pre>
     *
     * TODO 本当は、該当金額を取得するAPIを定義し、
     *   ApplyCouponService#checkDiscountLowerPrice で計算したクーポン対象の商品購入価格合計を返すべき。
     *   現状は、画面上のクーポン対象アイコンがついている商品の小計を足しこんでいる。
     *
     * @param model  受注詳細修正画面Model
     * @param coupon クーポンResponse
     */
    public void setGoodsPriceTotalDisp(DetailsUpdateModel model, CouponResponse coupon) {

        // クーポン対象商品が入力されていない場合は、全商品の合計金額
        String targetGoods = coupon.getTargetGoods();
        if (StringUtils.isBlank(targetGoods)) {
            model.setGoodsPriceTotalDisp(model.getGoodsPriceTotal());
            return;
        }

        OrderReceiverUpdateItem receiverItem = model.getOrderReceiverItem();
        if (receiverItem == null || receiverItem.getOrderGoodsUpdateItems() == null) {
            model.setGoodsPriceTotalDisp(BigDecimal.ZERO);
            return;
        }

        // クーポン対象商品アイコンが付与されている商品の小計のみを足しこみ
        model.setGoodsPriceTotalDisp(receiverItem.getOrderGoodsUpdateItems()
                                                 .stream()
                                                 .filter(OrderGoodsUpdateItem::isCouponTargetGoodsFlg)
                                                 .map(OrderGoodsUpdateItem::getPostTaxOrderGoodsPrice)
                                                 .filter(Objects::nonNull)
                                                 .reduce(BigDecimal.ZERO, BigDecimal::add));
    }

    /**
     * 商品詳細Dtoリストに変換
     *
     * @param productDetailListResponse
     * @param orderSlipForRevisionResponse
     * @return
     */
    public List<GoodsDetailsDto> toProductDetailList(ProductDetailListResponse productDetailListResponse,
                                                     OrderSlipForRevisionResponse orderSlipForRevisionResponse) {

        List<GoodsDetailsDto> goodsDetailDtoList = new ArrayList<>();

        for (GoodsDetailsResponse goodsDetailsResponse : productDetailListResponse.getGoodsDetailsList()) {

            GoodsDetailsDto goodsDetailsDto = new GoodsDetailsDto();

            goodsDetailsDto.setGoodsSeq(goodsDetailsResponse.getGoodsSeq());
            goodsDetailsDto.setGoodsGroupCode(goodsDetailsResponse.getGoodsGroupCode());
            goodsDetailsDto.setVersionNo(goodsDetailsResponse.getVersionNo());
            goodsDetailsDto.setGoodsCode(goodsDetailsResponse.getGoodsCode());
            if (goodsDetailsResponse.getGoodsTaxType() != null) {
                goodsDetailsDto.setGoodsTaxType(EnumTypeUtil.getEnumFromValue(HTypeGoodsTaxType.class,
                                                                              goodsDetailsResponse.getGoodsTaxType()
                                                                             ));
            }
            goodsDetailsDto.setTaxRate(goodsDetailsResponse.getTaxRate());
            goodsDetailsDto.setGoodsPriceInTax(goodsDetailsResponse.getGoodsPriceInTax());
            goodsDetailsDto.setGoodsPrice(goodsDetailsResponse.getGoodsPrice());
            if (goodsDetailsResponse.getAlcoholFlag() != null) {
                goodsDetailsDto.setAlcoholFlag(EnumTypeUtil.getEnumFromValue(HTypeAlcoholFlag.class,
                                                                             goodsDetailsResponse.getAlcoholFlag()
                                                                            ));
            }
            goodsDetailsDto.setDeliveryType(goodsDetailsResponse.getDeliveryType());
            if (goodsDetailsResponse.getSaleStatus() != null) {
                goodsDetailsDto.setSaleStatusPC(EnumTypeUtil.getEnumFromValue(HTypeGoodsSaleStatus.class,
                                                                              goodsDetailsResponse.getSaleStatus()
                                                                             ));
            }
            goodsDetailsDto.setSaleStartTimePC(
                            this.conversionUtility.toTimestamp(goodsDetailsResponse.getSaleStartTime()));
            goodsDetailsDto.setSaleEndTimePC(this.conversionUtility.toTimestamp(goodsDetailsResponse.getSaleEndTime()));
            if (goodsDetailsResponse.getUnitManagementFlag() != null) {
                goodsDetailsDto.setUnitManagementFlag(EnumTypeUtil.getEnumFromValue(HTypeUnitManagementFlag.class,
                                                                                    goodsDetailsResponse.getUnitManagementFlag()
                                                                                   ));
            }
            if (goodsDetailsResponse.getStockManagementFlag() != null) {
                goodsDetailsDto.setStockManagementFlag(EnumTypeUtil.getEnumFromValue(HTypeStockManagementFlag.class,
                                                                                     goodsDetailsResponse.getStockManagementFlag()
                                                                                    ));
            }
            if (goodsDetailsResponse.getIndividualDeliveryType() != null) {
                goodsDetailsDto.setIndividualDeliveryType(
                                EnumTypeUtil.getEnumFromValue(HTypeIndividualDeliveryType.class,
                                                              goodsDetailsResponse.getIndividualDeliveryType()
                                                             ));
            }
            goodsDetailsDto.setPurchasedMax(goodsDetailsResponse.getPurchasedMax());
            if (goodsDetailsResponse.getFreeDeliveryFlag() != null) {
                goodsDetailsDto.setFreeDeliveryFlag(EnumTypeUtil.getEnumFromValue(HTypeFreeDeliveryFlag.class,
                                                                                  goodsDetailsResponse.getFreeDeliveryFlag()
                                                                                 ));
            }
            goodsDetailsDto.setOrderDisplay(goodsDetailsResponse.getOrderDisplay());
            goodsDetailsDto.setUnitValue1(goodsDetailsResponse.getUnitValue1());
            goodsDetailsDto.setUnitValue2(goodsDetailsResponse.getUnitValue2());
            goodsDetailsDto.setJanCode(goodsDetailsResponse.getJanCode());
            goodsDetailsDto.setSalesPossibleStock(goodsDetailsResponse.getSalesPossibleStock());
            goodsDetailsDto.setRealStock(goodsDetailsResponse.getRealStock());
            goodsDetailsDto.setOrderReserveStock(goodsDetailsResponse.getOrderReserveStock());
            goodsDetailsDto.setRemainderFewStock(goodsDetailsResponse.getRemainderFewStock());
            goodsDetailsDto.setOrderPointStock(goodsDetailsResponse.getOrderPointStock());
            goodsDetailsDto.setSafetyStock(goodsDetailsResponse.getSafetyStock());
            goodsDetailsDto.setGoodsGroupCode(goodsDetailsResponse.getGoodsGroupCode());
            goodsDetailsDto.setWhatsnewDate(this.conversionUtility.toTimestamp(goodsDetailsResponse.getWhatsnewDate()));
            if (goodsDetailsResponse.getGoodsOpenStatus() != null) {
                goodsDetailsDto.setGoodsOpenStatusPC(EnumTypeUtil.getEnumFromValue(HTypeOpenDeleteStatus.class,
                                                                                   goodsDetailsResponse.getGoodsOpenStatus()
                                                                                  ));
            }
            goodsDetailsDto.setOpenStartTimePC(
                            this.conversionUtility.toTimestamp(goodsDetailsResponse.getOpenStartTime()));
            goodsDetailsDto.setOpenEndTimePC(this.conversionUtility.toTimestamp(goodsDetailsResponse.getOpenEndTime()));
            goodsDetailsDto.setGoodsGroupName(goodsDetailsResponse.getGoodsGroupName());
            goodsDetailsDto.setUnitTitle1(goodsDetailsResponse.getUnitTitle1());
            goodsDetailsDto.setUnitTitle2(goodsDetailsResponse.getUnitTitle2());
            if (CollectionUtil.isNotEmpty(goodsDetailsResponse.getGoodsGroupImageList())) {
                goodsDetailsDto.setGoodsGroupImageEntityList(abstractOrderDetailsHelper.toGoodsGroupImageList(
                                goodsDetailsResponse.getGoodsGroupImageList()));
            }
            if (goodsDetailsResponse.getSnsLinkFlag() != null) {
                goodsDetailsDto.setSnsLinkFlag(EnumTypeUtil.getEnumFromValue(HTypeSnsLinkFlag.class,
                                                                             goodsDetailsResponse.getSnsLinkFlag()
                                                                            ));
            }
            goodsDetailsDto.setMetaDescription(goodsDetailsResponse.getMetaDescription());
            if (goodsDetailsResponse.getStockStatus() != null) {
                goodsDetailsDto.setStockStatusPc(EnumTypeUtil.getEnumFromValue(HTypeStockStatusType.class,
                                                                               goodsDetailsResponse.getStockStatus()
                                                                              ));
            }
            if (CollectionUtil.isNotEmpty(goodsDetailsDto.getGoodsIconList())) {
                goodsDetailsDto.setGoodsIconList(
                                abstractOrderDetailsHelper.toGoodsGroupImage(goodsDetailsResponse.getGoodsIconList()));
            }
            if (!ObjectUtils.isEmpty(orderSlipForRevisionResponse) && !CollectionUtils.isEmpty(
                            orderSlipForRevisionResponse.getOrderItemRevisionList())) {
                for (int i = 0; i < orderSlipForRevisionResponse.getOrderItemRevisionList().size(); i++) {
                    if (!ObjectUtils.isEmpty(orderSlipForRevisionResponse.getOrderItemRevisionList().get(i))
                        && StringUtils.isNotEmpty(
                                    orderSlipForRevisionResponse.getOrderItemRevisionList().get(i).getItemId())) {
                        if (orderSlipForRevisionResponse.getOrderItemRevisionList()
                                                        .get(i)
                                                        .getItemId()
                                                        .equals(conversionUtility.toString(
                                                                        goodsDetailsDto.getGoodsSeq()))) {
                            goodsDetailsDto.setOrderGoodsCount(conversionUtility.toBigDecimal(
                                            orderSlipForRevisionResponse.getOrderItemRevisionList()
                                                                        .get(i)
                                                                        .getOrderCount()));
                        }
                    }
                }
            }
            goodsDetailsDto.setGoodsNote1(goodsDetailsResponse.getGoodsNote1());
            goodsDetailsDto.setGoodsNote2(goodsDetailsResponse.getGoodsNote2());
            goodsDetailsDto.setGoodsNote3(goodsDetailsResponse.getGoodsNote3());
            goodsDetailsDto.setGoodsNote4(goodsDetailsResponse.getGoodsNote4());
            goodsDetailsDto.setGoodsNote5(goodsDetailsResponse.getGoodsNote5());
            goodsDetailsDto.setGoodsNote6(goodsDetailsResponse.getGoodsNote6());
            goodsDetailsDto.setGoodsNote7(goodsDetailsResponse.getGoodsNote7());
            goodsDetailsDto.setGoodsNote8(goodsDetailsResponse.getGoodsNote8());
            goodsDetailsDto.setGoodsNote9(goodsDetailsResponse.getGoodsNote9());
            goodsDetailsDto.setGoodsNote10(goodsDetailsResponse.getGoodsNote10());
            goodsDetailsDto.setOrderSetting1(goodsDetailsResponse.getOrderSetting1());
            goodsDetailsDto.setOrderSetting2(goodsDetailsResponse.getOrderSetting2());
            goodsDetailsDto.setOrderSetting3(goodsDetailsResponse.getOrderSetting3());
            goodsDetailsDto.setOrderSetting4(goodsDetailsResponse.getOrderSetting4());
            goodsDetailsDto.setOrderSetting5(goodsDetailsResponse.getOrderSetting5());
            goodsDetailsDto.setOrderSetting6(goodsDetailsResponse.getOrderSetting6());
            goodsDetailsDto.setOrderSetting7(goodsDetailsResponse.getOrderSetting7());
            goodsDetailsDto.setOrderSetting8(goodsDetailsResponse.getOrderSetting8());
            goodsDetailsDto.setOrderSetting9(goodsDetailsResponse.getOrderSetting9());
            goodsDetailsDto.setOrderSetting10(goodsDetailsResponse.getOrderSetting10());

            goodsDetailDtoList.add(goodsDetailsDto);
        }

        return goodsDetailDtoList;
    }

    /**
     * 追加料金情報リストをセット
     *
     * @param detailsUpdateModel
     * @param salesSlipForRevisionResponse
     */
    private void setOrderAdditionalChargeDtoList(DetailsUpdateModel detailsUpdateModel,
                                                 GetSalesSlipForRevisionByTransactionRevisionIdResponse salesSlipForRevisionResponse) {

        List<OrderAdditionalChargeItem> orderAdditionalChargeItems = new ArrayList<>();
        if (!CollectionUtils.isEmpty(salesSlipForRevisionResponse.getAdjustmentAmountListResponse())) {

            for (AdjustmentAmountResponse adjustmentAmount : salesSlipForRevisionResponse.getAdjustmentAmountListResponse()) {
                OrderAdditionalChargeItem item = ApplicationContextUtility.getBean(OrderAdditionalChargeItem.class);
                item.setAdditionalDetailsName(adjustmentAmount.getAdjustName());
                item.setAdditionalDetailsPrice(new BigDecimal(adjustmentAmount.getAdjustPrice()));
                orderAdditionalChargeItems.add(item);
            }
        }

        detailsUpdateModel.setOrderAdditionalChargeItems(orderAdditionalChargeItems);
    }

    /**
     * 選択コンビニ名を設定<br/>
     *
     * @param model                   受注詳細修正ページ
     * @param convenienceListResponse コンビニ一覧
     * @param mulPayBillResponse      マルチペイメント請求エンティティ
     */
    protected void setConveniName(DetailsUpdateModel model,
                                  ConvenienceListResponse convenienceListResponse,
                                  MulPayBillResponse mulPayBillResponse) {
        // 選択コンビニ名を設定
        String conveniName = "";

        if (CollectionUtil.isNotEmpty(convenienceListResponse.getConvenienceList())) {
            // コンビニエンティティリスト取得
            List<ConvenienceResponse> conveniList = convenienceListResponse.getConvenienceList();

            for (ConvenienceResponse convenienceEntity : conveniList) {
                if (StringUtil.isNotEmpty(convenienceEntity.getConveniCode()) && convenienceEntity.getConveniCode()
                                                                                                  .equals(mulPayBillResponse.getConvenience())) {
                    // コンビニ名をPage（サブアプリケーションスコープ）に保持
                    conveniName = convenienceEntity.getConveniName();
                    break;
                }
            }
            model.setConveniName(conveniName);
        }
    }

    /**
     * 配送方法選択リストのセット
     *
     * @param model                      受注詳細修正ページ
     * @param shippingMethodResponseList
     */
    public void setDeliveryList(DetailsUpdateModel model, List<ShippingMethodResponse> shippingMethodResponseList) {

        // 配送方法選択map
        Map<Object, Object> map = new LinkedHashMap<>();
        if (CollectionUtils.isEmpty(shippingMethodResponseList)) {
            model.getOrderReceiverItem().setUpdateDeliveryMethodSeqItems(map);
            return;
        }

        for (ShippingMethodResponse shippingMethodResponse : shippingMethodResponseList) {
            if (shippingMethodResponse == null) {
                continue;
            }
            map.put(
                            shippingMethodResponse.getDeliveryMethodResponse().getDeliveryMethodSeq(),
                            shippingMethodResponse.getDeliveryMethodResponse().getDeliveryMethodName()
                   );
        }

        model.getOrderReceiverItem().setUpdateDeliveryMethodSeqItems(map);
    }

    /**
     * お届け時間帯選択アイテムリスト作成<br/>
     *
     * @param page                   受注詳細ページ
     * @param deliveryMethodResponse
     */
    public void setTimeZoneItem(DetailsUpdateModel page, DeliveryMethodResponse deliveryMethodResponse) {

        if (deliveryMethodResponse == null) {
            return;
        }

        Map<String, String> list = new LinkedHashMap<>();
        if (deliveryMethodResponse.getReceiverTimeZone1() != null) {
            list.put(deliveryMethodResponse.getReceiverTimeZone1(), deliveryMethodResponse.getReceiverTimeZone1());
        }
        if (deliveryMethodResponse.getReceiverTimeZone2() != null) {
            list.put(deliveryMethodResponse.getReceiverTimeZone2(), deliveryMethodResponse.getReceiverTimeZone2());
        }
        if (deliveryMethodResponse.getReceiverTimeZone3() != null) {
            list.put(deliveryMethodResponse.getReceiverTimeZone3(), deliveryMethodResponse.getReceiverTimeZone3());
        }
        if (deliveryMethodResponse.getReceiverTimeZone4() != null) {
            list.put(deliveryMethodResponse.getReceiverTimeZone4(), deliveryMethodResponse.getReceiverTimeZone4());
        }
        if (deliveryMethodResponse.getReceiverTimeZone5() != null) {
            list.put(deliveryMethodResponse.getReceiverTimeZone5(), deliveryMethodResponse.getReceiverTimeZone5());
        }
        if (deliveryMethodResponse.getReceiverTimeZone6() != null) {
            list.put(deliveryMethodResponse.getReceiverTimeZone6(), deliveryMethodResponse.getReceiverTimeZone6());
        }
        if (deliveryMethodResponse.getReceiverTimeZone7() != null) {
            list.put(deliveryMethodResponse.getReceiverTimeZone7(), deliveryMethodResponse.getReceiverTimeZone7());
        }
        if (deliveryMethodResponse.getReceiverTimeZone8() != null) {
            list.put(deliveryMethodResponse.getReceiverTimeZone8(), deliveryMethodResponse.getReceiverTimeZone8());
        }
        if (deliveryMethodResponse.getReceiverTimeZone9() != null) {
            list.put(deliveryMethodResponse.getReceiverTimeZone9(), deliveryMethodResponse.getReceiverTimeZone9());
        }
        if (deliveryMethodResponse.getReceiverTimeZone10() != null) {
            list.put(deliveryMethodResponse.getReceiverTimeZone10(), deliveryMethodResponse.getReceiverTimeZone10());
        }
        page.getOrderReceiverItem().setReceiverTimeZoneItems(list);
    }

    /**
     * クーポンポップアップ用対象商品設定<br/>
     *
     * @param page
     * @param coupon
     */
    public void setCouponTargetGoodsForJs(DetailsUpdateModel page, CouponResponse coupon) {

        // 全商品対象の場合
        if (StringUtils.isBlank(coupon.getTargetGoods())) {
            page.setCouponTargetGoodsIsAllFlg(true);
            return;
        }

        List<String> targetGoodsList = Arrays.asList(conversionUtility.toDivArray(coupon.getTargetGoods()));
        Map<String, String> couponTargetGoodsMap = new LinkedHashMap<>();
        // クーポン対象の商品コードと商品名を取得
        for (OrderGoodsUpdateItem orderGoodsUpdateItem : page.getOrderReceiverItem().getOrderGoodsUpdateItems()) {
            if (targetGoodsList.contains(orderGoodsUpdateItem.getGoodsCode())) {
                couponTargetGoodsMap.put(orderGoodsUpdateItem.getGoodsCode(), orderGoodsUpdateItem.getGoodsGroupName());
            }
        }
        // JS用に文字列に変換する。
        String couponTargetGoodsCode = null;
        String couponTargetGoodsName = null;
        for (Entry<String, String> goodsCodeEntry : couponTargetGoodsMap.entrySet()) {
            if (couponTargetGoodsCode == null) {
                couponTargetGoodsCode = goodsCodeEntry.getKey();
                couponTargetGoodsName = goodsCodeEntry.getValue();
            } else {
                couponTargetGoodsCode = couponTargetGoodsCode + "," + goodsCodeEntry.getKey();
                couponTargetGoodsName = couponTargetGoodsName + "," + goodsCodeEntry.getValue();
            }
        }

        page.setCouponTargetGoodsName(couponTargetGoodsName);
        page.setCouponTargetGoodsCode(couponTargetGoodsCode);
        page.setCouponTargetType(coupon.getTargetGoodsType());
    }

    /**
     * 改訂用取引に入力内容を反映するAPIリクエストモデル生成
     *
     * @param detailsUpdateModel 画面Model
     * @return RegistInputContentToTransactionForRevisionRequest
     */
    protected RegistInputContentToTransactionForRevisionRequest toRegistInputContentToTransactionForRevisionRequest(
                    DetailsUpdateModel detailsUpdateModel,
                    DetailsUpdateCommonModel detailsUpdateCommonModel) {

        RegistInputContentToTransactionForRevisionRequest registInputContentToTransactionForRevisionRequest =
                        new RegistInputContentToTransactionForRevisionRequest();
        registInputContentToTransactionForRevisionRequest.setTransactionRevisionId(
                        detailsUpdateCommonModel.getTransactionRevisionId());
        registInputContentToTransactionForRevisionRequest.setAdminMemo(detailsUpdateModel.getMemo());
        registInputContentToTransactionForRevisionRequest.setCustomerId(
                        String.valueOf(detailsUpdateModel.getMemberInfoSeq()));
        registInputContentToTransactionForRevisionRequest.setNotificationFlag(
                        "true".equals(detailsUpdateModel.getUpdateMailRequired()));
        registInputContentToTransactionForRevisionRequest.setNoveltyPresentJudgmentStatus(
                        detailsUpdateModel.getUpdateNoveltyPresentJudgmentStatus());
        // 注文商品
        List<OrderItemCountRequest> orderItemCountRequestList = new ArrayList<>();
        for (OrderGoodsUpdateItem orderGoodsUpdateItem : detailsUpdateModel.getOrderReceiverItem()
                                                                           .getOrderGoodsUpdateItems()) {

            OrderItemCountRequest orderItemCountRequest = new OrderItemCountRequest();
            orderItemCountRequest.setOrderItemSeq(orderGoodsUpdateItem.getOrderDisplay());
            orderItemCountRequest.setOrderCount(Integer.valueOf(orderGoodsUpdateItem.getUpdateGoodsCount()));

            orderItemCountRequestList.add(orderItemCountRequest);
        }
        registInputContentToTransactionForRevisionRequest.setOrderItemCountRequest(orderItemCountRequestList);
        // お届け先
        ReceiverRequest receiverRequest = new ReceiverRequest();
        receiverRequest.setShippingAddressId(detailsUpdateModel.getOrderReceiverItem().getAddressId());
        receiverRequest.setShippingMethodId(
                        String.valueOf(detailsUpdateModel.getOrderReceiverItem().getUpdateDeliveryMethodSeq()));
        receiverRequest.setReceiverDate(
                        conversionUtility.toDate(detailsUpdateModel.getOrderReceiverItem().getUpdateReceiverDate()));
        receiverRequest.setReceiverTimeZone(detailsUpdateModel.getOrderReceiverItem().getReceiverTimeZone());
        receiverRequest.setInvoiceNecessaryFlag(
                        conversionUtility.convertFlagToBoolean(detailsUpdateModel.getUpdateInvoiceAttachmentFlag()));
        receiverRequest.setShipmentStatusConfirmCode(detailsUpdateModel.getOrderReceiverItem().getDeliveryCode());
        // リクエストへ設定
        registInputContentToTransactionForRevisionRequest.setReceiverRequest(receiverRequest);
        // 請求先
        BillingRequest billingRequest = new BillingRequest();
        billingRequest.setBillingAddressId(detailsUpdateModel.getOrderBillingAddressId());
        // リクエストへ設定
        registInputContentToTransactionForRevisionRequest.setBillingRequest(billingRequest);

        return registInputContentToTransactionForRevisionRequest;
    }

    /**
     * AddressBookAddressResponseへお届け先画面データを設定<br/>
     * ※差分比較に使用
     *
     * @param orderReceiverUpdateItem
     * @return AddressBookAddressResponse
     */
    public AddressBookAddressResponse toReceiverAddressResponse(OrderReceiverUpdateItem orderReceiverUpdateItem) {

        AddressBookAddressResponse shippingAddressResponse = new AddressBookAddressResponse();
        shippingAddressResponse.setAddressId(orderReceiverUpdateItem.getAddressId());
        shippingAddressResponse.setLastName(orderReceiverUpdateItem.getReceiverLastName());
        shippingAddressResponse.setFirstName(orderReceiverUpdateItem.getReceiverFirstName());
        shippingAddressResponse.setLastKana(orderReceiverUpdateItem.getReceiverLastKana());
        shippingAddressResponse.setFirstKana(orderReceiverUpdateItem.getReceiverFirstKana());
        shippingAddressResponse.setZipCode(orderReceiverUpdateItem.getReceiverZipCode());
        shippingAddressResponse.setPrefecture(orderReceiverUpdateItem.getReceiverPrefecture());
        shippingAddressResponse.setAddress1(orderReceiverUpdateItem.getReceiverAddress1());
        shippingAddressResponse.setAddress2(orderReceiverUpdateItem.getReceiverAddress2());
        shippingAddressResponse.setAddress3(orderReceiverUpdateItem.getReceiverAddress3());
        shippingAddressResponse.setTel(orderReceiverUpdateItem.getReceiverTel());
        shippingAddressResponse.setShippingMemo(orderReceiverUpdateItem.getDeliveryNote());

        return shippingAddressResponse;
    }

    /**
     * AddressBookAddressRegistRequestへお届け先画面データを設定<br/>
     *
     * @param orderReceiverUpdateItem
     * @param hideFlag
     * @return
     */
    public AddressBookAddressRegistRequest toBillingAddressRegistRequest(OrderReceiverUpdateItem orderReceiverUpdateItem,
                                                                         boolean hideFlag) {

        AddressBookAddressRegistRequest shippingAddressRegistRequest = new AddressBookAddressRegistRequest();
        shippingAddressRegistRequest.setLastName(orderReceiverUpdateItem.getReceiverLastName());
        shippingAddressRegistRequest.setFirstName(orderReceiverUpdateItem.getReceiverFirstName());
        shippingAddressRegistRequest.setLastKana(orderReceiverUpdateItem.getReceiverLastKana());
        shippingAddressRegistRequest.setFirstKana(orderReceiverUpdateItem.getReceiverFirstKana());
        shippingAddressRegistRequest.setZipCode(orderReceiverUpdateItem.getReceiverZipCode());
        shippingAddressRegistRequest.setPrefecture(orderReceiverUpdateItem.getReceiverPrefecture());
        shippingAddressRegistRequest.setAddress1(orderReceiverUpdateItem.getReceiverAddress1());
        shippingAddressRegistRequest.setAddress2(orderReceiverUpdateItem.getReceiverAddress2());
        shippingAddressRegistRequest.setAddress3(orderReceiverUpdateItem.getReceiverAddress3());
        shippingAddressRegistRequest.setTel(orderReceiverUpdateItem.getReceiverTel());
        shippingAddressRegistRequest.setShippingMemo(orderReceiverUpdateItem.getDeliveryNote());
        shippingAddressRegistRequest.setHideFlag(hideFlag);

        return shippingAddressRegistRequest;
    }

    /**
     * AddressBookAddressResponseへ請求先画面データを設定<br/>
     * ※差分比較に使用
     *
     * @param detailsUpdateModel
     * @return
     */
    public AddressBookAddressResponse toBillingAddressResponse(DetailsUpdateModel detailsUpdateModel) {

        AddressBookAddressResponse billingAddressResponse = new AddressBookAddressResponse();
        billingAddressResponse.setAddressId(detailsUpdateModel.getOrderBillingAddressId());
        billingAddressResponse.setLastName(detailsUpdateModel.getOrderBillingLastName());
        billingAddressResponse.setFirstName(detailsUpdateModel.getOrderBillingFirstName());
        billingAddressResponse.setLastKana(detailsUpdateModel.getOrderBillingLastKana());
        billingAddressResponse.setFirstKana(detailsUpdateModel.getOrderBillingFirstKana());
        billingAddressResponse.setZipCode(detailsUpdateModel.getOrderBillingZipCode());
        billingAddressResponse.setPrefecture(detailsUpdateModel.getOrderBillingPrefecture());
        billingAddressResponse.setAddress1(detailsUpdateModel.getOrderBillingAddress1());
        billingAddressResponse.setAddress2(detailsUpdateModel.getOrderBillingAddress2());
        billingAddressResponse.setAddress3(detailsUpdateModel.getOrderBillingAddress3());
        billingAddressResponse.setTel(detailsUpdateModel.getOrderBillingTel());

        return billingAddressResponse;
    }

    /**
     * AddressBookAddressRegistRequestへ請求先画面データを設定<br/>
     *
     * @param detailsUpdateModel
     * @param hideFlag
     * @return AddressBookAddressRegistRequest
     */
    public AddressBookAddressRegistRequest toBillingAddressRegistRequest(DetailsUpdateModel detailsUpdateModel,
                                                                         boolean hideFlag) {

        AddressBookAddressRegistRequest billingAddressRegistRequest = new AddressBookAddressRegistRequest();
        billingAddressRegistRequest.setLastName(detailsUpdateModel.getOrderBillingLastName());
        billingAddressRegistRequest.setFirstName(detailsUpdateModel.getOrderBillingFirstName());
        billingAddressRegistRequest.setLastKana(detailsUpdateModel.getOrderBillingLastKana());
        billingAddressRegistRequest.setFirstKana(detailsUpdateModel.getOrderBillingFirstKana());
        billingAddressRegistRequest.setZipCode(detailsUpdateModel.getOrderBillingZipCode());
        billingAddressRegistRequest.setPrefecture(detailsUpdateModel.getOrderBillingPrefecture());
        billingAddressRegistRequest.setAddress1(detailsUpdateModel.getOrderBillingAddress1());
        billingAddressRegistRequest.setAddress2(detailsUpdateModel.getOrderBillingAddress2());
        billingAddressRegistRequest.setAddress3(detailsUpdateModel.getOrderBillingAddress3());
        billingAddressRegistRequest.setTel(detailsUpdateModel.getOrderBillingTel());
        billingAddressRegistRequest.setShippingMemo(detailsUpdateModel.getOrderReceiverItem().getDeliveryNote());
        billingAddressRegistRequest.setHideFlag(hideFlag);

        return billingAddressRegistRequest;
    }

    /**
     * 改訂用一時停止取引に入力内容を反映するAPIリクエストモデル生成<br/>
     * ※請求決済エラーが発生している受注を更新する場合に呼び出されるAPIリクエスト
     *
     * @param detailsUpdateModel 画面Model
     * @return RegistInputContentToSuspendedTransactionForRevisionRequest
     */
    protected RegistInputContentToSuspendedTransactionForRevisionRequest toRegistInputContentToSuspendedTransactionForRevisionRequest(
                    DetailsUpdateModel detailsUpdateModel,
                    DetailsUpdateCommonModel detailsUpdateCommonModel) {

        RegistInputContentToSuspendedTransactionForRevisionRequest
                        registInputContentToSuspendedTransactionForRevisionRequest =
                        new RegistInputContentToSuspendedTransactionForRevisionRequest();
        registInputContentToSuspendedTransactionForRevisionRequest.setTransactionRevisionId(
                        detailsUpdateCommonModel.getTransactionRevisionId());
        registInputContentToSuspendedTransactionForRevisionRequest.setAdminMemo(detailsUpdateModel.getMemo());
        registInputContentToSuspendedTransactionForRevisionRequest.setPaymentAgencyReleaseFlag(
                        detailsUpdateModel.isCancelOfCooperation());

        return registInputContentToSuspendedTransactionForRevisionRequest;
    }

    /**
     * 決済情報更新
     *
     * @param model    受注詳細修正画面Model
     * @param response 改訂用取引IDに紐づく改訂用販売伝票取得レスポンス
     */
    public void updatePaymentInformation(DetailsUpdateModel model,
                                         GetSalesSlipForRevisionByTransactionRevisionIdResponse response) {

        // ---------------------- 受注商品 ----------------------
        if (response.getItemPriceSubTotal() != null) {
            updateGoodsInformation(model, response.getItemPriceSubTotal());
        }

        // ---------------------- お支払い情報 ----------------------
        // 商品数量合計
        //      ↑の受注商品更新時に同時にセット
        // 商品合計金額（税抜）
        if (response.getItemPurchasePriceTotal() != null) {
            model.setGoodsPriceTotal(BigDecimal.valueOf(response.getItemPurchasePriceTotal()));
        }
        // 送料合計金額（税抜）
        if (response.getCarriage() != null) {
            model.setCarriage(BigDecimal.valueOf(response.getCarriage()));
        }
        // 手数料（税抜）
        if (response.getCommission() != null) {
            model.setSettlementCommission(BigDecimal.valueOf(response.getCommission()));
        }
        // その他の料金
        if (response.getAdjustmentAmountListResponse() != null) {
            setOrderAdditionalChargeDtoList(model, response);
        }
        // 消費税（消費税内訳）
        if (response.getSalesPriceConsumptionTaxResponse() != null) {
            updateTaxInformation(model, response.getSalesPriceConsumptionTaxResponse());
        }
        // クーポン
        if (response.getApplyCouponResponse() != null) {
            updateCouponInformation(model, response.getApplyCouponResponse());
        }
        // お支払い合計（税込）
        if (response.getBillingAmount() != null) {
            model.setOrderPrice(BigDecimal.valueOf(response.getBillingAmount()));
        }
    }

    /**
     * 決済情報更新 ※受注商品表示欄
     *
     * @param model        受注詳細修正画面Model
     * @param responseList 商品購入価格小計取得結果のリスト
     */
    private void updateGoodsInformation(DetailsUpdateModel model, List<ItemPriceSubTotal> responseList) {

        OrderReceiverUpdateItem receiverItem = model.getOrderReceiverItem();
        if (receiverItem == null) {
            return;
        }

        int goodsCountTotal = 0;

        for (OrderGoodsUpdateItem item : receiverItem.getOrderGoodsUpdateItems()) {
            ItemPriceSubTotal subTotal = responseList.stream()
                                                     .filter(response -> item.getOrderDisplay()
                                                                             .equals(response.getSalesItemSeq()))
                                                     .findFirst()
                                                     .orElseThrow();
            // 商品単価（税抜き）
            if (subTotal.getItemUnitPrice() != null) {
                item.setGoodsPrice(BigDecimal.valueOf(subTotal.getItemUnitPrice()));
            }
            // 商品数量（入力）
            Integer goodsCount = subTotal.getItemCount();
            if (goodsCount != null) {
                item.setUpdateGoodsCount(String.valueOf(goodsCount));
                goodsCountTotal += goodsCount;
            }
            // 商品小計
            if (subTotal.getItemPriceSubTotal() != null) {
                item.setPostTaxOrderGoodsPrice(BigDecimal.valueOf(subTotal.getItemPriceSubTotal()));
            }
            // 税率
            if (subTotal.getItemTaxRate() != null) {
                item.setTaxRate(subTotal.getItemTaxRate());
            }
        }

        // 商品合計点数
        model.setOrderGoodsCountTotal(BigDecimal.valueOf(goodsCountTotal));
    }

    /**
     * 決済情報更新 ※消費税内訳表示欄
     *
     * @param model    受注詳細修正画面Model
     * @param response 販売金額消費税取得レスポンス
     */
    private void updateTaxInformation(DetailsUpdateModel model, SalesPriceConsumptionTaxResponse response) {

        int taxPriceTotal = 0;

        // 標準消費税額
        Integer standardTax = response.getStandardTax();
        if (standardTax != null) {
            model.setStandardTaxPrice(BigDecimal.valueOf(standardTax));

            taxPriceTotal += standardTax;
        }
        // 軽減消費税額
        Integer reducedTax = response.getReducedTax();
        if (reducedTax != null) {
            model.setReducedTaxPrice(BigDecimal.valueOf(reducedTax));

            taxPriceTotal += reducedTax;
        }
        // 標準税率対象
        if (response.getStandardTaxTargetPrice() != null) {
            model.setStandardTaxTargetPrice(BigDecimal.valueOf(response.getStandardTaxTargetPrice()));
        }
        // 軽減税率対象
        if (response.getReducedTaxTargetPrice() != null) {
            model.setReducedTaxTargetPrice(BigDecimal.valueOf(response.getReducedTaxTargetPrice()));
        }

        // 消費税合計
        model.setTaxPrice(BigDecimal.valueOf(taxPriceTotal));
    }

    /**
     * 決済情報更新 ※クーポン表示欄
     *
     * @param model    受注詳細修正画面Model
     * @param response 適用クーポン取得レスポンス
     */
    private void updateCouponInformation(DetailsUpdateModel model, SalesSlipCouponApplyResponse response) {

        // クーポン割引金額
        if (response.getCouponPaymentPrice() != null) {
            model.setCouponDiscountPrice(BigDecimal.valueOf(response.getCouponPaymentPrice()).negate());
        }
        // クーポン利用制限対象種別
        if (response.getCouponUseFlag() != null) {
            model.setCouponLimitTargetTypeValue(response.getCouponUseFlag() ?
                                                                HTypeCouponLimitTargetType.ON.getValue() :
                                                                HTypeCouponLimitTargetType.OFF.getValue());
        }
    }

}
