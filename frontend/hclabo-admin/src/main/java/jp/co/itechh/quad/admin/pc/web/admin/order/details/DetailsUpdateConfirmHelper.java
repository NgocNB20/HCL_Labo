/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.order.details;

import jp.co.itechh.quad.addressbook.presentation.api.param.AddressBookAddressResponse;
import jp.co.itechh.quad.admin.base.util.common.CollectionUtil;
import jp.co.itechh.quad.admin.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.ConversionUtility;
import jp.co.itechh.quad.admin.base.utility.DateUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeAlcoholFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeBillType;
import jp.co.itechh.quad.admin.constant.type.HTypeCarrierType;
import jp.co.itechh.quad.admin.constant.type.HTypeCouponLimitTargetType;
import jp.co.itechh.quad.admin.constant.type.HTypeFreeDeliveryFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeGmoReleaseFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeGoodsSaleStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeGoodsTaxType;
import jp.co.itechh.quad.admin.constant.type.HTypeIndividualDeliveryType;
import jp.co.itechh.quad.admin.constant.type.HTypeInvoiceAttachmentFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeMailRequired;
import jp.co.itechh.quad.admin.constant.type.HTypeNoveltyPresentJudgmentStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.admin.constant.type.HTypePaymentLinkType;
import jp.co.itechh.quad.admin.constant.type.HTypePrefectureType;
import jp.co.itechh.quad.admin.constant.type.HTypeSend;
import jp.co.itechh.quad.admin.constant.type.HTypeSettlementMethodType;
import jp.co.itechh.quad.admin.constant.type.HTypeSnsLinkFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeStockManagementFlag;
import jp.co.itechh.quad.admin.constant.type.HTypeStockStatusType;
import jp.co.itechh.quad.admin.constant.type.HTypeUnitManagementFlag;
import jp.co.itechh.quad.admin.dto.goods.goods.GoodsDetailsDto;
import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipForRevisionByTransactionRevisionIdResponse;
import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipResponse;
import jp.co.itechh.quad.convenience.presentation.api.param.ConvenienceListResponse;
import jp.co.itechh.quad.coupon.presentation.api.param.CouponResponse;
import jp.co.itechh.quad.customer.presentation.api.param.CustomerResponse;
import jp.co.itechh.quad.method.presentation.api.param.PaymentMethodResponse;
import jp.co.itechh.quad.mulpay.presentation.api.param.MulPayBillResponse;
import jp.co.itechh.quad.orderreceived.presentation.api.param.OrderReceivedResponse;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderItemRevisionResponse;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderSlipForRevisionResponse;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderSlipResponse;
import jp.co.itechh.quad.product.presentation.api.param.GoodsDetailsResponse;
import jp.co.itechh.quad.product.presentation.api.param.ProductDetailListResponse;
import jp.co.itechh.quad.salesslip.presentation.api.param.AdjustmentAmountResponse;
import jp.co.itechh.quad.salesslip.presentation.api.param.GetSalesSlipForRevisionByTransactionRevisionIdResponse;
import jp.co.itechh.quad.salesslip.presentation.api.param.ItemPriceSubTotal;
import jp.co.itechh.quad.salesslip.presentation.api.param.SalesPriceConsumptionTaxResponse;
import jp.co.itechh.quad.salesslip.presentation.api.param.SalesSlipCouponApplyResponse;
import jp.co.itechh.quad.salesslip.presentation.api.param.SalesSlipResponse;
import jp.co.itechh.quad.shippingslip.presentation.api.param.ShippingSlipResponse;
import jp.co.itechh.quad.transaction.presentation.api.param.TransactionForRevisionResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 受注修正修正確認画面Helperクラス。
 *
 * @author yt23807
 */
@Component
public class DetailsUpdateConfirmHelper extends AbstractOrderDetailsHelper {

    /** コンストラクタ */
    public DetailsUpdateConfirmHelper(DateUtility dateUtility, ConversionUtility conversionUtility) {
        super(dateUtility, conversionUtility);
    }

