/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.method.presentation.api;

import jp.co.itechh.quad.core.base.utility.ConversionUtility;
import jp.co.itechh.quad.core.constant.type.HTypeDeliveryMethodType;
import jp.co.itechh.quad.core.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.core.constant.type.HTypePrefectureType;
import jp.co.itechh.quad.core.constant.type.HTypeShortfallDisplayFlag;
import jp.co.itechh.quad.core.dto.shop.delivery.DeliveryDetailsDto;
import jp.co.itechh.quad.core.dto.shop.delivery.DeliveryDto;
import jp.co.itechh.quad.core.dto.shop.delivery.DeliveryImpossibleAreaResultDto;
import jp.co.itechh.quad.core.dto.shop.delivery.DeliveryMethodDetailsDto;
import jp.co.itechh.quad.core.dto.shop.delivery.DeliverySearchForDaoConditionDto;
import jp.co.itechh.quad.core.dto.shop.delivery.DeliverySpecialChargeAreaResultDto;
import jp.co.itechh.quad.core.dto.shop.delivery.ReceiverDateDto;
import jp.co.itechh.quad.core.entity.shop.delivery.DeliveryMethodEntity;
import jp.co.itechh.quad.core.entity.shop.delivery.DeliveryMethodTypeCarriageEntity;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.ddd.usecase.method.GetSelectableShippingMethodListUseCaseDto;
import jp.co.itechh.quad.method.presentation.api.param.CarriageShippingMethodListGetRequest;
import jp.co.itechh.quad.method.presentation.api.param.CarriageShippingMethodListResponse;
import jp.co.itechh.quad.method.presentation.api.param.DeliveryDetailsResponse;
import jp.co.itechh.quad.method.presentation.api.param.DeliveryImpossibleAreaResultResponse;
import jp.co.itechh.quad.method.presentation.api.param.DeliveryMethodRequest;
import jp.co.itechh.quad.method.presentation.api.param.DeliveryMethodResponse;
import jp.co.itechh.quad.method.presentation.api.param.DeliveryMethodTypeCarriageRequest;
import jp.co.itechh.quad.method.presentation.api.param.DeliveryMethodTypeCarriageResponse;
import jp.co.itechh.quad.method.presentation.api.param.DeliveryResponse;
import jp.co.itechh.quad.method.presentation.api.param.DeliverySpecialChargeAreaResultResponse;
import jp.co.itechh.quad.method.presentation.api.param.ReceiverDateResponse;
import jp.co.itechh.quad.method.presentation.api.param.SelectableShippingMethod;
import jp.co.itechh.quad.method.presentation.api.param.SelectableShippingMethodListResponse;
import jp.co.itechh.quad.method.presentation.api.param.ShippingMethodCheckRequest;
import jp.co.itechh.quad.method.presentation.api.param.ShippingMethodListResponse;
import jp.co.itechh.quad.method.presentation.api.param.ShippingMethodListUpdateRequest;
import jp.co.itechh.quad.method.presentation.api.param.ShippingMethodRegistRequest;
import jp.co.itechh.quad.method.presentation.api.param.ShippingMethodResponse;
import jp.co.itechh.quad.method.presentation.api.param.ShippingMethodUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 配送方法　Helper
 *
 * @author PHAM QUANG DIEU (VJP)
 */
@Component
public class MethodHelper {

    /** 変換ユーティリティクラス */
    private final ConversionUtility conversionUtility;

    /**
     * コンストラクター
     */
    @Autowired
    public MethodHelper(ConversionUtility conversionUtility) {
        this.conversionUtility = conversionUtility;
    }

