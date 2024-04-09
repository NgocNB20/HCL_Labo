/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.pc.web.front.order;

import jp.co.itechh.quad.addressbook.presentation.api.AddressBookApi;
import jp.co.itechh.quad.addressbook.presentation.api.param.AddressBookAddressResponse;
import jp.co.itechh.quad.billingslip.presentation.api.BillingSlipApi;
import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipGetRequest;
import jp.co.itechh.quad.billingslip.presentation.api.param.BillingSlipResponse;
import jp.co.itechh.quad.billingslip.presentation.api.param.CreditResponse;
import jp.co.itechh.quad.front.base.util.common.CollectionUtil;
import jp.co.itechh.quad.front.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.front.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.front.base.utility.ConversionUtility;
import jp.co.itechh.quad.front.constant.type.HTypeAlcoholFlag;
import jp.co.itechh.quad.front.constant.type.HTypeBillType;
import jp.co.itechh.quad.front.constant.type.HTypeEffectiveFlag;
import jp.co.itechh.quad.front.constant.type.HTypeFreeDeliveryFlag;
import jp.co.itechh.quad.front.constant.type.HTypeGoodsSaleStatus;
import jp.co.itechh.quad.front.constant.type.HTypeGoodsTaxType;
import jp.co.itechh.quad.front.constant.type.HTypeIndividualDeliveryType;
import jp.co.itechh.quad.front.constant.type.HTypeMailRequired;
import jp.co.itechh.quad.front.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.front.constant.type.HTypeSettlementMethodCommissionType;
import jp.co.itechh.quad.front.constant.type.HTypeSettlementMethodPriceCommissionFlag;
import jp.co.itechh.quad.front.constant.type.HTypeSettlementMethodType;
import jp.co.itechh.quad.front.constant.type.HTypeSnsLinkFlag;
import jp.co.itechh.quad.front.constant.type.HTypeStockManagementFlag;
import jp.co.itechh.quad.front.constant.type.HTypeStockStatusType;
import jp.co.itechh.quad.front.constant.type.HTypeUnitManagementFlag;
import jp.co.itechh.quad.front.dto.goods.goods.GoodsDetailsDto;
import jp.co.itechh.quad.front.dto.icon.GoodsInformationIconDetailsDto;
import jp.co.itechh.quad.front.entity.goods.goodsgroup.GoodsGroupImageEntity;
import jp.co.itechh.quad.front.entity.shop.settlement.SettlementMethodEntity;
import jp.co.itechh.quad.front.pc.web.front.order.common.OrderCommonModel;
import jp.co.itechh.quad.front.pc.web.front.order.common.SalesSlipModel;
import jp.co.itechh.quad.front.pc.web.front.order.common.SalesSlipUtility;
import jp.co.itechh.quad.front.util.common.EnumTypeUtil;
import jp.co.itechh.quad.method.presentation.api.SettlementMethodApi;
import jp.co.itechh.quad.method.presentation.api.param.PaymentMethodResponse;
import jp.co.itechh.quad.orderslip.presentation.api.OrderSlipApi;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderSlipGetRequest;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderSlipResponse;
import jp.co.itechh.quad.orderslip.presentation.api.param.OrderSlipResponseItemList;
import jp.co.itechh.quad.pricecalculator.presentation.api.PriceCalculatorApi;
import jp.co.itechh.quad.pricecalculator.presentation.api.param.ItemSalesPriceCalculateRequest;
import jp.co.itechh.quad.pricecalculator.presentation.api.param.ItemSalesPriceCalculateResponse;
import jp.co.itechh.quad.product.presentation.api.ProductApi;
import jp.co.itechh.quad.product.presentation.api.param.GoodsDetailsResponse;
import jp.co.itechh.quad.product.presentation.api.param.GoodsGroupImageResponse;
import jp.co.itechh.quad.product.presentation.api.param.GoodsInformationIconDetailsResponse;
import jp.co.itechh.quad.product.presentation.api.param.PageInfoRequest;
import jp.co.itechh.quad.product.presentation.api.param.ProductDetailListGetRequest;
import jp.co.itechh.quad.product.presentation.api.param.ProductDetailListResponse;
import jp.co.itechh.quad.salesslip.presentation.api.SalesSlipApi;
import jp.co.itechh.quad.salesslip.presentation.api.param.ItemPriceSubTotal;
import jp.co.itechh.quad.salesslip.presentation.api.param.SalesSlipGetRequest;
import jp.co.itechh.quad.salesslip.presentation.api.param.SalesSlipResponse;
import jp.co.itechh.quad.shippingslip.presentation.api.ShippingSlipApi;
import jp.co.itechh.quad.shippingslip.presentation.api.param.ShippingSlipGetRequest;
import jp.co.itechh.quad.shippingslip.presentation.api.param.ShippingSlipResponse;
import jp.co.itechh.quad.transaction.presentation.api.param.TransactionConfirmOpenRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 注文内容確認 Helper
 *
 * @author Pham Quang Dieu
 */
