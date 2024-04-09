/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.order.details;

import jp.co.itechh.quad.addressbook.presentation.api.param.AddressBookAddressResponse;
import jp.co.itechh.quad.admin.base.util.common.CollectionUtil;
import jp.co.itechh.quad.admin.base.util.common.DiffUtil;
import jp.co.itechh.quad.admin.base.util.seasar.TimestampConversionUtil;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.ConversionUtility;
import jp.co.itechh.quad.admin.base.utility.DateUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeAbnormalValueType;
import jp.co.itechh.quad.admin.constant.type.HTypeAlcoholFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeBillStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeBillType;
import jp.co.itechh.quad.admin.constant.type.HTypeCancelFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeCarrierType;
import jp.co.itechh.quad.admin.constant.type.HTypeCouponLimitTargetType;
import jp.co.itechh.quad.admin.constant.type.HTypeCouponTargetType;
import jp.co.itechh.quad.admin.constant.type.HTypeEmergencyFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeExamCompletedFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeExamStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeFreeDeliveryFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeGmoReleaseFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeGoodsSaleStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeGoodsTaxType;
import jp.co.itechh.quad.admin.constant.type.HTypeIndividualDeliveryType;
import jp.co.itechh.quad.admin.constant.type.HTypeInvoiceAttachmentFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeMailRequired;
import jp.co.itechh.quad.admin.constant.type.HTypeNoveltyPresentJudgmentStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeOrderAgeType;
import jp.co.itechh.quad.admin.constant.type.HTypeOrderSex;
import jp.co.itechh.quad.admin.constant.type.HTypeOrderStatus;
import jp.co.itechh.quad.admin.constant.type.HTypePaymentLinkType;
import jp.co.itechh.quad.admin.constant.type.HTypePaymentStatus;
import jp.co.itechh.quad.admin.constant.type.HTypePrefectureType;
import jp.co.itechh.quad.admin.constant.type.HTypeReceiverDateDesignationFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeRepeatPurchaseType;
import jp.co.itechh.quad.admin.constant.type.HTypeSalesFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeSend;
import jp.co.itechh.quad.admin.constant.type.HTypeSettlementMethodType;
import jp.co.itechh.quad.admin.constant.type.HTypeShipmentStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeSnsLinkFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeStockManagementFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeStockStatusType;
import jp.co.itechh.quad.admin.constant.type.HTypeUnitManagementFlag;
import jp.co.itechh.quad.admin.dto.goods.goods.GoodsDetailsDto;
import jp.co.itechh.quad.admin.dto.icon.GoodsInformationIconDetailsDto;
import jp.co.itechh.quad.admin.entity.goods.goodsgroup.GoodsGroupImageEntity;
import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipResponse;
import jp.co.itechh.quad.billingslip.presentation.api.param.CreditResponse;
import jp.co.itechh.quad.billingslip.presentation.api.param.PaymentLinkResponse;
import jp.co.itechh.quad.convenience.presentation.api.param.ConvenienceListResponse;
import jp.co.itechh.quad.convenience.presentation.api.param.ConvenienceResponse;
import jp.co.itechh.quad.coupon.presentation.api.param.CouponResponse;
import jp.co.itechh.quad.customer.presentation.api.param.CustomerResponse;
import jp.co.itechh.quad.examination.presentation.api.param.ConfigInfoResponse;
import jp.co.itechh.quad.examination.presentation.api.param.ExamKitListResponse;
import jp.co.itechh.quad.examination.presentation.api.param.ExamKitResponse;
import jp.co.itechh.quad.examination.presentation.api.param.ExamResultsResponse;
import jp.co.itechh.quad.method.presentation.api.param.PaymentMethodResponse;
import jp.co.itechh.quad.mulpay.presentation.api.param.MulPayBillResponse;
import jp.co.itechh.quad.orderreceived.presentation.api.param.OrderReceivedCountResponse;
import jp.co.itechh.quad.orderreceived.presentation.api.param.OrderReceivedResponse;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderSlipResponse;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderSlipResponseItemList;
import jp.co.itechh.quad.product.presentation.api.param.GoodsDetailsResponse;
import jp.co.itechh.quad.product.presentation.api.param.GoodsGroupImageResponse;
import jp.co.itechh.quad.product.presentation.api.param.GoodsInformationIconDetailsResponse;
import jp.co.itechh.quad.product.presentation.api.param.ProductDetailListResponse;
import jp.co.itechh.quad.salesslip.presentation.api.param.AdjustmentAmountResponse;
import jp.co.itechh.quad.salesslip.presentation.api.param.ItemPriceSubTotal;
import jp.co.itechh.quad.salesslip.presentation.api.param.SalesSlipResponse;
import jp.co.itechh.quad.shippingslip.presentation.api.param.ShippingSlipResponse;
import lombok.Data;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static jp.co.itechh.quad.admin.pc.web.admin.order.details.AbstractOrderDetailsModel.CANCEL;
import static jp.co.itechh.quad.admin.pc.web.admin.order.details.AbstractOrderDetailsModel.ITEM_PREPARING;
import static jp.co.itechh.quad.admin.pc.web.admin.order.details.AbstractOrderDetailsModel.PAYMENT_CONFIRMING;
import static jp.co.itechh.quad.admin.pc.web.admin.order.details.AbstractOrderDetailsModel.PAYMENT_ERROR;
import static jp.co.itechh.quad.admin.pc.web.admin.order.details.AbstractOrderDetailsModel.SHIPMENT_COMPLETION;

/**
 * 受注詳細Helper<br/>
 *
 * @author kimura
 */
@Component
@Data
public class AbstractOrderDetailsHelper {

    /** DateUtility */
    private DateUtility dateUtility;

    /** ConversionUtility */
    private ConversionUtility conversionUtility;

    /** 預金種別ラベル */
    private static final String ACCOUNTTYPE_LABEL = "普通預金";

    /** コンストラクタ */
    @Autowired
    public AbstractOrderDetailsHelper(DateUtility dateUtility, ConversionUtility conversionUtility) {
        this.dateUtility = dateUtility;
        this.conversionUtility = conversionUtility;
    }

    /**
     * 受注詳細系ページ項目設定<br/>
     *
     * @param abstractOrderDetailsModel 受注詳細抽象モデル
     * @param orderDetailsCommonModel   共通モデル
     */
    public void toPage(AbstractOrderDetailsModel abstractOrderDetailsModel,
                       OrderDetailsCommonModel orderDetailsCommonModel) {

        // 受注情報をセット
        setOrderInfo(abstractOrderDetailsModel, orderDetailsCommonModel.getOrderReceivedResponse(),
                     orderDetailsCommonModel.getOrderReceivedCountResponse(),
                     orderDetailsCommonModel.getPaymentMethodResponse()
                    );

        // お客様情報をセット
        setOrderCustomerInfo(abstractOrderDetailsModel, orderDetailsCommonModel.getCustomerResponse(),
                             orderDetailsCommonModel.getCustomerAddressResponse()
                            );

        // 受注配送情報、受注商品情報の値をセット
        setOrderDeliveryInfo(abstractOrderDetailsModel, orderDetailsCommonModel.getShippingSlipResponse(),
                             orderDetailsCommonModel.getShippingAddressResponse(),
                             orderDetailsCommonModel.getGoodsDtoList(),
                             orderDetailsCommonModel.getOrderSlipResponse(),
                             orderDetailsCommonModel.getExamKitListResponse(),
                             orderDetailsCommonModel.getSalesSlipResponse()
                            );

        // 検査結果をセット
        setExamResultsInfo(abstractOrderDetailsModel, orderDetailsCommonModel.getExamKitListResponse());

        // 環境設定情報を取得
        setConfigInfo(abstractOrderDetailsModel, orderDetailsCommonModel.getConfigInfoResponse());

        // 受注金額情報をセット
        setOrderAmountInfo(abstractOrderDetailsModel, orderDetailsCommonModel.getSalesSlipResponse(),
                           orderDetailsCommonModel.getCouponResponse()
                          );

        // 受注決済情報、受注請求情報、受注入金情報をセット
        setOrderPaymentInfo(abstractOrderDetailsModel, orderDetailsCommonModel.getBillingSlipResponse(),
                            orderDetailsCommonModel.getBillingAddressResponse(),
                            orderDetailsCommonModel.getPaymentMethodResponse(),
                            orderDetailsCommonModel.getOrderReceivedResponse(),
                            orderDetailsCommonModel.getMulPayBillResponse(),
                            orderDetailsCommonModel.getConvenienceListResponse()
                           );

        // 受注売上情報をセット
        setOrderSalesInfo(abstractOrderDetailsModel, orderDetailsCommonModel.getShippingSlipResponse());

        // マルチペイメント情報をセット
        setMulPayInfo(abstractOrderDetailsModel, orderDetailsCommonModel.getMulPayBillResponse());
    }