    /**
     * 配送方法のレスポンスに変換
     *
     * @param deliveryMethodDetailsDto 配送方法詳細Dtoクラス
     * @return shippingMethodResponse 配送方法レスポンス
     */
    public ShippingMethodResponse toShippingMethodResponse(DeliveryMethodDetailsDto deliveryMethodDetailsDto) {
        ShippingMethodResponse shippingMethodResponse = new ShippingMethodResponse();
        shippingMethodResponse.setDeliveryMethodTypeCarriageResponseList(new ArrayList<>());
        shippingMethodResponse.setDeliveryImpossibleAreaResultResponseList(new ArrayList<>());
        shippingMethodResponse.setDeliverySpecialChargeAreaResultResponseList(new ArrayList<>());

        if (!ObjectUtils.isEmpty(deliveryMethodDetailsDto.getDeliveryMethodEntity())) {
            DeliveryMethodResponse deliveryMethodResponse =
                            toDeliveryMethodResponse(deliveryMethodDetailsDto.getDeliveryMethodEntity());

            shippingMethodResponse.setDeliveryMethodResponse(deliveryMethodResponse);
        }
        if (deliveryMethodDetailsDto.getDeliveryMethodTypeCarriageEntityList() != null) {

            for (DeliveryMethodTypeCarriageEntity deliveryMethodTypeCarriageEntity : deliveryMethodDetailsDto.getDeliveryMethodTypeCarriageEntityList()) {
                DeliveryMethodTypeCarriageResponse deliveryMethodTypeCarriageResponse =
                                new DeliveryMethodTypeCarriageResponse();

                deliveryMethodTypeCarriageResponse.setDeliveryMethodSeq(
                                deliveryMethodTypeCarriageEntity.getDeliveryMethodSeq());
                deliveryMethodTypeCarriageResponse.setPrefectureType(
                                EnumTypeUtil.getValue(deliveryMethodTypeCarriageEntity.getPrefectureType()));
                deliveryMethodTypeCarriageResponse.setMaxPrice(deliveryMethodTypeCarriageEntity.getMaxPrice());
                deliveryMethodTypeCarriageResponse.setCarriage(deliveryMethodTypeCarriageEntity.getCarriage());
                deliveryMethodTypeCarriageResponse.setRegistTime(deliveryMethodTypeCarriageEntity.getRegistTime());
                deliveryMethodTypeCarriageResponse.setUpdateTime(deliveryMethodTypeCarriageEntity.getUpdateTime());

                shippingMethodResponse.getDeliveryMethodTypeCarriageResponseList()
                                      .add(deliveryMethodTypeCarriageResponse);
            }
        }
        shippingMethodResponse.setDeliverySpecialChargeAreaCount(
                        deliveryMethodDetailsDto.getDeliverySpecialChargeAreaCount());
        shippingMethodResponse.setDeliveryImpossibleAreaCount(
                        deliveryMethodDetailsDto.getDeliveryImpossibleAreaCount());

        if (deliveryMethodDetailsDto.getDeliveryImpossibleAreaResultDtoList() != null) {

            for (DeliveryImpossibleAreaResultDto deliveryImpossibleAreaResultDto : deliveryMethodDetailsDto.getDeliveryImpossibleAreaResultDtoList()) {
                DeliveryImpossibleAreaResultResponse deliveryImpossibleAreaResultResponse =
                                new DeliveryImpossibleAreaResultResponse();

                deliveryImpossibleAreaResultResponse.setDeliveryMethodSeq(
                                deliveryImpossibleAreaResultDto.getDeliveryMethodSeq());
                deliveryImpossibleAreaResultResponse.setZipcode(deliveryImpossibleAreaResultDto.getZipcode());
                deliveryImpossibleAreaResultResponse.setPrefecture(deliveryImpossibleAreaResultDto.getPrefecture());
                deliveryImpossibleAreaResultResponse.setCity(deliveryImpossibleAreaResultDto.getCity());
                deliveryImpossibleAreaResultResponse.setTown(deliveryImpossibleAreaResultDto.getTown());
                deliveryImpossibleAreaResultResponse.setNumbers(deliveryImpossibleAreaResultDto.getNumbers());
                deliveryImpossibleAreaResultResponse.setAddressList(deliveryImpossibleAreaResultDto.getAddressList());

                shippingMethodResponse.getDeliveryImpossibleAreaResultResponseList()
                                      .add(deliveryImpossibleAreaResultResponse);
            }
        }

        if (deliveryMethodDetailsDto.getDeliverySpecialChargeAreaResultDtoList() != null) {

            for (DeliverySpecialChargeAreaResultDto deliverySpecialChargeAreaResultDto : deliveryMethodDetailsDto.getDeliverySpecialChargeAreaResultDtoList()) {
                DeliverySpecialChargeAreaResultResponse deliverySpecialChargeAreaResultResponse =
                                new DeliverySpecialChargeAreaResultResponse();

                deliverySpecialChargeAreaResultResponse.setDeliveryMethodSeq(
                                deliverySpecialChargeAreaResultDto.getDeliveryMethodSeq());
                deliverySpecialChargeAreaResultResponse.setZipcode(deliverySpecialChargeAreaResultDto.getZipcode());
                deliverySpecialChargeAreaResultResponse.setPrefecture(
                                deliverySpecialChargeAreaResultDto.getPrefecture());
                deliverySpecialChargeAreaResultResponse.setCity(deliverySpecialChargeAreaResultDto.getCity());
                deliverySpecialChargeAreaResultResponse.setTown(deliverySpecialChargeAreaResultDto.getTown());
                deliverySpecialChargeAreaResultResponse.setNumbers(deliverySpecialChargeAreaResultDto.getNumbers());
                deliverySpecialChargeAreaResultResponse.setAddressList(
                                deliverySpecialChargeAreaResultDto.getAddressList());

                shippingMethodResponse.getDeliverySpecialChargeAreaResultResponseList()
                                      .add(deliverySpecialChargeAreaResultResponse);
            }
        }

        return shippingMethodResponse;
    }

