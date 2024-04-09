/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.shop.delivery;

import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.ConversionUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeDeliveryMethodType;
import jp.co.itechh.quad.admin.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.admin.constant.type.HTypeShortfallDisplayFlag;
import jp.co.itechh.quad.admin.entity.shop.delivery.DeliveryMethodEntity;
import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import jp.co.itechh.quad.method.presentation.api.param.DeliveryMethodRequest;
import jp.co.itechh.quad.method.presentation.api.param.DeliveryMethodResponse;
import jp.co.itechh.quad.method.presentation.api.param.ShippingMethodListResponse;
import jp.co.itechh.quad.method.presentation.api.param.ShippingMethodListUpdateRequest;
import jp.co.itechh.quad.method.presentation.api.param.ShippingMethodResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 配送方法設定 Helper
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 *
 */
@Component
public class DeliveryHelper {

    /** 変換ユーティリティクラス */
    private final ConversionUtility conversionUtility;

    /**
     * コンストラクタ
     *
     * @param conversionUtility 変換ユーティリティクラス
     */
    public DeliveryHelper(ConversionUtility conversionUtility) {
        this.conversionUtility = conversionUtility;
    }

    /**
     * ページへの変換処理
     *
     * @param deliveryMethodEntityList 配送方法エンティティリスト
     * @param deliveryModel            配送方法設定画面ページ
     */
    public void toPageForLoad(List<DeliveryMethodEntity> deliveryMethodEntityList, DeliveryModel deliveryModel) {
        List<DeliveryResultItem> deliveryMethodItems = new ArrayList<>();
        DeliveryResultItem deliveryResultItem = null;
        // 配送方法エンティティリスト分ループ
        Integer orderDisplay = 1;
        for (DeliveryMethodEntity deliveryMethodEntity : deliveryMethodEntityList) {
            // 表示アイテムクラスを作成
            deliveryResultItem = ApplicationContextUtility.getBean(DeliveryResultItem.class);

            deliveryResultItem.setDeliveryMethodEntity(deliveryMethodEntity);
            deliveryResultItem.setDeliveryMethodRadioValue(deliveryMethodEntity.getDeliveryMethodSeq());
            // deliveryResultItem.setOrderDisplay(deliveryMethodEntity.getOrderDisplay());
            deliveryResultItem.setOrderDisplay(orderDisplay);
            deliveryResultItem.setDeliveryMethodSeq(deliveryMethodEntity.getDeliveryMethodSeq());
            deliveryResultItem.setDeliveryMethodName(deliveryMethodEntity.getDeliveryMethodName());
            deliveryResultItem.setOpenStatusPC(deliveryMethodEntity.getOpenStatusPC());
            deliveryResultItem.setDeliveryMethodType(deliveryMethodEntity.getDeliveryMethodType());

            // リストに貯める
            deliveryMethodItems.add(deliveryResultItem);

            orderDisplay++;
        }

        // 作ったリストをページに設定
        deliveryModel.setDeliveryMethodItems(deliveryMethodItems);
    }

