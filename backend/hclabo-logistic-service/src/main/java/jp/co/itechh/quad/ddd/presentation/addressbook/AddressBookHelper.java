/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.presentation.addressbook;

import jp.co.itechh.quad.addressbook.presentation.api.param.Address;
import jp.co.itechh.quad.addressbook.presentation.api.param.AddressBookAddressListResponse;
import jp.co.itechh.quad.addressbook.presentation.api.param.AddressBookAddressRegistResponse;
import jp.co.itechh.quad.addressbook.presentation.api.param.AddressBookAddressResponse;
import jp.co.itechh.quad.ddd.domain.addressbook.entity.AddressBookEntity;
import jp.co.itechh.quad.ddd.usecase.addressbook.query.model.AddressModel;
import jp.co.itechh.quad.ddd.usecase.addressbook.query.model.AddressQueryModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 住所録Helperクラス
 *
 * @author kimura
 */
@Component
public class AddressBookHelper {

    /**
     * 住所エンティティから住所レスポンスに変換
     *
     * @param entity 住所録エンティティ
     * @return response 住所レスポンス
     */
    public AddressBookAddressResponse toAddressBookAddressResponse(AddressBookEntity entity) {

        if (entity == null || entity.getAddress() == null) {
            return null;
        }

        AddressBookAddressResponse response = new AddressBookAddressResponse();
        response.setAddressId(entity.getAddressId().getValue());
        response.setAddressName(entity.getAddressName());
        response.setLastName(entity.getAddress().getFullName().getLastName());
        response.setFirstName(entity.getAddress().getFullName().getFirstName());
        response.setLastKana(entity.getAddress().getFurigana().getLastKana());
        response.setFirstKana(entity.getAddress().getFurigana().getFirstKana());
        response.setTel(entity.getAddress().getTel());
        response.setZipCode(entity.getAddress().getZipCode());
        response.setPrefecture(entity.getAddress().getPrefecture());
        response.setAddress1(entity.getAddress().getAddress1());
        response.setAddress2(entity.getAddress().getAddress2());
        response.setAddress3(entity.getAddress().getAddress3());
        response.setShippingMemo(entity.getShippingMemo());

        return response;
    }

    /**
     * 住所リストから住所リストレスポンスに変換
     *
     * @param addressModel 住所情報モデル
     * @return response 住所リストレスポンス
     */
    public AddressBookAddressListResponse toAddressBookAddressListResponse(AddressModel addressModel) {

        if (CollectionUtils.isEmpty(addressModel.getAddressList())) {
            return null;
        }

        AddressBookAddressListResponse response = new AddressBookAddressListResponse();
        List<Address> list = new ArrayList<>();
        for (int i = 0; i < addressModel.getAddressList().size(); i++) {

            // 冗長なため変換
            AddressQueryModel model = addressModel.getAddressList().get(i);

            Address address = new Address();
            address.setAddressId(model.getAddressId().getValue());
            address.setAddressName(model.getAddressName());
            address.setLastName(model.getAddress().getFullName().getLastName());
            address.setFirstName(model.getAddress().getFullName().getFirstName());
            address.setLastKana(model.getAddress().getFurigana().getLastKana());
            address.setFirstKana(model.getAddress().getFurigana().getFirstKana());
            address.setTel(model.getAddress().getTel());
            address.setZipCode(model.getAddress().getZipCode());
            address.setPrefecture(model.getAddress().getPrefecture());
            address.setAddress1(model.getAddress().getAddress1());
            address.setAddress2(model.getAddress().getAddress2());
            address.setAddress3(model.getAddress().getAddress3());
            address.setShippingMemo(model.getShippingMemo());
            list.add(address);
        }
        response.setAddressList(list);
        response.setPageInfo(addressModel.getPageInfoResponse());

        return response;
    }

    /**
     * 住所IDを住所登録レスポンスに変換
     *
     * @param addressId 住所ID
     * @return response 住所登録レスポンス
     */
    public AddressBookAddressRegistResponse toAddressBookAddressRegistResponse(String addressId) {

        if (StringUtils.isEmpty(addressId)) {
            return null;
        }

        AddressBookAddressRegistResponse response = new AddressBookAddressRegistResponse();
        response.setAddressId(addressId);
        return response;
    }

}