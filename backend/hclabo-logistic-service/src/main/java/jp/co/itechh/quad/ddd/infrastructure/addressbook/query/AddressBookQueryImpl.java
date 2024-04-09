/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.infrastructure.addressbook.query;

import jp.co.itechh.quad.ddd.infrastructure.addressbook.dao.AddressBookDao;
import jp.co.itechh.quad.ddd.infrastructure.addressbook.dbentity.AddressBookDbEntity;
import jp.co.itechh.quad.ddd.usecase.addressbook.query.IAddressBookQuery;
import jp.co.itechh.quad.ddd.usecase.addressbook.query.model.AddressQueryCondition;
import jp.co.itechh.quad.ddd.usecase.addressbook.query.model.AddressQueryModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 住所録クエリー実装クラス
 *
 * @author kimura
 */
@Component
public class AddressBookQueryImpl implements IAddressBookQuery {

    /** 住所録Dao */
    private final AddressBookDao addressBookDao;

    /** 住所録クエリー実装クラスHelperクラス */
    private final AddressBookQueryHelper addressBookQueryHelper;

    /**
     * コンストラクタ
     *
     * @param addressBookDao 住所録Dao
     * @param addressBookQueryHelper 住所録クエリー実装クラスHelperクラス
     */
    @Autowired
    public AddressBookQueryImpl(AddressBookDao addressBookDao, AddressBookQueryHelper addressBookQueryHelper) {
        this.addressBookDao = addressBookDao;
        this.addressBookQueryHelper = addressBookQueryHelper;
    }

    /**
     * 公開中住所リスト取得
     *
     * @param condition 住所録クエリーコンディション
     * @return 住所録クエリーモデルリスト
     */
    @Override
    public List<AddressQueryModel> getOpenAddressListByCustomerId(AddressQueryCondition condition) {

        List<AddressBookDbEntity> addressBookDbEntities =
                        addressBookDao.getOpenAddressListByCustomerId(condition.getCustomerId(),
                                                                      condition.getExclusionAddressId(),
                                                                      condition.getPageInfo().getSelectOptions()
                                                                     );

        List<AddressQueryModel> addressQueryModels =
                        addressBookQueryHelper.toAddressQueryModelList(addressBookDbEntities);

        return addressQueryModels;

    }

}