    /**
     * 配送方法の詳細Dtoに変換する
     *
     * @param shippingMethodRegistRequest 配送方法登録リクエスト
     * @return deliveryMethodDetailsDto 配送方法詳細Dto
     */
    public DeliveryMethodDetailsDto toDeliveryMethodDetailsDto(ShippingMethodRegistRequest shippingMethodRegistRequest) {
        DeliveryMethodDetailsDto deliveryMethodDetailsDto = new DeliveryMethodDetailsDto();
        deliveryMethodDetailsDto.setDeliveryMethodTypeCarriageEntityList(new ArrayList<>());

        if (!ObjectUtils.isEmpty(shippingMethodRegistRequest.getDeliveryMethodRequest())) {
            DeliveryMethodEntity deliveryMethodEntity = new DeliveryMethodEntity();

            deliveryMethodEntity.setDeliveryMethodSeq(
                            shippingMethodRegistRequest.getDeliveryMethodRequest().getDeliveryMethodSeq());
            deliveryMethodEntity.setDeliveryMethodName(
                            shippingMethodRegistRequest.getDeliveryMethodRequest().getDeliveryMethodName());
            deliveryMethodEntity.setDeliveryMethodDisplayNamePC(
                            shippingMethodRegistRequest.getDeliveryMethodRequest().getDeliveryMethodDisplayNamePC());
            deliveryMethodEntity.setOpenStatusPC(EnumTypeUtil.getEnumFromValue(HTypeOpenDeleteStatus.class,
                                                                               shippingMethodRegistRequest.getDeliveryMethodRequest()
                                                                                                          .getOpenStatusPC()
                                                                              ));
            deliveryMethodEntity.setDeliveryNotePC(
                            shippingMethodRegistRequest.getDeliveryMethodRequest().getDeliveryNotePC());
            deliveryMethodEntity.setDeliveryMethodType(EnumTypeUtil.getEnumFromValue(HTypeDeliveryMethodType.class,
                                                                                     shippingMethodRegistRequest.getDeliveryMethodRequest()
                                                                                                                .getDeliveryMethodType()
                                                                                    ));
            deliveryMethodEntity.setEqualsCarriage(
                            shippingMethodRegistRequest.getDeliveryMethodRequest().getEqualsCarriage());
            deliveryMethodEntity.setLargeAmountDiscountPrice(
                            shippingMethodRegistRequest.getDeliveryMethodRequest().getLargeAmountDiscountPrice());
            deliveryMethodEntity.setLargeAmountDiscountCarriage(
                            shippingMethodRegistRequest.getDeliveryMethodRequest().getLargeAmountDiscountCarriage());
            deliveryMethodEntity.setShortfallDisplayFlag(EnumTypeUtil.getEnumFromValue(HTypeShortfallDisplayFlag.class,
                                                                                       shippingMethodRegistRequest.getDeliveryMethodRequest()
                                                                                                                  .getShortfallDisplayFlag()
                                                                                      ));
            if (shippingMethodRegistRequest.getDeliveryMethodRequest().getDeliveryLeadTime() != null) {
                deliveryMethodEntity.setDeliveryLeadTime(
                                shippingMethodRegistRequest.getDeliveryMethodRequest().getDeliveryLeadTime());
            }
            deliveryMethodEntity.setDeliveryChaseURL(
                            shippingMethodRegistRequest.getDeliveryMethodRequest().getDeliveryChaseURL());
            deliveryMethodEntity.setDeliveryChaseURLDisplayPeriod(
                            shippingMethodRegistRequest.getDeliveryMethodRequest().getDeliveryChaseURLDisplayPeriod());
            if (shippingMethodRegistRequest.getDeliveryMethodRequest().getPossibleSelectDays() != null) {
                deliveryMethodEntity.setPossibleSelectDays(
                                shippingMethodRegistRequest.getDeliveryMethodRequest().getPossibleSelectDays());
            }
            deliveryMethodEntity.setReceiverTimeZone1(
                            shippingMethodRegistRequest.getDeliveryMethodRequest().getReceiverTimeZone1());
            deliveryMethodEntity.setReceiverTimeZone2(
                            shippingMethodRegistRequest.getDeliveryMethodRequest().getReceiverTimeZone2());
            deliveryMethodEntity.setReceiverTimeZone3(
                            shippingMethodRegistRequest.getDeliveryMethodRequest().getReceiverTimeZone3());
            deliveryMethodEntity.setReceiverTimeZone4(
                            shippingMethodRegistRequest.getDeliveryMethodRequest().getReceiverTimeZone4());
            deliveryMethodEntity.setReceiverTimeZone5(
                            shippingMethodRegistRequest.getDeliveryMethodRequest().getReceiverTimeZone5());
            deliveryMethodEntity.setReceiverTimeZone6(
                            shippingMethodRegistRequest.getDeliveryMethodRequest().getReceiverTimeZone6());
            deliveryMethodEntity.setReceiverTimeZone7(
                            shippingMethodRegistRequest.getDeliveryMethodRequest().getReceiverTimeZone7());
            deliveryMethodEntity.setReceiverTimeZone8(
                            shippingMethodRegistRequest.getDeliveryMethodRequest().getReceiverTimeZone8());
            deliveryMethodEntity.setReceiverTimeZone9(
                            shippingMethodRegistRequest.getDeliveryMethodRequest().getReceiverTimeZone9());
            deliveryMethodEntity.setReceiverTimeZone10(
                            shippingMethodRegistRequest.getDeliveryMethodRequest().getReceiverTimeZone10());
            deliveryMethodEntity.setOrderDisplay(
                            shippingMethodRegistRequest.getDeliveryMethodRequest().getOrderDisplay());

            deliveryMethodDetailsDto.setDeliveryMethodEntity(deliveryMethodEntity);
        }
        if (shippingMethodRegistRequest.getDeliveryMethodTypeCarriageRequestList() != null) {

            for (DeliveryMethodTypeCarriageRequest deliveryMethodTypeCarriageRequest : shippingMethodRegistRequest.getDeliveryMethodTypeCarriageRequestList()) {
                DeliveryMethodTypeCarriageEntity deliveryMethodTypeCarriageEntity =
                                new DeliveryMethodTypeCarriageEntity();

                deliveryMethodTypeCarriageEntity.setDeliveryMethodSeq(
                                deliveryMethodTypeCarriageRequest.getDeliveryMethodSeq());
                deliveryMethodTypeCarriageEntity.setPrefectureType(
                                EnumTypeUtil.getEnumFromValue(HTypePrefectureType.class,
                                                              deliveryMethodTypeCarriageRequest.getPrefectureType()
                                                             ));
                deliveryMethodTypeCarriageEntity.setMaxPrice(deliveryMethodTypeCarriageRequest.getMaxPrice());
                deliveryMethodTypeCarriageEntity.setCarriage(deliveryMethodTypeCarriageRequest.getCarriage());

                deliveryMethodDetailsDto.getDeliveryMethodTypeCarriageEntityList()
                                        .add(deliveryMethodTypeCarriageEntity);
            }
        }

        return deliveryMethodDetailsDto;
    }

