/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.addressbook;

import jp.co.itechh.quad.ddd.domain.addressbook.entity.AddressBookEntity;
import jp.co.itechh.quad.ddd.domain.addressbook.repository.IAddressBookRepository;
import jp.co.itechh.quad.ddd.domain.addressbook.valueobject.AddressId;
import jp.co.itechh.quad.ddd.exception.DomainException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 住所取得ユースケース
 */
@Service
public class GetAddressUseCase {

    /** 住所録リポジトリ */
    private final IAddressBookRepository addressBookRepository;

    /** コンストラクタ */
    @Autowired
    public GetAddressUseCase(IAddressBookRepository addressBookRepository) {
        this.addressBookRepository = addressBookRepository;
    }

    /**
     * 住所を取得する
     *
     * @param addressId 住所ID
     * @return addressBookEntity 住所録（住所情報）
     */
    public AddressBookEntity getAddress(String addressId) {

        // 住所録（住所情報）を取得
        AddressBookEntity addressBookEntity = this.addressBookRepository.get(new AddressId(addressId));

        // 住所録（住所情報）が取得できない場合はエラー
        if (addressBookEntity == null) {
            // 住所録（住所情報）取得失敗
            throw new DomainException("LOGISTIC-ADDB0001-E", new String[] {addressId});
        }

        return addressBookEntity;
    }

}