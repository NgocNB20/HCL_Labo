/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.pc.web.front.member.history;

import jp.co.itechh.quad.addressbook.presentation.api.param.AddressBookAddressResponse;
import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipResponse;
import jp.co.itechh.quad.billingslip.presentation.api.param.PaymentLinkResponse;
import jp.co.itechh.quad.convenience.presentation.api.param.ConvenienceListResponse;
import jp.co.itechh.quad.convenience.presentation.api.param.ConvenienceResponse;
import jp.co.itechh.quad.coupon.presentation.api.param.CouponResponse;
import jp.co.itechh.quad.examination.presentation.api.param.ConfigInfoResponse;
import jp.co.itechh.quad.examination.presentation.api.param.ExamKitListResponse;
import jp.co.itechh.quad.examination.presentation.api.param.ExamKitRequest;
import jp.co.itechh.quad.examination.presentation.api.param.ExamKitResponse;
import jp.co.itechh.quad.examination.presentation.api.param.ExamResultsResponse;
import jp.co.itechh.quad.front.base.util.common.CollectionUtil;
import jp.co.itechh.quad.front.base.util.seasar.TimestampConversionUtil;
import jp.co.itechh.quad.front.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.front.base.utility.ConversionUtility;
import jp.co.itechh.quad.front.base.utility.DateUtility;
import jp.co.itechh.quad.front.constant.type.HTypeAbnormalValueType;
import jp.co.itechh.quad.front.constant.type.HTypeAlcoholFlag;
import jp.co.itechh.quad.front.constant.type.HTypeCancelFlag;
import jp.co.itechh.quad.front.constant.type.HTypeExamCompletedFlag;
import jp.co.itechh.quad.front.constant.type.HTypeExamStatus;
import jp.co.itechh.quad.front.constant.type.HTypeFreeDeliveryFlag;
import jp.co.itechh.quad.front.constant.type.HTypeGoodsSaleStatus;
import jp.co.itechh.quad.front.constant.type.HTypeGoodsTaxType;
import jp.co.itechh.quad.front.constant.type.HTypeIndividualDeliveryType;
import jp.co.itechh.quad.front.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.front.constant.type.HTypeOrderStatus;
import jp.co.itechh.quad.front.constant.type.HTypePaymentLinkType;
import jp.co.itechh.quad.front.constant.type.HTypePaymentType;
import jp.co.itechh.quad.front.constant.type.HTypeSettlementMethodType;
import jp.co.itechh.quad.front.constant.type.HTypeShipmentStatus;
import jp.co.itechh.quad.front.constant.type.HTypeSnsLinkFlag;
import jp.co.itechh.quad.front.constant.type.HTypeStockManagementFlag;
import jp.co.itechh.quad.front.constant.type.HTypeStockStatusType;
import jp.co.itechh.quad.front.constant.type.HTypeUnitManagementFlag;
import jp.co.itechh.quad.front.dto.goods.goods.GoodsDetailsDto;
import jp.co.itechh.quad.front.dto.icon.GoodsInformationIconDetailsDto;
import jp.co.itechh.quad.front.entity.goods.goodsgroup.GoodsGroupImageEntity;
import jp.co.itechh.quad.front.util.common.EnumTypeUtil;
import jp.co.itechh.quad.front.utility.OrderUtility;
import jp.co.itechh.quad.front.web.PageInfo;
import jp.co.itechh.quad.method.presentation.api.param.PaymentMethodResponse;
import jp.co.itechh.quad.method.presentation.api.param.ShippingMethodResponse;
import jp.co.itechh.quad.mulpay.presentation.api.param.MulPayBillResponse;
import jp.co.itechh.quad.orderreceived.presentation.api.param.CustomerHistory;
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
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static jp.co.itechh.quad.front.pc.web.front.member.history.MemberHistoryModel.CANCEL;
import static jp.co.itechh.quad.front.pc.web.front.member.history.MemberHistoryModel.ITEM_PREPARING;
import static jp.co.itechh.quad.front.pc.web.front.member.history.MemberHistoryModel.PAYMENT_CONFIRMING;
import static jp.co.itechh.quad.front.pc.web.front.member.history.MemberHistoryModel.PAYMENT_ERROR;
import static jp.co.itechh.quad.front.pc.web.front.member.history.MemberHistoryModel.SHIPMENT_COMPLETION;
import static jp.co.itechh.quad.front.pc.web.front.member.history.MemberHistoryModel.SHIPPING_CANCEL;
import static jp.co.itechh.quad.front.pc.web.front.member.history.MemberHistoryModel.SHIPPING_RETURN;

/**
 * 注文履歴 Helper
 *
 * @author kimura
 */
@Component
public class MemberHistoryHelper {

    /** 変換Utility */
    private final ConversionUtility conversionUtility;

    /** 受注関連Utility */
    private final OrderUtility orderUtility;

    /** 預金種別ラベル */
    private static final String ACCOUNTTYPE_LABEL = "普通預金";

    /** コンストラクタ */
    public MemberHistoryHelper(ConversionUtility conversionUtility, OrderUtility orderUtility) {
        this.conversionUtility = conversionUtility;
        this.orderUtility = orderUtility;
    }

    /**
     * 一覧画面表示用のModelに変換<br/>
     *
     * @param customerHistoryList 注文履歴一覧Dリスト
     * @param memberHistoryModel  Model
     */
    public void toPageForLoad(List<CustomerHistory> customerHistoryList, MemberHistoryModel memberHistoryModel) {

        // 一覧用アイテムリストの作成
        List<MemberHistoryModelItem> itemlist = new ArrayList<>();
        if (!customerHistoryList.isEmpty()) {
            for (CustomerHistory customerHistory : customerHistoryList) {
                itemlist.add(createOrderItem(customerHistory));
            }
        }
        memberHistoryModel.setOrderHistoryItems(itemlist);
    }

