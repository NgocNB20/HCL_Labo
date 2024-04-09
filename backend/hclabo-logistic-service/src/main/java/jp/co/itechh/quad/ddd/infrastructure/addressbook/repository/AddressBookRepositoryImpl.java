/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.infrastructure.addressbook.repository;

import jp.co.itechh.quad.ddd.domain.addressbook.entity.AddressBookEntity;
import jp.co.itechh.quad.ddd.domain.addressbook.repository.IAddressBookRepository;
import jp.co.itechh.quad.ddd.domain.addressbook.valueobject.AddressId;
import jp.co.itechh.quad.ddd.infrastructure.addressbook.dao.AddressBookDao;
import jp.co.itechh.quad.ddd.infrastructure.addressbook.dbentity.AddressBookDbEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 住所録リポジトリ実装クラス
 *
 * @author kimura
 */
@Component
public class AddressBookRepositoryImpl implements IAddressBookRepository {

    /** 住所録Dao */
    private final AddressBookDao addressBookDao;

    /** 住所録リポジトリHelper */
    private final AddressBookRepositoryHelper addressBookRepositoryHelper;

    /**
     * コンストラクター
     *
     * @param addressBookDao              住所録Dao
     * @param addressBookRepositoryHelper 住所録リポジトリHelper
     */
    @Autowired
    public AddressBookRepositoryImpl(AddressBookDao addressBookDao,
                                     AddressBookRepositoryHelper addressBookRepositoryHelper) {
        this.addressBookDao = addressBookDao;
        this.addressBookRepositoryHelper = addressBookRepositoryHelper;
    }

    @Override
    public void save(AddressBookEntity addressBookEntity) {
        String addressId = null;
        if (addressBookEntity.getAddressId() != null) {
            addressId = addressBookEntity.getAddressId().getValue();
        }
        AddressBookDbEntity addressBookDbEntity =
                        addressBookRepositoryHelper.toAddressBookDbEntityFromAddressBookEntity(addressId,
                                                                                               addressBookEntity
                                                                                              );

        addressBookDao.insert(addressBookDbEntity);

    }

    @Override
    public int update(AddressBookEntity addressBookEntity) {
        String addressId = null;
        if (addressBookEntity.getAddressId() != null) {
            addressId = addressBookEntity.getAddressId().getValue();
        }
        AddressBookDbEntity addressBookDbEntity =
                        addressBookRepositoryHelper.toAddressBookDbEntityFromAddressBookEntity(addressId,
                                                                                               addressBookEntity
                                                                                              );

        return addressBookDao.update(addressBookDbEntity);
    }

    @Override
    public AddressBookEntity get(AddressId addressId) {
        AddressBookDbEntity addressBookDbEntity = addressBookDao.getByAddressId(addressId.getValue());

        AddressBookEntity addressBookEntity =
                        addressBookRepositoryHelper.toAddressBookEntityFromAddressBookDbEntity(addressBookDbEntity);

        return addressBookEntity;
    }

    @Override
    public int delete(AddressId addressId) {
        return addressBookDao.deleteByAddressId(addressId.getValue());
    }

    @Override
    public AddressBookEntity getDefault(String customerId) {
        AddressBookDbEntity dbEntity = addressBookDao.getDefaultByCustomerId(customerId);
        return addressBookRepositoryHelper.toAddressBookEntityFromAddressBookDbEntity(dbEntity);
    }

    @Override
    public int updateDefault(AddressId addressId) {
        return addressBookDao.updateDefaultByAddressId(addressId.getValue());
    }

    @Override
    public int updateAllClose(String customerId) {
        return addressBookDao.updateAllClose(customerId);
    }

    @Override
    public int updateAllNotDefault(String customerId) {
        return addressBookDao.updateAllNotDefault(customerId);
    }

}