    /**
     * 受注情報をセット<br/>
     *
     * @param abstractOrderDetailsModel  受注詳細抽象モデル
     * @param orderReceivedResponse      受注レスポンス
     * @param orderReceivedCountResponse 顧客受注件数レスポンス
     * @param paymentMethodResponse      決済方法レスポンス
     */
    protected void setOrderInfo(AbstractOrderDetailsModel abstractOrderDetailsModel,
                                OrderReceivedResponse orderReceivedResponse,
                                OrderReceivedCountResponse orderReceivedCountResponse,
                                PaymentMethodResponse paymentMethodResponse) {
        // 受注レスポンス
        // ※本メソッド前にModelクリアが行われている前提
        // 　（下記ifに対するelseでの、値のnull設定処理は行っていない）
        if (ObjectUtils.isNotEmpty(orderReceivedResponse)) {
            abstractOrderDetailsModel.setOrderCode(orderReceivedResponse.getOrderCode());
            abstractOrderDetailsModel.setTransactionId(orderReceivedResponse.getLatestTransactionId());

            abstractOrderDetailsModel.setSettlementMailRequired(
                            Boolean.TRUE.equals(orderReceivedResponse.getNotificationFlag()) ?
                                            HTypeMailRequired.REQUIRED :
                                            HTypeMailRequired.NO_NEED);
            abstractOrderDetailsModel.setReminderSentFlag(
                            Boolean.TRUE.equals(orderReceivedResponse.getReminderSentFlag()) ?
                                            HTypeSend.SENT :
                                            HTypeSend.UNSENT);
            abstractOrderDetailsModel.setExpiredSentFlag(
                            Boolean.TRUE.equals(orderReceivedResponse.getExpiredSentFlag()) ?
                                            HTypeSend.SENT :
                                            HTypeSend.UNSENT);
            abstractOrderDetailsModel.setProcessTime(
                            this.conversionUtility.toTimestamp(orderReceivedResponse.getProcessTime()));
            abstractOrderDetailsModel.setOrderTime(
                            this.conversionUtility.toTimestamp(orderReceivedResponse.getOrderReceivedDate()));
            abstractOrderDetailsModel.setCancelFlag(CANCEL.equals(orderReceivedResponse.getOrderStatus()) ?
                                                                    HTypeCancelFlag.ON :
                                                                    HTypeCancelFlag.OFF);
            abstractOrderDetailsModel.setCancelTime(
                            this.conversionUtility.toTimestamp(orderReceivedResponse.getCancelDate()));
            abstractOrderDetailsModel.setNoveltyPresentJudgmentStatus(
                            EnumTypeUtil.getEnumFromValue(HTypeNoveltyPresentJudgmentStatus.class,
                                                          orderReceivedResponse.getNoveltyPresentJudgmentStatus()
                                                         ));

            // 各種受注ステータスのセット
            if (ITEM_PREPARING.equals(orderReceivedResponse.getOrderStatus())) {
                // 商品準備中
                abstractOrderDetailsModel.setOrderStatus(HTypeOrderStatus.GOODS_PREPARING);
            } else if (PAYMENT_CONFIRMING.equals(orderReceivedResponse.getOrderStatus())) {
                // 入金確認中
                abstractOrderDetailsModel.setOrderStatus(HTypeOrderStatus.PAYMENT_CONFIRMING);
            } else if (SHIPMENT_COMPLETION.equals(orderReceivedResponse.getOrderStatus())) {
                // 出荷完了
                abstractOrderDetailsModel.setOrderStatus(HTypeOrderStatus.SHIPMENT_COMPLETION);
            }

            if (orderReceivedResponse.getPaymentStatusDetail() != null) {
                abstractOrderDetailsModel.setPaymentStatus(
                                HTypePaymentStatus.valueOf(orderReceivedResponse.getPaymentStatusDetail()).getValue());
            }

            // 受注レスポンス＋決済方法レスポンス
            if (ObjectUtils.isNotEmpty(paymentMethodResponse)) {
                // 請求ステータス
                abstractOrderDetailsModel.setBillStatus(
                                HTypeBillType.PRE_CLAIM.getValue().equals(paymentMethodResponse.getBillType())
                                || Boolean.TRUE.equals(orderReceivedResponse.getShippedFlag()) ?
                                                HTypeBillStatus.BILL_CLAIM :
                                                HTypeBillStatus.BILL_NO_CLAIM);
            }

            // 請求決済エラーのチェック
            abstractOrderDetailsModel.setEmergencyFlag(PAYMENT_ERROR.equals(orderReceivedResponse.getOrderStatus()) ?
                                                                       HTypeEmergencyFlag.ON :
                                                                       HTypeEmergencyFlag.OFF);

            // 受注メモの値をセット
            abstractOrderDetailsModel.setMemo(orderReceivedResponse.getAdminMemo());

        }

        // キャリア種別(常にPCを指定)
        abstractOrderDetailsModel.setCarrierType(HTypeCarrierType.PC);

        // 受注件数レスポンス
        if (ObjectUtils.isNotEmpty(orderReceivedCountResponse)) {
            // リピート購入種別
            abstractOrderDetailsModel.setRepeatPurchaseType(orderReceivedCountResponse.getOrderReceivedCount() == 1 ?
                                                                            HTypeRepeatPurchaseType.MEMBER_FIRST :
                                                                            HTypeRepeatPurchaseType.MEMBER_REPEATER);
        }
    }