    /**
     * 配送方法一覧更新リクエストに変換
     *
     * @param deliveryModel 配送方法設定画面ページ
     * @return 配送方法一覧更新リクエスト
     */
    public ShippingMethodListUpdateRequest toDeliveryMethodEntityListForSave(DeliveryModel deliveryModel) {
        ShippingMethodListUpdateRequest shippingMethodListUpdateRequest = new ShippingMethodListUpdateRequest();
        List<DeliveryMethodRequest> shippingMethodListUpdate = new ArrayList<>();

        for (DeliveryResultItem deliveryResultItem : deliveryModel.getDeliveryMethodItems()) {
            DeliveryMethodEntity deliveryMethodEntity = deliveryResultItem.getDeliveryMethodEntity();
            // 画面で並べ替えた表示順を設定
            deliveryMethodEntity.setOrderDisplay(deliveryResultItem.getOrderDisplay());

            if (deliveryMethodEntity != null) {
                DeliveryMethodRequest deliveryMethodRequest = new DeliveryMethodRequest();
                deliveryMethodRequest.setDeliveryMethodSeq(deliveryMethodEntity.getDeliveryMethodSeq());
                deliveryMethodRequest.setDeliveryMethodName(deliveryMethodEntity.getDeliveryMethodName());
                deliveryMethodRequest.setDeliveryMethodDisplayNamePC(
                                deliveryMethodEntity.getDeliveryMethodDisplayNamePC());
                if (deliveryMethodEntity.getOpenStatusPC() != null) {
                    deliveryMethodRequest.setOpenStatusPC(deliveryMethodEntity.getOpenStatusPC().getValue());
                }
                deliveryMethodRequest.setDeliveryNotePC(deliveryMethodEntity.getDeliveryNotePC());

                if (deliveryMethodEntity.getDeliveryMethodType() != null) {
                    deliveryMethodRequest.setDeliveryMethodType(
                                    deliveryMethodEntity.getDeliveryMethodType().getValue());
                }
                deliveryMethodRequest.setEqualsCarriage(deliveryMethodEntity.getEqualsCarriage());
                deliveryMethodRequest.setLargeAmountDiscountPrice(deliveryMethodEntity.getLargeAmountDiscountPrice());
                deliveryMethodRequest.setLargeAmountDiscountCarriage(
                                deliveryMethodEntity.getLargeAmountDiscountCarriage());

                if (deliveryMethodEntity.getShortfallDisplayFlag() != null) {
                    deliveryMethodRequest.setShortfallDisplayFlag(
                                    deliveryMethodEntity.getShortfallDisplayFlag().getValue());
                }
                deliveryMethodRequest.setDeliveryLeadTime(deliveryMethodEntity.getDeliveryLeadTime());
                deliveryMethodRequest.setDeliveryChaseURL(deliveryMethodEntity.getDeliveryChaseURL());
                deliveryMethodRequest.setDeliveryChaseURLDisplayPeriod(
                                deliveryMethodEntity.getDeliveryChaseURLDisplayPeriod());
                deliveryMethodRequest.setPossibleSelectDays(deliveryMethodEntity.getPossibleSelectDays());
                deliveryMethodRequest.setReceiverTimeZone1(deliveryMethodEntity.getReceiverTimeZone1());
                deliveryMethodRequest.setReceiverTimeZone2(deliveryMethodEntity.getReceiverTimeZone2());
                deliveryMethodRequest.setReceiverTimeZone3(deliveryMethodEntity.getReceiverTimeZone3());
                deliveryMethodRequest.setReceiverTimeZone4(deliveryMethodEntity.getReceiverTimeZone4());
                deliveryMethodRequest.setReceiverTimeZone5(deliveryMethodEntity.getReceiverTimeZone5());
                deliveryMethodRequest.setReceiverTimeZone6(deliveryMethodEntity.getReceiverTimeZone6());
                deliveryMethodRequest.setReceiverTimeZone7(deliveryMethodEntity.getReceiverTimeZone7());
                deliveryMethodRequest.setReceiverTimeZone8(deliveryMethodEntity.getReceiverTimeZone8());
                deliveryMethodRequest.setReceiverTimeZone9(deliveryMethodEntity.getReceiverTimeZone9());
                deliveryMethodRequest.setReceiverTimeZone10(deliveryMethodEntity.getReceiverTimeZone10());
                deliveryMethodRequest.setOrderDisplay(deliveryMethodEntity.getOrderDisplay());

                shippingMethodListUpdate.add(deliveryMethodRequest);
            }
        }
        shippingMethodListUpdateRequest.setShippingMethodListUpdate(shippingMethodListUpdate);

        return shippingMethodListUpdateRequest;
    }

