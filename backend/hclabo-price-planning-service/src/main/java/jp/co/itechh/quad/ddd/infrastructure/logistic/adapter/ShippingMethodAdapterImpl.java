/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.infrastructure.logistic.adapter;

import jp.co.itechh.quad.core.constant.type.HTypeFreeDeliveryFlag;
import jp.co.itechh.quad.core.constant.type.HTypePrefectureType;
import jp.co.itechh.quad.core.dto.shop.ZipCodeAddressDto;
import jp.co.itechh.quad.core.dto.shop.delivery.DeliveryDto;
import jp.co.itechh.quad.core.util.common.EnumTypeUtil;
import jp.co.itechh.quad.core.web.HeaderParamsUtility;
import jp.co.itechh.quad.ddd.domain.logistic.adapter.IShippingMethodAdapter;
import jp.co.itechh.quad.method.presentation.api.ShippingMethodApi;
import jp.co.itechh.quad.method.presentation.api.param.CarriageShippingMethodListGetRequest;
import jp.co.itechh.quad.method.presentation.api.param.CarriageShippingMethodListResponse;
import jp.co.itechh.quad.zipcode.presentation.api.ZipcodeApi;
import jp.co.itechh.quad.zipcode.presentation.api.param.ZipCodeAddressGetRequest;
import jp.co.itechh.quad.zipcode.presentation.api.param.ZipCodeAddressResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * 配送方法アダプター実装クラス
 *
 * @author kimura
 */
@Component
public class ShippingMethodAdapterImpl implements IShippingMethodAdapter {

    private final ZipcodeApi zipcodeApi;

    private final ShippingMethodApi shippingMethodApi;

    private final ShippingMethodAdapterHelper shippingMethodAdapterHelper;

    /**
     * コンストラクタ
     *
     * @param zipcodeApi
     * @param shippingMethodApi
     * @param shippingMethodAdapterHelper
     */
    @Autowired
    public ShippingMethodAdapterImpl(ZipcodeApi zipcodeApi,
                                     ShippingMethodApi shippingMethodApi,
                                     ShippingMethodAdapterHelper shippingMethodAdapterHelper,
                                     HeaderParamsUtility headerParamsUtil) {
        this.zipcodeApi = zipcodeApi;
        this.shippingMethodApi = shippingMethodApi;
        this.shippingMethodAdapterHelper = shippingMethodAdapterHelper;

        // サービス間のAPI呼び出す時、ヘッダーにパラメーターを設定する
        headerParamsUtil.setHeader(this.zipcodeApi.getApiClient());
        headerParamsUtil.setHeader(this.shippingMethodApi.getApiClient());
    }

    /**
     * 配送方法情報取得
     * ※引数パラメータを基に適切な送料を含む配送方法情報取得するAPI呼出し
     *
     * @param deliveryMethodSeq
     * @param zipcode
     * @param itemPriceTotal
     * @return DeliveryDto
     */
    @Override
    public DeliveryDto getDeliveryDto(Integer deliveryMethodSeq, String zipcode, int itemPriceTotal) {

        // 都道府県名をzipcodeから取得して設定
        ZipCodeAddressGetRequest zipCodeAddressGetRequest = new ZipCodeAddressGetRequest();
        zipCodeAddressGetRequest.setZipCode(zipcode);
        ZipCodeAddressResponse zipCodeAddressResponse = zipcodeApi.get(zipCodeAddressGetRequest);
        ZipCodeAddressDto addressDto = shippingMethodAdapterHelper.toZipCodeAddressDto(zipcode, zipCodeAddressResponse);

        CarriageShippingMethodListGetRequest carriageShippingMethodListGetRequest =
                        new CarriageShippingMethodListGetRequest();
        HTypePrefectureType prefectureType =
                        EnumTypeUtil.getEnumFromLabel(HTypePrefectureType.class, addressDto.getPrefectureName());
        carriageShippingMethodListGetRequest.setShopSeq(1001);
        carriageShippingMethodListGetRequest.setPrefectureType(EnumTypeUtil.getValue(prefectureType));
        carriageShippingMethodListGetRequest.setTotalGoodsPrice(BigDecimal.valueOf(itemPriceTotal));
        carriageShippingMethodListGetRequest.setZipcode(zipcode);
        carriageShippingMethodListGetRequest.setDeliveryMethodSeq(deliveryMethodSeq);
        carriageShippingMethodListGetRequest.setFreeDeliveryFlag(HTypeFreeDeliveryFlag.OFF.getValue());

        CarriageShippingMethodListResponse carriageShippingMethodListResponse =
                        shippingMethodApi.getCarriage(carriageShippingMethodListGetRequest);
        List<DeliveryDto> deliveryDtoList =
                        shippingMethodAdapterHelper.toDeliveryDtoList(carriageShippingMethodListResponse);

        if (CollectionUtils.isEmpty(deliveryDtoList)) {
            return null;
        }

        // 引数に渡された配送方法を返却する
        return deliveryDtoList.stream()
                              .filter(item -> item.getDeliveryDetailsDto()
                                                  .getDeliveryMethodSeq()
                                                  .equals(deliveryMethodSeq))
                              .findFirst()
                              .orElse(null);
    }

}