/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.addressbook.query;

import jp.co.itechh.quad.ddd.usecase.addressbook.query.model.AddressQueryCondition;
import jp.co.itechh.quad.ddd.usecase.addressbook.query.model.AddressQueryModel;

import java.util.List;

/**
 * 住所録クエリー
 */
public interface IAddressBookQuery {

    /**
     * 公開中住所リスト取得
     *
     * @param condition 住所録クエリーコンディション
     * @return 住所録クエリーモデルリスト
     */
    List<AddressQueryModel> getOpenAddressListByCustomerId(AddressQueryCondition condition);

}