    /**
     * 受注詳細系ページ項目設定<br/>
     *
     * @param abstractOrderDetailsModel 受注詳細抽象モデル
     * @param orderDetailsCommonModel   共通モデル
     */
    public void toPageForRevision(AbstractOrderDetailsModel abstractOrderDetailsModel,
                                  DetailsUpdateConfirmModel.RevisedResponseTmpModel orderDetailsCommonModel) {
        // -------------------------------------
        // 親クラスのtoPageメソッドの改訂用版
        // -------------------------------------

        // 受注情報をセット
        setOrderInfo(abstractOrderDetailsModel, orderDetailsCommonModel.getOrderReceivedResponse(),
                     orderDetailsCommonModel.getTransactionForRevisionResponse(),
                     orderDetailsCommonModel.getPaymentMethodResponse()
                    );

        // お客様情報をセット
        setOrderCustomerInfo(abstractOrderDetailsModel, orderDetailsCommonModel.getCustomerResponse(),
                             orderDetailsCommonModel.getCustomerAddressResponse()
                            );

        // 受注配送情報、受注商品情報の値をセット
        setOrderDeliveryInfo(abstractOrderDetailsModel, orderDetailsCommonModel.getShippingSlipForRevisionResponse(),
                             orderDetailsCommonModel.getShippingAddressResponse(),
                             orderDetailsCommonModel.getGoodsDtoList(),
                             orderDetailsCommonModel.getOrderSlipForRevisionResponse(),
                             orderDetailsCommonModel.getSalesSlipForRevisionResponse()
                            );

        // 受注金額情報をセット
        setOrderAmountInfo(abstractOrderDetailsModel, orderDetailsCommonModel.getSalesSlipForRevisionResponse(),
                           orderDetailsCommonModel.getCouponResponse()
                          );

        // 受注決済情報、受注請求情報、受注入金情報をセット
        setOrderPaymentInfo(abstractOrderDetailsModel, orderDetailsCommonModel.getBillingSlipForRevisionResponse(),
                            orderDetailsCommonModel.getBillingAddressResponse(),
                            orderDetailsCommonModel.getPaymentMethodResponse(),
                            orderDetailsCommonModel.getOrderReceivedResponse(),
                            orderDetailsCommonModel.getMulPayBillResponse(),
                            orderDetailsCommonModel.getConvenienceListResponse()
                           );

        // 受注売上情報をセット
        setOrderSalesInfo(abstractOrderDetailsModel, orderDetailsCommonModel.getShippingSlipForRevisionResponse());

        // マルチペイメント情報をセット
        setMulPayInfo(abstractOrderDetailsModel, orderDetailsCommonModel.getMulPayBillResponse());
    }