    /**
     * 配送方法エンティティに変換
     *
     * @param shippingMethodCheckRequest 配送方法リクエスト
     * @return deliveryMethodEntity 配送方法エンティティ
     */
    public DeliveryMethodEntity toDeliveryMethodEntity(ShippingMethodCheckRequest shippingMethodCheckRequest) {
        DeliveryMethodEntity deliveryMethodEntity = new DeliveryMethodEntity();

        if (!ObjectUtils.isEmpty(shippingMethodCheckRequest)) {
            deliveryMethodEntity.setDeliveryMethodSeq(shippingMethodCheckRequest.getDeliveryMethodSeq());
            deliveryMethodEntity.setDeliveryMethodName(shippingMethodCheckRequest.getDeliveryMethodName());
            deliveryMethodEntity.setDeliveryMethodDisplayNamePC(
                            shippingMethodCheckRequest.getDeliveryMethodDisplayNamePC());
            deliveryMethodEntity.setOpenStatusPC(EnumTypeUtil.getEnumFromValue(HTypeOpenDeleteStatus.class,
                                                                               shippingMethodCheckRequest.getOpenStatusPC()
                                                                              ));
            deliveryMethodEntity.setDeliveryNotePC(shippingMethodCheckRequest.getDeliveryNotePC());
            deliveryMethodEntity.setDeliveryMethodType(EnumTypeUtil.getEnumFromValue(HTypeDeliveryMethodType.class,
                                                                                     shippingMethodCheckRequest.getDeliveryMethodType()
                                                                                    ));
            deliveryMethodEntity.setEqualsCarriage(shippingMethodCheckRequest.getEqualsCarriage());
            deliveryMethodEntity.setLargeAmountDiscountPrice(shippingMethodCheckRequest.getLargeAmountDiscountPrice());
            deliveryMethodEntity.setLargeAmountDiscountCarriage(
                            shippingMethodCheckRequest.getLargeAmountDiscountCarriage());
            deliveryMethodEntity.setShortfallDisplayFlag(EnumTypeUtil.getEnumFromValue(HTypeShortfallDisplayFlag.class,
                                                                                       shippingMethodCheckRequest.getShortfallDisplayFlag()
                                                                                      ));
            if (shippingMethodCheckRequest.getDeliveryLeadTime() != null) {
                deliveryMethodEntity.setDeliveryLeadTime(shippingMethodCheckRequest.getDeliveryLeadTime());
            }
            deliveryMethodEntity.setDeliveryChaseURL(shippingMethodCheckRequest.getDeliveryChaseURL());
            deliveryMethodEntity.setDeliveryChaseURLDisplayPeriod(
                            shippingMethodCheckRequest.getDeliveryChaseURLDisplayPeriod());
            if (shippingMethodCheckRequest.getPossibleSelectDays() != null) {
                deliveryMethodEntity.setPossibleSelectDays(shippingMethodCheckRequest.getPossibleSelectDays());
            }
            deliveryMethodEntity.setReceiverTimeZone1(shippingMethodCheckRequest.getReceiverTimeZone1());
            deliveryMethodEntity.setReceiverTimeZone2(shippingMethodCheckRequest.getReceiverTimeZone2());
            deliveryMethodEntity.setReceiverTimeZone3(shippingMethodCheckRequest.getReceiverTimeZone3());
            deliveryMethodEntity.setReceiverTimeZone4(shippingMethodCheckRequest.getReceiverTimeZone4());
            deliveryMethodEntity.setReceiverTimeZone5(shippingMethodCheckRequest.getReceiverTimeZone5());
            deliveryMethodEntity.setReceiverTimeZone6(shippingMethodCheckRequest.getReceiverTimeZone6());
            deliveryMethodEntity.setReceiverTimeZone7(shippingMethodCheckRequest.getReceiverTimeZone7());
            deliveryMethodEntity.setReceiverTimeZone8(shippingMethodCheckRequest.getReceiverTimeZone8());
            deliveryMethodEntity.setReceiverTimeZone9(shippingMethodCheckRequest.getReceiverTimeZone9());
            deliveryMethodEntity.setReceiverTimeZone10(shippingMethodCheckRequest.getReceiverTimeZone10());
            deliveryMethodEntity.setOrderDisplay(shippingMethodCheckRequest.getDeliveryMethodSeq());
        }

        return deliveryMethodEntity;
    }

