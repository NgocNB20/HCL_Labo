/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.infrastructure.addressbook.query;

import jp.co.itechh.quad.ddd.domain.addressbook.valueobject.AddressId;
import jp.co.itechh.quad.ddd.infrastructure.addressbook.dbentity.AddressBookDbEntity;
import jp.co.itechh.quad.ddd.infrastructure.addressbook.repository.AddressBookRepositoryHelper;
import jp.co.itechh.quad.ddd.usecase.addressbook.query.model.AddressQueryModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 住所録クエリー実装クラスHelperクラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Component
public class AddressBookQueryHelper {

    /** 住所録リポジトリ実装クラスHelperクラス */
    private final AddressBookRepositoryHelper addressBookRepositoryHelper;

    /**
     * コンストラクタ
     *
     * @param addressBookRepositoryHelper 住所録リポジトリ実装クラスHelperクラス
     */
    @Autowired
    public AddressBookQueryHelper(AddressBookRepositoryHelper addressBookRepositoryHelper) {
        this.addressBookRepositoryHelper = addressBookRepositoryHelper;
    }

    /**
     * 住所録クエリーモデルリストに変換
     *
     * @param addressBookDbEntities 住所録DBエンティティリスト
     * @return 住所録クエリーモデルリスト
     */
    public List<AddressQueryModel> toAddressQueryModelList(List<AddressBookDbEntity> addressBookDbEntities) {

        if (CollectionUtils.isEmpty(addressBookDbEntities)) {
            return null;
        }

        List<AddressQueryModel> addressQueryModels = new ArrayList<>();

        for (AddressBookDbEntity addressBookDbEntity : addressBookDbEntities) {

            AddressQueryModel addressQueryModel = new AddressQueryModel();

            addressQueryModel.setAddressId(new AddressId(addressBookDbEntity.getAddressId()));
            addressQueryModel.setAddressName(addressBookDbEntity.getAddressName());
            addressQueryModel.setAddress(
                            addressBookRepositoryHelper.toAddressFromAddressBookDbEntity(addressBookDbEntity));
            addressQueryModel.setShippingMemo(addressBookDbEntity.getShippingMemo());

            addressQueryModels.add(addressQueryModel);
        }

        return addressQueryModels;

    }

}