    /**
     * 商品詳細Dtoリストに変換
     *
     * @param productDetailListResponse 商品詳細一覧レスポンス
     * @param orderSlipResponse         注文票レスポンス
     * @return 商品詳細Dtoリスト
     */
    public List<GoodsDetailsDto> toProductDetailList(ProductDetailListResponse productDetailListResponse,
                                                     OrderSlipForRevisionResponse orderSlipResponse) {

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
                                getConversionUtility().toTimestamp(goodsDetailsResponse.getSaleStartTime()));
                goodsDetailsDto.setSaleEndTimePC(
                                getConversionUtility().toTimestamp(goodsDetailsResponse.getSaleEndTime()));
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
                                getConversionUtility().toTimestamp(goodsDetailsResponse.getWhatsnewDate()));
                if (goodsDetailsResponse.getGoodsOpenStatus() != null) {
                    goodsDetailsDto.setGoodsOpenStatusPC(EnumTypeUtil.getEnumFromValue(HTypeOpenDeleteStatus.class,
                                                                                       goodsDetailsResponse.getGoodsOpenStatus()
                                                                                      ));
                }
                goodsDetailsDto.setOpenStartTimePC(
                                getConversionUtility().toTimestamp(goodsDetailsResponse.getOpenStartTime()));
                goodsDetailsDto.setOpenEndTimePC(
                                getConversionUtility().toTimestamp(goodsDetailsResponse.getOpenEndTime()));
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
                                orderSlipResponse.getOrderItemRevisionList())) {
                    for (int i = 0; i < orderSlipResponse.getOrderItemRevisionList().size(); i++) {
                        if (ObjectUtils.isNotEmpty(orderSlipResponse.getOrderItemRevisionList().get(i))
                            && StringUtils.isNotEmpty(
                                        orderSlipResponse.getOrderItemRevisionList().get(i).getItemId())) {
                            if (orderSlipResponse.getOrderItemRevisionList()
                                                 .get(i)
                                                 .getItemId()
                                                 .equals(getConversionUtility().toString(
                                                                 goodsDetailsDto.getGoodsSeq()))) {
                                goodsDetailsDto.setOrderGoodsCount(getConversionUtility().toBigDecimal(
                                                orderSlipResponse.getOrderItemRevisionList().get(i).getOrderCount()));
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
     * 受注情報をセット<br/>
     *
     * @param abstractOrderDetailsModel      受注詳細抽象モデル
     * @param orderReceivedResponse          受注レスポンス
     * @param transactionForRevisionResponse 改訂用取引レスポンス
     * @param paymentMethodResponse          決済方法レスポンス
     */
    protected void setOrderInfo(AbstractOrderDetailsModel abstractOrderDetailsModel,
                                OrderReceivedResponse orderReceivedResponse,
                                TransactionForRevisionResponse transactionForRevisionResponse,
                                PaymentMethodResponse paymentMethodResponse) {

        // 親クラスのsetOrderInfoを呼び出す
        super.setOrderInfo(abstractOrderDetailsModel, orderReceivedResponse, null, paymentMethodResponse);

        // 改訂用取引レスポンス
        // ※本メソッド前にModelクリアが行われている前提
        // 　（下記ifに対するelseでの、値のnull設定処理は行っていない）
        if (ObjectUtils.isNotEmpty(transactionForRevisionResponse)) {
            // 改訂用取引で変更可能性のある項目のみ値を設定
            abstractOrderDetailsModel.setSettlementMailRequired(
                            Boolean.TRUE.equals(transactionForRevisionResponse.getNotificationFlag()) ?
                                            HTypeMailRequired.REQUIRED :
                                            HTypeMailRequired.NO_NEED);
            abstractOrderDetailsModel.setReminderSentFlag(
                            Boolean.TRUE.equals(transactionForRevisionResponse.getReminderSentFlag()) ?
                                            HTypeSend.SENT :
                                            HTypeSend.UNSENT);
            abstractOrderDetailsModel.setExpiredSentFlag(
                            Boolean.TRUE.equals(transactionForRevisionResponse.getExpiredSentFlag()) ?
                                            HTypeSend.SENT :
                                            HTypeSend.UNSENT);
            abstractOrderDetailsModel.setNoveltyPresentJudgmentStatus(
                            EnumTypeUtil.getEnumFromValue(HTypeNoveltyPresentJudgmentStatus.class,
                                                          transactionForRevisionResponse.getNoveltyPresentJudgmentStatus()
                                                         ));

            // 受注メモの値をセット
            abstractOrderDetailsModel.setMemo(transactionForRevisionResponse.getAdminMemo());

        }

        // キャリア種別(常にPCを指定)
        abstractOrderDetailsModel.setCarrierType(HTypeCarrierType.PC);
    }

    /**
     * 受注配送情報リストをセット<br/>
     * 配送情報内の商品情報リストをセット<br/>
     *
     * @param abstractOrderDetailsModel    受注詳細抽象モデル
     * @param shippingSlipResponse         改訂用配送伝票レスポンス
     * @param shippingAddressResponse      お届け先住所レスポンス
     * @param goodsDtoList                 商品詳細Dtoリスト
     * @param orderSlipForRevisionResponse 改訂用注文票レスポンス
     * @param salesSlipForRevisionResponse 改訂用販売伝票レスポンス
     */
    protected void setOrderDeliveryInfo(AbstractOrderDetailsModel abstractOrderDetailsModel,
                                        ShippingSlipResponse shippingSlipResponse,
                                        AddressBookAddressResponse shippingAddressResponse,
                                        List<GoodsDetailsDto> goodsDtoList,
                                        OrderSlipForRevisionResponse orderSlipForRevisionResponse,
                                        GetSalesSlipForRevisionByTransactionRevisionIdResponse salesSlipForRevisionResponse) {

        if (ObjectUtils.isNotEmpty(shippingSlipResponse) && ObjectUtils.isNotEmpty(orderSlipForRevisionResponse)) {
            // お届け先情報をセット
            BigDecimal orderGoodsCountTotal = BigDecimal.ZERO;

            // 納品書 全配送共通の為、itemでは保持しない
            abstractOrderDetailsModel.setInvoiceAttachmentFlag(
                            Boolean.TRUE.equals(shippingSlipResponse.getInvoiceNecessaryFlag()) ?
                                            HTypeInvoiceAttachmentFlag.ON :
                                            HTypeInvoiceAttachmentFlag.OFF);
            OrderReceiverUpdateItem receiverItem = toOrderReceiverItem(shippingSlipResponse, shippingAddressResponse);

            // 商品情報リストをセット
            List<OrderGoodsUpdateItem> orderGoodsItems = new ArrayList<>();

            if (CollectionUtil.isNotEmpty(orderSlipForRevisionResponse.getOrderItemRevisionList())) {
                for (OrderItemRevisionResponse orderItemRevision : orderSlipForRevisionResponse.getOrderItemRevisionList()) {

                    if (ObjectUtils.isNotEmpty(salesSlipForRevisionResponse) && CollectionUtil.isNotEmpty(
                                    salesSlipForRevisionResponse.getItemPriceSubTotal()) && CollectionUtil.isNotEmpty(
                                    goodsDtoList)) {
                        ItemPriceSubTotal targetItemPrice = salesSlipForRevisionResponse.getItemPriceSubTotal()
                                                                                        .stream()
                                                                                        .filter(itemPrice -> itemPrice.getSalesItemSeq()
                                                                                                             != null
                                                                                                             && itemPrice.getSalesItemSeq()
                                                                                                                         .equals(orderItemRevision.getOrderItemSeq()))
                                                                                        .findFirst()
                                                                                        .orElse(null);
                        GoodsDetailsDto targetGoods = goodsDtoList.stream()
                                                                  .filter(goodsDto -> goodsDto.getGoodsSeq() != null
                                                                                      && goodsDto.getGoodsSeq()
                                                                                                 .toString()
                                                                                                 .equals(orderItemRevision.getItemId()))
                                                                  .findFirst()
                                                                  .orElse(null);

                        OrderGoodsUpdateItem goodsItem = toOrderGoodsUpdateItem(orderItemRevision,
                                                                                Objects.requireNonNull(targetItemPrice),
                                                                                Objects.requireNonNull(targetGoods)
                                                                               );
                        orderGoodsItems.add(goodsItem);
                        if (orderItemRevision.getOrderCount() != null) {
                            orderGoodsCountTotal =
                                            orderGoodsCountTotal.add(new BigDecimal(orderItemRevision.getOrderCount()));
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
     * 受注詳細商品アイテムクラスに変換
     *
     * @param item            改訂用注文商品
     * @param itemPrice       商品金額(販売伝票から取得したもの)
     * @param goodsDetailsDto 商品詳細Dto(注文票から取得したもの)
     * @return 商品アイテム
     */
    protected OrderGoodsUpdateItem toOrderGoodsUpdateItem(OrderItemRevisionResponse item,
                                                          ItemPriceSubTotal itemPrice,
                                                          GoodsDetailsDto goodsDetailsDto) {

        OrderGoodsUpdateItem goodsItem = ApplicationContextUtility.getBean(OrderGoodsUpdateItem.class);

        // 伝票が保持する商品情報をセット
        if (ObjectUtils.isNotEmpty(item)) {

            goodsItem.setOrderDisplay(item.getOrderItemSeq());
            goodsItem.setGoodsGroupName(item.getItemName());
            goodsItem.setUnitValue1(item.getUnitValue1());
            goodsItem.setUnitValue2(item.getUnitValue2());
            goodsItem.setJanCode(item.getJanCode());
            goodsItem.setGoodsCount(new BigDecimal(item.getOrderCount()));

            // 商品金額
            if (ObjectUtils.isNotEmpty(itemPrice)) {
                goodsItem.setGoodsPrice(new BigDecimal(itemPrice.getItemUnitPrice()));

                if (itemPrice.getItemPriceSubTotal() != null) {
                    goodsItem.setPostTaxOrderGoodsPrice(BigDecimal.valueOf(itemPrice.getItemPriceSubTotal()));
                }
            }

            // 商品詳細Dto
            if (ObjectUtils.isNotEmpty(goodsDetailsDto)) {

                goodsItem.setTaxRate(itemPrice.getItemTaxRate());
                goodsItem.setGoodsPriceInTax(BigDecimal.valueOf(itemPrice.getItemUnitPrice()));
                // TODO 下記2つのフラグは、マスタではなく、伝票から取得されるべきか確認が必要
                goodsItem.setFreeDeliveryFlag(goodsDetailsDto.getFreeDeliveryFlag());
                goodsItem.setIndividualDeliveryType(goodsDetailsDto.getIndividualDeliveryType());

                // 商品マスタから情報をセット
                goodsItem.setGoodsGroupCode(goodsDetailsDto.getGoodsGroupCode());
                goodsItem.setGoodsSeq(goodsDetailsDto.getGoodsSeq());
                goodsItem.setGoodsCode(goodsDetailsDto.getGoodsCode());
            }
        }

        return goodsItem;
    }

    /**
     * 金額情報をセット
     *
     * @param abstractOrderDetailsModel 受注詳細抽象モデル
     * @param salesSlipResponse         改訂用販売伝票レスポンス
     * @param couponResponse            クーポンレスポンス
     */
    protected void setOrderAmountInfo(AbstractOrderDetailsModel abstractOrderDetailsModel,
                                      GetSalesSlipForRevisionByTransactionRevisionIdResponse salesSlipResponse,
                                      CouponResponse couponResponse) {
        // 販売伝票レスポンス
        if (ObjectUtils.isNotEmpty(salesSlipResponse)) {

            if (salesSlipResponse.getBillingAmount() != null) {
                abstractOrderDetailsModel.setOrderPrice(new BigDecimal(salesSlipResponse.getBillingAmount()));
            }
            if (salesSlipResponse.getItemPurchasePriceTotal() != null) {
                abstractOrderDetailsModel.setGoodsPriceTotal(
                                new BigDecimal(salesSlipResponse.getItemPurchasePriceTotal()));
            }
            if (salesSlipResponse.getCarriage() != null) {
                abstractOrderDetailsModel.setCarriage(new BigDecimal(salesSlipResponse.getCarriage()));
            }
            if (salesSlipResponse.getCommission() != null) {
                abstractOrderDetailsModel.setSettlementCommission(new BigDecimal(salesSlipResponse.getCommission()));
            }
            if (ObjectUtils.isNotEmpty(salesSlipResponse.getSalesPriceConsumptionTaxResponse())) {
                SalesPriceConsumptionTaxResponse taxRes = salesSlipResponse.getSalesPriceConsumptionTaxResponse();
                abstractOrderDetailsModel.setTaxPrice(
                                new BigDecimal((taxRes.getStandardTax() != null ? taxRes.getStandardTax() : 0) + (
                                                taxRes.getReducedTax() != null ?
                                                                taxRes.getReducedTax() :
                                                                0)));
                if (taxRes.getStandardTax() != null) {
                    abstractOrderDetailsModel.setStandardTaxPrice(new BigDecimal(taxRes.getStandardTax()));
                }
                if (taxRes.getReducedTax() != null) {
                    abstractOrderDetailsModel.setReducedTaxPrice(new BigDecimal(taxRes.getReducedTax()));
                }
                if (taxRes.getStandardTaxTargetPrice() != null) {
                    abstractOrderDetailsModel.setStandardTaxTargetPrice(
                                    new BigDecimal(taxRes.getStandardTaxTargetPrice()));
                }
                if (taxRes.getReducedTaxTargetPrice() != null) {
                    abstractOrderDetailsModel.setReducedTaxTargetPrice(
                                    new BigDecimal(taxRes.getReducedTaxTargetPrice()));
                }
            }
            if (salesSlipResponse.getItemPurchasePriceTotal() != null) {
                abstractOrderDetailsModel.setPostTaxGoodsPriceTotal(
                                new BigDecimal(salesSlipResponse.getItemPurchasePriceTotal()));
            }

            // クーポンレスポンス
            if (ObjectUtils.isNotEmpty(couponResponse) && ObjectUtils.isNotEmpty(
                            salesSlipResponse.getApplyCouponResponse())) {
                // クーポンが適用されている場合のみクーポン情報を画面に反映する
                // クーポン名
                abstractOrderDetailsModel.setCouponName(salesSlipResponse.getApplyCouponResponse().getCouponName());
                // 適用クーポン名
                abstractOrderDetailsModel.setApplyCouponName(
                                salesSlipResponse.getApplyCouponResponse().getCouponName());
                // 適用クーポンID
                abstractOrderDetailsModel.setApplyCouponId(couponResponse.getCouponId());
                // 販売伝票×クーポン
                if (ObjectUtils.isNotEmpty(salesSlipResponse.getApplyCouponResponse())) {
                    SalesSlipCouponApplyResponse couponRes = salesSlipResponse.getApplyCouponResponse();
                    // クーポン利用フラグ
                    abstractOrderDetailsModel.setCouponLimitTargetType(
                                    Boolean.TRUE.equals(couponRes.getCouponUseFlag()) ?
                                                    HTypeCouponLimitTargetType.ON :
                                                    HTypeCouponLimitTargetType.OFF);
                    // クーポン割引額 画面上マイナス表示
                    if (couponRes.getCouponPaymentPrice() != null) {
                        abstractOrderDetailsModel.setCouponDiscountPrice(
                                        Boolean.TRUE.equals(couponRes.getCouponUseFlag()) ?
                                                        new BigDecimal(couponRes.getCouponPaymentPrice()).negate() :
                                                        BigDecimal.ZERO);
                    }
                }
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
     * 追加料金情報リストをセット<br/>
     *
     * @param abstractOrderDetailsModel 受注詳細抽象モデル
     * @param salesSlipResponse         改訂用販売伝票レスポンス
     */
    protected void setOrderAdditionalChargeDtoList(AbstractOrderDetailsModel abstractOrderDetailsModel,
                                                   GetSalesSlipForRevisionByTransactionRevisionIdResponse salesSlipResponse) {
        List<OrderAdditionalChargeItem> orderAdditionalChargeItems = new ArrayList<>();
        // 販売伝票レスポンス
        if (ObjectUtils.isNotEmpty(salesSlipResponse)) {
            if (!CollectionUtils.isEmpty(salesSlipResponse.getAdjustmentAmountListResponse())) {
                for (AdjustmentAmountResponse adjustmentAmount : salesSlipResponse.getAdjustmentAmountListResponse()) {
                    OrderAdditionalChargeItem item = toOrderAdditionalChargeItem(adjustmentAmount);
                    orderAdditionalChargeItems.add(item);
                }
            }
        }
        abstractOrderDetailsModel.setOrderAdditionalChargeItems(orderAdditionalChargeItems);
    }

    /**
     * 決済情報、請求情報、入金情報をセット
     *
     * @param abstractOrderDetailsModel 受注詳細抽象モデル
     * @param billingSlipResponse       改訂用請求伝票レスポンス
     * @param billingAddressResponse    請求先住所レスポンス
     * @param paymentMethodResponse     決済方法レスポンス
     * @param orderReceivedResponse     受注レスポンス
     */
    protected void setOrderPaymentInfo(AbstractOrderDetailsModel abstractOrderDetailsModel,
                                       BillingSlipForRevisionByTransactionRevisionIdResponse billingSlipResponse,
                                       AddressBookAddressResponse billingAddressResponse,
                                       PaymentMethodResponse paymentMethodResponse,
                                       OrderReceivedResponse orderReceivedResponse,
                                       MulPayBillResponse mulPayBillResponse,
                                       ConvenienceListResponse convenienceListResponse) {
        // 決済方法レスポンス
        if (ObjectUtils.isNotEmpty(paymentMethodResponse)) {
            // 決済情報をセット
            abstractOrderDetailsModel.setSettlementMethodSeq(
                            getConversionUtility().toInteger(billingSlipResponse.getPaymentMethodId()));
            abstractOrderDetailsModel.setSettlementMethodName(billingSlipResponse.getPaymentMethodName());
            if (billingSlipResponse.getMoneyReceiptTime() != null) {
                abstractOrderDetailsModel.setReceiptTime(
                                getConversionUtility().toTimestamp(billingSlipResponse.getMoneyReceiptTime()));
            }

            if (HTypeSettlementMethodType.CREDIT.getValue().equals(paymentMethodResponse.getSettlementMethodType())) {
                abstractOrderDetailsModel.setSettlementMethodType(HTypeSettlementMethodType.CREDIT);
            } else if (HTypeSettlementMethodType.LINK_PAYMENT.getValue()
                                                             .equals(paymentMethodResponse.getSettlementMethodType())) {
                abstractOrderDetailsModel.setSettlementMethodType(HTypeSettlementMethodType.LINK_PAYMENT);

                abstractOrderDetailsModel.setPayTypeName(billingSlipResponse.getPayTypeName());
                abstractOrderDetailsModel.setBillTypeLink(EnumTypeUtil.getEnumFromValue(HTypePaymentLinkType.class,
                                                                                        billingSlipResponse.getLinkPayType()
                                                                                       ));
                abstractOrderDetailsModel.setPayMethod(billingSlipResponse.getPaymethod());
                if (HTypePaymentLinkType.LATERDATEPAYMENT.getValue().equals(billingSlipResponse.getLinkPayType())) {
                    abstractOrderDetailsModel.setPaymentTimeLimitDate(
                                    getConversionUtility().toTimestamp(billingSlipResponse.getLaterDateLimit()));
                    abstractOrderDetailsModel.setCancelableDate(
                                    getConversionUtility().toTimestamp(billingSlipResponse.getCancelExpiredDate()));
                } else if (HTypePaymentLinkType.IMMEDIATEPAYMENT.getValue()
                                                                .equals(billingSlipResponse.getLinkPayType())) {
                    abstractOrderDetailsModel.setCancelLimit(
                                    getConversionUtility().toYmd(billingSlipResponse.getCancelLimit()));
                }

                if ("3".equals(billingSlipResponse.getPayType())) {
                    // 選択コンビニ名を設定
                    this.setConveniName(abstractOrderDetailsModel, convenienceListResponse, mulPayBillResponse);
                }
            }

            abstractOrderDetailsModel.setBillType(
                            HTypeBillType.PRE_CLAIM.getValue().equals(paymentMethodResponse.getBillType()) ?
                                            HTypeBillType.PRE_CLAIM :
                                            HTypeBillType.POST_CLAIM);
        }
        // 受注レスポンス
        if (ObjectUtils.isNotEmpty(orderReceivedResponse)) {
            // オーソリ保持期限の取得
            String authoryHoldPeriod = PropertiesUtil.getSystemPropertiesValue("authory.hold.period");
            if (StringUtils.isNotBlank(authoryHoldPeriod)) {
                Timestamp orderReceivedDate =
                                getConversionUtility().toTimestamp(orderReceivedResponse.getOrderReceivedDate());
                // オーソリ期限日（決済日付＋オーソリ保持期間）
                abstractOrderDetailsModel.setAuthoryLimitDate(getDateUtility().getAmountDayTimestamp(
                                getConversionUtility().toInteger(authoryHoldPeriod), true, orderReceivedDate));
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
                        Boolean.TRUE.equals(billingSlipResponse.getPaymentAgencyReleaseFlag()) ?
                                        HTypeGmoReleaseFlag.RELEASE :
                                        HTypeGmoReleaseFlag.NORMAL);
    }

    /********************************************
     * 判定用メソッド
     ********************************************/
    private void setOrderInfoByTransactionForRevision(AbstractOrderDetailsModel abstractOrderDetailsModel,
                                                      TransactionForRevisionResponse transactionRevisionResponse) {
        // 改訂用取引レスポンス
        // ※本メソッド前にModelクリアが行われている前提
        // 　（下記ifに対するelseでの、値のnull設定処理は行っていない）
        if (ObjectUtils.isNotEmpty(transactionRevisionResponse)) {
            abstractOrderDetailsModel.setTransactionId(transactionRevisionResponse.getTransactionId());

            abstractOrderDetailsModel.setSettlementMailRequired(
                            Boolean.TRUE.equals(transactionRevisionResponse.getNotificationFlag()) ?
                                            HTypeMailRequired.REQUIRED :
                                            HTypeMailRequired.NO_NEED);
            abstractOrderDetailsModel.setReminderSentFlag(
                            Boolean.TRUE.equals(transactionRevisionResponse.getReminderSentFlag()) ?
                                            HTypeSend.SENT :
                                            HTypeSend.UNSENT);
            abstractOrderDetailsModel.setExpiredSentFlag(
                            Boolean.TRUE.equals(transactionRevisionResponse.getExpiredSentFlag()) ?
                                            HTypeSend.SENT :
                                            HTypeSend.UNSENT);
            abstractOrderDetailsModel.setProcessTime(
                            getConversionUtility().toTimestamp(transactionRevisionResponse.getProcessTime()));

            // 受注メモの値をセット
            abstractOrderDetailsModel.setMemo(transactionRevisionResponse.getAdminMemo());
        }
    }

    private void setOSlip(AbstractOrderDetailsModel abstractOrderDetailsModel,
                          OrderSlipForRevisionResponse orderSlipForRevisionResponse) {
    }

    private void setShSlip(AbstractOrderDetailsModel abstractOrderDetailsModel,
                           ShippingSlipResponse shippingSlipForRevisionResponse) {
    }

    private void setSaSlip(AbstractOrderDetailsModel abstractOrderDetailsModel,
                           GetSalesSlipForRevisionByTransactionRevisionIdResponse salesSlipForRevisionResponse) {
    }

    private void setBSlip(AbstractOrderDetailsModel abstractOrderDetailsModel,
                          BillingSlipForRevisionByTransactionRevisionIdResponse billingSlipForRevisionResponse) {
    }

    /**
     * 顧客住所要否判定
     *
     * @param customerResponse 顧客情報
     * @return true...顧客住所要
     */
    protected boolean needCustomerAddress(CustomerResponse customerResponse) {
        return !ObjectUtils.isEmpty(customerResponse) && StringUtils.isNotBlank(
                        customerResponse.getMemberInfoAddressId());
    }

    /**
     * 配送先住所要否判定
     *
     * @param shippingSlipResponse 配送伝票
     * @return true...配送先住所要
     */
    protected boolean needShippingAddress(ShippingSlipResponse shippingSlipResponse) {
        return !ObjectUtils.isEmpty(shippingSlipResponse) && StringUtils.isNotBlank(
                        shippingSlipResponse.getShippingAddressId());
    }

    /**
     * 配送方法要否判定
     *
     * @param shippingSlipResponse 配送伝票
     * @return true..配送方法要
     */
    protected boolean needShippingMethod(ShippingSlipResponse shippingSlipResponse) {
        return !ObjectUtils.isEmpty(shippingSlipResponse) && StringUtils.isNotBlank(
                        shippingSlipResponse.getShippingMethodId());
    }

    /**
     * 請求先住所要否判定
     *
     * @param billingSlipResponse 請求伝票
     * @return true...請求先住所要
     */
    protected boolean needBillingAddress(BillingSlipResponse billingSlipResponse) {
        return !ObjectUtils.isEmpty(billingSlipResponse) && StringUtils.isNotBlank(
                        billingSlipResponse.getBillingAddressId());
    }

    /**
     * 決済方法要否判定
     *
     * @param billingSlipResponse 請求伝票
     * @return true..決済方法要
     */
    protected boolean needPaymentMethod(BillingSlipResponse billingSlipResponse) {
        return !ObjectUtils.isEmpty(billingSlipResponse) && StringUtils.isNotBlank(
                        billingSlipResponse.getPaymentMethodId());
    }

    /**
     * クーポン要否判定
     *
     * @param salesSlipResponse 販売伝票
     * @return true..クーポン要
     */
    protected boolean needCoupon(SalesSlipResponse salesSlipResponse) {
        return (!ObjectUtils.isEmpty(salesSlipResponse) && salesSlipResponse.getCouponSeq() != null);
    }

    /**
     * 商品詳細情報要否判定
     *
     * @param orderSlipResponse 注文票
     * @return true..商品詳細情報要
     */
    protected boolean needProductDetailList(OrderSlipResponse orderSlipResponse) {
        return (!ObjectUtils.isEmpty(orderSlipResponse) && !CollectionUtils.isEmpty(orderSlipResponse.getItemList()));
    }

    // /**
    //  * 配送先住所要否判定
    //  * @param shippingSlipResponse 改訂用配送伝票
    //  * @return true...配送先住所要
    //  */
    // protected boolean needShippingAddress(ShippingSlipResponse shippingSlipResponse) {
    //     return !ObjectUtils.isEmpty(shippingSlipResponse) && StringUtils.isNotBlank(shippingSlipResponse.getShippingAddressId());
    // }

    // /**
    //  * 配送方法要否判定
    //  * @param billingSlipResponse 改訂用配送伝票
    //  * @return true..配送方法要
    //  */
    // protected boolean needShippingMethod(ShippingSlipResponse shippingSlipResponse) {
    //     return !ObjectUtils.isEmpty(shippingSlipResponse) && StringUtils.isNotBlank(shippingSlipResponse.getShippingMethodId());
    // }

    /**
     * 請求先住所要否判定
     *
     * @param billingSlipResponse 改訂用請求伝票
     * @return true...請求先住所要
     */
    protected boolean needBillingAddress(BillingSlipForRevisionByTransactionRevisionIdResponse billingSlipResponse) {
        return !ObjectUtils.isEmpty(billingSlipResponse) && StringUtils.isNotBlank(
                        billingSlipResponse.getBillingAddressId());
    }

    /**
     * 決済方法要否判定
     *
     * @param billingSlipResponse 改訂用請求伝票
     * @return true..決済方法要
     */
    protected boolean needPaymentMethod(BillingSlipForRevisionByTransactionRevisionIdResponse billingSlipResponse) {
        return !ObjectUtils.isEmpty(billingSlipResponse) && StringUtils.isNotBlank(
                        billingSlipResponse.getPaymentMethodId());
    }

    /**
     * クーポン要否判定
     *
     * @param salesSlipResponse 改訂用販売伝票
     * @return true..クーポン要
     */
    protected boolean needCoupon(GetSalesSlipForRevisionByTransactionRevisionIdResponse salesSlipResponse) {
        return (!ObjectUtils.isEmpty(salesSlipResponse) && !ObjectUtils.isEmpty(
                        salesSlipResponse.getApplyCouponResponse())
                && salesSlipResponse.getApplyCouponResponse().getCouponSeq() != null);
    }

    /**
     * 商品詳細情報要否判定
     *
     * @param orderSlipResponse 改訂用注文票
     * @return true..商品詳細情報要
     */
    protected boolean needProductDetailList(OrderSlipForRevisionResponse orderSlipResponse) {
        return (!ObjectUtils.isEmpty(orderSlipResponse) && !CollectionUtils.isEmpty(
                        orderSlipResponse.getOrderItemRevisionList()));
    }
}