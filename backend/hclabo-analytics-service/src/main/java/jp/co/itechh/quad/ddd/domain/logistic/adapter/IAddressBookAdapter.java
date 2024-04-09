/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.logistic.adapter;

import jp.co.itechh.quad.ddd.domain.logistic.adapter.model.AddressBook;

/**
 * 住所録アダプター
 */
public interface IAddressBookAdapter {

    /**
     * 住所録取得
     *
     * @param addressId 住所ID
     * @return 住所録
     */
    AddressBook getAddressBook(String addressId);
}
