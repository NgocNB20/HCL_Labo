/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.pc.web.front.member.abook;

import jp.co.itechh.quad.addressbook.presentation.api.param.Address;
import jp.co.itechh.quad.addressbook.presentation.api.param.AddressBookAddressRegistRequest;
import jp.co.itechh.quad.front.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.front.base.utility.ConversionUtility;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * アドレス帳 Helperクラス
 *
 * @author kn23834
 * @version $Revision: 1.0 $
 */
@Component
public class MemberAbookHelper {

    /** 変換ユーティリティクラス */
    private final ConversionUtility conversionUtility;

    /** コンストラクタ */
    public MemberAbookHelper(ConversionUtility conversionUtility) {
        this.conversionUtility = conversionUtility;
    }

    /**
     * ページ変換：初期表示<br/>
     *
     * @param addressList      住所録
     * @param memberAbookModel アドレス帳Model
     */
    public void toPageForLoad(List<Address> addressList, MemberAbookModel memberAbookModel) {

        List<MemberAbookModelItem> abookModelItemList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(addressList)) {
            for (Address address : addressList) {
                MemberAbookModelItem abookModelItem = ApplicationContextUtility.getBean(MemberAbookModelItem.class);

                abookModelItem.setAddressBookSeq(address.getAddressId());
                abookModelItem.setMemberInfoSeq(
                                memberAbookModel.getCommonInfo().getCommonInfoUser().getMemberInfoSeq());

                abookModelItem.setAddressBookName(address.getAddressName());

                abookModelItem.setAddressBookLastName(address.getLastName());
                abookModelItem.setAddressBookFirstName(address.getFirstName());
                abookModelItem.setAddressBookLastKana(address.getLastKana());
                abookModelItem.setAddressBookFirstKana(address.getFirstKana());

                abookModelItem.setAddressBookTel(address.getTel());

                String[] zipCode = this.conversionUtility.toZipCodeArray(address.getZipCode());
                abookModelItem.setAddressBookZipCode1(zipCode[0]);
                abookModelItem.setAddressBookZipCode2(zipCode[1]);
                abookModelItem.setAddressBookPrefecture(address.getPrefecture());
                abookModelItem.setAddressBookAddress1(address.getAddress1());
                abookModelItem.setAddressBookAddress2(address.getAddress2());
                abookModelItem.setAddressBookAddress3(address.getAddress3());
                abookModelItem.setShippingMemo(address.getShippingMemo());

                abookModelItem.setAbid(address.getAddressId());
                abookModelItemList.add(abookModelItem);
            }
        }

        memberAbookModel.setAddressBookItems(abookModelItemList);
    }

    /**
     * 住所録登録リクエスト変換<br/>
     *
     * @param memberAbookModel アドレス帳モデル
     * @return 住所録登録リクエスト
     */
    public AddressBookAddressRegistRequest toRequestForAddressBookRegist(MemberAbookModel memberAbookModel) {

        AddressBookAddressRegistRequest request = new AddressBookAddressRegistRequest();

        request.setAddressName(memberAbookModel.getAddressBookName());
        request.setLastName(memberAbookModel.getLastName());
        request.setFirstName(memberAbookModel.getFirstName());
        request.setLastKana(memberAbookModel.getLastKana());
        request.setFirstKana(memberAbookModel.getFirstKana());
        request.setTel(memberAbookModel.getTel());
        request.setZipCode(memberAbookModel.getZipCode1() + memberAbookModel.getZipCode2());
        request.setPrefecture(memberAbookModel.getPrefecture());
        request.setAddress1(memberAbookModel.getAddress1());
        request.setAddress2(memberAbookModel.getAddress2());
        request.setAddress3(memberAbookModel.getAddress3());
        request.setShippingMemo(memberAbookModel.getShippingMemo());

        return request;
    }

}