@Component
public class ConfirmHelper {

    /**
     * 変換Helper
     */
    private final ConversionUtility conversionUtility;

    /**
     * コンストラクタ
     */
    public ConfirmHelper(ConversionUtility conversionUtility) {
        this.conversionUtility = conversionUtility;
    }

    /**
     * 商品詳細Dtoリストに変換し、画面Modelに変換<br/>
     * 注文票が保持するトランザクションデータは伝票から取得して画面Modelに設定
     *
     * @param productDetailListResponse
     * @param salesSlipResponse
     * @param orderSlipResponse
     * @return 商品詳細Dtoリスト
     */
    public List<GoodsDetailsDto> toProductDetailList(ProductDetailListResponse productDetailListResponse,
                                                     SalesSlipResponse salesSlipResponse,
                                                     OrderSlipResponse orderSlipResponse) {

        // 処理前は存在しないためnullを返す
        if (ObjectUtils.isEmpty(productDetailListResponse) || CollectionUtils.isEmpty(
                        productDetailListResponse.getGoodsDetailsList())) {
            return null;
        }

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
            goodsDetailsDto.setSaleStartTimePC(conversionUtility.toTimestamp(goodsDetailsResponse.getSaleStartTime()));
            goodsDetailsDto.setSaleEndTimePC(conversionUtility.toTimestamp(goodsDetailsResponse.getSaleEndTime()));
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
            goodsDetailsDto.setSalesPossibleStock(goodsDetailsResponse.getSalesPossibleStock());
            goodsDetailsDto.setRealStock(goodsDetailsResponse.getRealStock());
            goodsDetailsDto.setOrderReserveStock(goodsDetailsResponse.getOrderReserveStock());
            goodsDetailsDto.setRemainderFewStock(goodsDetailsResponse.getRemainderFewStock());
            goodsDetailsDto.setOrderPointStock(goodsDetailsResponse.getOrderPointStock());
            goodsDetailsDto.setSafetyStock(goodsDetailsResponse.getSafetyStock());
            goodsDetailsDto.setGoodsGroupCode(goodsDetailsResponse.getGoodsGroupCode());
            goodsDetailsDto.setWhatsnewDate(conversionUtility.toTimestamp(goodsDetailsResponse.getWhatsnewDate()));
            if (goodsDetailsResponse.getGoodsOpenStatus() != null) {
                goodsDetailsDto.setGoodsOpenStatusPC(EnumTypeUtil.getEnumFromValue(HTypeOpenDeleteStatus.class,
                                                                                   goodsDetailsResponse.getGoodsOpenStatus()
                                                                                  ));
            }
            goodsDetailsDto.setOpenStartTimePC(conversionUtility.toTimestamp(goodsDetailsResponse.getOpenStartTime()));
            goodsDetailsDto.setOpenEndTimePC(conversionUtility.toTimestamp(goodsDetailsResponse.getOpenEndTime()));
            if (CollectionUtil.isNotEmpty(goodsDetailsResponse.getGoodsGroupImageList())) {
                List<GoodsGroupImageEntity> imageEntityList =
                                toGoodsGroupImageList(goodsDetailsResponse.getGoodsGroupImageList());

                // 商品画像を設定
                List<String> goodsImageList = new ArrayList<>();
                if (CollectionUtil.isNotEmpty(imageEntityList)) {
                    for (GoodsGroupImageEntity goodsGroupImageEntity : imageEntityList) {
                        goodsImageList.add(goodsGroupImageEntity.getImageFileName());
                    }
                }
                goodsDetailsDto.setGoodsGroupImageEntityList(imageEntityList);
                goodsDetailsDto.setGoodsImageItems(goodsImageList);
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

            /** 画面モデルに 注文票.注文商品 に設定しているトランザクションデータを設定 */
            if (!CollectionUtils.isEmpty(orderSlipResponse.getItemList())) {
                OrderSlipResponseItemList targetOrderItem = orderSlipResponse.getItemList()
                                                                             .stream()
                                                                             .filter(orderItem -> goodsDetailsResponse.getGoodsSeq()
                                                                                                  != null
                                                                                                  && goodsDetailsResponse.getGoodsSeq()
                                                                                                                         .equals(conversionUtility.toInteger(
                                                                                                                                         orderItem.getItemId())))
                                                                             .findFirst()
                                                                             .orElse(null);
                if (targetOrderItem != null) {
                    goodsDetailsDto.setGoodsGroupName(targetOrderItem.getItemName());
                    goodsDetailsDto.setUnitTitle1(targetOrderItem.getUnitTitle1());
                    goodsDetailsDto.setUnitValue1(targetOrderItem.getUnitValue1());
                    goodsDetailsDto.setUnitTitle2(targetOrderItem.getUnitTitle2());
                    goodsDetailsDto.setUnitValue2(targetOrderItem.getUnitValue2());
                    goodsDetailsDto.setJanCode(targetOrderItem.getJanCode());
                    goodsDetailsDto.setOrderGoodsCount(conversionUtility.toBigDecimal(targetOrderItem.getItemCount()));
                }
            }

            /** 画面モデルに 販売伝票 に設定している金額項目系トランザクションデータを設定 */
            if (!CollectionUtils.isEmpty(salesSlipResponse.getItemPriceSubTotalList())) {
                ItemPriceSubTotal targetSalesItem = salesSlipResponse.getItemPriceSubTotalList()
                                                                     .stream()
                                                                     .filter(salesItem -> goodsDetailsResponse.getGoodsSeq()
                                                                                          != null
                                                                                          && goodsDetailsResponse.getGoodsSeq()
                                                                                                                 .equals(conversionUtility.toInteger(
                                                                                                                                 salesItem.getItemId())))
                                                                     .findFirst()
                                                                     .orElse(null);
                if (targetSalesItem != null) {
                    goodsDetailsDto.setGoodsPrice(new BigDecimal(targetSalesItem.getItemUnitPrice()));
                    goodsDetailsDto.setTaxRate(targetSalesItem.getItemTaxRate());
                    goodsDetailsDto.setGoodsTotalPrice(
                                    conversionUtility.toBigDecimal(targetSalesItem.getItemPriceSubTotal()));
                }
            }

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
                goodsGroupImageEntity.setRegistTime(conversionUtility.toTimestamp(item.getRegistTime()));
                goodsGroupImageEntity.setUpdateTime(conversionUtility.toTimestamp(item.getUpdateTime()));

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
     * 請求情報を確認Modelに変換
     *
     * @param billingSlipResponse
     * @param confirmModel
     */
    public void toConfirmModelFromBillingSlip(BillingSlipResponse billingSlipResponse,
                                              ConfirmModel confirmModel,
                                              OrderCommonModel orderCommonModel) {

        if (!ObjectUtils.isEmpty(billingSlipResponse) && !ObjectUtils.isEmpty(billingSlipResponse.getCreditResponse())
            && confirmModel.getSettlementMethodEntity() != null
            && HTypeSettlementMethodType.CREDIT == confirmModel.getSettlementMethodEntity().getSettlementMethodType()) {
            CreditResponse creditResponse = billingSlipResponse.getCreditResponse();
            confirmModel.setRegistedCardMaskNo(creditResponse.getCardMaskNo());
            confirmModel.setExpirationDateMonth(creditResponse.getExpirationMonth());
            confirmModel.setExpirationDateYear(creditResponse.getExpirationYear());
            confirmModel.setToken(creditResponse.getPaymentToken());
            if (StringUtils.isNotBlank(creditResponse.getDividedNumber())) {
                confirmModel.setPaymentFrequency(Integer.parseInt(creditResponse.getDividedNumber()));
            }
            confirmModel.setPaymentType(creditResponse.getPaymentType());
            // セキュリティコード
            confirmModel.setSecurityCode(orderCommonModel.getSecurityCode());
        }
    }

    /**
     * 商品販売金額計算<br/>
     *
     * @param orderSlipId 注文票ID
     * @return 商品販売金額計算レスポンス
     */
    protected ItemSalesPriceCalculateResponse doCalculatePrice(String orderSlipId,
                                                               PriceCalculatorApi priceCalculatorApi) {
        ItemSalesPriceCalculateRequest itemSalesPriceCalculateRequest = new ItemSalesPriceCalculateRequest();

        itemSalesPriceCalculateRequest.setOrderSlipId(orderSlipId);
        ItemSalesPriceCalculateResponse itemSalesPriceCalculateResponse =
                        priceCalculatorApi.calculateItemSalesPrice(itemSalesPriceCalculateRequest);

        return itemSalesPriceCalculateResponse;
    }

    /**
     * 金額情報を画面Modelに変換
     *
     * @param salesSlipResponse
     * @param salesSlipUtility
     * @param confirmModel
     */
    public void toConfirmSalesSlip(SalesSlipResponse salesSlipResponse,
                                   SalesSlipUtility salesSlipUtility,
                                   ConfirmModel confirmModel) {
        SalesSlipModel salesSlipModel = salesSlipUtility.convertToSalesSlipCommon(salesSlipResponse);
        confirmModel.setSalesSlip(salesSlipModel);
    }

    /**
     * 配送伝票設定
     *
     * @param shippingSlipResponse
     * @param confirmModel
     */
    public void toConfirmShippingSlip(ShippingSlipResponse shippingSlipResponse, ConfirmModel confirmModel) {
        if (!ObjectUtils.isEmpty(shippingSlipResponse)) {
            confirmModel.setDeliveryMethodName(shippingSlipResponse.getShippingMethodName());
            if (shippingSlipResponse.getReceiverDate() != null) {
                confirmModel.setDeliveryDate(ConversionUtility.getFormatMdWithWeek(
                                conversionUtility.toTimestamp(shippingSlipResponse.getReceiverDate())));
            }
            confirmModel.setDeliveryTime(shippingSlipResponse.getReceiverTimeZone());
            confirmModel.setInvoiceNecessaryFlag(!ObjectUtils.isEmpty(shippingSlipResponse.getInvoiceNecessaryFlag())
                                                 && shippingSlipResponse.getInvoiceNecessaryFlag());
        }
    }

    /**
     * お届け先設定
     *
     * @param addressBookAddressResponse
     * @param confirmModel
     */
    public void toConfirmAddress(AddressBookAddressResponse addressBookAddressResponse, ConfirmModel confirmModel) {
        if (!ObjectUtils.isEmpty(addressBookAddressResponse)) {
            confirmModel.setAddressSelected(true);
            confirmModel.setAddressName(
                            addressBookAddressResponse.getLastName() + " " + addressBookAddressResponse.getFirstName());
            confirmModel.setAddressKana(
                            addressBookAddressResponse.getLastKana() + " " + addressBookAddressResponse.getFirstKana());
            confirmModel.setAddressZipCode(addressBookAddressResponse.getZipCode());
            if (addressBookAddressResponse.getAddress3() != null) {
                confirmModel.setAddress(
                                addressBookAddressResponse.getPrefecture() + addressBookAddressResponse.getAddress1()
                                + addressBookAddressResponse.getAddress2() + addressBookAddressResponse.getAddress3());
            } else {
                confirmModel.setAddress(
                                addressBookAddressResponse.getPrefecture() + addressBookAddressResponse.getAddress1()
                                + addressBookAddressResponse.getAddress2());
            }
            confirmModel.setAddressTel(addressBookAddressResponse.getTel());
            confirmModel.setAddressShippingMemo(addressBookAddressResponse.getShippingMemo());
        }
    }

    /**
     * ご請求先住所情報を設定
     *
     * @param addressBookAddressResponse
     * @param confirmModel
     */
    public void toConfirmBillingAddress(AddressBookAddressResponse addressBookAddressResponse,
                                        ConfirmModel confirmModel) {
        if (!ObjectUtils.isEmpty(addressBookAddressResponse)) {
            confirmModel.setBillingSelected(true);
            confirmModel.setBillingName(
                            addressBookAddressResponse.getLastName() + " " + addressBookAddressResponse.getFirstName());
            confirmModel.setBillingFirstName(addressBookAddressResponse.getFirstName());
            confirmModel.setBillingLastName(addressBookAddressResponse.getLastName());
            confirmModel.setBillingKana(
                            addressBookAddressResponse.getLastKana() + " " + addressBookAddressResponse.getFirstKana());
            confirmModel.setBillingZipCode(addressBookAddressResponse.getZipCode());
            if (addressBookAddressResponse.getAddress3() != null) {
                confirmModel.setBillingAddress(
                                addressBookAddressResponse.getPrefecture() + addressBookAddressResponse.getAddress1()
                                + addressBookAddressResponse.getAddress2() + addressBookAddressResponse.getAddress3());
            } else {
                confirmModel.setBillingAddress(
                                addressBookAddressResponse.getPrefecture() + addressBookAddressResponse.getAddress1()
                                + addressBookAddressResponse.getAddress2());
            }
            confirmModel.setBillingTel(addressBookAddressResponse.getTel());
            confirmModel.setBillingShippingMemo(addressBookAddressResponse.getShippingMemo());
        }
    }

    /**
     * 商品詳細情報を画面モデルへ変換
     *
     * @param goodsSeqList
     * @param productDetailListResponse
     * @param salesSlipResponse
     * @param orderSlipResponse
     * @param confirmModel
     */
    public void toConfirmProductDetail(List<Integer> goodsSeqList,
                                       ProductDetailListResponse productDetailListResponse,
                                       SalesSlipResponse salesSlipResponse,
                                       OrderSlipResponse orderSlipResponse,
                                       ConfirmModel confirmModel) {
        // ソート商品詳細一覧レスポンス
        reSortByItemListForDisplay(goodsSeqList, productDetailListResponse);

        // ページモデル用の商品情報を設定
        List<GoodsDetailsDto> goodsDetailDtoList =
                        toProductDetailList(productDetailListResponse, salesSlipResponse, orderSlipResponse);

        confirmModel.setGoodsDetailDtoList(goodsDetailDtoList);
    }

    /**
     * 商品詳細一覧取得リクエストに変換
     *
     * @param orderSlipResponse 注文票レスポンス
     * @return 商品詳細一覧取得リクエスト
     */
    public ProductDetailListGetRequest toProductDetailListGetRequest(OrderSlipResponse orderSlipResponse) {
        List<Integer> goodsSeqList = new ArrayList<>();

        if (!ObjectUtils.isEmpty(orderSlipResponse) && !CollectionUtils.isEmpty(orderSlipResponse.getItemList())) {
            List<OrderSlipResponseItemList> itemList = orderSlipResponse.getItemList();
            for (OrderSlipResponseItemList item : itemList) {
                if (item.getItemId() != null) {
                    goodsSeqList.add(Integer.parseInt(item.getItemId()));
                }
            }
        }
        ProductDetailListGetRequest productDetailListGetRequest = new ProductDetailListGetRequest();
        productDetailListGetRequest.setGoodsSeqList(goodsSeqList);

        return productDetailListGetRequest;
    }

    /**
     * ソート商品詳細一覧レスポンス<br/>
     *
     * @param listIdOrderBy                 商品SEQリスト
     * @param productDetailListResponseList 商品詳細一覧レスポンス
     * @return ソート済み商品詳細一覧レスポンス
     */
    protected void reSortByItemListForDisplay(List<Integer> listIdOrderBy,
                                              ProductDetailListResponse productDetailListResponseList) {
        if (!ObjectUtils.isEmpty(productDetailListResponseList) && !CollectionUtils.isEmpty(
                        productDetailListResponseList.getGoodsDetailsList())) {
            List<GoodsDetailsResponse> goodsDetailsResponseList = productDetailListResponseList.getGoodsDetailsList();
            goodsDetailsResponseList.sort(Comparator.comparing(item -> listIdOrderBy.indexOf(item.getGoodsSeq())));
        }
    }

    /**
     * 決済情報を画面Modelに変換<br/>
     * 請求伝票が保持するトランザクションデータは伝票から取得して画面Modelに設定
     *
     * @param paymentMethodResponse
     * @param billingSlipResponse
     * @param confirmModel
     */
    protected void toConfirmPaymentMethod(BillingSlipResponse billingSlipResponse,
                                          PaymentMethodResponse paymentMethodResponse,
                                          ConfirmModel confirmModel) {
        SettlementMethodEntity settlementMethodEntity = ApplicationContextUtility.getBean(SettlementMethodEntity.class);

        if (!ObjectUtils.isEmpty(paymentMethodResponse)) {
            settlementMethodEntity.setSettlementMethodSeq(paymentMethodResponse.getSettlementMethodSeq());
            settlementMethodEntity.setSettlementMethodName(paymentMethodResponse.getSettlementMethodName());
            settlementMethodEntity.setSettlementMethodDisplayNamePC(
                            paymentMethodResponse.getSettlementMethodDisplayNamePC());
            if (paymentMethodResponse.getOpenStatusPC() != null) {
                settlementMethodEntity.setOpenStatusPC(EnumTypeUtil.getEnumFromValue(HTypeOpenDeleteStatus.class,
                                                                                     paymentMethodResponse.getOpenStatusPC()
                                                                                    ));
            }
            settlementMethodEntity.setSettlementNotePC(paymentMethodResponse.getSettlementMethodType());
            if (paymentMethodResponse.getOpenStatusPC() != null) {
                settlementMethodEntity.setSettlementMethodType(
                                EnumTypeUtil.getEnumFromValue(HTypeSettlementMethodType.class,
                                                              paymentMethodResponse.getSettlementMethodType()
                                                             ));
            }
            if (paymentMethodResponse.getSettlementMethodCommissionType() != null) {
                settlementMethodEntity.setSettlementMethodCommissionType(
                                EnumTypeUtil.getEnumFromValue(HTypeSettlementMethodCommissionType.class,
                                                              paymentMethodResponse.getSettlementMethodCommissionType()
                                                             ));
            }
            if (paymentMethodResponse.getBillType() != null) {
                settlementMethodEntity.setBillType(EnumTypeUtil.getEnumFromValue(HTypeBillType.class,
                                                                                 paymentMethodResponse.getBillType()
                                                                                ));
            }
            settlementMethodEntity.setDeliveryMethodSeq(paymentMethodResponse.getDeliveryMethodSeq());
            settlementMethodEntity.setEqualsCommission(paymentMethodResponse.getEqualsCommission());
            if (paymentMethodResponse.getSettlementMethodPriceCommissionFlag() != null) {
                settlementMethodEntity.setSettlementMethodPriceCommissionFlag(
                                EnumTypeUtil.getEnumFromValue(HTypeSettlementMethodPriceCommissionFlag.class,
                                                              paymentMethodResponse.getSettlementMethodPriceCommissionFlag()
                                                             ));
            }
            settlementMethodEntity.setLargeAmountDiscountPrice(paymentMethodResponse.getLargeAmountDiscountPrice());
            settlementMethodEntity.setLargeAmountDiscountCommission(
                            paymentMethodResponse.getLargeAmountDiscountCommission());
            settlementMethodEntity.setOrderDisplay(paymentMethodResponse.getOrderDisplay());
            settlementMethodEntity.setMaxPurchasedPrice(paymentMethodResponse.getMaxPurchasedPrice());
            settlementMethodEntity.setPaymentTimeLimitDayCount(paymentMethodResponse.getPaymentTimeLimitDayCount());
            settlementMethodEntity.setMinPurchasedPrice(paymentMethodResponse.getMinPurchasedPrice());
            settlementMethodEntity.setCancelTimeLimitDayCount(paymentMethodResponse.getCancelTimeLimitDayCount());
            if (paymentMethodResponse.getSettlementMailRequired() != null) {
                settlementMethodEntity.setSettlementMailRequired(EnumTypeUtil.getEnumFromValue(HTypeMailRequired.class,
                                                                                               paymentMethodResponse.getSettlementMailRequired()
                                                                                              ));
            }
            if (paymentMethodResponse.getEnableCardNoHolding() != null) {
                settlementMethodEntity.setEnableCardNoHolding(EnumTypeUtil.getEnumFromValue(HTypeEffectiveFlag.class,
                                                                                            paymentMethodResponse.getEnableCardNoHolding()
                                                                                           ));
            }
            if (paymentMethodResponse.getEnableSecurityCode() != null) {
                settlementMethodEntity.setEnableSecurityCode(EnumTypeUtil.getEnumFromValue(HTypeEffectiveFlag.class,
                                                                                           paymentMethodResponse.getEnableSecurityCode()
                                                                                          ));
            }
            if (paymentMethodResponse.getEnable3dSecure() != null) {
                settlementMethodEntity.setEnable3dSecure(EnumTypeUtil.getEnumFromValue(HTypeEffectiveFlag.class,
                                                                                       paymentMethodResponse.getEnable3dSecure()
                                                                                      ));
            }
            if (paymentMethodResponse.getEnableInstallment() != null) {
                settlementMethodEntity.setEnableInstallment(EnumTypeUtil.getEnumFromValue(HTypeEffectiveFlag.class,
                                                                                          paymentMethodResponse.getEnableInstallment()
                                                                                         ));
            }
            if (paymentMethodResponse.getEnableBonusSinglePayment() != null) {
                settlementMethodEntity.setEnableBonusSinglePayment(
                                EnumTypeUtil.getEnumFromValue(HTypeEffectiveFlag.class,
                                                              paymentMethodResponse.getEnableBonusSinglePayment()
                                                             ));
            }
            if (paymentMethodResponse.getEnableBonusInstallment() != null) {
                settlementMethodEntity.setEnableBonusInstallment(EnumTypeUtil.getEnumFromValue(HTypeEffectiveFlag.class,
                                                                                               paymentMethodResponse.getEnableBonusInstallment()
                                                                                              ));
            }
            if (paymentMethodResponse.getEnableRevolving() != null) {
                settlementMethodEntity.setEnableRevolving(EnumTypeUtil.getEnumFromValue(HTypeEffectiveFlag.class,
                                                                                        paymentMethodResponse.getEnableRevolving()
                                                                                       ));
            }
            settlementMethodEntity.setRegistTime(conversionUtility.toTimestamp(paymentMethodResponse.getRegistTime()));
            settlementMethodEntity.setUpdateTime(conversionUtility.toTimestamp(paymentMethodResponse.getUpdateTime()));

            /** 画面モデルに 決済方法レスポンスから取得した値を設定 */
            confirmModel.setPaymentNote(paymentMethodResponse.getSettlementNotePC());
            confirmModel.setSettlementMethodEntity(settlementMethodEntity);

            /** 画面モデルに 請求伝票 に設定しているトランザクションデータを設定 */
            if (!ObjectUtils.isEmpty(billingSlipResponse)) {
                confirmModel.setSettlementMethodName(billingSlipResponse.getPaymentMethodName());
            }
        }
    }

    /**
     * 取引確定可能確認リクエストに変換<br/>
     *
     * @param confirmModel     注文確認モデル
     * @param orderCommonModel 注文フロー共通Model
     * @return 取引確定可能確認リクエスト
     */
    protected TransactionConfirmOpenRequest toTransactionConfirmOpenRequest(ConfirmModel confirmModel,
                                                                            OrderCommonModel orderCommonModel) {
        TransactionConfirmOpenRequest transactionConfirmOpenRequest = new TransactionConfirmOpenRequest();

        transactionConfirmOpenRequest.setTransactionId(orderCommonModel.getTransactionId());
        transactionConfirmOpenRequest.securityCode(confirmModel.getSecurityCode());
        transactionConfirmOpenRequest.setCallBackType(
                        PropertiesUtil.getSystemPropertiesValue("credit.td.result.callback.type"));
        transactionConfirmOpenRequest.setCreditTdResultReceiveUrl(
                        PropertiesUtil.getSystemPropertiesValue("credit.td.result.receive.url"));
        transactionConfirmOpenRequest.setPaymentLinkReturnUrl(
                        PropertiesUtil.getSystemPropertiesValue("link.payment.return.url"));

        return transactionConfirmOpenRequest;
    }

    /**
     * 取引確定可能確認リクエストに変換<br/>
     *
     * @param transactionId 取引ID
     * @return 取引確定可能確認リクエスト
     */
    public TransactionConfirmOpenRequest toTransactionConfirmOpenRequest(String transactionId) {
        TransactionConfirmOpenRequest transactionConfirmOpenRequest = new TransactionConfirmOpenRequest();

        transactionConfirmOpenRequest.setTransactionId(transactionId);
        transactionConfirmOpenRequest.setCallBackType(
                        PropertiesUtil.getSystemPropertiesValue("credit.td.result.callback.type"));
        transactionConfirmOpenRequest.setCreditTdResultReceiveUrl(
                        PropertiesUtil.getSystemPropertiesValue("credit.td.result.receive.url"));
        transactionConfirmOpenRequest.setPaymentLinkReturnUrl(
                        PropertiesUtil.getSystemPropertiesValue("link.payment.return.url"));

        return transactionConfirmOpenRequest;
    }

}