    /**
     * お客様情報をセット
     *
     * @param abstractOrderDetailsModel 受注詳細抽象モデル
     * @param customerResponse          顧客レスポンス
     * @param customerAddressResponse   顧客住所レスポンス
     */
    protected void setOrderCustomerInfo(AbstractOrderDetailsModel abstractOrderDetailsModel,
                                        CustomerResponse customerResponse,
                                        AddressBookAddressResponse customerAddressResponse) {
        // 顧客レスポンス
        // ※本メソッド前にModelクリアが行われている前提
        // 　（下記ifに対するelseでの、値のnull設定処理は行っていない）
        if (ObjectUtils.isNotEmpty(customerResponse)) {
            abstractOrderDetailsModel.setMemberInfoSeq(customerResponse.getMemberInfoSeq());
            abstractOrderDetailsModel.setOrderLastName(customerResponse.getMemberInfoLastName());
            abstractOrderDetailsModel.setOrderFirstName(customerResponse.getMemberInfoFirstName());
            abstractOrderDetailsModel.setOrderLastKana(customerResponse.getMemberInfoLastKana());
            abstractOrderDetailsModel.setOrderFirstKana(customerResponse.getMemberInfoFirstKana());
            abstractOrderDetailsModel.setOrderTel(customerResponse.getMemberInfoTel());
            abstractOrderDetailsModel.setOrderMail(customerResponse.getMemberInfoMail());
            abstractOrderDetailsModel.setOrderAgeType(HTypeOrderAgeType.getType(
                            this.conversionUtility.toTimestamp(customerResponse.getMemberInfoBirthday())));
            abstractOrderDetailsModel.setOrderBirthday(
                            this.conversionUtility.toTimestamp(customerResponse.getMemberInfoBirthday()));
            abstractOrderDetailsModel.setOrderSex(
                            EnumTypeUtil.getEnumFromValue(HTypeOrderSex.class, customerResponse.getMemberInfoSex()));
        }

        // 顧客住所レスポンス
        if (ObjectUtils.isNotEmpty(customerAddressResponse)) {
            abstractOrderDetailsModel.setOrderZipCode(customerAddressResponse.getZipCode());
            abstractOrderDetailsModel.setOrderPrefecture(customerAddressResponse.getPrefecture());
            abstractOrderDetailsModel.setPrefectureType(EnumTypeUtil.getEnumFromLabel(HTypePrefectureType.class,
                                                                                      customerAddressResponse.getPrefecture()
                                                                                     ));
            abstractOrderDetailsModel.setOrderAddress1(customerAddressResponse.getAddress1());
            abstractOrderDetailsModel.setOrderAddress2(customerAddressResponse.getAddress2());
            abstractOrderDetailsModel.setOrderAddress3(customerAddressResponse.getAddress3());
        }
    }

    /**
     * 受注配送情報リストをセット<br/>
     * 配送情報内の商品情報リストをセット<br/>
     *
     * @param abstractOrderDetailsModel 受注詳細抽象モデル
     * @param shippingSlipResponse      配送伝票レスポンス
     * @param shippingAddressResponse   お届け先住所レスポンス
     * @param goodsDtoList              商品詳細Dtoリスト
     * @param orderSlipResponse         注文票レスポンス
     * @param examKitListResponse       検査キットリストレスポンス
     * @param salesSlipResponse         販売伝票レスポンス
     */
    protected void setOrderDeliveryInfo(AbstractOrderDetailsModel abstractOrderDetailsModel,
                                        ShippingSlipResponse shippingSlipResponse,
                                        AddressBookAddressResponse shippingAddressResponse,
                                        List<GoodsDetailsDto> goodsDtoList,
                                        OrderSlipResponse orderSlipResponse,
                                        ExamKitListResponse examKitListResponse,
                                        SalesSlipResponse salesSlipResponse) {

        if (ObjectUtils.isNotEmpty(shippingSlipResponse) && ObjectUtils.isNotEmpty(orderSlipResponse)) {
            // お届け先情報をセット
            BigDecimal orderGoodsCountTotal = BigDecimal.ZERO;

            // 納品書 全配送共通の為、itemでは保持しない
            abstractOrderDetailsModel.setInvoiceAttachmentFlag(
                            Boolean.TRUE.equals(shippingSlipResponse.getInvoiceNecessaryFlag()) ?
                                            HTypeInvoiceAttachmentFlag.ON :
                                            HTypeInvoiceAttachmentFlag.OFF);
            OrderReceiverUpdateItem receiverItem = toOrderReceiverItem(shippingSlipResponse, shippingAddressResponse);

            // 商品情報リストをセット
            List<OrderSlipResponseItemList> orderItemList = orderSlipResponse.getItemList();
            List<OrderGoodsUpdateItem> orderGoodsItems = new ArrayList<>();

            if (CollectionUtil.isNotEmpty(orderItemList)) {
                for (OrderSlipResponseItemList item : orderItemList) {
                    if (ObjectUtils.isNotEmpty(salesSlipResponse) && CollectionUtil.isNotEmpty(
                                    salesSlipResponse.getItemPriceSubTotalList()) && CollectionUtil.isNotEmpty(
                                    goodsDtoList)) {
                        ExamKitResponse examKitResponse = null;

                        if (examKitListResponse != null && !CollectionUtils.isEmpty(examKitListResponse.getExamKitList())) {
                            examKitResponse = examKitListResponse.getExamKitList().stream().filter(examKit ->
                                    Objects.equals(examKit.getOrderItemId(), item.getOrderItemId())).findFirst().orElse(null);
                        }

                        ItemPriceSubTotal salesItemPrice = salesSlipResponse.getItemPriceSubTotalList()
                                                                            .stream()
                                                                            .filter(itemPrice -> itemPrice.getSalesItemSeq()
                                                                                                 != null
                                                                                                 && itemPrice.getSalesItemSeq()
                                                                                                             .equals(item.getOrderItemSeq()))
                                                                            .findFirst()
                                                                            .orElse(null);

                        GoodsDetailsDto goodsDetailsDto = goodsDtoList.stream()
                                                                      .filter(goodsDto -> goodsDto.getGoodsSeq() != null
                                                                                          && goodsDto.getGoodsSeq()
                                                                                                     .toString()
                                                                                                     .equals(item.getItemId()))
                                                                      .findFirst()
                                                                      .orElse(null);

                        OrderGoodsUpdateItem goodsItem =
                                        toOrderGoodsUpdateItem(item, Objects.requireNonNull(salesItemPrice),
                                                               examKitResponse,
                                                               Objects.requireNonNull(goodsDetailsDto)
                                                              );
                        orderGoodsItems.add(goodsItem);
                        if (item.getItemCount() != null) {
                            orderGoodsCountTotal = orderGoodsCountTotal.add(new BigDecimal(item.getItemCount()));
                        }
                    }
                }
            }

            // 配送アイテムに商品アイテムリストをセット
            receiverItem.setOrderGoodsUpdateItems(orderGoodsItems);
            abstractOrderDetailsModel.setOrderReceiverItem(receiverItem);
            abstractOrderDetailsModel.setOrderGoodsCountTotal(orderGoodsCountTotal);
        }
    }

    /**
     * 検査キットアイテムをModelに設定する<br/>
     *
     * @param examKitListResponse       検査キットリストレスポンス
     * @param abstractOrderDetailsModel 受注詳細抽象モデル
     */
    protected void setExamResultsInfo(AbstractOrderDetailsModel abstractOrderDetailsModel,
                                      ExamKitListResponse examKitListResponse) {
        if (examKitListResponse == null || CollectionUtils.isEmpty(examKitListResponse.getExamKitList())) {
            return;
        }

        List<OrderExamKitItem> orderExamKitItemList = new ArrayList<>();

        for (ExamKitResponse examKit : examKitListResponse.getExamKitList()) {

            if (HTypeExamStatus.WAITING_RETURN.getValue().equals(examKit.getExamStatus())
                    || HTypeExamStatus.RECEIVED.getValue().equals(examKit.getExamStatus())
                    || CollectionUtils.isEmpty(examKit.getExamResultList())) {
                continue;
            }

            OrderExamKitItem orderExamKitItem = ApplicationContextUtility.getBean(OrderExamKitItem.class);

            orderExamKitItem.setExamKitCode(examKit.getExamKitCode());
            orderExamKitItem.setOrderCode(examKit.getOrderCode());
            orderExamKitItem.setExamStatus(EnumTypeUtil.getEnumFromValue(HTypeExamStatus.class, examKit.getExamStatus()));
            orderExamKitItem.setSpecimenCode(examKit.getSpecimenCode());
            orderExamKitItem.setSpecimenComment(examKit.getSpecimenComment());
            orderExamKitItem.setOrderItemId(examKit.getOrderItemId());
            orderExamKitItem.setExamResultsPdf(examKit.getExamResultsPdf());
            orderExamKitItem.setReceptionDate(conversionUtility.toYmd(examKit.getReceptionDate()));
            orderExamKitItem.setExamResultList(this.createExamResultList(examKit.getExamResultList()));

            orderExamKitItemList.add(orderExamKitItem);

        }

        if (!CollectionUtils.isEmpty(orderExamKitItemList)) {
            // 検査結果を表示する
            abstractOrderDetailsModel.setExamResultsListNotEmpty(true);

            // 検査キット番号の順番は、購入商品情報一覧の表示順と同じとする
            List<String> examKitCodeOrderedList = abstractOrderDetailsModel.getOrderReceiverItem()
                                                                           .getOrderGoodsUpdateItems()
                                                                           .stream()
                                                                           .map(OrderGoodsUpdateItem::getExamKitCode)
                                                                           .collect(Collectors.toList());
            orderExamKitItemList.sort(
                            Comparator.comparing(item -> examKitCodeOrderedList.indexOf(item.getExamKitCode())));
        }

        abstractOrderDetailsModel.setOrderExamKitItemList(orderExamKitItemList);
    }

