/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.logistic.adapter;

/**
 * 住所録 アダプター
 */
public interface IAddressBookAdapter {

    /**
     * zipcode取得
     *
     * @param shippingAddressId
     * @return zipcode
     */
    String getZipcode(String shippingAddressId);
}