    /**
     * 注文Itemを作成する<br/>
     *
     * @param customerHistory 顧客注文履歴
     * @return MemberHistoryModelItem
     */
    protected MemberHistoryModelItem createOrderItem(CustomerHistory customerHistory) {

        MemberHistoryModelItem item = ApplicationContextUtility.getBean(MemberHistoryModelItem.class);

        item.setOrderTime(this.conversionUtility.toTimestamp(customerHistory.getOrderReceivedDate()));
        item.setOrderCode(customerHistory.getOrderCode());
        item.setOrderPrice(new BigDecimal(customerHistory.getPaymentPrice()));

        // 注文ステータス
        // 商品準備中
        if (ITEM_PREPARING.equals(customerHistory.getOrderStatus())) {
            item.setStatus(HTypeOrderStatus.GOODS_PREPARING.getLabel());
            item.setStatusValue(MemberHistoryModel.GOODS_PREPARING_STATUS);
        }
        // 入金確認中
        else if (PAYMENT_CONFIRMING.equals(customerHistory.getOrderStatus())) {
            item.setStatus(HTypeOrderStatus.PAYMENT_CONFIRMING.getLabel());
            item.setStatusValue(MemberHistoryModel.PAYMENT_CONFIRMING_STATUS);
        }
        // 出荷完了
        else if (SHIPMENT_COMPLETION.equals(customerHistory.getOrderStatus())) {
            item.setStatus(HTypeOrderStatus.SHIPMENT_COMPLETION.getLabel());
            item.setStatusValue(MemberHistoryModel.SHIPMENT_COMPLETION_STATUS);
        }
        // キャンセル
        else if (CANCEL.equals(customerHistory.getOrderStatus())) {
            item.setStatus(HTypeCancelFlag.ON.getLabel());
            item.setStatusValue(MemberHistoryModel.CANCEL_STATUS);
        }
        // 請求決済エラー
        else if (PAYMENT_ERROR.equals(customerHistory.getOrderStatus())) {
            // TODO 【ClickUP：2y8kg6c】暫定対応として、OrderReceivedResponse.OrderStatusが請求決済エラーの場合、出荷完了扱いとする
            item.setStatus(HTypeOrderStatus.SHIPMENT_COMPLETION.getLabel());
            item.setStatusValue(MemberHistoryModel.SHIPMENT_COMPLETION_STATUS);
        }

        return item;
    }

    /**
     * 詳細画面表示用のModelに変換<br/>
     *
     * @param orderReceivedResponse   受注レスポンス
     * @param shippingSlipResponse    配送伝票レスポンス
     * @param billingSlipResponse     請求伝票レスポンス
     * @param salesSlipResponse       販売伝票レスポンス
     * @param orderSlipResponse       注文票レスポンス
     * @param examKitListResponse     検査キットリストレスポンス
     * @param shippingAddressResponse お届け先住所レスポンス
     * @param billingAddressResponse  請求先住所レスポンス
     * @param paymentMethodResponse   決済方法レスポンス
     * @param shippingMethodResponse  配送方法レスポンス
     * @param mulPayBillResponse      マルチペイメント請求レスポンス
     * @param couponResponse          クーポン情報レスポンス
     * @param convenienceListResponse コンビニ一覧レスポンス
     * @param goodsDtoList            商品詳細Dtoリスト
     * @param configInfoResponse      環境設定情報レスポンス
     * @param memberHistoryModel      Model
     */
    public void toPageForLoadDetail(OrderReceivedResponse orderReceivedResponse,
                                    ShippingSlipResponse shippingSlipResponse,
                                    BillingSlipResponse billingSlipResponse,
                                    SalesSlipResponse salesSlipResponse,
                                    OrderSlipResponse orderSlipResponse,
                                    ExamKitListResponse examKitListResponse,
                                    AddressBookAddressResponse shippingAddressResponse,
                                    AddressBookAddressResponse billingAddressResponse,
                                    PaymentMethodResponse paymentMethodResponse,
                                    ShippingMethodResponse shippingMethodResponse,
                                    MulPayBillResponse mulPayBillResponse,
                                    ConvenienceListResponse convenienceListResponse,
                                    CouponResponse couponResponse,
                                    List<GoodsDetailsDto> goodsDtoList,
                                    ConfigInfoResponse configInfoResponse,
                                    MemberHistoryModel memberHistoryModel) {

        // 「注文履歴一覧へもどる」ボタン用情報設定
        if (memberHistoryModel.getPageInfo() == null) {
            PageInfo pageInfo = new PageInfo();
            pageInfo.setOrderAsc(false);
            pageInfo.setPnum(1);
            memberHistoryModel.setPageInfo(pageInfo);
        }

        // 受注状態
        setupOrder(orderReceivedResponse, memberHistoryModel);
        // お届け先情報と商品リスト
        setupDeliveryList(shippingSlipResponse, shippingAddressResponse, shippingMethodResponse, goodsDtoList,
                          orderSlipResponse, examKitListResponse, salesSlipResponse, memberHistoryModel
                         );

        // 検査キット情報
        setupExamResultList(examKitListResponse, memberHistoryModel);
        // 決済情報と請求情報
        setupSettlement(salesSlipResponse, billingSlipResponse, billingAddressResponse, paymentMethodResponse,
                        mulPayBillResponse, convenienceListResponse, memberHistoryModel
                       );
        // 追加料金
        setupExtraCharge(salesSlipResponse, memberHistoryModel);
        // クーポン
        setCoupon(salesSlipResponse, couponResponse, memberHistoryModel);

        memberHistoryModel.setExaminationRulePdfPath(configInfoResponse.getExaminationRulePdfPath());
        memberHistoryModel.setExamresultsPdfStoragePath(configInfoResponse.getExamresultsPdfStoragePath());
    }

