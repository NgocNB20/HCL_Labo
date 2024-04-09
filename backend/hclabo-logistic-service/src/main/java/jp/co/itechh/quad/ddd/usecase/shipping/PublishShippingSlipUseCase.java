/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.shipping;

import jp.co.itechh.quad.core.base.utility.ConversionUtility;
import jp.co.itechh.quad.core.dto.shop.delivery.DeliveryDetailsDto;
import jp.co.itechh.quad.core.dto.shop.delivery.DeliveryDto;
import jp.co.itechh.quad.ddd.domain.addressbook.entity.AddressBookEntity;
import jp.co.itechh.quad.ddd.domain.addressbook.repository.IAddressBookRepository;
import jp.co.itechh.quad.ddd.domain.method.proxy.ShippingMethodProxyService;
import jp.co.itechh.quad.ddd.domain.shipping.entity.ShippingSlipEntity;
import jp.co.itechh.quad.ddd.domain.shipping.repository.IShippingSlipRepository;
import jp.co.itechh.quad.ddd.domain.shipping.valueobject.ShippingAddressId;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 配送伝票発行ユースケース
 */
@Service
public class PublishShippingSlipUseCase {

    /** 配送伝票リポジトリ */
    private final IShippingSlipRepository shippingSlipRepository;

    /** 住所録リポジトリ */
    private final IAddressBookRepository addressBookRepository;

    /** 配送方法プロキシサービス */
    private final ShippingMethodProxyService shippingMethodProxyService;

    /** 変換Helper */
    private final ConversionUtility conversionUtility;

    /** コンストラクタ */
    @Autowired
    public PublishShippingSlipUseCase(IShippingSlipRepository shippingSlipRepository,
                                      IAddressBookRepository addressBookRepository,
                                      ShippingMethodProxyService shippingMethodProxyService,
                                      ConversionUtility conversionUtility) {
        this.shippingSlipRepository = shippingSlipRepository;
        this.addressBookRepository = addressBookRepository;
        this.shippingMethodProxyService = shippingMethodProxyService;
        this.conversionUtility = conversionUtility;
    }

    /**
     * 配送伝票を発行する
     *
     * @param transactionId 取引ID
     * @param customerId    顧客ID
     */
    public void publishShippingSlip(String transactionId, String customerId) {

        // 配送伝票を発行
        ShippingSlipEntity shippingSlipEntity = new ShippingSlipEntity(transactionId, new Date());

        // 住所録を取得
        AddressBookEntity defaultShippingAddress = getDefaultShippingAddress(customerId);

        // 住所録が存在する場合
        if (ObjectUtils.isNotEmpty(defaultShippingAddress) && ObjectUtils.isNotEmpty(
                        defaultShippingAddress.getAddressId())) {
            shippingSlipEntity.settingShippingAddress(
                            new ShippingAddressId(defaultShippingAddress.getAddressId().getValue()));
        }

        // デフォルト配送方法を取得
        DeliveryDto defaultShippingMethod = getDefaultShippingMethod(defaultShippingAddress);

        // デフォルト配送方法が存在する場合
        if (ObjectUtils.isNotEmpty(defaultShippingMethod)) {
            shippingSlipEntity.settingShippingMethod(
                            this.conversionUtility.toString(
                                            defaultShippingMethod.getDeliveryDetailsDto().getDeliveryMethodSeq()),
                            defaultShippingMethod.getDeliveryDetailsDto().getDeliveryMethodName()
                                                    );
            shippingSlipEntity.settingDefaultDeliveryTime(
                            createDeliveryTimeList(defaultShippingMethod.getDeliveryDetailsDto()));
        }

        // 配送伝票を登録する
        this.shippingSlipRepository.save(shippingSlipEntity);
    }

    /**
     * デフォルトのお届け先住所録取得
     *
     * @param customerId 顧客ID
     * @return デフォルトのお届け先
     */
    private AddressBookEntity getDefaultShippingAddress(String customerId) {

        // デフォルトの住所録が存在しない場合は、null返却
        // 現時点の仕様では、会員登録時に会員の住所録をデフォルト指定するので必ず取得できる
        return addressBookRepository.getDefault(customerId);
    }

    /**
     * デフォルト配送方法取得
     *
     * @param shippingAddress デフォルトのお届け先
     * @return デフォルト配送方法
     */
    private DeliveryDto getDefaultShippingMethod(AddressBookEntity shippingAddress) {

        // デフォルトのお届け先から、郵便番号と都道府県を取得
        String zipCode = null;
        String prefecture = null;
        if (ObjectUtils.isNotEmpty(shippingAddress) && ObjectUtils.isNotEmpty(shippingAddress.getAddress())) {
            zipCode = shippingAddress.getAddress().getZipCode();
            prefecture = shippingAddress.getAddress().getPrefecture();
        }

        // デフォルト配送方法を取得
        return this.shippingMethodProxyService.getDefaultShippingMethod(zipCode, prefecture);
    }

    /**
     * 配送時間リスト作成
     *
     * @param dto 配送方法詳細DTO
     * @return 配送時間リスト
     */
    private List<String> createDeliveryTimeList(DeliveryDetailsDto dto) {

        List<String> deliveryTimeList = new ArrayList<>();

        if (ObjectUtils.isEmpty(dto)) {
            return deliveryTimeList;
        }

        if (StringUtils.isNotBlank(dto.getReceiverTimeZone1())) {
            deliveryTimeList.add(dto.getReceiverTimeZone1());
        }
        if (StringUtils.isNotBlank(dto.getReceiverTimeZone2())) {
            deliveryTimeList.add(dto.getReceiverTimeZone2());
        }
        if (StringUtils.isNotBlank(dto.getReceiverTimeZone3())) {
            deliveryTimeList.add(dto.getReceiverTimeZone3());
        }
        if (StringUtils.isNotBlank(dto.getReceiverTimeZone4())) {
            deliveryTimeList.add(dto.getReceiverTimeZone4());
        }
        if (StringUtils.isNotBlank(dto.getReceiverTimeZone5())) {
            deliveryTimeList.add(dto.getReceiverTimeZone5());
        }
        if (StringUtils.isNotBlank(dto.getReceiverTimeZone6())) {
            deliveryTimeList.add(dto.getReceiverTimeZone6());
        }
        if (StringUtils.isNotBlank(dto.getReceiverTimeZone7())) {
            deliveryTimeList.add(dto.getReceiverTimeZone7());
        }
        if (StringUtils.isNotBlank(dto.getReceiverTimeZone8())) {
            deliveryTimeList.add(dto.getReceiverTimeZone8());
        }
        if (StringUtils.isNotBlank(dto.getReceiverTimeZone9())) {
            deliveryTimeList.add(dto.getReceiverTimeZone9());
        }
        if (StringUtils.isNotBlank(dto.getReceiverTimeZone10())) {
            deliveryTimeList.add(dto.getReceiverTimeZone10());
        }
        return deliveryTimeList;
    }

}