    /**
     * 注文履歴詳細検査結果ModelItemリストを作成する
     *
     * @param examResultList 検査結果リスト
     * @return 注文履歴詳細検査結果ModelItemリスト
     */
    private List<OrderExamResultItem> createExamResultList(List<ExamResultsResponse> examResultList) {
        if (CollectionUtils.isEmpty(examResultList)) {
            return null;
        }

        List<OrderExamResultItem> examResultItemList = new ArrayList<>();

        examResultList.forEach(examResult -> {
            OrderExamResultItem resultItem = ApplicationContextUtility.getBean(OrderExamResultItem.class);

            resultItem.setExamItemCode(examResult.getExamItemCode());
            resultItem.setExamItemName(examResult.getExamItemName());
            resultItem.setExamResultValue(examResult.getExamResultValue());
            resultItem.setUnit(examResult.getUnit());
            resultItem.setStandardValue(examResult.getStandardvalue());
            resultItem.setAbnormalValueType(EnumTypeUtil.getEnumFromValue(HTypeAbnormalValueType.class,
                                                                          examResult.getAbnormalValueType()));
            resultItem.setComment1(examResult.getComment1());
            resultItem.setComment2(examResult.getComment2());
            resultItem.setExamCompletedDate(conversionUtility.toYmd(examResult.getExamCompletedDate()));
            resultItem.setExamCompletedFlag(EnumTypeUtil.getEnumFromValue(HTypeExamCompletedFlag.class,
                                                                          examResult.getExamcompletedflag()));

            examResultItemList.add(resultItem);
        });

        return examResultItemList;
    }

    /**
     * 環境設定情報取得<br/>
     *
     * @param abstractOrderDetailsModel 受注詳細抽象モデル
     * @param configInfoResponse        環境設定情報レスポンス
     */
    protected void setConfigInfo(AbstractOrderDetailsModel abstractOrderDetailsModel,
                                 ConfigInfoResponse configInfoResponse) {

        if (configInfoResponse != null) {
            abstractOrderDetailsModel.setExamresultsPdfStoragePath(configInfoResponse.getExamresultsPdfStoragePath());
            abstractOrderDetailsModel.setExaminationRulePdfPath(configInfoResponse.getExaminationRulePdfPath());
        }
    }

    /**
     * お届け先表示情報Itemに変換
     *
     * @param shippingSlipResponse    配送伝票レスポンス
     * @param shippingAddressResponse お届け先住所レスポンス
     * @return お届け先アイテム
     */
    protected OrderReceiverUpdateItem toOrderReceiverItem(ShippingSlipResponse shippingSlipResponse,
                                                          AddressBookAddressResponse shippingAddressResponse) {

        OrderReceiverUpdateItem receiverItem = ApplicationContextUtility.getBean(OrderReceiverUpdateItem.class);

        // お届け先住所から取得
        // ※本メソッド前にModelクリアが行われている前提
        // 　（下記ifに対するelseでの、値のnull設定処理は行っていない）
        if (ObjectUtils.isNotEmpty(shippingAddressResponse)) {
            receiverItem.setReceiverFirstName(shippingAddressResponse.getFirstName());
            receiverItem.setReceiverLastName(shippingAddressResponse.getLastName());
            receiverItem.setReceiverFirstKana(shippingAddressResponse.getFirstKana());
            receiverItem.setReceiverLastKana(shippingAddressResponse.getLastKana());
            receiverItem.setReceiverTel(shippingAddressResponse.getTel());
            receiverItem.setReceiverZipCode(shippingAddressResponse.getZipCode());
            receiverItem.setReceiverPrefecture(shippingAddressResponse.getPrefecture());
            receiverItem.setReceiverAddress1(shippingAddressResponse.getAddress1());
            receiverItem.setReceiverAddress2(shippingAddressResponse.getAddress2());
            receiverItem.setReceiverAddress3(shippingAddressResponse.getAddress3());
            receiverItem.setDeliveryNote(shippingAddressResponse.getShippingMemo());
        }

        // 配送伝票から取得
        if (ObjectUtils.isNotEmpty(shippingSlipResponse)) {
            receiverItem.setShipmentStatus(shippingSlipResponse.getCompleteShipmentDate() != null ?
                                                           HTypeShipmentStatus.SHIPPED :
                                                           HTypeShipmentStatus.UNSHIPMENT);
            receiverItem.setShipmentDate(
                            this.conversionUtility.toTimestamp(shippingSlipResponse.getCompleteShipmentDate()));
            receiverItem.setDeliveryCode(shippingSlipResponse.getShipmentStatusConfirmCode());
            if (ObjectUtils.isNotEmpty(shippingSlipResponse.getReceiverDate())) {
                receiverItem.setReceiverDateDesignationFlag(HTypeReceiverDateDesignationFlag.ON);
                receiverItem.setReceiverDate(
                                this.conversionUtility.toTimestamp(shippingSlipResponse.getReceiverDate()));
            }
            receiverItem.setReceiverTimeZone(StringUtils.isEmpty(shippingSlipResponse.getReceiverTimeZone()) ?
                                                             "指定なし" :
                                                             shippingSlipResponse.getReceiverTimeZone());
            receiverItem.setDeliveryMethodSeq(
                            this.conversionUtility.toInteger(shippingSlipResponse.getShippingMethodId()));
            receiverItem.setDeliveryMethodName(shippingSlipResponse.getShippingMethodName());
        }
        return receiverItem;
    }

    /**
     * 受注詳細商品アイテムクラスに変換
     *
     * @param item            注文商品
     * @param itemPrice       商品金額(販売伝票から取得したもの)
     * @param examKitResponse 検査キットレスポンス
     * @param goodsDetailsDto 商品詳細Dto(注文票から取得したもの)
     * @return 商品アイテム
     */
    protected OrderGoodsUpdateItem toOrderGoodsUpdateItem(OrderSlipResponseItemList item,
                                                          ItemPriceSubTotal itemPrice,
                                                          ExamKitResponse examKitResponse,
                                                          GoodsDetailsDto goodsDetailsDto) {

        OrderGoodsUpdateItem goodsItem = ApplicationContextUtility.getBean(OrderGoodsUpdateItem.class);

        // 伝票が保持する商品マスタ情報をセット
        if (ObjectUtils.isNotEmpty(item)) {

            goodsItem.setOrderDisplay(item.getOrderItemSeq());
            goodsItem.setGoodsGroupName(item.getItemName());
            goodsItem.setUnitValue1(item.getUnitValue1());
            goodsItem.setUnitValue2(item.getUnitValue2());
            goodsItem.setJanCode(item.getJanCode());
            goodsItem.setGoodsCount(new BigDecimal(item.getItemCount()));

            // 商品金額
            if (ObjectUtils.isNotEmpty(itemPrice)) {
                goodsItem.setGoodsPrice(new BigDecimal(itemPrice.getItemUnitPrice()));
                goodsItem.setGoodsPriceInTax(BigDecimal.valueOf(itemPrice.getItemUnitPrice()));
                if (itemPrice.getItemPriceSubTotal() != null) {
                    goodsItem.setPostTaxOrderGoodsPrice(BigDecimal.valueOf(itemPrice.getItemPriceSubTotal()));
                }
                goodsItem.setTaxRate(itemPrice.getItemTaxRate());
            }

            // 商品詳細Dto
            if (ObjectUtils.isNotEmpty(goodsDetailsDto)) {

                // TODO 下記2つのフラグは、マスタではなく、伝票から取得されるべきか確認が必要
                goodsItem.setFreeDeliveryFlag(goodsDetailsDto.getFreeDeliveryFlag());
                goodsItem.setIndividualDeliveryType(goodsDetailsDto.getIndividualDeliveryType());
                // 商品マスタから情報をセット
                goodsItem.setGoodsGroupCode(goodsDetailsDto.getGoodsGroupCode());
                goodsItem.setGoodsSeq(goodsDetailsDto.getGoodsSeq());
                goodsItem.setGoodsCode(goodsDetailsDto.getGoodsCode());
            }

            // 検査キットレスポンスから情報をセット
            if (ObjectUtils.isNotEmpty(examKitResponse)) {
                goodsItem.setExamKitCode(examKitResponse.getExamKitCode());
                goodsItem.setExamStatus(EnumTypeUtil.getEnumFromValue(HTypeExamStatus.class, examKitResponse.getExamStatus()));
                goodsItem.setExamResultsPdf(examKitResponse.getExamResultsPdf());
            }
        }

        return goodsItem;
    }