    /**
     * 配送方法の更新リクエストを配送方法詳細Dtoに変換する
     *
     * @param shippingMethodUpdateRequest 配送方法更新リクエスト
     * @return deliveryMethodDetailsDtoUpdate 配送方法詳細Dto
     */
    public DeliveryMethodDetailsDto updateRequestToDeliveryMethodDetailsDto(ShippingMethodUpdateRequest shippingMethodUpdateRequest) {

        DeliveryMethodDetailsDto deliveryMethodDetailsDtoUpdate = new DeliveryMethodDetailsDto();
        deliveryMethodDetailsDtoUpdate.setDeliveryMethodTypeCarriageEntityList(new ArrayList<>());

        if (!ObjectUtils.isEmpty(shippingMethodUpdateRequest.getDeliveryMethodRequest())) {
            DeliveryMethodEntity deliveryMethodEntity = new DeliveryMethodEntity();

            deliveryMethodEntity.setDeliveryMethodSeq(
                            shippingMethodUpdateRequest.getDeliveryMethodRequest().getDeliveryMethodSeq());
            deliveryMethodEntity.setDeliveryMethodName(
                            shippingMethodUpdateRequest.getDeliveryMethodRequest().getDeliveryMethodName());
            deliveryMethodEntity.setDeliveryMethodDisplayNamePC(
                            shippingMethodUpdateRequest.getDeliveryMethodRequest().getDeliveryMethodDisplayNamePC());
            deliveryMethodEntity.setOpenStatusPC(EnumTypeUtil.getEnumFromValue(HTypeOpenDeleteStatus.class,
                                                                               shippingMethodUpdateRequest.getDeliveryMethodRequest()
                                                                                                          .getOpenStatusPC()
                                                                              ));
            deliveryMethodEntity.setDeliveryNotePC(
                            shippingMethodUpdateRequest.getDeliveryMethodRequest().getDeliveryNotePC());
            deliveryMethodEntity.setDeliveryMethodType(EnumTypeUtil.getEnumFromValue(HTypeDeliveryMethodType.class,
                                                                                     shippingMethodUpdateRequest.getDeliveryMethodRequest()
                                                                                                                .getDeliveryMethodType()
                                                                                    ));
            deliveryMethodEntity.setEqualsCarriage(
                            shippingMethodUpdateRequest.getDeliveryMethodRequest().getEqualsCarriage());
            deliveryMethodEntity.setLargeAmountDiscountPrice(
                            shippingMethodUpdateRequest.getDeliveryMethodRequest().getLargeAmountDiscountPrice());
            deliveryMethodEntity.setLargeAmountDiscountCarriage(
                            shippingMethodUpdateRequest.getDeliveryMethodRequest().getLargeAmountDiscountCarriage());
            deliveryMethodEntity.setShortfallDisplayFlag(EnumTypeUtil.getEnumFromValue(HTypeShortfallDisplayFlag.class,
                                                                                       shippingMethodUpdateRequest.getDeliveryMethodRequest()
                                                                                                                  .getShortfallDisplayFlag()
                                                                                      ));
            if (shippingMethodUpdateRequest.getDeliveryMethodRequest().getDeliveryLeadTime() != null) {
                deliveryMethodEntity.setDeliveryLeadTime(
                                shippingMethodUpdateRequest.getDeliveryMethodRequest().getDeliveryLeadTime());
            }
            deliveryMethodEntity.setDeliveryChaseURL(
                            shippingMethodUpdateRequest.getDeliveryMethodRequest().getDeliveryChaseURL());
            deliveryMethodEntity.setDeliveryChaseURLDisplayPeriod(
                            shippingMethodUpdateRequest.getDeliveryMethodRequest().getDeliveryChaseURLDisplayPeriod());
            if (shippingMethodUpdateRequest.getDeliveryMethodRequest().getPossibleSelectDays() != null) {
                deliveryMethodEntity.setPossibleSelectDays(
                                shippingMethodUpdateRequest.getDeliveryMethodRequest().getPossibleSelectDays());
            }
            deliveryMethodEntity.setReceiverTimeZone1(
                            shippingMethodUpdateRequest.getDeliveryMethodRequest().getReceiverTimeZone1());
            deliveryMethodEntity.setReceiverTimeZone2(
                            shippingMethodUpdateRequest.getDeliveryMethodRequest().getReceiverTimeZone2());
            deliveryMethodEntity.setReceiverTimeZone3(
                            shippingMethodUpdateRequest.getDeliveryMethodRequest().getReceiverTimeZone3());
            deliveryMethodEntity.setReceiverTimeZone4(
                            shippingMethodUpdateRequest.getDeliveryMethodRequest().getReceiverTimeZone4());
            deliveryMethodEntity.setReceiverTimeZone5(
                            shippingMethodUpdateRequest.getDeliveryMethodRequest().getReceiverTimeZone5());
            deliveryMethodEntity.setReceiverTimeZone6(
                            shippingMethodUpdateRequest.getDeliveryMethodRequest().getReceiverTimeZone6());
            deliveryMethodEntity.setReceiverTimeZone7(
                            shippingMethodUpdateRequest.getDeliveryMethodRequest().getReceiverTimeZone7());
            deliveryMethodEntity.setReceiverTimeZone8(
                            shippingMethodUpdateRequest.getDeliveryMethodRequest().getReceiverTimeZone8());
            deliveryMethodEntity.setReceiverTimeZone9(
                            shippingMethodUpdateRequest.getDeliveryMethodRequest().getReceiverTimeZone9());
            deliveryMethodEntity.setReceiverTimeZone10(
                            shippingMethodUpdateRequest.getDeliveryMethodRequest().getReceiverTimeZone10());
            deliveryMethodEntity.setOrderDisplay(
                            shippingMethodUpdateRequest.getDeliveryMethodRequest().getOrderDisplay());

            deliveryMethodDetailsDtoUpdate.setDeliveryMethodEntity(deliveryMethodEntity);
        }
        if (shippingMethodUpdateRequest.getDeliveryMethodTypeCarriageRequestList() != null) {
            for (DeliveryMethodTypeCarriageRequest deliveryMethodTypeCarriageRequest : shippingMethodUpdateRequest.getDeliveryMethodTypeCarriageRequestList()) {
                DeliveryMethodTypeCarriageEntity deliveryMethodTypeCarriageEntity =
                                new DeliveryMethodTypeCarriageEntity();

                deliveryMethodTypeCarriageEntity.setDeliveryMethodSeq(
                                deliveryMethodTypeCarriageRequest.getDeliveryMethodSeq());
                deliveryMethodTypeCarriageEntity.setPrefectureType(
                                EnumTypeUtil.getEnumFromValue(HTypePrefectureType.class,
                                                              deliveryMethodTypeCarriageRequest.getPrefectureType()
                                                             ));
                deliveryMethodTypeCarriageEntity.setMaxPrice(deliveryMethodTypeCarriageRequest.getMaxPrice());
                deliveryMethodTypeCarriageEntity.setCarriage(deliveryMethodTypeCarriageRequest.getCarriage());

                deliveryMethodDetailsDtoUpdate.getDeliveryMethodTypeCarriageEntityList()
                                              .add(deliveryMethodTypeCarriageEntity);
            }
        }

        return deliveryMethodDetailsDtoUpdate;
    }

