/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.infrastructure.logistic.adapter;

import jp.co.itechh.quad.addressbook.presentation.api.AddressBookApi;
import jp.co.itechh.quad.addressbook.presentation.api.param.AddressBookAddressResponse;
import jp.co.itechh.quad.core.web.HeaderParamsUtility;
import jp.co.itechh.quad.ddd.domain.logistic.adapter.IAddressBookAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 配送アダプター実装クラス
 *
 * @author kimura
 */
@Component
public class AddressBookAdapterImpl implements IAddressBookAdapter {

    /** 住所API */
    private final AddressBookApi addressBookApi;

    /**
     * コンストラクタ
     *
     * @param addressBookApi
     * @param headerParamsUtil ヘッダパラメーターユーティル
     */
    @Autowired
    public AddressBookAdapterImpl(AddressBookApi addressBookApi, HeaderParamsUtility headerParamsUtil) {
        this.addressBookApi = addressBookApi;

        // サービス間のAPI呼び出す時、ヘッダーにパラメーターを設定する
        headerParamsUtil.setHeader(this.addressBookApi.getApiClient());
    }

    /**
     * 物流マイクロサービス<br/>
     * 住所取得
     *
     * @param shippingAddressId 住所ID
     * @return AddressBookAddressResponse 住所レスポンス
     */
    @Override
    public AddressBookAddressResponse getAddressById(String shippingAddressId) {

        return addressBookApi.getAddressById(shippingAddressId);
    }
}