    /**
     * 金額情報をセット
     *
     * @param abstractOrderDetailsModel 受注詳細抽象モデル
     * @param salesSlipResponse         販売伝票レスポンス
     * @param couponResponse            クーポンレスポンス
     */
    protected void setOrderAmountInfo(AbstractOrderDetailsModel abstractOrderDetailsModel,
                                      SalesSlipResponse salesSlipResponse,
                                      CouponResponse couponResponse) {
        // 販売伝票レスポンス
        if (ObjectUtils.isNotEmpty(salesSlipResponse)) {

            if (salesSlipResponse.getBillingAmount() != null) {
                abstractOrderDetailsModel.setOrderPrice(new BigDecimal(salesSlipResponse.getBillingAmount()));
            }
            if (salesSlipResponse.getItemSalesPriceTotal() != null) {
                abstractOrderDetailsModel.setGoodsPriceTotal(
                                new BigDecimal(salesSlipResponse.getItemSalesPriceTotal()));
            }
            if (salesSlipResponse.getCarriage() != null) {
                abstractOrderDetailsModel.setCarriage(new BigDecimal(salesSlipResponse.getCarriage()));
            }
            if (salesSlipResponse.getCommission() != null) {
                abstractOrderDetailsModel.setSettlementCommission(new BigDecimal(salesSlipResponse.getCommission()));
            }
            abstractOrderDetailsModel.setTaxPrice(new BigDecimal((salesSlipResponse.getStandardTax() != null ?
                            salesSlipResponse.getStandardTax() :
                            0) + (salesSlipResponse.getReducedTax() != null ? salesSlipResponse.getReducedTax() : 0)));
            if (salesSlipResponse.getStandardTax() != null) {
                abstractOrderDetailsModel.setStandardTaxPrice(new BigDecimal(salesSlipResponse.getStandardTax()));
            }
            if (salesSlipResponse.getReducedTax() != null) {
                abstractOrderDetailsModel.setReducedTaxPrice(new BigDecimal(salesSlipResponse.getReducedTax()));
            }
            if (salesSlipResponse.getStandardTaxTargetPrice() != null) {
                abstractOrderDetailsModel.setStandardTaxTargetPrice(
                                new BigDecimal(salesSlipResponse.getStandardTaxTargetPrice()));
            }
            if (salesSlipResponse.getReducedTaxTargetPrice() != null) {
                abstractOrderDetailsModel.setReducedTaxTargetPrice(
                                new BigDecimal(salesSlipResponse.getReducedTaxTargetPrice()));
            }
            if (salesSlipResponse.getItemSalesPriceTotal() != null) {
                abstractOrderDetailsModel.setPostTaxGoodsPriceTotal(
                                new BigDecimal(salesSlipResponse.getItemSalesPriceTotal()));
            }

            // クーポンレスポンス
            if (ObjectUtils.isNotEmpty(couponResponse)) {
                // クーポンが適用されている場合のみクーポン情報を画面に反映する
                // クーポン名
                abstractOrderDetailsModel.setCouponName(salesSlipResponse.getCouponName());
                // 適用クーポン名
                abstractOrderDetailsModel.setApplyCouponName(salesSlipResponse.getCouponName());
                // 適用クーポンID
                abstractOrderDetailsModel.setApplyCouponId(couponResponse.getCouponId());
                // クーポン利用フラグ
                abstractOrderDetailsModel.setCouponLimitTargetType(
                                Boolean.TRUE.equals(salesSlipResponse.getCouponUseFlag()) ?
                                                HTypeCouponLimitTargetType.ON :
                                                HTypeCouponLimitTargetType.OFF);
                // クーポン割引額 画面上マイナス表示
                abstractOrderDetailsModel.setCouponDiscountPrice(
                                Boolean.TRUE.equals(salesSlipResponse.getCouponUseFlag()) ?
                                                new BigDecimal(salesSlipResponse.getCouponPaymentPrice()).negate() :
                                                BigDecimal.ZERO);
                // クーポン対象商品適用
                setCouponTargetGoods(abstractOrderDetailsModel, couponResponse);
            } else {
                // クーポンが適用されていない場合はクーポン情報を初期化する
                // クーポン割引額 割引額を0円として処理する
                abstractOrderDetailsModel.setCouponDiscountPrice(BigDecimal.ZERO);
                // 適用クーポン名
                abstractOrderDetailsModel.setApplyCouponName(null);
                // 適用クーポンID
                abstractOrderDetailsModel.setApplyCouponId(null);
            }

        }
        // 受注追加料金をセット
        setOrderAdditionalChargeDtoList(abstractOrderDetailsModel, salesSlipResponse);
    }