    /**
     * 配送方法エンティティリストに変換
     *
     * @param shippingMethodListUpdateRequest 配送方法一覧更新リクエスト
     * @return deliveryMethodEntityList 配送方法エンティティリスト
     */
    public List<DeliveryMethodEntity> toDeliveryMethodEntityList(ShippingMethodListUpdateRequest shippingMethodListUpdateRequest) {
        // 返却用のリストを作成
        List<DeliveryMethodEntity> deliveryMethodEntityList = new ArrayList<>();

        if (shippingMethodListUpdateRequest.getShippingMethodListUpdate() != null) {

            for (DeliveryMethodRequest deliveryMethodRequest : shippingMethodListUpdateRequest.getShippingMethodListUpdate()) {
                DeliveryMethodEntity deliveryMethodEntity = new DeliveryMethodEntity();
                deliveryMethodEntity.setDeliveryMethodSeq(deliveryMethodRequest.getDeliveryMethodSeq());
                deliveryMethodEntity.setDeliveryMethodName(deliveryMethodRequest.getDeliveryMethodName());
                deliveryMethodEntity.setDeliveryMethodDisplayNamePC(
                                deliveryMethodRequest.getDeliveryMethodDisplayNamePC());
                deliveryMethodEntity.setOpenStatusPC(EnumTypeUtil.getEnumFromValue(HTypeOpenDeleteStatus.class,
                                                                                   deliveryMethodRequest.getOpenStatusPC()
                                                                                  ));
                deliveryMethodEntity.setDeliveryNotePC(deliveryMethodRequest.getDeliveryNotePC());
                deliveryMethodEntity.setDeliveryMethodType(EnumTypeUtil.getEnumFromValue(HTypeDeliveryMethodType.class,
                                                                                         deliveryMethodRequest.getDeliveryMethodType()
                                                                                        ));
                deliveryMethodEntity.setEqualsCarriage(deliveryMethodRequest.getEqualsCarriage());
                deliveryMethodEntity.setLargeAmountDiscountPrice(deliveryMethodRequest.getLargeAmountDiscountPrice());
                deliveryMethodEntity.setLargeAmountDiscountCarriage(
                                deliveryMethodRequest.getLargeAmountDiscountCarriage());
                deliveryMethodEntity.setShortfallDisplayFlag(
                                EnumTypeUtil.getEnumFromValue(HTypeShortfallDisplayFlag.class,
                                                              deliveryMethodRequest.getShortfallDisplayFlag()
                                                             ));
                if (deliveryMethodRequest.getDeliveryLeadTime() != null) {
                    deliveryMethodEntity.setDeliveryLeadTime(deliveryMethodRequest.getDeliveryLeadTime());
                }
                deliveryMethodEntity.setDeliveryChaseURL(deliveryMethodRequest.getDeliveryChaseURL());
                deliveryMethodEntity.setDeliveryChaseURLDisplayPeriod(
                                deliveryMethodRequest.getDeliveryChaseURLDisplayPeriod());
                if (deliveryMethodRequest.getPossibleSelectDays() != null) {
                    deliveryMethodEntity.setPossibleSelectDays(deliveryMethodRequest.getPossibleSelectDays());
                }
                deliveryMethodEntity.setReceiverTimeZone1(deliveryMethodRequest.getReceiverTimeZone1());
                deliveryMethodEntity.setReceiverTimeZone2(deliveryMethodRequest.getReceiverTimeZone2());
                deliveryMethodEntity.setReceiverTimeZone3(deliveryMethodRequest.getReceiverTimeZone3());
                deliveryMethodEntity.setReceiverTimeZone4(deliveryMethodRequest.getReceiverTimeZone4());
                deliveryMethodEntity.setReceiverTimeZone5(deliveryMethodRequest.getReceiverTimeZone5());
                deliveryMethodEntity.setReceiverTimeZone6(deliveryMethodRequest.getReceiverTimeZone6());
                deliveryMethodEntity.setReceiverTimeZone7(deliveryMethodRequest.getReceiverTimeZone7());
                deliveryMethodEntity.setReceiverTimeZone8(deliveryMethodRequest.getReceiverTimeZone8());
                deliveryMethodEntity.setReceiverTimeZone9(deliveryMethodRequest.getReceiverTimeZone9());
                deliveryMethodEntity.setReceiverTimeZone10(deliveryMethodRequest.getReceiverTimeZone10());
                deliveryMethodEntity.setOrderDisplay(deliveryMethodRequest.getOrderDisplay());

                deliveryMethodEntityList.add(deliveryMethodEntity);
            }
        }

        return deliveryMethodEntityList;
    }

    /**
     * 配送方法リストレスポンスに変換
     *
     * @param deliveryMethodEntityList 配送方法エンティティリスト取得
     * @return 配送方法リストレスポンス
     */
    public ShippingMethodListResponse toShippingMethodListResponse(List<DeliveryMethodEntity> deliveryMethodEntityList) {
        ShippingMethodListResponse shippingMethodListResponse = new ShippingMethodListResponse();
        List<ShippingMethodResponse> listshippingMethodResponse = new ArrayList<>();

        for (DeliveryMethodEntity deliveryMethodEntity : deliveryMethodEntityList) {
            ShippingMethodResponse shippingMethodResponse = new ShippingMethodResponse();

            DeliveryMethodResponse deliveryMethodResponse = toDeliveryMethodResponse(deliveryMethodEntity);

            shippingMethodResponse.setDeliveryMethodResponse(deliveryMethodResponse);
            listshippingMethodResponse.add(shippingMethodResponse);
        }
        shippingMethodListResponse.setShippingMethodListResponse(listshippingMethodResponse);

        return shippingMethodListResponse;
    }