    /**
     * 注文情報をModelに設定する
     *
     * @param orderReceivedResponse 受注レスポンス
     * @param memberHistoryModel    Model
     */
    protected void setupOrder(OrderReceivedResponse orderReceivedResponse, MemberHistoryModel memberHistoryModel) {

        // 注文ステータス
        // 商品準備中
        if (ITEM_PREPARING.equals(orderReceivedResponse.getOrderStatus())) {
            memberHistoryModel.setStatus(HTypeOrderStatus.GOODS_PREPARING.getLabel());
            memberHistoryModel.setStatusValue(MemberHistoryModel.GOODS_PREPARING_STATUS);
        }
        // 入金確認中
        else if (PAYMENT_CONFIRMING.equals(orderReceivedResponse.getOrderStatus())) {
            memberHistoryModel.setStatus(HTypeOrderStatus.PAYMENT_CONFIRMING.getLabel());
            memberHistoryModel.setStatusValue(MemberHistoryModel.PAYMENT_CONFIRMING_STATUS);
        }
        // 出荷完了
        else if (SHIPMENT_COMPLETION.equals(orderReceivedResponse.getOrderStatus())) {
            memberHistoryModel.setStatus(HTypeOrderStatus.SHIPMENT_COMPLETION.getLabel());
            memberHistoryModel.setStatusValue(MemberHistoryModel.SHIPMENT_COMPLETION_STATUS);
        }
        // キャンセル
        else if (CANCEL.equals(orderReceivedResponse.getOrderStatus())) {
            memberHistoryModel.setStatus(HTypeCancelFlag.ON.getLabel());
            memberHistoryModel.setStatusValue(MemberHistoryModel.CANCEL_STATUS);
        }
        // 請求決済エラー
        else if (PAYMENT_ERROR.equals(orderReceivedResponse.getOrderStatus())) {
            // TODO 【ClickUP：2y8kg6c】暫定対応として、OrderReceivedResponse.OrderStatusが請求決済エラーの場合、出荷完了扱いとする
            memberHistoryModel.setStatus(HTypeOrderStatus.SHIPMENT_COMPLETION.getLabel());
            memberHistoryModel.setStatusValue(MemberHistoryModel.SHIPMENT_COMPLETION_STATUS);
        }

        memberHistoryModel.setOrderCode(orderReceivedResponse.getOrderCode());
        memberHistoryModel.setOrderTime(
                        this.conversionUtility.toTimestamp(orderReceivedResponse.getOrderReceivedDate()));

        // 保持用
        memberHistoryModel.setOcd(orderReceivedResponse.getOrderCode());
        memberHistoryModel.setSaveOcd(orderReceivedResponse.getOrderCode());
    }

    /**
     * お届け先アイテムをModelに設定する<br/>
     *
     * @param shippingSlipResponse    配送伝票レスポンス
     * @param shippingAddressResponse お届け先住所レスポンス
     * @param shippingMethodResponse  配送方法レスポンス
     * @param goodsDtoList            商品詳細Dtoリスト
     * @param orderSlipResponse       注文票
     * @param examKitListResponse     検査キットリストレスポンス
     * @param salesSlipResponse       販売伝票
     * @param memberHistoryModel      Model
     */
    protected void setupDeliveryList(ShippingSlipResponse shippingSlipResponse,
                                     AddressBookAddressResponse shippingAddressResponse,
                                     ShippingMethodResponse shippingMethodResponse,
                                     List<GoodsDetailsDto> goodsDtoList,
                                     OrderSlipResponse orderSlipResponse,
                                     ExamKitListResponse examKitListResponse,
                                     SalesSlipResponse salesSlipResponse,
                                     MemberHistoryModel memberHistoryModel) {

        HistoryModelDeliveryItem HistoryModelDeliveryItem =
                        createDeliveryItem(shippingSlipResponse, shippingAddressResponse, shippingMethodResponse,
                                           goodsDtoList, orderSlipResponse, examKitListResponse, salesSlipResponse, memberHistoryModel
                                          );
        memberHistoryModel.setOrderDeliveryItem(HistoryModelDeliveryItem);
    }

    /**
     * 検査キットアイテムをModelに設定する<br/>
     *
     * @param examKitListResponse 検査キットリストレスポンス
     * @param memberHistoryModel  会員注文履歴Model
     */
    protected void setupExamResultList(ExamKitListResponse examKitListResponse, MemberHistoryModel memberHistoryModel) {

        if (examKitListResponse == null || CollectionUtils.isEmpty(examKitListResponse.getExamKitList())) {
            return;
        }

        List<HistoryModelExamKitItem> historyModelExamKitList = new ArrayList<>();

        for (ExamKitResponse examKit : examKitListResponse.getExamKitList()) {

            if (HTypeExamStatus.WAITING_RETURN.getValue().equals(examKit.getExamStatus())
                || HTypeExamStatus.RECEIVED.getValue().equals(examKit.getExamStatus())
                || CollectionUtils.isEmpty(examKit.getExamResultList())) {
                continue;
            }

            HistoryModelExamKitItem historyModelExamKitItem = ApplicationContextUtility.getBean(HistoryModelExamKitItem.class);

            historyModelExamKitItem.setExamKitCode(examKit.getExamKitCode());
            historyModelExamKitItem.setExamStatus(
                            EnumTypeUtil.getEnumFromValue(HTypeExamStatus.class, examKit.getExamStatus()));
            historyModelExamKitItem.setOrderCode(examKit.getOrderCode());
            historyModelExamKitItem.setSpecimenComment(examKit.getSpecimenComment());
            historyModelExamKitItem.setExamResultsPdf(examKit.getExamResultsPdf());
            historyModelExamKitItem.setSpecimenCode(examKit.getSpecimenCode());
            historyModelExamKitItem.setOrderItemId(examKit.getOrderItemId());
            historyModelExamKitItem.setReceptionDate(conversionUtility.toYmd(examKit.getReceptionDate()));
            historyModelExamKitItem.setExamResultList(this.createExamResultList(examKit.getExamResultList()));

            historyModelExamKitList.add(historyModelExamKitItem);
        }
        if (!CollectionUtils.isEmpty(historyModelExamKitList)) {
            // 調査結果を表示する
            memberHistoryModel.setExamResultsListNotEmpty(true);

            // 検査キット番号の順番は、購入商品情報一覧の表示順と同じとする
            List<String> examKitCodeOrderedList = memberHistoryModel.getOrderDeliveryItem()
                                                                           .getGoodsListItems()
                                                                           .stream()
                                                                           .map(HistoryModelGoodsItem::getExamKitCode)
                                                                           .collect(Collectors.toList());
            historyModelExamKitList.sort(
                            Comparator.comparing(item -> examKitCodeOrderedList.indexOf(item.getExamKitCode())));
        }

        memberHistoryModel.setExamKitItemList(historyModelExamKitList);
    }

