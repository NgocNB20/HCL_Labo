/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.pc.web.front.regist;

import jp.co.itechh.quad.addressbook.presentation.api.param.AddressBookAddressRegistRequest;
import jp.co.itechh.quad.addressbook.presentation.api.param.AddressBookAddressRegistResponse;
import jp.co.itechh.quad.customer.presentation.api.param.CustomerAddressRegistRequest;
import jp.co.itechh.quad.customer.presentation.api.param.CustomerRegistRequest;
import jp.co.itechh.quad.front.base.utility.ConversionUtility;
import jp.co.itechh.quad.temp.presentation.api.param.MemberInforResponse;
import jp.co.itechh.quad.temp.presentation.api.param.TempCustomerResponse;
import org.springframework.stereotype.Component;

/**
 * 本会員登録 Helperクラス
 *
 * @author kimura
 */
@Component
public class RegistHelper {

    /** 変換Helper */
    private final ConversionUtility conversionUtility;

    /**
     * コンストラクター
     *
     * @param conversionUtility 変換Helper
     */
    public RegistHelper(ConversionUtility conversionUtility) {
        this.conversionUtility = conversionUtility;
    }

    /**
     * 初期表示
     * ・画面表示用の各種ラジオ選択子のセット
     * ・仮会員情報を画面項目にセット
     *
     * @param tempCustomerResponse 仮会員レスポンス
     * @param registModel          本会員登録入力画面
     */
    public void toPageForLoad(TempCustomerResponse tempCustomerResponse, RegistModel registModel) {

        MemberInforResponse memberInforResponse = tempCustomerResponse.getMemberInfo();

        // メールアドレス
        registModel.setMemberInfoMail(memberInforResponse.getMemberInfoMail());

        // 氏名
        registModel.setMemberInfoLastName(memberInforResponse.getMemberInfoLastName());
        registModel.setMemberInfoFirstName(memberInforResponse.getMemberInfoFirstName());

        // フリガナ
        registModel.setMemberInfoLastKana(memberInforResponse.getMemberInfoLastKana());
        registModel.setMemberInfoFirstKana(memberInforResponse.getMemberInfoFirstKana());

        // 性別
        if (memberInforResponse.getMemberInfoSex() != null) {
            registModel.setMemberInfoSex(memberInforResponse.getMemberInfoSex());
        }

        // 電話番号
        registModel.setMemberInfoTel(memberInforResponse.getMemberInfoTel());
    }

    /**
     * 登録する会員情報の作成
     *
     * @param registModel     本会員登録確認画面
     * @param addressResponse 住所登録レスポンス
     * @return 登録する会員情報
     */
    public CustomerRegistRequest toMemberInfoEntityForMemberInfoRegist(String accessUid,
                                                                       RegistModel registModel,
                                                                       AddressBookAddressRegistResponse addressResponse) {

        // 会員情報の作成
        CustomerRegistRequest customerRegistRequest = new CustomerRegistRequest();

        // ID・メールアドレス
        customerRegistRequest.setMemberInfoMail(registModel.getMemberInfoMail());
        // 画面入力値で上書き
        customerRegistRequest.setMemberInfoFirstKana(registModel.getMemberInfoFirstKana());
        customerRegistRequest.setMemberInfoFirstName(registModel.getMemberInfoFirstName());
        customerRegistRequest.setMemberInfoLastKana(registModel.getMemberInfoLastKana());
        customerRegistRequest.setMemberInfoLastName(registModel.getMemberInfoLastName());
        customerRegistRequest.setMemberInfoAddressId(addressResponse.getAddressId());
        customerRegistRequest.setMemberInfoTel(registModel.getMemberInfoTel());
        customerRegistRequest.setMemberInfoPassword(registModel.getMemberInfoPassWord());
        customerRegistRequest.setMailMagazine(registModel.isMailMagazine());
        customerRegistRequest.setPreMailMagazine(registModel.isPreMailMagazine());
        customerRegistRequest.setMid(registModel.getMid());
        customerRegistRequest.setAccessUid(accessUid);
        // 性別
        customerRegistRequest.setMemberInfoSex(registModel.getMemberInfoSex());
        // 生年月日
        customerRegistRequest.setMemberInfoBirthdayDay(registModel.getMemberInfoBirthdayDay());
        customerRegistRequest.setMemberInfoBirthdayMonth(registModel.getMemberInfoBirthdayMonth());
        customerRegistRequest.setMemberInfoBirthdayYear(registModel.getMemberInfoBirthdayYear());

        // 住所情報を設定
        CustomerAddressRegistRequest customerAddressRegistRequest = new CustomerAddressRegistRequest();
        // 住所名は「性＋名」を設定
        customerAddressRegistRequest.setAddressName(
                        registModel.getMemberInfoLastName() + registModel.getMemberInfoFirstName());
        customerAddressRegistRequest.setLastName(registModel.getMemberInfoLastName());
        customerAddressRegistRequest.setFirstName(registModel.getMemberInfoFirstName());
        customerAddressRegistRequest.setLastKana(registModel.getMemberInfoLastKana());
        customerAddressRegistRequest.setFirstKana(registModel.getMemberInfoFirstKana());
        customerAddressRegistRequest.setTel(registModel.getMemberInfoTel());
        customerAddressRegistRequest.setZipCode(
                        registModel.getMemberInfoZipCode1() + registModel.getMemberInfoZipCode2());
        customerAddressRegistRequest.setPrefecture(registModel.getMemberInfoPrefecture());
        customerAddressRegistRequest.setAddress1(registModel.getMemberInfoAddress1());
        customerAddressRegistRequest.setAddress2(registModel.getMemberInfoAddress2());
        customerAddressRegistRequest.setAddress3(registModel.getMemberInfoAddress3());
        customerRegistRequest.setAddressBook(customerAddressRegistRequest);

        return customerRegistRequest;
    }

    /**
     * 登録する会員住所情報の作成
     *
     * @param registModel 本会員登録確認画面
     * @return 登録する会員情報
     */
    public AddressBookAddressRegistRequest toRegistAddressRequestForMemberInfoRegist(RegistModel registModel) {

        AddressBookAddressRegistRequest request = new AddressBookAddressRegistRequest();

        // 住所情報
        // 住所名は「性＋名」を設定
        request.setAddressName(registModel.getMemberInfoLastName() + registModel.getMemberInfoFirstName());
        request.setLastName(registModel.getMemberInfoLastName());
        request.setFirstName(registModel.getMemberInfoFirstName());
        request.setLastKana(registModel.getMemberInfoLastKana());
        request.setFirstKana(registModel.getMemberInfoFirstKana());
        request.setTel(registModel.getMemberInfoTel());
        request.setZipCode(registModel.getMemberInfoZipCode1() + registModel.getMemberInfoZipCode2());
        request.setPrefecture(registModel.getMemberInfoPrefecture());
        request.setAddress1(registModel.getMemberInfoAddress1());
        request.setAddress2(registModel.getMemberInfoAddress2());
        request.setAddress3(registModel.getMemberInfoAddress3());

        // 会員住所情報の住所録は、デフォルト指定フラグを立てる
        // 注文フローで初期選択される配送先となる
        request.setDefaultFlag(true);

        return request;
    }

}
