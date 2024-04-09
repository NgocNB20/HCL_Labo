/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.admin.pc.web.admin.shop.delivery.dia;

import jp.co.itechh.quad.admin.base.util.common.CollectionUtil;
import jp.co.itechh.quad.admin.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.admin.base.utility.ConversionUtility;
import jp.co.itechh.quad.admin.constant.type.HTypeDeliveryMethodType;
import jp.co.itechh.quad.admin.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.admin.constant.type.HTypePrefectureType;
import jp.co.itechh.quad.admin.constant.type.HTypeShortfallDisplayFlag;
import jp.co.itechh.quad.admin.dto.shop.ZipCodeAddressDto;
import jp.co.itechh.quad.admin.dto.shop.delivery.DeliveryImpossibleAreaConditionDto;
import jp.co.itechh.quad.admin.dto.shop.delivery.DeliveryImpossibleAreaResultDto;
import jp.co.itechh.quad.admin.entity.shop.delivery.DeliveryMethodEntity;
import jp.co.itechh.quad.admin.util.common.EnumTypeUtil;
import jp.co.itechh.quad.impossiblearea.presentation.api.param.ImpossibleAreaDeleteRequest;
import jp.co.itechh.quad.impossiblearea.presentation.api.param.ImpossibleAreaListGetRequest;
import jp.co.itechh.quad.impossiblearea.presentation.api.param.ImpossibleAreaListResponse;
import jp.co.itechh.quad.impossiblearea.presentation.api.param.ImpossibleAreaRegistRequest;
import jp.co.itechh.quad.impossiblearea.presentation.api.param.ImpossibleAreaResponse;
import jp.co.itechh.quad.method.presentation.api.param.DeliveryMethodResponse;
import jp.co.itechh.quad.method.presentation.api.param.ShippingMethodResponse;
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
 * 配送方法設定：配送不可能エリア設定検索画面用 Helper
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class DeliveryDiaHelper {

    /**
     * 変換ユーティリティクラス
     */
    private final ConversionUtility conversionUtility;

    /**
     * コンストラクタ
     *
     * @param conversionUtility 変換ユーティリティクラス
     */
    public DeliveryDiaHelper(ConversionUtility conversionUtility) {
        this.conversionUtility = conversionUtility;
    }

    /**
     * 検索結果をdeliveryDiaModelItemsに反映します
     *
     * @param resultList       List&lt;DeliveryImpossibleAreaResultDto&gt;
     * @param deliveryDiaModel DeliveryDiaModel
     * @param conditionDto     DeliveryImpossibleAreaConditionDto
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public void convertToIndexPageItemForResult(List<DeliveryImpossibleAreaResultDto> resultList,
                                                DeliveryDiaModel deliveryDiaModel,
                                                DeliveryImpossibleAreaConditionDto conditionDto)
                    throws IllegalAccessException, InvocationTargetException {

        List<DeliveryDiaModelItem> deliveryDiaModelItems = new ArrayList<>();

        // オフセット+1
        int index = deliveryDiaModel.getPageInfo().getOffset() + 1;

        if ((resultList != null) && !resultList.isEmpty()) {
            for (DeliveryImpossibleAreaResultDto resultDto : resultList) {
                DeliveryDiaModelItem pageItem = ApplicationContextUtility.getBean(DeliveryDiaModelItem.class);
                pageItem.setResultDataIndex(index++);
                BeanUtils.copyProperties(pageItem, resultDto);
                deliveryDiaModelItems.add(pageItem);
            }
        }
        deliveryDiaModel.setResultItems(deliveryDiaModelItems);
    }

    /**
     * 検索結果をdeliveryDiaModelに反映します
     *
     * @param deliveryDiaModel deliveryDiaModel
     * @param resultEntity     DeliveryMethodEntity
     */
    public void convertToRegistPageForResult(DeliveryDiaModel deliveryDiaModel, DeliveryMethodEntity resultEntity) {
        deliveryDiaModel.setDeliveryMethodName(resultEntity.getDeliveryMethodName());
        deliveryDiaModel.setDeliveryMethodType(resultEntity.getDeliveryMethodType());
        deliveryDiaModel.setOpenStatusPC(resultEntity.getOpenStatusPC());
    }

    /**
     * 郵便番号住所情報をDeliveryDiaModel.addressに変換します
     *
     * @param deliveryDiaModel DeliveryDiaModel
     * @param entityList       List&lt;ZipCodeAddressDto&gt;
     */
    public void convertToRegistPageForZipCodeResult(DeliveryDiaModel deliveryDiaModel,
                                                    List<ZipCodeAddressDto> entityList) {
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

        deliveryDiaModel.setAddress(builder.toString());
    }

    /**
     * 配送方法クラスに変換します
     *
     * @param shippingMethodResponse 配送方法レスポンス
     * @return 配送方法クラス
     */
    public DeliveryMethodEntity toDeliveryMethodEntity(ShippingMethodResponse shippingMethodResponse) {
        DeliveryMethodResponse deliveryMethodResponse = shippingMethodResponse.getDeliveryMethodResponse();

        if (ObjectUtils.isEmpty(deliveryMethodResponse)) {
            return null;
        }

        DeliveryMethodEntity deliveryMethodEntity = new DeliveryMethodEntity();
        deliveryMethodEntity.setDeliveryMethodSeq(deliveryMethodResponse.getDeliveryMethodSeq());
        deliveryMethodEntity.setDeliveryMethodName(deliveryMethodResponse.getDeliveryMethodName());
        deliveryMethodEntity.setDeliveryMethodDisplayNamePC(deliveryMethodResponse.getDeliveryMethodDisplayNamePC());
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
        deliveryMethodEntity.setLargeAmountDiscountCarriage(deliveryMethodResponse.getLargeAmountDiscountCarriage());
        if (deliveryMethodResponse.getShortfallDisplayFlag() != null) {
            deliveryMethodEntity.setShortfallDisplayFlag(EnumTypeUtil.getEnumFromValue(HTypeShortfallDisplayFlag.class,
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

        return deliveryMethodEntity;

    }

    /**
     * 配送不可能エリア削除リクエストに変換します
     *
     * @param deliveryDiaModel 配送不可能エリア設定検索画面用Pageクラス
     * @return 配送不可能エリア削除リクエスト
     */
    public ImpossibleAreaDeleteRequest toImpossibleAreaDeleteRequest(DeliveryDiaModel deliveryDiaModel) {
        ImpossibleAreaDeleteRequest impossibleAreaDeleteRequest = new ImpossibleAreaDeleteRequest();
        List<String> deleteList = new ArrayList<>();

        if (CollectionUtil.isNotEmpty(deliveryDiaModel.getResultItems())) {
            for (DeliveryDiaModelItem deliveryDiaModelItem : deliveryDiaModel.getResultItems()) {

                if (deliveryDiaModelItem.isCheck()) {
                    deleteList.add(deliveryDiaModelItem.getZipcode());
                }
            }
            impossibleAreaDeleteRequest.setDeleteList(deleteList);
        }
        return impossibleAreaDeleteRequest;
    }

    /**
     * 配送不可能エリア一覧リクエストに変換します
     *
     * @param deliveryDiaModel 配送特別料金エリア検索条件Dtoクラス
     * @return 配送不可能エリア一覧リクエスト
     */
    public ImpossibleAreaListGetRequest toImpossibleAreaListGetRequest(DeliveryDiaModel deliveryDiaModel) {
        ImpossibleAreaListGetRequest impossibleAreaListGetRequest = new ImpossibleAreaListGetRequest();

        if (StringUtils.isEmpty(deliveryDiaModel.getPrefectureName())) {
            impossibleAreaListGetRequest.setPrefecture("");
        } else {
            impossibleAreaListGetRequest.setPrefecture(EnumTypeUtil.getEnumFromValue(HTypePrefectureType.class,
                                                                                     deliveryDiaModel.getPrefectureName()
                                                                                    ).getLabel());
        }

        return impossibleAreaListGetRequest;
    }

    /**
     * 配送不可能エリア詳細Dtoクラスリストに変換します
     *
     * @param impossibleAreaListResponse 配送不可能エリア一覧レスポンス
     * @return 配送不可能エリア詳細Dtoクラスリスト
     */
    public List<DeliveryImpossibleAreaResultDto> toDeliveryImpossibleAreaResultDtoList(ImpossibleAreaListResponse impossibleAreaListResponse) {
        List<DeliveryImpossibleAreaResultDto> deliveryImpossibleAreaResultDtos = new ArrayList<>();

        if (CollectionUtil.isNotEmpty(impossibleAreaListResponse.getImpossibleAreaList())) {
            for (ImpossibleAreaResponse impossibleAreaResponse : impossibleAreaListResponse.getImpossibleAreaList()) {
                DeliveryImpossibleAreaResultDto deliveryImpossibleAreaResultDto = new DeliveryImpossibleAreaResultDto();

                deliveryImpossibleAreaResultDto.setDeliveryMethodSeq(impossibleAreaResponse.getDeliveryMethodSeq());
                deliveryImpossibleAreaResultDto.setZipcode(impossibleAreaResponse.getZipcode());
                deliveryImpossibleAreaResultDto.setPrefecture(impossibleAreaResponse.getPrefecture());
                deliveryImpossibleAreaResultDto.setCity(impossibleAreaResponse.getCity());
                deliveryImpossibleAreaResultDto.setTown(impossibleAreaResponse.getTown());
                deliveryImpossibleAreaResultDto.setNumbers(impossibleAreaResponse.getNumbers());
                deliveryImpossibleAreaResultDto.setAddressList(impossibleAreaResponse.getAddressList());

                deliveryImpossibleAreaResultDtos.add(deliveryImpossibleAreaResultDto);
            }
        }

        return deliveryImpossibleAreaResultDtos;
    }

    /**
     * 配送不可能エリア登録リクエストに変換します
     *
     * @param deliveryDiaModel 配送不可能エリア設定検索画面用Pageクラス
     * @return 配送不可能エリア登録リクエスト
     */
    public ImpossibleAreaRegistRequest toImpossibleAreaRegistRequest(DeliveryDiaModel deliveryDiaModel) {
        ImpossibleAreaRegistRequest impossibleAreaRegistRequest = new ImpossibleAreaRegistRequest();

        if (ObjectUtils.isNotEmpty(deliveryDiaModel)) {
            impossibleAreaRegistRequest.setDeliveryMethodSeq(deliveryDiaModel.getDmcd());
            impossibleAreaRegistRequest.setZipCode(deliveryDiaModel.getZipCode());
        }
        return impossibleAreaRegistRequest;
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