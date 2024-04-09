/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.addressbook;

import jp.co.itechh.quad.ddd.domain.addressbook.entity.AddressBookEntity;
import jp.co.itechh.quad.ddd.domain.addressbook.repository.IAddressBookRepository;
import jp.co.itechh.quad.ddd.domain.addressbook.valueobject.Address;
import jp.co.itechh.quad.ddd.domain.addressbook.valueobject.FullName;
import jp.co.itechh.quad.ddd.domain.addressbook.valueobject.Furigana;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 住所追加ユースケース
 */
@Service
public class AddAddressUseCase {

    /** 住所録リポジトリ */
    private final IAddressBookRepository addressBookRepository;

    /** コンストラクタ */
    @Autowired
    public AddAddressUseCase(IAddressBookRepository addressBookRepository) {
        this.addressBookRepository = addressBookRepository;
    }

    /**
     * 住所を追加する
     *
     * @param customerId   顧客ID
     * @param addressName  住所名
     * @param lastName     氏名(姓)
     * @param firstName    氏名(名)
     * @param lastKana     フリガナ(姓)
     * @param firstKana    フリガナ(名)
     * @param tel          電話番号
     * @param zipCode      郵便番号
     * @param prefecture   都道府県
     * @param address1     住所1
     * @param address2     住所2
     * @param address3     住所3
     * @param shippingMemo 配送メモ
     * @param hideFlag     非公開フラグ
     * @param defaultFlag  デフォルト指定フラグ
     * @return 住所ID
     */
    public String addAddress(String customerId,
                             String addressName,
                             String lastName,
                             String firstName,
                             String lastKana,
                             String firstKana,
                             String tel,
                             String zipCode,
                             String prefecture,
                             String address1,
                             String address2,
                             String address3,
                             String shippingMemo,
                             boolean hideFlag,
                             boolean defaultFlag) {

        // 住所を作成
        Address address =
                        new Address(new FullName(lastName, firstName), new Furigana(lastKana, firstKana), tel, zipCode,
                                    prefecture, address1, address2, address3
                        );

        // 住所を住所録に設定
        AddressBookEntity addressBookEntity =
                        new AddressBookEntity(customerId, addressName, address, shippingMemo, new Date());

        if (hideFlag) {
            addressBookEntity.close();
        }

        // デフォルト指定フラグが立っている場合、今回登録する住所録以外のフラグを落とし、自分のフラグを立てる
        if (defaultFlag) {
            addressBookRepository.updateAllNotDefault(customerId);
            addressBookEntity.setDefault();
        }

        // 住所録を登録
        this.addressBookRepository.save(addressBookEntity);

        return addressBookEntity.getAddressId().getValue();
    }

}
