/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.addressbook;

import jp.co.itechh.quad.ddd.domain.addressbook.entity.AddressBookEntity;
import jp.co.itechh.quad.ddd.domain.addressbook.entity.AddressBookEntityService;
import jp.co.itechh.quad.ddd.domain.addressbook.repository.IAddressBookRepository;
import jp.co.itechh.quad.ddd.domain.addressbook.valueobject.Address;
import jp.co.itechh.quad.ddd.domain.addressbook.valueobject.AddressId;
import jp.co.itechh.quad.ddd.domain.addressbook.valueobject.FullName;
import jp.co.itechh.quad.ddd.domain.addressbook.valueobject.Furigana;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 顧客住所登録ユースケース
 */
@Service
public class RegistAddressRelateCustomerInfoUseCase {

    /** 住所録ドメインサービス */
    private final AddressBookEntityService addressBookEntityService;

    /** 住所録リポジトリ */
    private final IAddressBookRepository addressBookRepository;

    /** コンストラクタ */
    @Autowired
    public RegistAddressRelateCustomerInfoUseCase(AddressBookEntityService addressBookEntityService,
                                                  IAddressBookRepository addressBookRepository) {
        this.addressBookEntityService = addressBookEntityService;
        this.addressBookRepository = addressBookRepository;
    }

    /**
     * 顧客情報に紐づく住所を登録する
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
     * @param defaultFlag  デフォルト指定フラグ
     * @param preAddressId 変更前の住所ID
     * @return addressId 変更後の住所ID
     */
    public String registAddressRelateCustomerInfo(String customerId,
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
                                                  boolean defaultFlag,
                                                  String preAddressId) {

        // 会員情報に紐づく住所録を生成する
        AddressBookEntity addressBookEntity =
                        this.addressBookEntityService.createAddressBookRelateCustomerInfo(customerId, addressName,
                                                                                          new Address(new FullName(
                                                                                                          lastName,
                                                                                                          firstName
                                                                                          ), new Furigana(lastKana,
                                                                                                          firstKana
                                                                                          ), tel, zipCode, prefecture,
                                                                                                      address1,
                                                                                                      address2, address3
                                                                                          ), shippingMemo,
                                                                                          new AddressId(preAddressId)
                                                                                         );

        // 変更後の住所録がnullの場合、登録不要のため処理をスキップする
        if (addressBookEntity == null) {
            return null;
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