    /**
     * 注文履歴詳細検査結果ModelItemリストを作成する
     *
     * @param examResultList 検査結果リスト
     * @return 注文履歴詳細検査結果ModelItemリスト
     */
    private List<HistoryModelExamResultItem> createExamResultList(List<ExamResultsResponse> examResultList) {
        if (CollectionUtils.isEmpty(examResultList)) {
            return null;
        }

        List<HistoryModelExamResultItem> examResultItemList = new ArrayList<>();

        examResultList.forEach(examResult -> {
            HistoryModelExamResultItem resultItem = ApplicationContextUtility.getBean(HistoryModelExamResultItem.class);

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
     * 配送Itemを作成する
     *
     * @param shippingSlipResponse    配送伝票レスポンス
     * @param shippingAddressResponse お届け先住所レスポンス
     * @param shippingMethodResponse  配送方法レスポンス
     * @param goodsDtoList            商品詳細Dtoリスト
     * @param orderSlipResponse       注文票
     * @param examKitListResponse     検査キットリストレスポンス
     * @param salesSlipResponse       販売伝票
     * @param memberHistoryModel      Model
     * @return DetailPageDeliveryItem
     */
    protected HistoryModelDeliveryItem createDeliveryItem(ShippingSlipResponse shippingSlipResponse,
                                                          AddressBookAddressResponse shippingAddressResponse,
                                                          ShippingMethodResponse shippingMethodResponse,
                                                          List<GoodsDetailsDto> goodsDtoList,
                                                          OrderSlipResponse orderSlipResponse,
                                                          ExamKitListResponse examKitListResponse,
                                                          SalesSlipResponse salesSlipResponse,
                                                          MemberHistoryModel memberHistoryModel) {

        HistoryModelDeliveryItem HistoryModelDeliveryItem =
                        ApplicationContextUtility.getBean(HistoryModelDeliveryItem.class);
        // お届け先情報
        setupReceiver(HistoryModelDeliveryItem, shippingAddressResponse);
        // 配送情報
        setupDelivery(HistoryModelDeliveryItem, shippingSlipResponse, shippingAddressResponse.getShippingMemo(),
                      shippingMethodResponse, memberHistoryModel
                     );
        // 商品
        HistoryModelDeliveryItem.setGoodsListItems(new ArrayList<>());

        if (CollectionUtil.isNotEmpty(orderSlipResponse.getItemList())) {
            for (OrderSlipResponseItemList orderItem : orderSlipResponse.getItemList()) {
                ExamKitResponse examKitResponse = null;

                if (examKitListResponse != null && !CollectionUtils.isEmpty(examKitListResponse.getExamKitList())) {
                     examKitResponse = examKitListResponse.getExamKitList().stream().filter(examKit ->
                            Objects.equals(examKit.getOrderItemId(), orderItem.getOrderItemId())).findFirst().orElse(null);
                }

                if (ObjectUtils.isNotEmpty(salesSlipResponse) && CollectionUtil.isNotEmpty(
                                salesSlipResponse.getItemPriceSubTotalList()) && CollectionUtil.isNotEmpty(
                                goodsDtoList)) {
                    ItemPriceSubTotal priceSubTotal = salesSlipResponse.getItemPriceSubTotalList()
                                                                       .stream()
                                                                       .filter(itemPriceSubTotal ->
                                                                                               itemPriceSubTotal.getSalesItemSeq()
                                                                                               != null
                                                                                               && itemPriceSubTotal.getSalesItemSeq()
                                                                                                                   .equals(orderItem.getOrderItemSeq()))
                                                                       .findFirst()
                                                                       .orElse(null);
                    GoodsDetailsDto targetGoods = goodsDtoList.stream()
                                                              .filter(goodsDto -> goodsDto.getGoodsSeq()
                                                                                          .toString()
                                                                                          .equals(orderItem.getItemId()))
                                                              .findFirst()
                                                              .orElse(null);


                    HistoryModelGoodsItem historyPageGoodsItem =
                                    createGoodsItem(orderItem, examKitResponse, Objects.requireNonNull(priceSubTotal),
                                                    Objects.requireNonNull(targetGoods)
                                                   );
                    HistoryModelDeliveryItem.getGoodsListItems().add(historyPageGoodsItem);
                }
            }
        }

        return HistoryModelDeliveryItem;
    }

    /**
     * お届け先情報をModelに設定する
     *
     * @param historyModelDeliveryItem ModelItem
     * @param shippingAddressResponse  お届け先住所レスポンス
     */
    protected void setupReceiver(HistoryModelDeliveryItem historyModelDeliveryItem,
                                 AddressBookAddressResponse shippingAddressResponse) {

        historyModelDeliveryItem.setReceiverTel(shippingAddressResponse.getTel());
        historyModelDeliveryItem.setReceiverFirstKana(shippingAddressResponse.getFirstKana());
        historyModelDeliveryItem.setReceiverLastKana(shippingAddressResponse.getLastKana());
        historyModelDeliveryItem.setReceiverFirstName(shippingAddressResponse.getFirstName());
        historyModelDeliveryItem.setReceiverLastName(shippingAddressResponse.getLastName());

        String[] receiverZipcodeArray = this.conversionUtility.toZipCodeArray(shippingAddressResponse.getZipCode());
        historyModelDeliveryItem.setReceiverZipCode1(receiverZipcodeArray[0]);
        historyModelDeliveryItem.setReceiverZipCode2(receiverZipcodeArray[1]);
        historyModelDeliveryItem.setReceiverPrefecture(shippingAddressResponse.getPrefecture());
        historyModelDeliveryItem.setReceiverAddress1(shippingAddressResponse.getAddress1());
        historyModelDeliveryItem.setReceiverAddress2(shippingAddressResponse.getAddress2());
        historyModelDeliveryItem.setReceiverAddress3(shippingAddressResponse.getAddress3());
    }

    /**
     * 配送情報をModelに設定する
     *
     * @param historyModelDeliveryItem ModelItem
     * @param shippingSlipResponse     配送伝票レスポンス
     * @param shippingMemo             配送メモ
     * @param shippingMethodResponse   配送方法レスポンス
     * @param memberHistoryModel       Model
     */
    protected void setupDelivery(HistoryModelDeliveryItem historyModelDeliveryItem,
                                 ShippingSlipResponse shippingSlipResponse,
                                 String shippingMemo,
                                 ShippingMethodResponse shippingMethodResponse,
                                 MemberHistoryModel memberHistoryModel) {

        // 配送方法名
        historyModelDeliveryItem.setDeliveryMethodName(shippingSlipResponse.getShippingMethodName());
        // 配送状況確認番号
        historyModelDeliveryItem.setDeliveryCode(shippingSlipResponse.getShipmentStatusConfirmCode());
        // お届け希望日設定
        String receiverDate = ConversionUtility.getFormatMdWithWeek(
                        this.conversionUtility.toTimestamp(shippingSlipResponse.getReceiverDate()));
        historyModelDeliveryItem.setReceiverDate(StringUtils.isNotEmpty(receiverDate) ? receiverDate : null);
        // お届け希望時間帯設定
        historyModelDeliveryItem.setReceiverTimeZone(shippingSlipResponse.getReceiverTimeZone());
        // 備考
        historyModelDeliveryItem.setDeliveryNote(shippingMemo);
        // 出荷状態
        if (shippingSlipResponse.getCompleteShipmentDate() != null) {
            // 出荷済み
            historyModelDeliveryItem.setShipmentStatus(HTypeShipmentStatus.SHIPPED);
        } else {
            // 未出荷
            historyModelDeliveryItem.setShipmentStatus(HTypeShipmentStatus.UNSHIPMENT);
        }

        // 配送追跡URL表示を判定し、trueなら値を MessageFormat してセット
        // 配送伝票ステータスが取消、または返品の場合は表示しない
        String deliveryChaseURL = this.orderUtility.getDeliveryChaseURL(
                        shippingMethodResponse.getDeliveryMethodResponse().getDeliveryChaseURL(),
                        shippingMethodResponse.getDeliveryMethodResponse().getDeliveryChaseURLDisplayPeriod(),
                        shippingSlipResponse.getShipmentStatusConfirmCode(),
                        this.conversionUtility.toTimestamp(shippingSlipResponse.getCompleteShipmentDate())
                                                                       );
        if (StringUtils.isNotEmpty(deliveryChaseURL) && !SHIPPING_CANCEL.equals(
                        shippingSlipResponse.getShippingStatus()) && !SHIPPING_RETURN.equals(
                        shippingSlipResponse.getShippingStatus())) {
            historyModelDeliveryItem.setDeliveryChaseURL(deliveryChaseURL);
        }

    }

    /**
     * 商品Itemを作成する<br/>
     *
     * @param orderItem         注文商品
     * @param examKitResponse   検査キットレスポンス
     * @param itemPriceSubTotal 商品金額（販売伝票）
     * @param goodsDetails      商品詳細Dto
     * @return HistoryModelGoodsItem
     */
    protected HistoryModelGoodsItem createGoodsItem(OrderSlipResponseItemList orderItem,
                                                    ExamKitResponse examKitResponse,
                                                    ItemPriceSubTotal itemPriceSubTotal,
                                                    GoodsDetailsDto goodsDetails) {

        HistoryModelGoodsItem historyPageGoodsItem = ApplicationContextUtility.getBean(HistoryModelGoodsItem.class);

        // 伝票が保持する商品マスタ情報をセット
        historyPageGoodsItem.setGoodsGroupName(orderItem.getItemName());
        historyPageGoodsItem.setUnitTitle1(orderItem.getUnitTitle1());
        historyPageGoodsItem.setUnitValue1(orderItem.getUnitValue1());
        historyPageGoodsItem.setUnitTitle2(orderItem.getUnitTitle2());
        historyPageGoodsItem.setUnitValue2(orderItem.getUnitValue2());
        historyPageGoodsItem.setGoodsPrice(new BigDecimal(itemPriceSubTotal.getItemUnitPrice()));
        historyPageGoodsItem.setTaxRate(itemPriceSubTotal.getItemTaxRate());
        historyPageGoodsItem.setGoodsTaxType(goodsDetails.getGoodsTaxType());

        BigDecimal goodsCount = new BigDecimal(orderItem.getItemCount());
        historyPageGoodsItem.setGoodsCount(goodsCount);
        if (goodsCount != null) {
            historyPageGoodsItem.setSubTotalGoodsPrice(new BigDecimal(itemPriceSubTotal.getItemPriceSubTotal()));
        }

        // 検査キットレスポンスから情報をセット
        if (examKitResponse != null) {
            historyPageGoodsItem.setExamKitCode(examKitResponse.getExamKitCode());
            historyPageGoodsItem.setExamStatus(EnumTypeUtil.getEnumFromValue(HTypeExamStatus.class, examKitResponse.getExamStatus()));
            historyPageGoodsItem.setExamResultsPdf(examKitResponse.getExamResultsPdf());
        }

        // 商品マスタから情報をセット
        historyPageGoodsItem.setGoodsCode(goodsDetails.getGoodsCode());
        historyPageGoodsItem.setGoodsOpenStatus(goodsDetails.getGoodsOpenStatusPC());
        historyPageGoodsItem.setOpenStartTime(goodsDetails.getOpenStartTimePC());
        historyPageGoodsItem.setOpenEndTime(goodsDetails.getOpenEndTimePC());
        historyPageGoodsItem.setNoveltyGoodsType(goodsDetails.getNoveltyGoodsType());

        List<GoodsGroupImageEntity> goodsGroupImageEntityList = goodsDetails.getGoodsGroupImageEntityList();
        if (goodsGroupImageEntityList != null) {
            List<String> goodsImageList = new ArrayList<>();
            // 商品画像リストを取り出す。
            for (GoodsGroupImageEntity goodsGroupImageEntity : goodsGroupImageEntityList) {
                goodsImageList.add(goodsGroupImageEntity.getImageFileName());
            }
            historyPageGoodsItem.setGoodsImageItems(goodsImageList);
        }

        return historyPageGoodsItem;
    }

    /**
     * 決済情報と請求情報をModelに設定する
     *
     * @param salesSlipResponse      販売伝票レスポンス
     * @param billingSlipResponse    請求伝票レスポンス
     * @param billingAddressResponse 請求先住所レスポンス
     * @param paymentMethodResponse  決済方法レスポンス
     * @param mulPayBillResponse     マルチペイメント請求レスポンス
     * @param memberHistoryModel     Model
     */
    protected void setupSettlement(SalesSlipResponse salesSlipResponse,
                                   BillingSlipResponse billingSlipResponse,
                                   AddressBookAddressResponse billingAddressResponse,
                                   PaymentMethodResponse paymentMethodResponse,
                                   MulPayBillResponse mulPayBillResponse,
                                   ConvenienceListResponse convenienceListResponse,
                                   MemberHistoryModel memberHistoryModel) {

        memberHistoryModel.setSettlementMethodName(billingSlipResponse.getPaymentMethodName());
        // TODO 本来BillingSlipResponseのクレジット用レスポンスと、リンク決済用レスポンスのnull判定で行えば、決済方法取得APIが不要になるが、左記レスポンスがオブジェクト自体は設定されてしまっているため、その判定が行えない
        //  そのため決済方法取得APIで取得した決済方法種別で判定を行っている
        if (HTypeSettlementMethodType.CREDIT.getValue().equals(paymentMethodResponse.getSettlementMethodType())) {
            memberHistoryModel.setSettlementMethodType(HTypeSettlementMethodType.CREDIT);
        } else if (HTypeSettlementMethodType.LINK_PAYMENT.getValue()
                                                         .equals(paymentMethodResponse.getSettlementMethodType())) {

            if (ObjectUtils.isNotEmpty(billingSlipResponse.getPaymentLinkResponse())) {
                PaymentLinkResponse paymentLinkResponse = billingSlipResponse.getPaymentLinkResponse();

                memberHistoryModel.setSettlementMethodType(HTypeSettlementMethodType.LINK_PAYMENT);

                if ("3".equals(paymentLinkResponse.getPayType())) {
                    memberHistoryModel.setConvenienceCode(mulPayBillResponse.getConvenience());
                    memberHistoryModel.setConvenienceName(
                                    this.createConveniName(convenienceListResponse.getConvenienceList(),
                                                           mulPayBillResponse
                                                          ));
                }

                memberHistoryModel.setPayType(paymentLinkResponse.getPayType());
                memberHistoryModel.setPayTypeName(paymentLinkResponse.getPayTypeName());
                memberHistoryModel.setPayMethod(paymentLinkResponse.getPaymethod());
                memberHistoryModel.setBillType(EnumTypeUtil.getEnumFromValue(HTypePaymentLinkType.class,
                                                                             paymentLinkResponse.getLinkPayType()
                                                                            ).getLabel());

                // if later payment
                if (HTypePaymentLinkType.LATERDATEPAYMENT.getValue().equals(paymentLinkResponse.getLinkPayType())) {
                    memberHistoryModel.setPaymentTimeLimitDate(
                                    conversionUtility.toTimestamp(paymentLinkResponse.getLaterDateLimit()));
                }
            }
        }

        // 請求金額設定
        if (billingSlipResponse.getBillingPrice() != null) {
            memberHistoryModel.setOrderPrice(new BigDecimal(billingSlipResponse.getBillingPrice()));
        }

        // 各種金額設定
        if (salesSlipResponse.getItemSalesPriceTotal() != null) {
            memberHistoryModel.setGoodsPriceTotal(new BigDecimal(salesSlipResponse.getItemSalesPriceTotal()));
        }
        if (salesSlipResponse.getCarriage() != null) {
            memberHistoryModel.setCarriage(new BigDecimal(salesSlipResponse.getCarriage()));
        }
        if (salesSlipResponse.getCommission() != null) {
            memberHistoryModel.setSettlementCommission(new BigDecimal(salesSlipResponse.getCommission()));
        }

        // 消費税算出
        memberHistoryModel.setTaxPrice(new BigDecimal((salesSlipResponse.getStandardTax() != null ?
                        salesSlipResponse.getStandardTax() :
                        0) + (salesSlipResponse.getReducedTax() != null ? salesSlipResponse.getReducedTax() : 0)));
        if (salesSlipResponse.getStandardTax() != null) {
            memberHistoryModel.setStandardTaxPrice(new BigDecimal(salesSlipResponse.getStandardTax()));
        }
        if (salesSlipResponse.getReducedTax() != null) {
            memberHistoryModel.setReducedTaxPrice(new BigDecimal(salesSlipResponse.getReducedTax()));
        }
        if (salesSlipResponse.getStandardTaxTargetPrice() != null) {
            memberHistoryModel.setStandardTaxTargetPrice(new BigDecimal(salesSlipResponse.getStandardTaxTargetPrice()));
        }
        if (salesSlipResponse.getReducedTaxTargetPrice() != null) {
            memberHistoryModel.setReducedTaxTargetPrice(new BigDecimal(salesSlipResponse.getReducedTaxTargetPrice()));
        }

        // 請求先住所設定
        HistoryModelBillingAddressItem historyModelBillingAddressItem =
                        ApplicationContextUtility.getBean(HistoryModelBillingAddressItem.class);
        setupBillingAddress(historyModelBillingAddressItem, billingAddressResponse);
        memberHistoryModel.setOrderBillingAddressItem(historyModelBillingAddressItem);

        // マルチペイメント請求設定
        if (mulPayBillResponse == null) {
            return;
        }

        if (mulPayBillResponse.getPayTimes() != null) {
            memberHistoryModel.setPaytimes(mulPayBillResponse.getPayTimes().toString());
        }

        memberHistoryModel.setReceiptNo(mulPayBillResponse.getReceiptNo());
        memberHistoryModel.setConfNo(mulPayBillResponse.getConfNo());
        memberHistoryModel.setBankName(mulPayBillResponse.getBankName() + "（" + mulPayBillResponse.getBankCode() + "）");
        memberHistoryModel.setBranchName(
                        mulPayBillResponse.getBranchName() + "（" + mulPayBillResponse.getBranchCode() + "）");
        memberHistoryModel.setAccountType(this.getAccountTypeLabel(mulPayBillResponse.getAccountType()));
        memberHistoryModel.setAccountNumber(mulPayBillResponse.getAccountNumber());
        //取引有効期限
        String exprireDate = mulPayBillResponse.getExprireDate();
        if (!StringUtils.isEmpty(exprireDate)) {
            // 日付関連Helper取得
            DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);
            exprireDate = dateUtility.format(TimestampConversionUtil.toTimestamp(exprireDate, "yyyyMMdd"),
                                             "yyyy/MM/dd"
                                            );
        }
        memberHistoryModel.setExprireDate(exprireDate);
        memberHistoryModel.setBkCode(mulPayBillResponse.getBkCode());
        memberHistoryModel.setCustId(mulPayBillResponse.getCustId());
        memberHistoryModel.setCode(mulPayBillResponse.getEncryptReceiptNo());
        memberHistoryModel.setPaymentUrl(mulPayBillResponse.getPaymentURL());

        if (mulPayBillResponse.getMethod() == null) {
            return;
        }

        if (mulPayBillResponse.getMethod().equals(HTypePaymentType.SINGLE.getValue())) {
            memberHistoryModel.setPaymentTypeDisplay(HTypePaymentType.SINGLE.getLabel());
        } else if (mulPayBillResponse.getMethod().equals(HTypePaymentType.INSTALLMENT.getValue())) {
            memberHistoryModel.setPaymentTypeDisplay(HTypePaymentType.INSTALLMENT.getLabel());
        } else if (mulPayBillResponse.getMethod().equals(HTypePaymentType.REVOLVING.getValue())) {
            memberHistoryModel.setPaymentTypeDisplay(HTypePaymentType.REVOLVING.getLabel());
        }
    }

    /**
     * 請求先情報をModelに設定する
     *
     * @param historyModelBillingAddressItem ModelItem
     * @param billingAddressResponse         請求先住所レスポンス
     */
    protected void setupBillingAddress(HistoryModelBillingAddressItem historyModelBillingAddressItem,
                                       AddressBookAddressResponse billingAddressResponse) {

        historyModelBillingAddressItem.setBillingTel(billingAddressResponse.getTel());
        historyModelBillingAddressItem.setBillingFirstKana(billingAddressResponse.getFirstKana());
        historyModelBillingAddressItem.setBillingLastKana(billingAddressResponse.getLastKana());
        historyModelBillingAddressItem.setBillingFirstName(billingAddressResponse.getFirstName());
        historyModelBillingAddressItem.setBillingLastName(billingAddressResponse.getLastName());

        String[] receiverZipcodeArray = this.conversionUtility.toZipCodeArray(billingAddressResponse.getZipCode());
        historyModelBillingAddressItem.setBillingZipCode1(receiverZipcodeArray[0]);
        historyModelBillingAddressItem.setBillingZipCode2(receiverZipcodeArray[1]);
        historyModelBillingAddressItem.setBillingPrefecture(billingAddressResponse.getPrefecture());
        historyModelBillingAddressItem.setBillingAddress1(billingAddressResponse.getAddress1());
        historyModelBillingAddressItem.setBillingAddress2(billingAddressResponse.getAddress2());
        historyModelBillingAddressItem.setBillingAddress3(billingAddressResponse.getAddress3());
    }

    /**
     * 追加料金（調整金額）情報をModelに設定する
     *
     * @param salesSlipResponse  販売伝票レスポンス
     * @param memberHistoryModel Model
     */
    protected void setupExtraCharge(SalesSlipResponse salesSlipResponse, MemberHistoryModel memberHistoryModel) {

        List<HistoryModelAdditionalChargeItem> historyPageAdditionalChargeItems = new ArrayList<>();
        BigDecimal additionalPriceTotal = BigDecimal.ZERO;
        if (!CollectionUtils.isEmpty(salesSlipResponse.getAdjustmentAmountList())) {
            for (AdjustmentAmountResponse adjustmentAmount : salesSlipResponse.getAdjustmentAmountList()) {
                historyPageAdditionalChargeItems.add(createAdditionalChargeItem(adjustmentAmount));
                additionalPriceTotal = additionalPriceTotal.add(new BigDecimal(adjustmentAmount.getAdjustPrice()));
            }
        }
        memberHistoryModel.setOrderAdditionalChargeItems(historyPageAdditionalChargeItems);
    }

    /**
     * 追加料金（調整金額）Itemを作成する<br/>
     *
     * @param adjustmentAmount 調整金額
     * @return DetailPageAdditionalChargeItem
     */
    protected HistoryModelAdditionalChargeItem createAdditionalChargeItem(AdjustmentAmountResponse adjustmentAmount) {

        HistoryModelAdditionalChargeItem historyPageAdditionalChargeItem =
                        ApplicationContextUtility.getBean(HistoryModelAdditionalChargeItem.class);

        historyPageAdditionalChargeItem.setAdditionalDetailsName(adjustmentAmount.getAdjustName());
        historyPageAdditionalChargeItem.setAdditionalDetailsPrice(new BigDecimal(adjustmentAmount.getAdjustPrice()));

        return historyPageAdditionalChargeItem;
    }

    /**
     * クーポン情報を設定する<br/>
     *
     * @param salesSlipResponse  販売伝票レスポンス
     * @param couponResponse     クーポン情報レスポンス
     * @param memberHistoryModel 対象Model
     */
    protected void setCoupon(SalesSlipResponse salesSlipResponse,
                             CouponResponse couponResponse,
                             MemberHistoryModel memberHistoryModel) {

        // クーポン情報が存在しない場合はスキップ
        if (couponResponse == null) {
            return;
        }

        // クーポン名
        memberHistoryModel.setCouponName(salesSlipResponse.getCouponName());
        // クーポン割引額
        if (salesSlipResponse.getCouponPaymentPrice() != null && salesSlipResponse.getCouponPaymentPrice() > 0) {
            BigDecimal couponDiscountPrice = new BigDecimal(salesSlipResponse.getCouponPaymentPrice());
            // マイナスで表示
            memberHistoryModel.setCouponDiscountPrice(couponDiscountPrice.negate());
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

        for (GoodsDetailsResponse goodsDetailsResponse : productDetailListResponse.getGoodsDetailsList()) {

            GoodsDetailsDto goodsDetailsDto = new GoodsDetailsDto();

            goodsDetailsDto.setGoodsSeq(goodsDetailsResponse.getGoodsSeq());
            goodsDetailsDto.setGoodsGroupCode(goodsDetailsResponse.getGoodsGroupCode());
            goodsDetailsDto.setVersionNo(goodsDetailsResponse.getVersionNo());
            goodsDetailsDto.setGoodsCode(goodsDetailsResponse.getGoodsCode());
            goodsDetailsDto.setNoveltyGoodsType(goodsDetailsResponse.getNoveltyGoodsType());
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
            if (!ObjectUtils.isEmpty(orderSlipResponse) && !CollectionUtils.isEmpty(orderSlipResponse.getItemList())) {
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

        if (!CollectionUtils.isEmpty(goodsInformationIconDetailsResponseList)) {
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
     * コンビニ名称作成
     *
     * @param convenienceResponsesList コンビニ一覧レスポンス
     * @param mulPayBillResponse       マルチペイメント請求
     * @return コンビニ名称
     */
    public String createConveniName(List<ConvenienceResponse> convenienceResponsesList,
                                    MulPayBillResponse mulPayBillResponse) {
        String conveniName = "";

        if (!CollectionUtils.isEmpty(convenienceResponsesList)) {
            for (ConvenienceResponse response : convenienceResponsesList) {
                if (StringUtils.isNotEmpty(mulPayBillResponse.getConvenience()) && mulPayBillResponse.getConvenience()
                                                                                                     .equals(response.getConveniCode())) {
                    conveniName = response.getConveniName();
                    break;
                }
            }
        }

        return conveniName;
    }

    /**
     * 検査キットリクエストに変換
     *
     * @param itemList
     * @return 検査キットリクエスト
     */
    ExamKitRequest toExamKitRequest(List<OrderSlipResponseItemList> itemList) {
        ExamKitRequest examKitRequest = new ExamKitRequest();

        if (CollectionUtils.isEmpty(itemList)) {
            return null;
        }
        List<String> orderItemIdList = new ArrayList<>();

        itemList.forEach(item -> {
            orderItemIdList.add(item.getOrderItemId());
        });

        examKitRequest.setOrderItemIdList(orderItemIdList);

        return examKitRequest;
    }

    /**
     * 振込先口座種別ラベル取得
     *
     * @param accountType 振込先口座種別
     * @return 振込先口座種別ラベル
     */
    public String getAccountTypeLabel(String accountType) {
        return StringUtils.isNotEmpty(accountType) && "1".equals(accountType) ? ACCOUNTTYPE_LABEL : StringUtils.EMPTY;
    }

}