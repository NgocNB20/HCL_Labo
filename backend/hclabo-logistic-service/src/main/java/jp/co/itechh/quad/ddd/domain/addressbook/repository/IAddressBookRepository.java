/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.addressbook.repository;

import jp.co.itechh.quad.ddd.domain.addressbook.entity.AddressBookEntity;
import jp.co.itechh.quad.ddd.domain.addressbook.valueobject.AddressId;

/**
 * 住所録リポジトリ
 */
public interface IAddressBookRepository {

    /**
     * 住所録登録
     *
     * @param addressBookEntity 住所録エンティティ
     */
    void save(AddressBookEntity addressBookEntity);

    /**
     * 住所録更新
     *
     * @param addressBookEntity 住所録エンティティ
     * @return 更新件数
     */
    int update(AddressBookEntity addressBookEntity);

    /**
     * 住所録（住所情報）取得
     *
     * @param addressId 住所ID
     * @return 住所録エンティティ
     */
    AddressBookEntity get(AddressId addressId);

    /**
     * 住所IDで住所録削除
     *
     * @param addressId 住所ID
     * @return 削除件数
     */
    int delete(AddressId addressId);

    /**
     * 顧客IDでデフォルト指定住所録取得
     *
     * @param customerId 顧客ID
     * @return 住所録エンティティ
     */
    AddressBookEntity getDefault(String customerId);

    /**
     * 住所IDでデフォルト指定フラグON
     *
     * @param addressId 住所ID
     * @return 更新件数
     */
    int updateDefault(AddressId addressId);

    /**
     * 顧客IDで住所録一括非公開
     *
     * @param customerId 顧客ID
     * @return 更新件数
     */
    int updateAllClose(String customerId);

    /**
     * 顧客IDで住所録一括デフォルト指定フラグOFF
     *
     * @param customerId 顧客ID
     * @return 更新件数
     */
    int updateAllNotDefault(String customerId);

}