    /**
     * 商品にクーポン対象フラグ設定
     *
     * @param model 受注詳細抽象モデル
     * @param couponResponse クーポンレスポンス
     */
    protected void setCouponTargetGoods(AbstractOrderDetailsModel model, CouponResponse couponResponse) {

        // クーポンレスポンスがない場合は適用対象のクーポンが存在しないため、判定しない
        if (ObjectUtils.isEmpty(couponResponse)) {
            return;
        }

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
     * 追加料金情報リストをセット<br/>
     *
     * @param abstractOrderDetailsModel 受注詳細抽象モデル
     * @param salesSlipResponse         販売伝票レスポンス
     */
    protected void setOrderAdditionalChargeDtoList(AbstractOrderDetailsModel abstractOrderDetailsModel,
                                                   SalesSlipResponse salesSlipResponse) {
        List<OrderAdditionalChargeItem> orderAdditionalChargeItems = new ArrayList<>();
        // 販売伝票レスポンス
        if (ObjectUtils.isNotEmpty(salesSlipResponse)) {
            if (!CollectionUtils.isEmpty(salesSlipResponse.getAdjustmentAmountList())) {
                for (AdjustmentAmountResponse adjustmentAmount : salesSlipResponse.getAdjustmentAmountList()) {
                    OrderAdditionalChargeItem item = toOrderAdditionalChargeItem(adjustmentAmount);
                    orderAdditionalChargeItems.add(item);
                }
            }
        }
        abstractOrderDetailsModel.setOrderAdditionalChargeItems(orderAdditionalChargeItems);
    }

    /**
     * 受注追加料金アイテムクラスに変換
     *
     * @param adjustmentAmount 調整金額
     * @return 追加料金アイテム
     */
    protected OrderAdditionalChargeItem toOrderAdditionalChargeItem(AdjustmentAmountResponse adjustmentAmount) {
        OrderAdditionalChargeItem item = ApplicationContextUtility.getBean(OrderAdditionalChargeItem.class);

        // 調整金額レスポンス
        if (ObjectUtils.isNotEmpty(adjustmentAmount)) {
            item.setAdditionalDetailsName(adjustmentAmount.getAdjustName());
            item.setAdditionalDetailsPrice(new BigDecimal(adjustmentAmount.getAdjustPrice()));
        }

        return item;
    }

    /**
     * 決済情報、請求情報、入金情報をセット
     *
     * @param abstractOrderDetailsModel 受注詳細抽象モデル
     * @param billingSlipResponse       請求伝票レスポンス
     * @param billingAddressResponse    請求先住所レスポンス
     * @param paymentMethodResponse     決済方法レスポンス
     * @param orderReceivedResponse     受注レスポンス
     * @param mulPayBillResponse        マルチペイメント請求レスポンス
     * @param convenienceListResponse   コンビニ一覧レスポンス
     */
    protected void setOrderPaymentInfo(AbstractOrderDetailsModel abstractOrderDetailsModel,
                                       BillingSlipResponse billingSlipResponse,
                                       AddressBookAddressResponse billingAddressResponse,
                                       PaymentMethodResponse paymentMethodResponse,
                                       OrderReceivedResponse orderReceivedResponse,
                                       MulPayBillResponse mulPayBillResponse,
                                       ConvenienceListResponse convenienceListResponse) {
        // 請求伝票レスポンス + 決済方法レスポンス
        if (ObjectUtils.isNotEmpty(billingSlipResponse) && ObjectUtils.isNotEmpty(paymentMethodResponse)) {
            // 決済情報をセット
            abstractOrderDetailsModel.setSettlementMethodSeq(
                            this.conversionUtility.toInteger(billingSlipResponse.getPaymentMethodId()));
            abstractOrderDetailsModel.setSettlementMethodName(billingSlipResponse.getPaymentMethodName());

            // クレジット決済の場合
            if (HTypeSettlementMethodType.CREDIT.getValue().equals(paymentMethodResponse.getSettlementMethodType())) {
                abstractOrderDetailsModel.setSettlementMethodType(HTypeSettlementMethodType.CREDIT);
            }

            // リンク決済の場合
            if (HTypeSettlementMethodType.LINK_PAYMENT.getValue()
                                                      .equals(paymentMethodResponse.getSettlementMethodType())) {

                abstractOrderDetailsModel.setSettlementMethodType(HTypeSettlementMethodType.LINK_PAYMENT);

                if (ObjectUtils.isNotEmpty(billingSlipResponse.getPaymentLinkResponse())) {
                    PaymentLinkResponse paymentLinkResponse = billingSlipResponse.getPaymentLinkResponse();

                    abstractOrderDetailsModel.setPayTypeName(paymentLinkResponse.getPayTypeName());
                    abstractOrderDetailsModel.setBillTypeLink(EnumTypeUtil.getEnumFromValue(HTypePaymentLinkType.class,
                                                                                            paymentLinkResponse.getLinkPayType()
                                                                                           ));
                    abstractOrderDetailsModel.setPayMethod(paymentLinkResponse.getPaymethod());

                    if (HTypePaymentLinkType.LATERDATEPAYMENT.getValue().equals(paymentLinkResponse.getLinkPayType())) {
                        // 後日払いの場合
                        abstractOrderDetailsModel.setPaymentTimeLimitDate(
                                        conversionUtility.toTimestamp(paymentLinkResponse.getLaterDateLimit()));
                        abstractOrderDetailsModel.setCancelableDate(
                                        conversionUtility.toTimestamp(paymentLinkResponse.getCancelExpiredDate()));

                    } else if (HTypePaymentLinkType.IMMEDIATEPAYMENT.getValue()
                                                                    .equals(paymentLinkResponse.getLinkPayType())) {
                        // 即日払いの場合
                        abstractOrderDetailsModel.setCancelLimit(
                                        conversionUtility.toYmd(paymentLinkResponse.getCancelLimit()));
                    }

                    if ("3".equals(paymentLinkResponse.getPayType())) {
                        // 選択コンビニ名を設定
                        this.setConveniName(abstractOrderDetailsModel, convenienceListResponse, mulPayBillResponse);
                    }
                }

            }

            abstractOrderDetailsModel.setBillType(
                            HTypeBillType.PRE_CLAIM.getValue().equals(paymentMethodResponse.getBillType()) ?
                                            HTypeBillType.PRE_CLAIM :
                                            HTypeBillType.POST_CLAIM);

            // 受注レスポンス
            if (ObjectUtils.isNotEmpty(orderReceivedResponse)) {
                // オーソリ期限取得対象の場合
                if (judgeGetAuthoryLimit(billingSlipResponse.getCreditResponse(),
                                         orderReceivedResponse.getOrderStatus(), paymentMethodResponse
                                        )) {
                    // オーソリ期限日をセット
                    abstractOrderDetailsModel.setAuthoryLimitDate(this.conversionUtility.toTimestamp(
                                    billingSlipResponse.getCreditResponse().getAuthLimitDate()));
                }
            }
        }

        // 請求先住所レスポンス
        if (ObjectUtils.isNotEmpty(billingAddressResponse)) {
            // 請求情報をセット
            abstractOrderDetailsModel.setOrderBillingLastName(billingAddressResponse.getLastName());
            abstractOrderDetailsModel.setOrderBillingFirstName(billingAddressResponse.getFirstName());
            abstractOrderDetailsModel.setOrderBillingLastKana(billingAddressResponse.getLastKana());
            abstractOrderDetailsModel.setOrderBillingFirstKana(billingAddressResponse.getFirstKana());
            abstractOrderDetailsModel.setOrderBillingTel(billingAddressResponse.getTel());
            abstractOrderDetailsModel.setOrderBillingPrefecture(billingAddressResponse.getPrefecture());
            abstractOrderDetailsModel.setBillingPrefectureType(EnumTypeUtil.getEnumFromLabel(HTypePrefectureType.class,
                                                                                             billingAddressResponse.getPrefecture()
                                                                                            ));
            abstractOrderDetailsModel.setOrderBillingZipCode(billingAddressResponse.getZipCode());
            abstractOrderDetailsModel.setOrderBillingAddress1(billingAddressResponse.getAddress1());
            abstractOrderDetailsModel.setOrderBillingAddress2(billingAddressResponse.getAddress2());
            abstractOrderDetailsModel.setOrderBillingAddress3(billingAddressResponse.getAddress3());
        }

        // 決済代行連携解除
        abstractOrderDetailsModel.setGmoReleaseFlag(
                        Boolean.TRUE.equals(billingSlipResponse.getCreditResponse().getPaymentAgencyReleaseFlag()) ?
                                        HTypeGmoReleaseFlag.RELEASE :
                                        HTypeGmoReleaseFlag.NORMAL);

        // 入金日時
        if (billingSlipResponse.getMoneyReceiptTime() != null) {
            abstractOrderDetailsModel.setReceiptTime(
                            conversionUtility.toTimestamp(billingSlipResponse.getMoneyReceiptTime()));
        }
    }

    /**
     * 売上情報をセット
     *
     * @param abstractOrderDetailsModel 受注詳細抽象モデル
     * @param shippingSlipResponse      配送伝票レスポンス
     */
    protected void setOrderSalesInfo(AbstractOrderDetailsModel abstractOrderDetailsModel,
                                     ShippingSlipResponse shippingSlipResponse) {
        // 請求先住所レスポンス
        if (ObjectUtils.isNotEmpty(shippingSlipResponse)) {
            // 出荷完了 = 売上のため、出荷完了日時の有無で判断
            abstractOrderDetailsModel.setSalesFlag(shippingSlipResponse.getCompleteShipmentDate() != null ?
                                                                   HTypeSalesFlag.ON :
                                                                   HTypeSalesFlag.OFF);
            // 出荷完了日時 = 売上日時のため、出荷完了日時をセット
            abstractOrderDetailsModel.setSalesTime(
                            this.conversionUtility.toTimestamp(shippingSlipResponse.getCompleteShipmentDate()));
        }
    }

    /**
     * マルチペイメント情報をセット
     *
     * @param abstractOrderDetailsModel 受注詳細抽象モデル
     * @param mulPayBillResponse        マルチペイメント請求レスポンス
     */
    protected void setMulPayInfo(AbstractOrderDetailsModel abstractOrderDetailsModel,
                                 MulPayBillResponse mulPayBillResponse) {
        // マルチペイメント請求
        if (ObjectUtils.isNotEmpty(mulPayBillResponse)) {

            abstractOrderDetailsModel.setMulPayBillSeq(mulPayBillResponse.getMulPayBillSeq());
            abstractOrderDetailsModel.setPayType(mulPayBillResponse.getPayType());
            abstractOrderDetailsModel.setTranType(mulPayBillResponse.getTranType());
            abstractOrderDetailsModel.setOrderId(mulPayBillResponse.getOrderId());
            abstractOrderDetailsModel.setAccessId(mulPayBillResponse.getAccessId());
            abstractOrderDetailsModel.setAccessPass(mulPayBillResponse.getAccessPass());
            abstractOrderDetailsModel.setJobCd(mulPayBillResponse.getJobCd());
            abstractOrderDetailsModel.setMethod(mulPayBillResponse.getMethod());
            abstractOrderDetailsModel.setPayTimes(mulPayBillResponse.getPayTimes());
            abstractOrderDetailsModel.setSeqMode(mulPayBillResponse.getSeqMode());
            abstractOrderDetailsModel.setCardSeq(mulPayBillResponse.getCardSeq());
            abstractOrderDetailsModel.setAmount(mulPayBillResponse.getAmount());
            abstractOrderDetailsModel.setTax(mulPayBillResponse.getTax());
            abstractOrderDetailsModel.setTdFlag(mulPayBillResponse.getTdFlag());
            abstractOrderDetailsModel.setAcs(mulPayBillResponse.getMethod());
            abstractOrderDetailsModel.setForward(mulPayBillResponse.getForward());
            abstractOrderDetailsModel.setApprove(mulPayBillResponse.getApprove());
            abstractOrderDetailsModel.setTranId(mulPayBillResponse.getTranId());
            abstractOrderDetailsModel.setBankName(mulPayBillResponse.getBankName());
            abstractOrderDetailsModel.setBranchName(mulPayBillResponse.getBranchName());
            abstractOrderDetailsModel.setAccountType(this.getAccountTypeLabel(mulPayBillResponse.getAccountType()));
            abstractOrderDetailsModel.setAccountNumber(mulPayBillResponse.getAccountNumber());
            abstractOrderDetailsModel.setExprireDate(this.formatIntDate(mulPayBillResponse.getExprireDate()));
            abstractOrderDetailsModel.setBankName(mulPayBillResponse.getBankName());
            abstractOrderDetailsModel.setBranchName(mulPayBillResponse.getBranchName());
            abstractOrderDetailsModel.setAccountNumber(mulPayBillResponse.getAccountNumber());
            abstractOrderDetailsModel.setTranDate(mulPayBillResponse.getTranDate());
            abstractOrderDetailsModel.setConvenience(mulPayBillResponse.getConvenience());
            abstractOrderDetailsModel.setConfNo(mulPayBillResponse.getConfNo());
            abstractOrderDetailsModel.setReceiptNo(mulPayBillResponse.getReceiptNo());
            abstractOrderDetailsModel.setPaymentTerm(mulPayBillResponse.getPaymentTerm());
            abstractOrderDetailsModel.setCustId(mulPayBillResponse.getCustId());
            abstractOrderDetailsModel.setBkCode(mulPayBillResponse.getBkCode());
            abstractOrderDetailsModel.setEncryptReceiptNo(mulPayBillResponse.getEncryptReceiptNo());
            abstractOrderDetailsModel.setErrCode(mulPayBillResponse.getErrCode());
            abstractOrderDetailsModel.setErrInfo(mulPayBillResponse.getErrInfo());

        }
    }

    /**
     * 商品詳細Dtoリストに変換
     *
     * @param productDetailListResponse 商品詳細一覧レスポンス
     * @param orderSlipResponse         注文票レスポンス
     * @return 商品詳細Dtoリスト
     */
    public List<GoodsDetailsDto> toProductDetailList(ProductDetailListResponse productDetailListResponse,
                                                     OrderSlipResponse orderSlipResponse) {

        List<GoodsDetailsDto> goodsDetailDtoList = new ArrayList<>();

        // マルチペイメント請求
        if (ObjectUtils.isNotEmpty(productDetailListResponse)) {
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
                goodsDetailsDto.setSaleEndTimePC(
                                this.conversionUtility.toTimestamp(goodsDetailsResponse.getSaleEndTime()));
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
                goodsDetailsDto.setWhatsnewDate(
                                this.conversionUtility.toTimestamp(goodsDetailsResponse.getWhatsnewDate()));
                if (goodsDetailsResponse.getGoodsOpenStatus() != null) {
                    goodsDetailsDto.setGoodsOpenStatusPC(EnumTypeUtil.getEnumFromValue(HTypeOpenDeleteStatus.class,
                                                                                       goodsDetailsResponse.getGoodsOpenStatus()
                                                                                      ));
                }
                goodsDetailsDto.setOpenStartTimePC(
                                this.conversionUtility.toTimestamp(goodsDetailsResponse.getOpenStartTime()));
                goodsDetailsDto.setOpenEndTimePC(
                                this.conversionUtility.toTimestamp(goodsDetailsResponse.getOpenEndTime()));
                goodsDetailsDto.setGoodsGroupName(goodsDetailsResponse.getGoodsGroupName());
                goodsDetailsDto.setUnitTitle1(goodsDetailsResponse.getUnitTitle1());
                goodsDetailsDto.setUnitTitle2(goodsDetailsResponse.getUnitTitle2());
                if (CollectionUtil.isNotEmpty(goodsDetailsResponse.getGoodsGroupImageList())) {
                    goodsDetailsDto.setGoodsGroupImageEntityList(
                                    toGoodsGroupImageList(goodsDetailsResponse.getGoodsGroupImageList()));
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
                    goodsDetailsDto.setGoodsIconList(toGoodsGroupImage(goodsDetailsResponse.getGoodsIconList()));
                }

                // 注文票レスポンス
                if (ObjectUtils.isNotEmpty(orderSlipResponse) && CollectionUtil.isNotEmpty(
                                orderSlipResponse.getItemList())) {
                    for (int i = 0; i < orderSlipResponse.getItemList().size(); i++) {
                        if (ObjectUtils.isNotEmpty(orderSlipResponse.getItemList().get(i)) && StringUtils.isNotEmpty(
                                        orderSlipResponse.getItemList().get(i).getItemId())) {
                            if (orderSlipResponse.getItemList()
                                                 .get(i)
                                                 .getItemId()
                                                 .equals(conversionUtility.toString(goodsDetailsDto.getGoodsSeq()))) {
                                goodsDetailsDto.setOrderGoodsCount(conversionUtility.toBigDecimal(
                                                orderSlipResponse.getItemList().get(i).getItemCount()));
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
        }

        return goodsDetailDtoList;
    }

    /**
     * 商品グループ画像クラスリストに変換
     *
     * @param goodsGroupImageResponseList 商品グループ画像クラスリスト
     * @return 商品グループ画像クラスリスト
     */
    public List<GoodsGroupImageEntity> toGoodsGroupImageList(List<GoodsGroupImageResponse> goodsGroupImageResponseList) {

        List<GoodsGroupImageEntity> goodsGroupImageEntityList = new ArrayList<>();

        if (CollectionUtil.isNotEmpty(goodsGroupImageResponseList)) {
            goodsGroupImageResponseList.forEach(item -> {
                GoodsGroupImageEntity goodsGroupImageEntity = new GoodsGroupImageEntity();

                goodsGroupImageEntity.setGoodsGroupSeq(item.getGoodsGroupSeq());
                goodsGroupImageEntity.setImageTypeVersionNo(item.getImageTypeVersionNo());
                goodsGroupImageEntity.setImageFileName(item.getImageFileName());
                goodsGroupImageEntity.setRegistTime(this.conversionUtility.toTimestamp(item.getRegistTime()));
                goodsGroupImageEntity.setUpdateTime(this.conversionUtility.toTimestamp(item.getUpdateTime()));

                goodsGroupImageEntityList.add(goodsGroupImageEntity);
            });
        }
        return goodsGroupImageEntityList;
    }

    /**
     * アイコン詳細レスポンスに変換
     *
     * @param goodsInformationIconDetailsResponseList アイコン詳細レスポンスクラスリスト
     * @return アイコン詳細DTOクラス
     */
    public List<GoodsInformationIconDetailsDto> toGoodsGroupImage(List<GoodsInformationIconDetailsResponse> goodsInformationIconDetailsResponseList) {

        List<GoodsInformationIconDetailsDto> GoodsInformationIconDetailsDtoList = new ArrayList<>();

        if (CollectionUtil.isNotEmpty(goodsInformationIconDetailsResponseList)) {
            goodsInformationIconDetailsResponseList.forEach(item -> {

                GoodsInformationIconDetailsDto goodsInformationIconDetailsDto =
                                ApplicationContextUtility.getBean(GoodsInformationIconDetailsDto.class);
                goodsInformationIconDetailsDto.setIconSeq(item.getIconSeq());
                goodsInformationIconDetailsDto.setGoodsGroupSeq(item.getGoodsGroupSeq());
                goodsInformationIconDetailsDto.setIconName(item.getIconName());
                goodsInformationIconDetailsDto.setColorCode(item.getColorCode());
                goodsInformationIconDetailsDto.setOrderDisplay(item.getOrderDisplay());

                GoodsInformationIconDetailsDtoList.add(goodsInformationIconDetailsDto);
            });
        }

        return GoodsInformationIconDetailsDtoList;
    }

    /**
     * オーソリ期限日を取得するか否かを判定する<br />
     * <pre>
     * 以下条件に一致する場合取得対象
     * ①受注状態：キャンセル以外
     * ②出荷状態：未出荷
     * ③請求決済エラー：未発生
     * ④決済方法：クレジット
     * ⑤請求種別：後請求
     * ⑥決済代行連携フラグ：OFF
     * </pre>
     *
     * @param creditResponse        クレジットカード決済レスポンス
     * @param orderStatus           注文ステータス
     * @param paymentMethodResponse 決済方法レスポンス
     * @return true:取得する
     */
    private boolean judgeGetAuthoryLimit(CreditResponse creditResponse,
                                         String orderStatus,
                                         PaymentMethodResponse paymentMethodResponse) {

        if (ObjectUtils.isEmpty(creditResponse) || ObjectUtils.isEmpty(creditResponse.getAuthLimitDate())) {
            return false;
        }
        return !CANCEL.equals(orderStatus) && !SHIPMENT_COMPLETION.equals(orderStatus) && !PAYMENT_ERROR.equals(
                        orderStatus) && HTypeSettlementMethodType.CREDIT.getValue()
                                                                        .equals(paymentMethodResponse.getSettlementMethodType())
               && HTypeBillType.POST_CLAIM.getValue().equals(paymentMethodResponse.getBillType())
               && Boolean.FALSE.equals(creditResponse.getPaymentAgencyReleaseFlag());
    }

    /**
     * 受注商品の変更箇所の表示スタイル取得
     *
     * @param modelName
     * @param modifiedList
     * @param originList
     * @return 受注商品の変更箇所のリスト
     */
    public List<String> getOrderItemGoodsDiff(String modelName,
                                              List<OrderGoodsUpdateItem> modifiedList,
                                              List<OrderGoodsUpdateItem> originList) {
        List<String> diffList = new ArrayList<>();
        String goodsDiff = modelName + ".goodsCount_";
        int min = Math.min(modifiedList.size(), originList.size());
        int max = Math.max(modifiedList.size(), originList.size());
        for (int i = 0; i < min; i++) {
            List<String> subDiff = DiffUtil.diff(modifiedList.get(i), originList.get(i));
            if (!subDiff.isEmpty()) {
                diffList.add(goodsDiff + i);
            }
        }
        if (max - min != 0) {
            for (int i = min; i < max; i++) {
                diffList.add(goodsDiff + i);
            }
        }

        return diffList;
    }

    /**
     * 受注追加料金アイテムの変更箇所の表示スタイル取得
     *
     * @param modelName
     * @param modifiedList
     * @param originList
     * @return 受注商品の変更箇所のリスト
     */
    public List<String> getOrderAdditionalChargeDiff(String modelName,
                                                     List<OrderAdditionalChargeItem> modifiedList,
                                                     List<OrderAdditionalChargeItem> originList) {
        List<String> diffList = new ArrayList<>();
        String additionalCharge = modelName + ".additionalCharge_";
        int min = Math.min(modifiedList.size(), originList.size());
        int max = Math.max(modifiedList.size(), originList.size());
        for (int i = 0; i < min; i++) {
            List<String> subDiff = DiffUtil.diff(modifiedList.get(i), originList.get(i));
            if (!subDiff.isEmpty()) {
                diffList.add(additionalCharge + i);
            }
        }
        if (max - min != 0) {
            for (int i = min; i < max; i++) {
                diffList.add(additionalCharge + i);
            }
        }
        return diffList;
    }

    /**
     * 選択コンビニ名を設定<br/>
     *
     * @param model                   受注詳細モデル
     * @param convenienceListResponse コンビニ一覧
     * @param mulPayBillResponse      マルチペイメント請求エンティティ
     */
    protected void setConveniName(AbstractOrderDetailsModel model,
                                  ConvenienceListResponse convenienceListResponse,
                                  MulPayBillResponse mulPayBillResponse) {
        // 選択コンビニ名を設定
        String conveniName = "";

        if (CollectionUtil.isNotEmpty(convenienceListResponse.getConvenienceList())) {
            // コンビニエンティティリスト取得
            List<ConvenienceResponse> conveniList = convenienceListResponse.getConvenienceList();

            for (ConvenienceResponse convenienceEntity : conveniList) {
                if (StringUtils.isNotEmpty(convenienceEntity.getConveniCode()) && convenienceEntity.getConveniCode()
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
     * 振込先口座種別ラベル取得
     *
     * @param accountType 振込先口座種別
     * @return 振込先口座種別ラベル
     */
    protected String getAccountTypeLabel(String accountType) {
        return StringUtils.isNotEmpty(accountType) && "1".equals(accountType) ? ACCOUNTTYPE_LABEL : StringUtils.EMPTY;
    }

    /**
     * yyyy/MM/dd 文字列にフォーマット
     *
     * @param expireDate 取引有効期限
     * @return フォーマットされた文字列の日付
     */
    protected String formatIntDate(String expireDate) {
        if (!StringUtils.isEmpty(expireDate)) {
            // 日付関連Helper取得
            DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);
            return dateUtility.format(TimestampConversionUtil.toTimestamp(expireDate, "yyyyMMdd"), "yyyy/MM/dd");
        }

        return StringUtils.EMPTY;
    }
}
