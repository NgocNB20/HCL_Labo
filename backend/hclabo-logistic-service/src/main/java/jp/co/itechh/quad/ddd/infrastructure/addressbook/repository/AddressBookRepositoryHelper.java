/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.infrastructure.addressbook.repository;

import jp.co.itechh.quad.ddd.domain.addressbook.entity.AddressBookEntity;
import jp.co.itechh.quad.ddd.domain.addressbook.valueobject.Address;
import jp.co.itechh.quad.ddd.domain.addressbook.valueobject.AddressId;
import jp.co.itechh.quad.ddd.domain.addressbook.valueobject.FullName;
import jp.co.itechh.quad.ddd.domain.addressbook.valueobject.Furigana;
import jp.co.itechh.quad.ddd.infrastructure.addressbook.dbentity.AddressBookDbEntity;
import org.springframework.stereotype.Component;

/**
 * 住所録リポジトリ実装クラスHelperクラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class AddressBookRepositoryHelper {

    /**
     * 住所録DBエンティティに変換
     *
     * @param addressId         住所ID
     * @param addressBookEntity 住所録 エンティティ
     * @return 住所録DBエンティティ
     */
    public AddressBookDbEntity toAddressBookDbEntityFromAddressBookEntity(String addressId,
                                                                          AddressBookEntity addressBookEntity) {

        if (addressBookEntity == null) {
            return null;
        }

        AddressBookDbEntity addressBookDbEntity = new AddressBookDbEntity();

        addressBookDbEntity.setCustomerId(addressBookEntity.getCustomerId());
        addressBookDbEntity.setAddressId(addressId);
        addressBookDbEntity.setAddressName(addressBookEntity.getAddressName());

        if (addressBookEntity.getAddress() != null) {
            if (addressBookEntity.getAddress().getFullName() != null) {
                addressBookDbEntity.setLastName(addressBookEntity.getAddress().getFullName().getLastName());
                addressBookDbEntity.setFirstName(addressBookEntity.getAddress().getFullName().getFirstName());
            }

            if (addressBookEntity.getAddress().getFurigana() != null) {
                addressBookDbEntity.setLastKana(addressBookEntity.getAddress().getFurigana().getLastKana());
                addressBookDbEntity.setFirstKana(addressBookEntity.getAddress().getFurigana().getFirstKana());
            }

            addressBookDbEntity.setTel(addressBookEntity.getAddress().getTel());
            addressBookDbEntity.setZipCode(addressBookEntity.getAddress().getZipCode());
            addressBookDbEntity.setPrefecture(addressBookEntity.getAddress().getPrefecture());
            addressBookDbEntity.setAddress1(addressBookEntity.getAddress().getAddress1());
            addressBookDbEntity.setAddress2(addressBookEntity.getAddress().getAddress2());
            addressBookDbEntity.setAddress3(addressBookEntity.getAddress().getAddress3());
        }

        addressBookDbEntity.setShippingMemo(addressBookEntity.getShippingMemo());
        addressBookDbEntity.setRegistDate(addressBookEntity.getRegistDate());
        addressBookDbEntity.setHideFlag(addressBookEntity.isHideFlag());
        addressBookDbEntity.setDefaultFlag(addressBookEntity.isDefaultFlag());

        return addressBookDbEntity;
    }

    /**
     * 住所録 エンティティに変換
     *
     * @param addressBookDbEntity 住所録DBエンティティ
     * @return 住所録 エンティティ
     */
    public AddressBookEntity toAddressBookEntityFromAddressBookDbEntity(AddressBookDbEntity addressBookDbEntity) {

        if (addressBookDbEntity == null) {
            return null;
        }

        return new AddressBookEntity(addressBookDbEntity.getCustomerId(),
                                     new AddressId(addressBookDbEntity.getAddressId()),
                                     addressBookDbEntity.getAddressName(),
                                     toAddressFromAddressBookDbEntity(addressBookDbEntity),
                                     addressBookDbEntity.getShippingMemo(), addressBookDbEntity.getRegistDate(),
                                     addressBookDbEntity.isHideFlag(), addressBookDbEntity.isDefaultFlag()
        );
    }

    /**
     * 住所 値オブジェクトに変換
     *
     * @param addressBookDbEntity 住所録DBエンティティ
     * @return 住所 値オブジェクト
     */
    public Address toAddressFromAddressBookDbEntity(AddressBookDbEntity addressBookDbEntity) {

        if (addressBookDbEntity == null) {
            return null;
        }

        return new Address(new FullName(addressBookDbEntity.getLastName(), addressBookDbEntity.getFirstName()),
                           new Furigana(addressBookDbEntity.getLastKana(), addressBookDbEntity.getFirstKana()),
                           addressBookDbEntity.getTel(), addressBookDbEntity.getZipCode(),
                           addressBookDbEntity.getPrefecture(), addressBookDbEntity.getAddress1(),
                           addressBookDbEntity.getAddress2(), addressBookDbEntity.getAddress3(), null
        );
    }

}
