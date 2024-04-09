/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.infrastructure.logistic.adapter;

import jp.co.itechh.quad.core.base.utility.ConversionUtility;
import jp.co.itechh.quad.core.constant.type.HTypeDeliveryMethodType;
import jp.co.itechh.quad.core.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.core.constant.type.HTypePrefectureType;
import jp.co.itechh.quad.core.constant.type.HTypeReceiverDateDesignationFlag;
import jp.co.itechh.quad.core.constant.type.HTypeShortfallDisplayFlag;
import jp.co.itechh.quad.core.constant.type.HTypeZipCodeType;
import jp.co.itechh.quad.core.dto.shop.ZipCodeAddressDto;
import jp.co.itechh.quad.core.dto.shop.delivery.DeliveryDetailsDto;
import jp.co.itechh.quad.core.dto.shop.delivery.DeliveryDto;
import jp.co.itechh.quad.core.dto.shop.delivery.ReceiverDateDto;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.method.presentation.api.param.CarriageShippingMethodListResponse;
import jp.co.itechh.quad.method.presentation.api.param.DeliveryDetailsResponse;
import jp.co.itechh.quad.method.presentation.api.param.DeliveryResponse;
import jp.co.itechh.quad.method.presentation.api.param.ReceiverDateResponse;
import jp.co.itechh.quad.zipcode.presentation.api.param.ZipCodeAddressResponse;
import jp.co.itechh.quad.zipcode.presentation.api.param.ZipCodeResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 配送方法アダプター実装Helperクラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class ShippingMethodAdapterHelper {

    /** 変換ユーティリティクラス */
    private final ConversionUtility conversionUtility;

    /** 以下に掲載がない場合：〒番号下2桁「00」*/
    String ZIPCODE_LAST_DIGIT = "00";

    /**
     * コンストラクター
     *
     * @param conversionUtility
     */
    @Autowired
    public ShippingMethodAdapterHelper(ConversionUtility conversionUtility) {
        this.conversionUtility = conversionUtility;
    }

    /**
     * 郵便番号住所情報Dtoクラスに変換
     *
     * @param zipcode                郵便番号
     * @param zipCodeAddressResponse 郵便番号住所情報レスポンス
     * @return 郵便番号住所情報Dtoクラス
     */
    public ZipCodeAddressDto toZipCodeAddressDto(String zipcode, ZipCodeAddressResponse zipCodeAddressResponse) {

        if (CollectionUtils.isEmpty(zipCodeAddressResponse.getListZipCodeAddress())) {
            return null;
        }

        ZipCodeAddressDto zipCodeAddressDto = correctAddress(zipcode, toZipCodeAddressDtoList(zipCodeAddressResponse));

        return zipCodeAddressDto;
    }

    /**
     * 郵便番号住所情報Dtoクラスに変換
     *
     * @param zipCodeAddressResponse 郵便番号住所情報レスポンス
     * @return 郵便番号住所情報Dtoクラス
     */
    protected List<ZipCodeAddressDto> toZipCodeAddressDtoList(ZipCodeAddressResponse zipCodeAddressResponse) {

        List<ZipCodeAddressDto> zipCodeAddressDtos = new ArrayList<>();

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

            zipCodeAddressDtos.add(zipCodeAddressDto);
        }
        return zipCodeAddressDtos;
    }

    /**
     * 住所データの補正を行う<br/>
     * 住所データに対して以下の補正を行う。<br/>
     * １．複数の住所から共通部分を抽出する。<br/>
     * 例：以下の場合、大阪府大阪市福島区を住所として返却する。<br/>
     * 大阪府大阪市福島区吉野<br/>
     * 大阪府大阪市福島区海老江<br/>
     * <br/>
     * ２．以下に掲載にない場合という文言を消去する。<br/>
     * 住所の郵便番号の末尾が 00 の場合、町域名に設定されている「以下に掲載のない場合」を空文字に変換する<br/>
     *
     * @param zipCode     郵便番号
     * @param addressList 郵便番号情報リスト。null, emptyでないこと。
     * @return 郵便番号住所情報Dto
     */
    protected ZipCodeAddressDto correctAddress(String zipCode, List<ZipCodeAddressDto> addressList) {
        // 複数取得できた住所情報の1件目と2件目以降を比較し、住所の差異を探す。
        // 差異が見つかった場合、その項目も含め、下位の項目を空白でクリアする。
        ZipCodeAddressDto resultDto = addressList.get(0);

        // 住所の郵便番号の末尾が 00 の場合、町域名をクリアする。
        // 町域名には町域ではなく、「以下に掲載のない場合」という文言が入っているため。
        if (HTypeZipCodeType.NORMAL.getValue().equals(resultDto.getZipCodeType()) && zipCode.endsWith(
                        ZIPCODE_LAST_DIGIT)) {
            resultDto.setTownName("");
            resultDto.setTownNameKana("");
            // 住所の郵便番号には丁目はないのでクリア不要
        }

        for (int i = 1; i < addressList.size(); i++) {
            ZipCodeAddressDto addressDto = addressList.get(i);

            // 都道府県名が一致しない場合、すべての情報をクリア
            if (StringUtils.isNotEmpty(resultDto.getPrefectureName()) && !resultDto.getPrefectureName()
                                                                                   .equals(addressDto.getPrefectureName())) {
                resultDto.setPrefectureName("");
                resultDto.setPrefectureNameKana("");
                resultDto.setCityName("");
                resultDto.setCityNameKana("");
                resultDto.setTownName("");
                resultDto.setTownNameKana("");
                resultDto.setNumbers("");
                break;
            }

            // 市区町村が一致しない場合、市区町村、町域、丁目をクリア
            if (StringUtils.isNotEmpty(resultDto.getCityName()) && !resultDto.getCityName()
                                                                             .equals(addressDto.getCityName())) {
                resultDto.setCityName("");
                resultDto.setCityNameKana("");
                resultDto.setTownName("");
                resultDto.setTownNameKana("");
                resultDto.setNumbers("");
                break;
            }

            // 町域が一致しない場合、町域、丁目をクリア
            if (StringUtils.isNotEmpty(resultDto.getTownName()) && !resultDto.getTownName()
                                                                             .equals(addressDto.getTownName())) {
                resultDto.setTownName("");
                resultDto.setTownNameKana("");
                resultDto.setNumbers("");
                break;
            }

            // 丁目が一致しない場合、丁目をクリア
            if (StringUtils.isNotEmpty(resultDto.getNumbers()) && !resultDto.getNumbers()
                                                                            .equals(addressDto.getNumbers())) {
                resultDto.setNumbers("");
                break;
            }
        }

        return resultDto;
    }

    /**
     * 配送DTOクラスリストに変換
     *
     * @param carriageShippingMethodListResponse 配送リストレスポンス
     * @return 配送DTOクラスリスト
     */
    public List<DeliveryDto> toDeliveryDtoList(CarriageShippingMethodListResponse carriageShippingMethodListResponse) {

        if (ObjectUtils.isEmpty(carriageShippingMethodListResponse) || CollectionUtils.isEmpty(
                        carriageShippingMethodListResponse.getDeliveryListResponse())) {
            return null;
        }

        List<DeliveryDto> deliveryDtos = new ArrayList<>();

        for (DeliveryResponse deliveryResponse : carriageShippingMethodListResponse.getDeliveryListResponse()) {
            DeliveryDto deliveryDto = new DeliveryDto();

            deliveryDto.setDeliveryDetailsDto(toDeliveryDetailsDto(deliveryResponse.getDeliveryDetailsResponse()));
            deliveryDto.setCarriage(deliveryResponse.getCarriage());
            deliveryDto.setSelectClass(deliveryResponse.getSelectClass());
            deliveryDto.setReceiverDateDto(toReceiverDateDto(deliveryResponse.getReceiverDateResponse()));
            deliveryDto.setSpecialChargeAreaFlag(deliveryResponse.getSpecialChargeAreaFlag());

            deliveryDtos.add(deliveryDto);
        }

        return deliveryDtos;
    }

    /**
     * 配送方法詳細DTOクラスに変換
     *
     * @param deliveryDetailsResponse 配送方法詳細レスポンス
     * @return 配送方法詳細DTOクラス
     */
    private DeliveryDetailsDto toDeliveryDetailsDto(DeliveryDetailsResponse deliveryDetailsResponse) {

        if (ObjectUtils.isEmpty(deliveryDetailsResponse)) {
            return null;
        }

        DeliveryDetailsDto deliveryDetailsDto = new DeliveryDetailsDto();

        deliveryDetailsDto.setDeliveryMethodSeq(deliveryDetailsResponse.getDeliveryMethodSeq());
        deliveryDetailsDto.setShopSeq(1001);
        deliveryDetailsDto.setDeliveryMethodName(deliveryDetailsResponse.getDeliveryMethodName());
        deliveryDetailsDto.setDeliveryMethodDisplayNamePC(deliveryDetailsResponse.getDeliveryMethodDisplayNamePC());
        deliveryDetailsDto.setOpenStatusPC(EnumTypeUtil.getEnumFromValue(HTypeOpenDeleteStatus.class,
                                                                         deliveryDetailsResponse.getOpenStatusPC()
                                                                        ));
        deliveryDetailsDto.setDeliveryNotePC(deliveryDetailsResponse.getDeliveryNotePC());
        deliveryDetailsDto.setDeliveryMethodType(EnumTypeUtil.getEnumFromValue(HTypeDeliveryMethodType.class,
                                                                               deliveryDetailsResponse.getDeliveryMethodType()
                                                                              ));
        deliveryDetailsDto.setEqualsCarriage(deliveryDetailsResponse.getEqualsCarriage());
        deliveryDetailsDto.setLargeAmountDiscountPrice(deliveryDetailsResponse.getLargeAmountDiscountPrice());
        deliveryDetailsDto.setLargeAmountDiscountCarriage(deliveryDetailsResponse.getLargeAmountDiscountCarriage());
        deliveryDetailsDto.setShortfallDisplayFlag(EnumTypeUtil.getEnumFromValue(HTypeShortfallDisplayFlag.class,
                                                                                 deliveryDetailsResponse.getShortfallDisplayFlag()
                                                                                ));
        deliveryDetailsDto.setDeliveryLeadTime(deliveryDetailsResponse.getDeliveryLeadTime());
        deliveryDetailsDto.setPossibleSelectDays(deliveryDetailsResponse.getPossibleSelectDays());
        deliveryDetailsDto.setReceiverTimeZone1(deliveryDetailsResponse.getReceiverTimeZone1());
        deliveryDetailsDto.setReceiverTimeZone2(deliveryDetailsResponse.getReceiverTimeZone2());
        deliveryDetailsDto.setReceiverTimeZone3(deliveryDetailsResponse.getReceiverTimeZone3());
        deliveryDetailsDto.setReceiverTimeZone4(deliveryDetailsResponse.getReceiverTimeZone4());
        deliveryDetailsDto.setReceiverTimeZone5(deliveryDetailsResponse.getReceiverTimeZone5());
        deliveryDetailsDto.setReceiverTimeZone6(deliveryDetailsResponse.getReceiverTimeZone6());
        deliveryDetailsDto.setReceiverTimeZone7(deliveryDetailsResponse.getReceiverTimeZone7());
        deliveryDetailsDto.setReceiverTimeZone8(deliveryDetailsResponse.getReceiverTimeZone8());
        deliveryDetailsDto.setReceiverTimeZone9(deliveryDetailsResponse.getReceiverTimeZone9());
        deliveryDetailsDto.setReceiverTimeZone10(deliveryDetailsResponse.getReceiverTimeZone10());
        deliveryDetailsDto.setOrderDisplay(deliveryDetailsResponse.getOrderDisplay());
        deliveryDetailsDto.setPrefectureType(EnumTypeUtil.getEnumFromValue(HTypePrefectureType.class,
                                                                           deliveryDetailsResponse.getPrefectureType()
                                                                          ));
        deliveryDetailsDto.setMaxPrice(deliveryDetailsResponse.getMaxPrice());
        deliveryDetailsDto.setCarriage(deliveryDetailsResponse.getCarriage());

        return deliveryDetailsDto;
    }

    /**
     * 通知サブドメインリストに変換
     *
     * @param receiverDateResponse 通知サブドメインリスト
     * @return お届け希望日Dto
     */
    private ReceiverDateDto toReceiverDateDto(ReceiverDateResponse receiverDateResponse) {

        if (ObjectUtils.isEmpty(receiverDateResponse)) {
            return null;
        }

        ReceiverDateDto receiverDateDto = new ReceiverDateDto();

        receiverDateDto.setDateMap(receiverDateResponse.getDateMap());
        receiverDateDto.setReceiverDateDesignationFlag(
                        EnumTypeUtil.getEnumFromValue(HTypeReceiverDateDesignationFlag.class,
                                                      receiverDateResponse.getReceiverDateDesignationFlag()
                                                     ));
        receiverDateDto.setShortestDeliveryDateToRegist(
                        conversionUtility.toTimeStamp(receiverDateResponse.getShortestDeliveryDateToRegist()));

        return receiverDateDto;
    }
}