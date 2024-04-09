/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.pc.web.front.order;

import jp.co.itechh.quad.addressbook.presentation.api.param.Address;
import jp.co.itechh.quad.addressbook.presentation.api.param.AddressBookAddressListResponse;
import jp.co.itechh.quad.addressbook.presentation.api.param.AddressBookAddressRegistRequest;
import jp.co.itechh.quad.front.base.utility.ConversionUtility;
import jp.co.itechh.quad.front.pc.web.front.order.common.AddressBookModel;
import jp.co.itechh.quad.front.pc.web.front.order.common.OrderCommonModel;
import jp.co.itechh.quad.shippingslip.presentation.api.param.ShippingSlipAddressUpdateRequest;
import jp.co.itechh.quad.shippingslip.presentation.api.param.ShippingSlipResponse;
import jp.co.itechh.quad.zipcode.presentation.api.param.ZipCodeCheckRequest;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * お届け先入力画面 Helper
 *
 * @author Pham Quang Dieu
 */
@Component
public class AddressSelectHelper {

    /** 変換Helper */
    private final ConversionUtility conversionUtility;

    /**
     * コンストラク
     * @param conversionUtility
     */
    @Autowired
    public AddressSelectHelper(ConversionUtility conversionUtility) {
        this.conversionUtility = conversionUtility;
    }

    /**
     * 住所登録リクエストに変換
     *
     * @param addressSelectModel お届け先入力画面Model
     * @return 住所登録リクエスト
     */
    public AddressBookAddressRegistRequest toAddressBookRegistRequest(AddressSelectModel addressSelectModel) {

        AddressBookAddressRegistRequest addressBookAddressRegistRequest = new AddressBookAddressRegistRequest();

        addressBookAddressRegistRequest.setAddressName(addressSelectModel.getAddressName());
        addressBookAddressRegistRequest.setLastName(addressSelectModel.getAddressLastName());
        addressBookAddressRegistRequest.setFirstName(addressSelectModel.getAddressFirstName());
        addressBookAddressRegistRequest.setFirstKana(addressSelectModel.getAddressFirstKana());
        addressBookAddressRegistRequest.setLastKana(addressSelectModel.getAddressLastKana());
        addressBookAddressRegistRequest.setTel(addressSelectModel.getAddressTel());
        addressBookAddressRegistRequest.setZipCode(
                        addressSelectModel.getAddressZipCode1() + addressSelectModel.getAddressZipCode2());
        addressBookAddressRegistRequest.setPrefecture(addressSelectModel.getAddressPrefecture());
        addressBookAddressRegistRequest.setAddress1(addressSelectModel.getAddressAddress1());
        addressBookAddressRegistRequest.setAddress2(addressSelectModel.getAddressAddress2());
        addressBookAddressRegistRequest.setAddress3(
                        addressSelectModel.getAddressAddress3() != null ? addressSelectModel.getAddressAddress3() : "");
        addressBookAddressRegistRequest.setShippingMemo(addressSelectModel.getShippingMemo());

        return addressBookAddressRegistRequest;
    }

    /**
     * 配送先設定リクエストに変換
     *
     * @param orderCommonModel 注文フロー共通Model
     * @param addressId        住所ID
     * @return 配送先設定リクエスト
     */
    public ShippingSlipAddressUpdateRequest toShippingSlipAddressUpdateRequest(OrderCommonModel orderCommonModel,
                                                                               String addressId) {

        ShippingSlipAddressUpdateRequest shippingSlipAddressUpdateRequest = new ShippingSlipAddressUpdateRequest();
        shippingSlipAddressUpdateRequest.setTransactionId(orderCommonModel.getTransactionId());
        shippingSlipAddressUpdateRequest.setShippingAddressId(addressId);

        return shippingSlipAddressUpdateRequest;
    }

    /**
     * お届け先住所の選択画面 Modelに変換
     *
     * @param addressBookAddressListResponse 住所録レスポンス
     * @param addressSelectModel            お届け先入力画面 Model
     */
    public void toAddressSelectModel(AddressBookAddressListResponse addressBookAddressListResponse,
                                     AddressSelectModel addressSelectModel) {
        // 処理前は存在しないためnullを返す
        if (addressBookAddressListResponse == null || CollectionUtils.isEmpty(
                        addressBookAddressListResponse.getAddressList())) {
            return;
        }

        List<AddressBookModel> addressBookModelList = new ArrayList<>();

        for (Address address : addressBookAddressListResponse.getAddressList()) {
            AddressBookModel model = new AddressBookModel();

            model.setAddressId(address.getAddressId());
            model.setAddressName(address.getAddressName());
            model.setLastName(address.getLastName());
            model.setFirstName(address.getFirstName());
            model.setLastKana(address.getLastKana());
            model.setFirstKana(address.getFirstKana());
            model.setTel(address.getTel());
            model.setZipCode(address.getZipCode());
            model.setPrefecture(address.getPrefecture());
            model.setAddress1(address.getAddress1());
            model.setAddress2(address.getAddress2());
            model.setAddress3(address.getAddress3() != null ? address.getAddress3() : "");
            model.setShippingMemo(address.getShippingMemo());

            addressBookModelList.add(model);
        }
        addressSelectModel.setAddressBookModelList(addressBookModelList);
    }

    /**
     * お届け先住所の選択 Modelに変換
     *
     * @param shippingSlipResponse 配送伝票レスポンス
     * @param addressSelectModel   お届け先入力画面 Model
     */
    public void toAddressSelectModel(ShippingSlipResponse shippingSlipResponse, AddressSelectModel addressSelectModel) {
        if (ObjectUtils.isNotEmpty(shippingSlipResponse)) {
            addressSelectModel.setShippingAddressId(shippingSlipResponse.getShippingAddressId());
        }
    }

    /**
     * 郵便番号整合性チェックリクエストに変換
     *
     * @param zipCode    郵便番号
     * @param prefecture 都道府県
     * @return 郵便番号整合性チェックリクエスト
     */
    public ZipCodeCheckRequest toZipCodeCheckRequest(String zipCode, String prefecture) {
        ZipCodeCheckRequest zipCodeCheckRequest = new ZipCodeCheckRequest();

        zipCodeCheckRequest.setZipCode(zipCode);
        zipCodeCheckRequest.setPrefecture(prefecture);

        return zipCodeCheckRequest;
    }

}