/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.infrastructure.logistic.adapter;

import jp.co.itechh.quad.addressbook.presentation.api.AddressBookApi;
import jp.co.itechh.quad.addressbook.presentation.api.param.AddressBookAddressResponse;
import jp.co.itechh.quad.core.web.HeaderParamsUtility;
import jp.co.itechh.quad.ddd.domain.logistic.adapter.IAddressBookAdapter;
import org.springframework.stereotype.Component;

/**
 * 住所録アダプター実装クラス
 *
 * @author kimura
 */
@Component
public class AddressBookAdapterImpl implements IAddressBookAdapter {

    /** 名簿API */
    private final AddressBookApi addressBookApi;

    /** 住所録アダプタヘルパー */
    private final AddressBookAdapterHelper addressBookAdapterHelper;

    /**
     * コンストラクタ
     *
     * @param addressBookApi
     * @param addressBookAdapterHelper
     * @param headerParamsUtil ヘッダパラメーターユーティル
     */
    public AddressBookAdapterImpl(AddressBookApi addressBookApi,
                                  AddressBookAdapterHelper addressBookAdapterHelper,
                                  HeaderParamsUtility headerParamsUtil) {
        this.addressBookApi = addressBookApi;
        this.addressBookAdapterHelper = addressBookAdapterHelper;

        // サービス間のAPI呼び出す時、ヘッダーにパラメーターを設定する
        headerParamsUtil.setHeader(this.addressBookApi.getApiClient());
    }

    /**
     * 住所録取得
     *
     * @param shippingAddressId
     * @return zipcode
     */
    @Override
    public String getZipcode(String shippingAddressId) {
        AddressBookAddressResponse addressBookAddressResponse = addressBookApi.getAddressById(shippingAddressId);
        return addressBookAdapterHelper.toZipcode(addressBookAddressResponse);
    }

}