    /**
     * 配送方法レスポンスに変換
     *
     * @param deliveryMethodEntity 配送方法クラス
     * @return 配送方法レスポンス
     */
    public DeliveryMethodResponse toDeliveryMethodResponse(DeliveryMethodEntity deliveryMethodEntity) {
        DeliveryMethodResponse deliveryMethodResponse = new DeliveryMethodResponse();

        deliveryMethodResponse.setDeliveryMethodSeq(deliveryMethodEntity.getDeliveryMethodSeq());
        deliveryMethodResponse.setDeliveryMethodName(deliveryMethodEntity.getDeliveryMethodName());
        deliveryMethodResponse.setDeliveryMethodDisplayNamePC(deliveryMethodEntity.getDeliveryMethodDisplayNamePC());
        deliveryMethodResponse.setOpenStatusPC(EnumTypeUtil.getValue(deliveryMethodEntity.getOpenStatusPC()));
        deliveryMethodResponse.setDeliveryNotePC(deliveryMethodEntity.getDeliveryNotePC());
        deliveryMethodResponse.setDeliveryMethodType(
                        EnumTypeUtil.getValue(deliveryMethodEntity.getDeliveryMethodType()));
        deliveryMethodResponse.setEqualsCarriage(deliveryMethodEntity.getEqualsCarriage());
        deliveryMethodResponse.setLargeAmountDiscountPrice(deliveryMethodEntity.getLargeAmountDiscountPrice());
        deliveryMethodResponse.setLargeAmountDiscountCarriage(deliveryMethodEntity.getLargeAmountDiscountCarriage());
        deliveryMethodResponse.setShortfallDisplayFlag(
                        EnumTypeUtil.getValue(deliveryMethodEntity.getShortfallDisplayFlag()));
        deliveryMethodResponse.setDeliveryLeadTime(deliveryMethodEntity.getDeliveryLeadTime());
        deliveryMethodResponse.setDeliveryChaseURL(deliveryMethodEntity.getDeliveryChaseURL());
        deliveryMethodResponse.setDeliveryChaseURLDisplayPeriod(
                        deliveryMethodEntity.getDeliveryChaseURLDisplayPeriod());
        deliveryMethodResponse.setPossibleSelectDays(deliveryMethodEntity.getPossibleSelectDays());
        deliveryMethodResponse.setReceiverTimeZone1(deliveryMethodEntity.getReceiverTimeZone1());
        deliveryMethodResponse.setReceiverTimeZone2(deliveryMethodEntity.getReceiverTimeZone2());
        deliveryMethodResponse.setReceiverTimeZone3(deliveryMethodEntity.getReceiverTimeZone3());
        deliveryMethodResponse.setReceiverTimeZone4(deliveryMethodEntity.getReceiverTimeZone4());
        deliveryMethodResponse.setReceiverTimeZone5(deliveryMethodEntity.getReceiverTimeZone5());
        deliveryMethodResponse.setReceiverTimeZone6(deliveryMethodEntity.getReceiverTimeZone6());
        deliveryMethodResponse.setReceiverTimeZone7(deliveryMethodEntity.getReceiverTimeZone7());
        deliveryMethodResponse.setReceiverTimeZone8(deliveryMethodEntity.getReceiverTimeZone8());
        deliveryMethodResponse.setReceiverTimeZone9(deliveryMethodEntity.getReceiverTimeZone9());
        deliveryMethodResponse.setReceiverTimeZone10(deliveryMethodEntity.getReceiverTimeZone10());
        deliveryMethodResponse.setOrderDisplay(deliveryMethodEntity.getOrderDisplay());
        deliveryMethodResponse.setRegistTime(deliveryMethodEntity.getRegistTime());
        deliveryMethodResponse.setUpdateTime(deliveryMethodEntity.getUpdateTime());

        return deliveryMethodResponse;
    }

    /** ここからDDD設計範囲 */

    /**
     * 選択可能配送方法一覧から選択可能配送方法一覧レスポンスに変換
     *
     * @param selectAbleList 選択可能配送方法一覧
     * @return response 選択可能配送方法一覧レスポンス
     */
    public SelectableShippingMethodListResponse toSelectableShippingMethodListResponse(List<GetSelectableShippingMethodListUseCaseDto> selectAbleList) {

        if (CollectionUtils.isEmpty(selectAbleList)) {
            return null;
        }

        SelectableShippingMethodListResponse response = new SelectableShippingMethodListResponse();

        List<SelectableShippingMethod> methodList = new ArrayList<>();
        for (int i = 0; i < selectAbleList.size(); i++) {
            SelectableShippingMethod method = new SelectableShippingMethod();
            method.setShippingMethodId(selectAbleList.get(i).getShippingMethodId());
            method.setShippingMethodName(selectAbleList.get(i).getShippingMethodName());
            method.setShippingMethodNote(selectAbleList.get(i).getShippingMethodNote());
            method.setReceiverDateList(selectAbleList.get(i).getReceiverDateList());
            method.setReceiverTimeZoneList(selectAbleList.get(i).getReceiverTimeZoneList());
            methodList.add(method);
        }
        response.setSelectableShippingMethodList(methodList);

        return response;
    }

    /**
     * To delivery search for dao condition dto delivery search for dao condition dto.
     *
     * @param carriageShippingMethodListGetRequest the carriage shipping method list get request
     * @return the delivery search for dao condition dto
     */
    public DeliverySearchForDaoConditionDto toDeliverySearchForDaoConditionDto(CarriageShippingMethodListGetRequest carriageShippingMethodListGetRequest) {

        if (ObjectUtils.isEmpty(carriageShippingMethodListGetRequest)) {
            return null;
        }

        DeliverySearchForDaoConditionDto deliverySearchForDaoConditionDto = new DeliverySearchForDaoConditionDto();

        deliverySearchForDaoConditionDto.setShopSeq(1001);
        deliverySearchForDaoConditionDto.setPrefectureType(EnumTypeUtil.getEnumFromValue(HTypePrefectureType.class,
                                                                                         carriageShippingMethodListGetRequest.getPrefectureType()
                                                                                        ));
        deliverySearchForDaoConditionDto.setTotalGoodsPrice(carriageShippingMethodListGetRequest.getTotalGoodsPrice());
        deliverySearchForDaoConditionDto.setZipcode(carriageShippingMethodListGetRequest.getZipcode());

        return deliverySearchForDaoConditionDto;
    }

