/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.method;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.ConversionUtility;
import jp.co.itechh.quad.core.dto.shop.delivery.DeliveryDetailsDto;
import jp.co.itechh.quad.core.dto.shop.delivery.DeliveryDto;
import jp.co.itechh.quad.ddd.domain.addressbook.entity.AddressBookEntity;
import jp.co.itechh.quad.ddd.domain.addressbook.repository.IAddressBookRepository;
import jp.co.itechh.quad.ddd.domain.addressbook.valueobject.AddressId;
import jp.co.itechh.quad.ddd.domain.method.proxy.ShippingMethodProxyService;
import jp.co.itechh.quad.ddd.domain.shipping.entity.ShippingSlipEntity;
import jp.co.itechh.quad.ddd.domain.shipping.repository.IShippingSlipRepository;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 選択可能配送方法一覧取得ユースケース
 */
@Service
public class GetSelectableShippingMethodListUseCase {

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
    public GetSelectableShippingMethodListUseCase(IShippingSlipRepository shippingSlipRepository,
                                                  IAddressBookRepository addressBookRepository,
                                                  ShippingMethodProxyService shippingMethodProxyService,
                                                  ConversionUtility conversionUtility) {
        this.shippingSlipRepository = shippingSlipRepository;
        this.addressBookRepository = addressBookRepository;
        this.shippingMethodProxyService = shippingMethodProxyService;
        this.conversionUtility = conversionUtility;
    }

    /**
     * 選択可能配送方法一覧を取得する
     *
     * @param transactionId 取引ID
     * @return 存在する ... 選択可能配送方法一覧 / 存在しない ... null
     */
    public List<GetSelectableShippingMethodListUseCaseDto> getSelectableShippingMethodList(String transactionId) {

        // 取引IDに紐づく配送伝票を取得する
        ShippingSlipEntity shippingSlipEntity = this.shippingSlipRepository.getByTransactionId(transactionId);

        AddressBookEntity addressBookEntity = null;
        // 配送先住所IDが設定されている場合
        if (ObjectUtils.isNotEmpty(shippingSlipEntity) && shippingSlipEntity.getShippingAddressId() != null) {
            // 住所を取得する
            addressBookEntity = this.addressBookRepository.get(
                            new AddressId(shippingSlipEntity.getShippingAddressId().getValue()));
        }

        String zipCode = null;
        String prefecture = null;
        // 住所が取得できた場合、郵便番号を設定する
        if (addressBookEntity != null) {
            zipCode = addressBookEntity.getAddress().getZipCode();
            prefecture = addressBookEntity.getAddress().getPrefecture();
        }

        // 選択可能配送方法リスト取得＋変換して返却
        return toGetSelectableShippingMethodUseCaseDto(
                        this.shippingMethodProxyService.getSelectableShippingMethodList(zipCode, prefecture));
    }

    /**
     * 選択可能配送方法確認ユースケースDtoリストへの変換処理
     *
     * @param deliveryDtoList 配送Dtoリスト
     * @return dtoList 選択可能配送方法確認ユースケースDtoリスト
     */
    private List<GetSelectableShippingMethodListUseCaseDto> toGetSelectableShippingMethodUseCaseDto(List<DeliveryDto> deliveryDtoList) {

        if (CollectionUtils.isEmpty(deliveryDtoList)) {
            return null;
        }

        List<GetSelectableShippingMethodListUseCaseDto> dtoList = new ArrayList<>();

        for (DeliveryDto deliveryDto : deliveryDtoList) {
            GetSelectableShippingMethodListUseCaseDto dto =
                            ApplicationContextUtility.getBean(GetSelectableShippingMethodListUseCaseDto.class);
            // 配送方法IDを設定
            dto.setShippingMethodId(this.conversionUtility.toString(
                            deliveryDto.getDeliveryDetailsDto().getDeliveryMethodSeq()));
            // 配送方法名を設定
            dto.setShippingMethodName(deliveryDto.getDeliveryDetailsDto().getDeliveryMethodName());
            // 配送方法説明文を設定
            dto.setShippingMethodNote(deliveryDto.getDeliveryDetailsDto().getDeliveryNotePC());
            // お届け希望日リストを設定
            // 希望日が指定不可の場合、Mapがnullとなるので未セット
            if (deliveryDto.getReceiverDateDto().getDateMap() != null) {
                // shippingMethodProxyServiceではフェーズ１ロジックを流用して取得しているので、Mapには(key=YYYYMMDD,value=YYYY/MM/DD)で値が設定されている前提
                List<String> dateList = new ArrayList<>(deliveryDto.getReceiverDateDto().getDateMap().values());
                dto.setReceiverDateList(dateList);
            }
            // お届け希望時間帯リストを設定
            dto.setReceiverTimeZoneList(createTimeZoneList(deliveryDto.getDeliveryDetailsDto()));

            dtoList.add(dto);
        }
        return dtoList;
    }

    /**
     * お届け希望時間帯リストを作成
     *
     * @param deliveryDetailsDto 配送詳細
     * @return timeZoneList お届け希望時間帯リスト
     */
    private List<String> createTimeZoneList(DeliveryDetailsDto deliveryDetailsDto) {

        List<String> timeZoneList = new ArrayList<>();
        if (deliveryDetailsDto.getReceiverTimeZone1() != null) {
            timeZoneList.add(deliveryDetailsDto.getReceiverTimeZone1());
        }
        if (deliveryDetailsDto.getReceiverTimeZone2() != null) {
            timeZoneList.add(deliveryDetailsDto.getReceiverTimeZone2());
        }
        if (deliveryDetailsDto.getReceiverTimeZone3() != null) {
            timeZoneList.add(deliveryDetailsDto.getReceiverTimeZone3());
        }
        if (deliveryDetailsDto.getReceiverTimeZone4() != null) {
            timeZoneList.add(deliveryDetailsDto.getReceiverTimeZone4());
        }
        if (deliveryDetailsDto.getReceiverTimeZone5() != null) {
            timeZoneList.add(deliveryDetailsDto.getReceiverTimeZone5());
        }
        if (deliveryDetailsDto.getReceiverTimeZone6() != null) {
            timeZoneList.add(deliveryDetailsDto.getReceiverTimeZone6());
        }
        if (deliveryDetailsDto.getReceiverTimeZone7() != null) {
            timeZoneList.add(deliveryDetailsDto.getReceiverTimeZone7());
        }
        if (deliveryDetailsDto.getReceiverTimeZone8() != null) {
            timeZoneList.add(deliveryDetailsDto.getReceiverTimeZone8());
        }
        if (deliveryDetailsDto.getReceiverTimeZone9() != null) {
            timeZoneList.add(deliveryDetailsDto.getReceiverTimeZone9());
        }
        if (deliveryDetailsDto.getReceiverTimeZone10() != null) {
            timeZoneList.add(deliveryDetailsDto.getReceiverTimeZone10());
        }
        return timeZoneList;
    }

}