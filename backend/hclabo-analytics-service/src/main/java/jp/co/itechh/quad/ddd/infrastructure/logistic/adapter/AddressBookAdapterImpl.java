/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.infrastructure.logistic.adapter;

import jp.co.itechh.quad.addressbook.presentation.api.AddressBookApi;
import jp.co.itechh.quad.addressbook.presentation.api.param.AddressBookAddressResponse;
import jp.co.itechh.quad.core.web.HeaderParamsUtility;
import jp.co.itechh.quad.ddd.domain.logistic.adapter.IAddressBookAdapter;
import jp.co.itechh.quad.ddd.domain.logistic.adapter.model.AddressBook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 住所録アダプター実装クラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class AddressBookAdapterImpl implements IAddressBookAdapter {

    /**
     * 名簿API
     */
    private final AddressBookApi addressBookApi;

    /**
     * 住所録アダプターHelperクラス
     */
    private final AddressBookAdapterHelper addressBookAdapterHelper;

    /**
     * コンストラクタ
     *
     * @param addressBookApi           住所録API
     * @param addressBookAdapterHelper 住所録アダプターHelperクラス
     * @param headerParamsUtil         ヘッダパラメーターユーティル
     */
    @Autowired
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
     * @param addressId
     * @return 住所録
     */
    @Override
    public AddressBook getAddressBook(String addressId) {

        AddressBookAddressResponse addressBookAddressResponse = addressBookApi.getAddressById(addressId);

        return addressBookAdapterHelper.toAddressBook(addressBookAddressResponse);
    }

}