    /**
     * 配送リストレスポンスに変換
     *
     * @param deliveryDtos 配送DTOクラスリスト
     * @return 配送リストレスポンス
     */
    public CarriageShippingMethodListResponse toCarriageShippingMethodListResponse(List<DeliveryDto> deliveryDtos) {
        if (CollectionUtils.isEmpty(deliveryDtos)) {
            return null;
        }

        CarriageShippingMethodListResponse carriageShippingMethodListResponse =
                        new CarriageShippingMethodListResponse();
        List<DeliveryResponse> deliveryListResponse = new ArrayList<>();
        for (DeliveryDto deliveryDto : deliveryDtos) {
            DeliveryResponse deliveryResponse = new DeliveryResponse();

            DeliveryDetailsResponse deliveryDetailsResponse =
                            toDeliveryDetailsResponse(deliveryDto.getDeliveryDetailsDto());
            ReceiverDateResponse receiverDateResponse = toReceiverDateResponse(deliveryDto.getReceiverDateDto());

            deliveryResponse.setDeliveryDetailsResponse(deliveryDetailsResponse);
            deliveryResponse.setCarriage(deliveryDto.getCarriage());
            deliveryResponse.setSelectClass(deliveryDto.isSelectClass());
            deliveryResponse.setReceiverDateResponse(receiverDateResponse);
            deliveryResponse.setSpecialChargeAreaFlag(deliveryDto.isSpecialChargeAreaFlag());

            deliveryListResponse.add(deliveryResponse);
        }
        carriageShippingMethodListResponse.setDeliveryListResponse(deliveryListResponse);

        return carriageShippingMethodListResponse;
    }

    /**
     * 配送方法詳細レスポンスに変換
     *
     * @param deliveryDetailsDto 配送方法詳細DTOクラス
     * @return 配送方法詳細レスポンス
     */
    private DeliveryDetailsResponse toDeliveryDetailsResponse(DeliveryDetailsDto deliveryDetailsDto) {

        if (ObjectUtils.isEmpty(deliveryDetailsDto)) {
            return null;
        }

        DeliveryDetailsResponse deliveryDetailsResponse = new DeliveryDetailsResponse();

        deliveryDetailsResponse.setDeliveryMethodSeq(deliveryDetailsDto.getDeliveryMethodSeq());
        deliveryDetailsResponse.setDeliveryMethodName(deliveryDetailsDto.getDeliveryMethodName());
        deliveryDetailsResponse.setDeliveryMethodDisplayNamePC(deliveryDetailsDto.getDeliveryMethodDisplayNamePC());
        deliveryDetailsResponse.setOpenStatusPC(EnumTypeUtil.getValue(deliveryDetailsDto.getOpenStatusPC()));
        deliveryDetailsResponse.setDeliveryNotePC(deliveryDetailsDto.getDeliveryNotePC());
        deliveryDetailsResponse.setDeliveryMethodType(
                        EnumTypeUtil.getValue(deliveryDetailsDto.getDeliveryMethodType()));
        deliveryDetailsResponse.setEqualsCarriage(deliveryDetailsDto.getEqualsCarriage());
        deliveryDetailsResponse.setLargeAmountDiscountPrice(deliveryDetailsDto.getLargeAmountDiscountPrice());
        deliveryDetailsResponse.setLargeAmountDiscountCarriage(deliveryDetailsDto.getLargeAmountDiscountCarriage());
        deliveryDetailsResponse.setShortfallDisplayFlag(
                        EnumTypeUtil.getValue(deliveryDetailsDto.getShortfallDisplayFlag()));
        deliveryDetailsResponse.setDeliveryLeadTime(deliveryDetailsDto.getDeliveryLeadTime());
        deliveryDetailsResponse.setPossibleSelectDays(deliveryDetailsDto.getPossibleSelectDays());
        deliveryDetailsResponse.setReceiverTimeZone1(deliveryDetailsDto.getReceiverTimeZone1());
        deliveryDetailsResponse.setReceiverTimeZone2(deliveryDetailsDto.getReceiverTimeZone2());
        deliveryDetailsResponse.setReceiverTimeZone3(deliveryDetailsDto.getReceiverTimeZone3());
        deliveryDetailsResponse.setReceiverTimeZone4(deliveryDetailsDto.getReceiverTimeZone4());
        deliveryDetailsResponse.setReceiverTimeZone5(deliveryDetailsDto.getReceiverTimeZone5());
        deliveryDetailsResponse.setReceiverTimeZone6(deliveryDetailsDto.getReceiverTimeZone6());
        deliveryDetailsResponse.setReceiverTimeZone7(deliveryDetailsDto.getReceiverTimeZone7());
        deliveryDetailsResponse.setReceiverTimeZone8(deliveryDetailsDto.getReceiverTimeZone8());
        deliveryDetailsResponse.setReceiverTimeZone9(deliveryDetailsDto.getReceiverTimeZone9());
        deliveryDetailsResponse.setReceiverTimeZone10(deliveryDetailsDto.getReceiverTimeZone10());
        deliveryDetailsResponse.setOrderDisplay(deliveryDetailsDto.getOrderDisplay());
        deliveryDetailsResponse.setPrefectureType(EnumTypeUtil.getValue(deliveryDetailsDto.getPrefectureType()));
        deliveryDetailsResponse.setMaxPrice(deliveryDetailsDto.getMaxPrice());
        deliveryDetailsResponse.setCarriage(deliveryDetailsDto.getCarriage());

        return deliveryDetailsResponse;
    }

    /**
     * 通知サブドメインリストに変換
     *
     * @param receiverDateDto お届け希望日Dto
     * @return 通知サブドメインリスト
     */
    private ReceiverDateResponse toReceiverDateResponse(ReceiverDateDto receiverDateDto) {

        if (ObjectUtils.isEmpty(receiverDateDto)) {
            return null;
        }

        ReceiverDateResponse receiverDateResponse = new ReceiverDateResponse();

        receiverDateResponse.setDateMap(receiverDateDto.getDateMap());
        receiverDateResponse.setReceiverDateDesignationFlag(
                        EnumTypeUtil.getValue(receiverDateDto.getReceiverDateDesignationFlag()));
        receiverDateResponse.setShortestDeliveryDateToRegist(
                        conversionUtility.toString(receiverDateDto.getShortestDeliveryDateToRegist()));

        return receiverDateResponse;
    }

    /** ここまでDDD設計範囲 */

}