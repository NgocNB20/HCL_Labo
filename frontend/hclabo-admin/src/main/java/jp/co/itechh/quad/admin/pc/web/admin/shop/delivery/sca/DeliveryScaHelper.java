/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.shop.delivery.sca;

import jp.co.itechh.quad.admin.base.util.common.CollectionUtil;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.ConversionUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeDeliveryMethodType;
import jp.co.itechh.quad.admin.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.admin.constant.type.HTypePrefectureType;
import jp.co.itechh.quad.admin.constant.type.HTypeShortfallDisplayFlag;
import jp.co.itechh.quad.admin.dto.shop.ZipCodeAddressDto;
import jp.co.itechh.quad.admin.dto.shop.delivery.DeliverySpecialChargeAreaConditionDto;
import jp.co.itechh.quad.admin.dto.shop.delivery.DeliverySpecialChargeAreaResultDto;
import jp.co.itechh.quad.admin.entity.shop.delivery.DeliveryMethodEntity;
import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import jp.co.itechh.quad.method.presentation.api.param.DeliveryMethodResponse;
import jp.co.itechh.quad.method.presentation.api.param.ShippingMethodResponse;
import jp.co.itechh.quad.specialchargearea.presentation.api.param.SpecialChargeAreaDeleteRequest;
import jp.co.itechh.quad.specialchargearea.presentation.api.param.SpecialChargeAreaListGetRequest;
import jp.co.itechh.quad.specialchargearea.presentation.api.param.SpecialChargeAreaListResponse;
import jp.co.itechh.quad.specialchargearea.presentation.api.param.SpecialChargeAreaRegistRequest;
import jp.co.itechh.quad.specialchargearea.presentation.api.param.SpecialChargeAreaResponse;
import jp.co.itechh.quad.zipcode.presentation.api.param.ZipCodeAddressGetRequest;
import jp.co.itechh.quad.zipcode.presentation.api.param.ZipCodeAddressResponse;
import jp.co.itechh.quad.zipcode.presentation.api.param.ZipCodeResponse;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 配送方法設定：特別料金エリア設定検索画面用Helper
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class DeliveryScaHelper {

    /**
     * 変換ユーティリティクラス
     */
    private final ConversionUtility conversionUtility;

    /**
     * コンストラクタ
     *
     * @param conversionUtility 変換ユーティリティクラス
     */
    public DeliveryScaHelper(ConversionUtility conversionUtility) {
        this.conversionUtility = conversionUtility;
    }

    /**
     * 検索結果をpageItemsに反映します
     *
     * @param resultList       List&lt;DeliverySpecialChargeAreaResultDto&gt;
     * @param deliveryScaModel DeliveryScaModel
     * @param conditionDto     DeliverySpecialChargeAreaConditionDto
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public void convertToIndexPageItemForResult(List<DeliverySpecialChargeAreaResultDto> resultList,
                                                DeliveryScaModel deliveryScaModel,
                                                DeliverySpecialChargeAreaConditionDto conditionDto)
                    throws IllegalAccessException, InvocationTargetException {
        List<DeliveryScaModelItem> deliveryScaModelItems = new ArrayList<>();

        // オフセット+1
        int index = deliveryScaModel.getPageInfo().getOffset() + 1;

        if ((resultList != null) && !resultList.isEmpty()) {
            for (DeliverySpecialChargeAreaResultDto resultDto : resultList) {
                DeliveryScaModelItem deliveryScaModelItem =
                                ApplicationContextUtility.getBean(DeliveryScaModelItem.class);
                deliveryScaModelItem.setResultDataIndex(index++);
                BeanUtils.copyProperties(deliveryScaModelItem, resultDto);
                deliveryScaModelItems.add(deliveryScaModelItem);
            }
        }
        deliveryScaModel.setResultItems(deliveryScaModelItems);
    }

    /**
     * 検索結果をIndexPageに反映します
     *
     * @param deliveryScaModel IndexPage
     * @param resultEntity     DeliveryMethodEntity
     */
    public void convertToRegistPageForResult(DeliveryScaModel deliveryScaModel, DeliveryMethodEntity resultEntity) {
        deliveryScaModel.setDeliveryMethodName(resultEntity.getDeliveryMethodName());
        deliveryScaModel.setDeliveryMethodType(resultEntity.getDeliveryMethodType());
        deliveryScaModel.setOpenStatusPC(resultEntity.getOpenStatusPC());
    }

    /**
     * 郵便番号住所情報をRegistPage.addressに変換します
     *
     * @param registPage 特別料金エリア設定検索画面用モデル
     * @param entityList 郵便番号住所情報Dtoクラスリスト
     */
    public void convertToRegistPageForZipCodeResult(DeliveryScaModel registPage, List<ZipCodeAddressDto> entityList) {

        // 郵便番号に複数の住所が紐づいている場合にはすべての住所を表示する仕様
        final String lineSeparator = "<br />";
        StringBuilder builder = new StringBuilder();
        Iterator<ZipCodeAddressDto> ite = entityList.iterator();
        ZipCodeAddressDto entityDto = ite.next();

        builder.append(entityDto.getPrefectureName());
        builder.append(entityDto.getCityName());
        builder.append(entityDto.getTownName());
        builder.append(entityDto.getNumbers());

        while (ite.hasNext()) {
            entityDto = ite.next();
            builder.append(lineSeparator);
            builder.append(entityDto.getPrefectureName());
            builder.append(entityDto.getCityName());
            builder.append(entityDto.getTownName());
            builder.append(entityDto.getNumbers());
        }

        registPage.setAddress(builder.toString());
    }

    /**
     * 配送方法クラスに変換します
     *
     * @param shippingMethodResponse 配送方法レスポンス
     * @return 配送方法クラス
     */
    public DeliveryMethodEntity toDeliveryMethodEntity(ShippingMethodResponse shippingMethodResponse) {
        if (ObjectUtils.isEmpty(shippingMethodResponse.getDeliveryMethodResponse())) {
            return null;
        }

        DeliveryMethodEntity deliveryMethodEntity = new DeliveryMethodEntity();

        DeliveryMethodResponse deliveryMethodResponse = shippingMethodResponse.getDeliveryMethodResponse();

        if (ObjectUtils.isNotEmpty(deliveryMethodResponse)) {
            deliveryMethodEntity.setDeliveryMethodSeq(deliveryMethodResponse.getDeliveryMethodSeq());
            deliveryMethodEntity.setDeliveryMethodName(deliveryMethodResponse.getDeliveryMethodName());
            deliveryMethodEntity.setDeliveryMethodDisplayNamePC(
                            deliveryMethodResponse.getDeliveryMethodDisplayNamePC());
            if (deliveryMethodResponse.getOpenStatusPC() != null) {
                deliveryMethodEntity.setOpenStatusPC(EnumTypeUtil.getEnumFromValue(HTypeOpenDeleteStatus.class,
                                                                                   deliveryMethodResponse.getOpenStatusPC()
                                                                                  ));
            }
            deliveryMethodEntity.setDeliveryNotePC(deliveryMethodResponse.getDeliveryNotePC());
            if (deliveryMethodResponse.getDeliveryMethodType() != null) {
                deliveryMethodEntity.setDeliveryMethodType(EnumTypeUtil.getEnumFromValue(HTypeDeliveryMethodType.class,
                                                                                         deliveryMethodResponse.getDeliveryMethodType()
                                                                                        ));
            }
            deliveryMethodEntity.setEqualsCarriage(deliveryMethodResponse.getEqualsCarriage());
            deliveryMethodEntity.setLargeAmountDiscountPrice(deliveryMethodResponse.getLargeAmountDiscountPrice());
            deliveryMethodEntity.setLargeAmountDiscountCarriage(
                            deliveryMethodResponse.getLargeAmountDiscountCarriage());
            if (deliveryMethodResponse.getShortfallDisplayFlag() != null) {
                deliveryMethodEntity.setShortfallDisplayFlag(
                                EnumTypeUtil.getEnumFromValue(HTypeShortfallDisplayFlag.class,
                                                              deliveryMethodResponse.getShortfallDisplayFlag()
                                                             ));
            }
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
            deliveryMethodEntity.setRegistTime(conversionUtility.toTimestamp(deliveryMethodResponse.getRegistTime()));
            deliveryMethodEntity.setUpdateTime(conversionUtility.toTimestamp(deliveryMethodResponse.getUpdateTime()));
        }

        return deliveryMethodEntity;
    }

    /**
     * 特別料金エリア削除リクエストに変換します
     *
     * @param deliveryScaModel 特別料金エリア設定検索画面用モデル
     * @return 特別料金エリア削除リクエスト
     */
    public SpecialChargeAreaDeleteRequest toSpecialChargeAreaDeleteRequest(DeliveryScaModel deliveryScaModel) {
        SpecialChargeAreaDeleteRequest specialChargeAreaDeleteRequest = new SpecialChargeAreaDeleteRequest();

        if (CollectionUtil.isNotEmpty(deliveryScaModel.getResultItems())) {
            List<String> deleteList = new ArrayList<>();
            for (DeliveryScaModelItem deliveryScaModelItem : deliveryScaModel.getResultItems()) {

                if (deliveryScaModelItem.isCheck()) {
                    deleteList.add(deliveryScaModelItem.getZipcode());
                }
            }
            specialChargeAreaDeleteRequest.setDeleteList(deleteList);
        }
        return specialChargeAreaDeleteRequest;
    }

    /**
     * 特別料金エリア一覧取得リクエストに変換します
     *
     * @param deliveryScaModel 特別料金エリア設定検索画面用モデル
     * @return 特別料金エリア一覧取得リクエスト
     */
    public SpecialChargeAreaListGetRequest toSpecialChargeAreaListGetRequest(DeliveryScaModel deliveryScaModel) {
        SpecialChargeAreaListGetRequest specialChargeAreaListGetRequest = new SpecialChargeAreaListGetRequest();

        if (ObjectUtils.isNotEmpty(deliveryScaModel)) {

            if (StringUtils.isEmpty(deliveryScaModel.getPrefectureName())) {
                specialChargeAreaListGetRequest.setPrefecture("");
            } else {
                specialChargeAreaListGetRequest.setPrefecture(EnumTypeUtil.getEnumFromValue(HTypePrefectureType.class,
                                                                                            deliveryScaModel.getPrefectureName()
                                                                                           ).getLabel());
            }
        }

        return specialChargeAreaListGetRequest;
    }

    /**
     * 配送特別料金エリア詳細Dtoクラスリストに変換します
     *
     * @param specialChargeAreaListResponse 特別料金エリア一覧レスポンス
     * @return 配送特別料金エリア詳細Dtoクラスリスト
     */
    public List<DeliverySpecialChargeAreaResultDto> toDeliverySpecialChargeAreaResultDtoList(
                    SpecialChargeAreaListResponse specialChargeAreaListResponse) {
        List<DeliverySpecialChargeAreaResultDto> deliverySpecialChargeAreaResultDtoList = new ArrayList<>();

        if (CollectionUtil.isNotEmpty(specialChargeAreaListResponse.getSpecialChargeAreaList())) {
            for (SpecialChargeAreaResponse specialChargeAreaResponse : specialChargeAreaListResponse.getSpecialChargeAreaList()) {
                DeliverySpecialChargeAreaResultDto deliverySpecialChargeAreaResultDto =
                                new DeliverySpecialChargeAreaResultDto();

                deliverySpecialChargeAreaResultDto.setDeliveryMethodSeq(
                                specialChargeAreaResponse.getDeliveryMethodSeq());
                deliverySpecialChargeAreaResultDto.setZipcode(specialChargeAreaResponse.getZipcode());
                deliverySpecialChargeAreaResultDto.setCarriage(specialChargeAreaResponse.getCarriage());
                deliverySpecialChargeAreaResultDto.setPrefecture(specialChargeAreaResponse.getPrefecture());
                deliverySpecialChargeAreaResultDto.setCity(specialChargeAreaResponse.getCity());
                deliverySpecialChargeAreaResultDto.setTown(specialChargeAreaResponse.getTown());
                deliverySpecialChargeAreaResultDto.setNumbers(specialChargeAreaResponse.getNumbers());
                deliverySpecialChargeAreaResultDto.setAddressList(specialChargeAreaResponse.getAddressList());

                deliverySpecialChargeAreaResultDtoList.add(deliverySpecialChargeAreaResultDto);
            }
        }

        return deliverySpecialChargeAreaResultDtoList;
    }

    /**
     * 特別料金エリア登録リクエストに変換します
     *
     * @param deliveryScaModel 特別料金エリア設定検索画面用モデル
     * @return 特別料金エリア登録リクエスト
     */
    public SpecialChargeAreaRegistRequest toSpecialChargeAreaRegistRequest(DeliveryScaModel deliveryScaModel) {
        SpecialChargeAreaRegistRequest specialChargeAreaRegistRequest = new SpecialChargeAreaRegistRequest();

        if (ObjectUtils.isNotEmpty(deliveryScaModel)) {
            specialChargeAreaRegistRequest.setCarriage(conversionUtility.toBigDecimal(deliveryScaModel.getCarriage()));
            specialChargeAreaRegistRequest.setZipCode(deliveryScaModel.getZipCode());
        }

        return specialChargeAreaRegistRequest;
    }

    /**
     * 郵便番号住所情報取得リクエストに変換します
     *
     * @param zipCode 郵便番号
     * @return 郵便番号住所情報取得リクエスト
     */
    public ZipCodeAddressGetRequest toZipCodeAddressGetRequest(String zipCode) {
        ZipCodeAddressGetRequest zipCodeAddressGetRequest = new ZipCodeAddressGetRequest();

        zipCodeAddressGetRequest.setZipCode(zipCode);

        return zipCodeAddressGetRequest;
    }

    /**
     * 郵便番号住所情報Dtoクラスリストに変換します
     *
     * @param zipCodeAddressResponse 郵便番号住所情報レスポンス
     * @return 郵便番号住所情報Dtoクラスリスト
     */
    public List<ZipCodeAddressDto> toZipCodeAddressDtoList(ZipCodeAddressResponse zipCodeAddressResponse) {
        List<ZipCodeAddressDto> zipCodeAddressDtoList = new ArrayList<>();

        if (CollectionUtil.isNotEmpty(zipCodeAddressResponse.getListZipCodeAddress())) {
            for (ZipCodeResponse zipCodeResponse : zipCodeAddressResponse.getListZipCodeAddress()) {
                ZipCodeAddressDto zipCodeAddressDto = new ZipCodeAddressDto();

                zipCodeAddressDto.setZipCode(zipCodeResponse.getZipCode());
                zipCodeAddressDto.setPrefectureName(zipCodeResponse.getPrefectureName());
                zipCodeAddressDto.setPrefectureNameKana(zipCodeResponse.getPrefectureNameKana());
                zipCodeAddressDto.setCityName(zipCodeResponse.getCityName());
                zipCodeAddressDto.setCityNameKana(zipCodeResponse.getCityNameKana());
                zipCodeAddressDto.setTownName(zipCodeResponse.getTownName());
                zipCodeAddressDto.setTownNameKana(zipCodeResponse.getTownNameKana());
                zipCodeAddressDto.setNumbers(zipCodeResponse.getNumbers());
                zipCodeAddressDto.setZipCodeType(zipCodeResponse.getZipCodeType());

                zipCodeAddressDtoList.add(zipCodeAddressDto);
            }
        }
        return zipCodeAddressDtoList;
    }
}