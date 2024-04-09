/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.logistic.adapter;

import jp.co.itechh.quad.addressbook.presentation.api.param.AddressBookAddressResponse;

/**
 * 住所アダプター
 */
public interface IAddressBookAdapter {

    /**
     * 物流マイクロサービス<br/>
     * 配送伝票取得
     *
     * @param shippingAddressId 取引ID
     * @return AddressBookAddressResponse 住所レスポンス
     */
    AddressBookAddressResponse getAddressById(String shippingAddressId);

}