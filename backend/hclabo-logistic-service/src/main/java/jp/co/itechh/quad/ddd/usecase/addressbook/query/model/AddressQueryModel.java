/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.addressbook.query.model;

import jp.co.itechh.quad.ddd.domain.addressbook.valueobject.Address;
import jp.co.itechh.quad.ddd.domain.addressbook.valueobject.AddressId;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 住所録クエリーモデル<br/>
 */
@Data
public class AddressQueryModel {

    /** 住所ID */
    private AddressId addressId;

    /** 住所名 */
    private String addressName;

    /** 住所 */
    private Address address;

    /** 配送メモ */
    private String shippingMemo;

}
