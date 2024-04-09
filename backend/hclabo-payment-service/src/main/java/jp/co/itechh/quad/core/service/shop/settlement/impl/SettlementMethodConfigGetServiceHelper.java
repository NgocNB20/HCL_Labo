/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.core.service.shop.settlement.impl;

import jp.co.itechh.quad.core.base.utility.ConversionUtility;
import jp.co.itechh.quad.core.constant.type.HTypeDeliveryMethodType;
import jp.co.itechh.quad.core.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.core.constant.type.HTypeShortfallDisplayFlag;
import jp.co.itechh.quad.core.entity.shop.delivery.DeliveryMethodEntity;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.method.presentation.api.param.ShippingMethodResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 決済方法詳細設定取得Helper
 *
 * @author PHAM QUANG DIEU (VJP)
 */
@Component
public class SettlementMethodConfigGetServiceHelper {

    /** 変換Utility */
    private final ConversionUtility conversionUtility;

    /**
     * 決済方法詳細設定取得
     *
     * @param conversionUtility
     */
    @Autowired
    public SettlementMethodConfigGetServiceHelper(ConversionUtility conversionUtility) {
        this.conversionUtility = conversionUtility;
    }

    /**
     * 配送方法DeliveryMethodEntityクラスに変換
     *
     * @param shippingMethodResponse 配送方法レスポンス
     * @return 配送方法
     */
    public DeliveryMethodEntity toDeliveryMethodEntity(ShippingMethodResponse shippingMethodResponse) {
        if (shippingMethodResponse == null || shippingMethodResponse.getDeliveryMethodResponse() == null) {
            return null;
        }
        DeliveryMethodEntity deliveryMethodEntity = new DeliveryMethodEntity();
        if (shippingMethodResponse.getDeliveryMethodResponse() != null) {
            deliveryMethodEntity.setDeliveryMethodSeq(
                            shippingMethodResponse.getDeliveryMethodResponse().getDeliveryMethodSeq());
            deliveryMethodEntity.setShopSeq(1001);
            deliveryMethodEntity.setDeliveryMethodName(
                            shippingMethodResponse.getDeliveryMethodResponse().getDeliveryMethodName());
            deliveryMethodEntity.setDeliveryMethodDisplayNamePC(
                            shippingMethodResponse.getDeliveryMethodResponse().getDeliveryMethodDisplayNamePC());
            if (shippingMethodResponse.getDeliveryMethodResponse().getOpenStatusPC() != null) {
                HTypeOpenDeleteStatus openDeleteStatus = EnumTypeUtil.getEnumFromValue(
                                HTypeOpenDeleteStatus.class,
                                shippingMethodResponse.getDeliveryMethodResponse().getOpenStatusPC()
                                                                                      );
                deliveryMethodEntity.setOpenStatusPC(openDeleteStatus);
            }
            deliveryMethodEntity.setDeliveryNotePC(
                            shippingMethodResponse.getDeliveryMethodResponse().getDeliveryNotePC());
            if (shippingMethodResponse.getDeliveryMethodResponse().getDeliveryMethodType() != null) {
                HTypeDeliveryMethodType deliveryMethodType = EnumTypeUtil.getEnumFromValue(
                                HTypeDeliveryMethodType.class,
                                shippingMethodResponse.getDeliveryMethodResponse().getDeliveryMethodType()
                                                                                          );
                deliveryMethodEntity.setDeliveryMethodType(deliveryMethodType);
            }
            deliveryMethodEntity.setEqualsCarriage(
                            shippingMethodResponse.getDeliveryMethodResponse().getEqualsCarriage());
            deliveryMethodEntity.setLargeAmountDiscountPrice(
                            shippingMethodResponse.getDeliveryMethodResponse().getLargeAmountDiscountPrice());
            deliveryMethodEntity.setLargeAmountDiscountCarriage(
                            shippingMethodResponse.getDeliveryMethodResponse().getLargeAmountDiscountCarriage());
            if (shippingMethodResponse.getDeliveryMethodResponse().getShortfallDisplayFlag() != null) {
                HTypeShortfallDisplayFlag shortfallDisplayFlag = EnumTypeUtil.getEnumFromValue(
                                HTypeShortfallDisplayFlag.class,
                                shippingMethodResponse.getDeliveryMethodResponse().getShortfallDisplayFlag()
                                                                                              );
                deliveryMethodEntity.setShortfallDisplayFlag(shortfallDisplayFlag);
            }
            if (shippingMethodResponse.getDeliveryMethodResponse().getDeliveryLeadTime() != null) {
                deliveryMethodEntity.setDeliveryLeadTime(
                                shippingMethodResponse.getDeliveryMethodResponse().getDeliveryLeadTime());
            }
            deliveryMethodEntity.setDeliveryChaseURL(
                            shippingMethodResponse.getDeliveryMethodResponse().getDeliveryChaseURL());
            deliveryMethodEntity.setDeliveryChaseURLDisplayPeriod(
                            shippingMethodResponse.getDeliveryMethodResponse().getDeliveryChaseURLDisplayPeriod());
            if (shippingMethodResponse.getDeliveryMethodResponse().getPossibleSelectDays() != null) {
                deliveryMethodEntity.setPossibleSelectDays(
                                shippingMethodResponse.getDeliveryMethodResponse().getPossibleSelectDays());
            }
            deliveryMethodEntity.setReceiverTimeZone1(
                            shippingMethodResponse.getDeliveryMethodResponse().getReceiverTimeZone1());
            deliveryMethodEntity.setReceiverTimeZone2(
                            shippingMethodResponse.getDeliveryMethodResponse().getReceiverTimeZone2());
            deliveryMethodEntity.setReceiverTimeZone3(
                            shippingMethodResponse.getDeliveryMethodResponse().getReceiverTimeZone3());
            deliveryMethodEntity.setReceiverTimeZone4(
                            shippingMethodResponse.getDeliveryMethodResponse().getReceiverTimeZone4());
            deliveryMethodEntity.setReceiverTimeZone5(
                            shippingMethodResponse.getDeliveryMethodResponse().getReceiverTimeZone5());
            deliveryMethodEntity.setReceiverTimeZone6(
                            shippingMethodResponse.getDeliveryMethodResponse().getReceiverTimeZone6());
            deliveryMethodEntity.setReceiverTimeZone7(
                            shippingMethodResponse.getDeliveryMethodResponse().getReceiverTimeZone7());
            deliveryMethodEntity.setReceiverTimeZone8(
                            shippingMethodResponse.getDeliveryMethodResponse().getReceiverTimeZone8());
            deliveryMethodEntity.setReceiverTimeZone9(
                            shippingMethodResponse.getDeliveryMethodResponse().getReceiverTimeZone9());
            deliveryMethodEntity.setReceiverTimeZone10(
                            shippingMethodResponse.getDeliveryMethodResponse().getReceiverTimeZone10());
            deliveryMethodEntity.setOrderDisplay(shippingMethodResponse.getDeliveryMethodResponse().getOrderDisplay());
            if (shippingMethodResponse.getDeliveryMethodResponse().getRegistTime() != null) {
                deliveryMethodEntity.setRegistTime(conversionUtility.toTimestamp(
                                shippingMethodResponse.getDeliveryMethodResponse().getRegistTime()));
            }
            if (shippingMethodResponse.getDeliveryMethodResponse().getUpdateTime() != null) {
                deliveryMethodEntity.setUpdateTime(conversionUtility.toTimestamp(
                                shippingMethodResponse.getDeliveryMethodResponse().getUpdateTime()));
            }
        }
        return deliveryMethodEntity;
    }

}