    /**
     * 配送方法エンティティリストに変換
     *
     * @param shippingMethodListResponse 配送方法リストレスポンス
     * @return 配送方法エンティティリスト
     */
    public List<DeliveryMethodEntity> toDeliveryMethodEntityList(ShippingMethodListResponse shippingMethodListResponse) {
        List<DeliveryMethodEntity> deliveryMethodEntityList = new ArrayList<>();

        if (shippingMethodListResponse != null && shippingMethodListResponse.getShippingMethodListResponse() != null) {
            for (ShippingMethodResponse shippingMethodResponse : shippingMethodListResponse.getShippingMethodListResponse()) {
                DeliveryMethodEntity deliveryMethodEntity = new DeliveryMethodEntity();

                if (shippingMethodResponse != null && shippingMethodResponse.getDeliveryMethodResponse() != null) {

                    DeliveryMethodResponse deliveryMethodResponse = shippingMethodResponse.getDeliveryMethodResponse();

                    deliveryMethodEntity.setDeliveryMethodSeq(deliveryMethodResponse.getDeliveryMethodSeq());
                    deliveryMethodEntity.setDeliveryMethodName(deliveryMethodResponse.getDeliveryMethodName());
                    deliveryMethodEntity.setDeliveryMethodDisplayNamePC(
                                    deliveryMethodResponse.getDeliveryMethodDisplayNamePC());
                    deliveryMethodEntity.setOpenStatusPC(EnumTypeUtil.getEnumFromValue(HTypeOpenDeleteStatus.class,
                                                                                       deliveryMethodResponse.getOpenStatusPC()
                                                                                      ));
                    deliveryMethodEntity.setDeliveryNotePC(deliveryMethodResponse.getDeliveryNotePC());
                    deliveryMethodEntity.setDeliveryMethodType(
                                    EnumTypeUtil.getEnumFromValue(HTypeDeliveryMethodType.class,
                                                                  deliveryMethodResponse.getDeliveryMethodType()
                                                                 ));
                    deliveryMethodEntity.setEqualsCarriage(deliveryMethodResponse.getEqualsCarriage());
                    deliveryMethodEntity.setLargeAmountDiscountPrice(
                                    deliveryMethodResponse.getLargeAmountDiscountPrice());
                    deliveryMethodEntity.setLargeAmountDiscountCarriage(
                                    deliveryMethodResponse.getLargeAmountDiscountCarriage());
                    deliveryMethodEntity.setShortfallDisplayFlag(
                                    EnumTypeUtil.getEnumFromValue(HTypeShortfallDisplayFlag.class,
                                                                  deliveryMethodResponse.getShortfallDisplayFlag()
                                                                 ));
                    deliveryMethodEntity.setDeliveryLeadTime(deliveryMethodResponse.getDeliveryLeadTime());
                    deliveryMethodEntity.setDeliveryChaseURL(deliveryMethodResponse.getDeliveryChaseURL());
                    deliveryMethodEntity.setDeliveryChaseURLDisplayPeriod(
                                    deliveryMethodResponse.getDeliveryChaseURLDisplayPeriod());
                    deliveryMethodEntity.setPossibleSelectDays(deliveryMethodResponse.getPossibleSelectDays());
                    deliveryMethodEntity.setReceiverTimeZone1(deliveryMethodResponse.getReceiverTimeZone1());
                    deliveryMethodEntity.setReceiverTimeZone2(deliveryMethodResponse.getReceiverTimeZone2());
                    deliveryMethodEntity.setReceiverTimeZone3(deliveryMethodResponse.getReceiverTimeZone3());
                    deliveryMethodEntity.setReceiverTimeZone4(deliveryMethodResponse.getReceiverTimeZone4());
                    deliveryMethodEntity.setReceiverTimeZone5(deliveryMethodResponse.getReceiverTimeZone5());
                    deliveryMethodEntity.setReceiverTimeZone6(deliveryMethodResponse.getReceiverTimeZone6());
                    deliveryMethodEntity.setReceiverTimeZone7(deliveryMethodResponse.getReceiverTimeZone7());
                    deliveryMethodEntity.setReceiverTimeZone8(deliveryMethodResponse.getReceiverTimeZone8());
                    deliveryMethodEntity.setReceiverTimeZone9(deliveryMethodResponse.getReceiverTimeZone9());
                    deliveryMethodEntity.setReceiverTimeZone10(deliveryMethodResponse.getReceiverTimeZone10());
                    deliveryMethodEntity.setOrderDisplay(deliveryMethodResponse.getOrderDisplay());
                    deliveryMethodEntity.setRegistTime(
                                    conversionUtility.toTimestamp(deliveryMethodResponse.getRegistTime()));
                    deliveryMethodEntity.setUpdateTime(
                                    conversionUtility.toTimestamp(deliveryMethodResponse.getUpdateTime()));
                }

                deliveryMethodEntityList.add(deliveryMethodEntity);
            }
        }

        return deliveryMethodEntityList;
    }
}