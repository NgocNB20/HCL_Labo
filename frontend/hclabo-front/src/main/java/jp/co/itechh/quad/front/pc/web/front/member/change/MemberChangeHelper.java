package jp.co.itechh.quad.front.pc.web.front.member.change;

import jp.co.itechh.quad.addressbook.presentation.api.param.AddressBookAddressRegistRequest;
import jp.co.itechh.quad.addressbook.presentation.api.param.AddressBookAddressRegistResponse;
import jp.co.itechh.quad.addressbook.presentation.api.param.AddressBookAddressResponse;
import jp.co.itechh.quad.addressbook.presentation.api.param.AddressBookCustomerAddressRegistRequest;
import jp.co.itechh.quad.customer.presentation.api.param.CustomerAddressRegistRequest;
import jp.co.itechh.quad.customer.presentation.api.param.CustomerResponse;
import jp.co.itechh.quad.customer.presentation.api.param.CustomerUpdateRequest;
import jp.co.itechh.quad.front.base.utility.ConversionUtility;
import jp.co.itechh.quad.front.constant.type.HTypeSendStatus;
import jp.co.itechh.quad.front.constant.type.HTypeSexUnnecessaryAnswer;
import jp.co.itechh.quad.front.entity.mailmagazine.MailMagazineMemberEntity;
import jp.co.itechh.quad.front.entity.memberinfo.MemberInfoEntity;
import jp.co.itechh.quad.front.util.common.EnumTypeUtil;
import jp.co.itechh.quad.mailmagazine.presentation.api.param.MailmagazineMemberResponse;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.InvocationTargetException;

/**
 * 会員情報変更 Helper
 *
 * @author PHAM QUANG DIEU (VJP)
 */
@Component
public class MemberChangeHelper {

    /** 変換Helper */
    private final ConversionUtility conversionUtility;

    /** コンストラクタ */
    public MemberChangeHelper(ConversionUtility conversionUtility) {
        this.conversionUtility = conversionUtility;
    }

    /**
     * 初期処理の画面表示<br/>
     * 取得した会員情報をページの各項目にセット<br/>
     *
     * @param memberInfoEntity  会員エンティティ
     * @param memberChangeModel 会員情報変更入力画面
     * @param addressResponse   住所録レスポンス
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public void toPageForLoad(MemberInfoEntity memberInfoEntity,
                              MemberChangeModel memberChangeModel,
                              AddressBookAddressResponse addressResponse)
                    throws IllegalAccessException, InvocationTargetException {
        // 処理前は存在しないためnullを返す
        if (memberInfoEntity != null) {

            // 取得した会員情報をセッションに保存
            memberChangeModel.setMemberInfoEntity(memberInfoEntity);

            // フィールドコピー
            BeanUtils.copyProperties(memberChangeModel, memberInfoEntity);

            // 性別
            memberChangeModel.setMemberInfoSex(memberInfoEntity.getMemberInfoSex().getValue());

            // 生年月日
            if (memberInfoEntity.getMemberInfoBirthday() != null) {

                String[] birthdayArray = this.conversionUtility.toYmdArray(memberInfoEntity.getMemberInfoBirthday());
                memberChangeModel.setMemberInfoBirthdayYear(birthdayArray[0]);
                memberChangeModel.setMemberInfoBirthdayMonth(birthdayArray[1]);
                memberChangeModel.setMemberInfoBirthdayDay(birthdayArray[2]);
            }
        }

        if (addressResponse != null) {

            // 郵便番号
            String[] zipCodeArray = this.conversionUtility.toZipCodeArray(addressResponse.getZipCode());
            memberChangeModel.setMemberInfoZipCode1(zipCodeArray[0]);
            memberChangeModel.setMemberInfoZipCode2(zipCodeArray[1]);

            // 住所情報
            memberChangeModel.setMemberInfoPrefecture(addressResponse.getPrefecture());
            memberChangeModel.setMemberInfoAddress1(addressResponse.getAddress1());
            memberChangeModel.setMemberInfoAddress2(addressResponse.getAddress2());
            memberChangeModel.setMemberInfoAddress3(addressResponse.getAddress3());

            // 完了画面に引き継ぐための画面入力外項目を設定
            memberChangeModel.setPreAddressId(addressResponse.getAddressId());
            memberChangeModel.setAddressName(addressResponse.getAddressName());
            memberChangeModel.setShippingMemo(addressResponse.getShippingMemo());
        }
    }

    /**
     * 会員エンティティに変換
     *
     * @param customerResponse 会員レスポンス
     * @return 会員クラス
     */
    public MemberInfoEntity toMemberInfoEntity(CustomerResponse customerResponse) {
        // 処理前は存在しないためnullを返す
        if (ObjectUtils.isEmpty(customerResponse)) {
            return null;
        }
        MemberInfoEntity memberInfoEntity = new MemberInfoEntity();
        // 会員ID
        memberInfoEntity.setMemberInfoId(customerResponse.getMemberInfoId());
        memberInfoEntity.setMemberInfoSeq(customerResponse.getMemberInfoSeq());
        // 氏名
        memberInfoEntity.setMemberInfoFirstName(customerResponse.getMemberInfoFirstName());
        memberInfoEntity.setMemberInfoLastName(customerResponse.getMemberInfoLastName());
        // フリガナ
        memberInfoEntity.setMemberInfoFirstKana(customerResponse.getMemberInfoFirstKana());
        memberInfoEntity.setMemberInfoLastKana(customerResponse.getMemberInfoLastKana());
        // 性別
        if (customerResponse.getMemberInfoSex() != null) {
            memberInfoEntity.setMemberInfoSex(EnumTypeUtil.getEnumFromValue(HTypeSexUnnecessaryAnswer.class,
                                                                            customerResponse.getMemberInfoSex()
                                                                           ));
        }
        // 誕生日
        memberInfoEntity.setMemberInfoBirthday(conversionUtility.toTimestamp(customerResponse.getMemberInfoBirthday()));
        // 電話番号
        memberInfoEntity.setMemberInfoTel(customerResponse.getMemberInfoTel());
        // メール
        memberInfoEntity.setMemberInfoMail(customerResponse.getMemberInfoMail());

        memberInfoEntity.setMemberInfoUniqueId(customerResponse.getMemberInfoUniqueId());

        return memberInfoEntity;
    }

    /**
     * メルマガ購読者クラスに変換
     *
     * @param mailmagazineMemberResponse メルマガ会員レスポンス
     * @return メルマガ購読者クラス
     */
    public MailMagazineMemberEntity toMailMagazineMemberEntity(MailmagazineMemberResponse mailmagazineMemberResponse) {
        // 処理前は存在しないためnullを返す
        if (ObjectUtils.isEmpty(mailmagazineMemberResponse)) {
            return null;
        }

        MailMagazineMemberEntity mailMagazineMemberEntity = new MailMagazineMemberEntity();
        mailMagazineMemberEntity.setShopSeq(1001);
        mailMagazineMemberEntity.setMemberinfoSeq(mailmagazineMemberResponse.getMemberinfoSeq());
        mailMagazineMemberEntity.setUniqueMail(mailmagazineMemberResponse.getUniqueMail());
        mailMagazineMemberEntity.setMail(mailmagazineMemberResponse.getMail());
        mailMagazineMemberEntity.setSendStatus(EnumTypeUtil.getEnumFromValue(HTypeSendStatus.class,
                                                                             mailmagazineMemberResponse.getSendStatus()
                                                                            ));
        mailMagazineMemberEntity.setRegistTime(
                        conversionUtility.toTimestamp(mailmagazineMemberResponse.getRegistTime()));
        mailMagazineMemberEntity.setUpdateTime(
                        conversionUtility.toTimestamp(mailmagazineMemberResponse.getUpdateTime()));

        return mailMagazineMemberEntity;
    }

    /**
     * 変更する会員情報を作成する<br/>
     *
     * @param memberChangeModel 会員情報変更確認画面
     * @param addressResponse   住所登録レスポンス
     * @return MemberInfoEntity 変更する会員情報
     */
    public CustomerUpdateRequest toMemberInfoRequestForMemberInfoUpdate(MemberChangeModel memberChangeModel,
                                                                        AddressBookAddressRegistResponse addressResponse) {
        // 処理前は存在しないためnullを返す
        if (memberChangeModel == null) {
            return null;
        }

        // 元会員情報コピー
        CustomerUpdateRequest customerUpdateRequest = new CustomerUpdateRequest();

        // 画面入力情報で上書き
        customerUpdateRequest.setMemberInfoFirstKana(memberChangeModel.getMemberInfoFirstKana());
        customerUpdateRequest.setMemberInfoFirstName(memberChangeModel.getMemberInfoFirstName());
        customerUpdateRequest.setMemberInfoLastKana(memberChangeModel.getMemberInfoLastKana());
        customerUpdateRequest.setMemberInfoLastName(memberChangeModel.getMemberInfoLastName());
        customerUpdateRequest.setMemberInfoTel(memberChangeModel.getMemberInfoTel());

        // 性別
        customerUpdateRequest.setMemberInfoSex(memberChangeModel.getMemberInfoSex());

        customerUpdateRequest.setMailMagazine(memberChangeModel.isMailMagazine());
        customerUpdateRequest.setPreMailMagazine(memberChangeModel.isPreMailMagazine());

        // 生年月日
        if (memberChangeModel.getMemberInfoBirthdayYear() != null) {
            customerUpdateRequest.setMemberInfoBirthdayYear(memberChangeModel.getMemberInfoBirthdayYear());
            customerUpdateRequest.setMemberInfoBirthdayMonth(memberChangeModel.getMemberInfoBirthdayMonth());
            customerUpdateRequest.setMemberInfoBirthdayDay(memberChangeModel.getMemberInfoBirthdayDay());
        }

        // 会員情報に紐づく住所情報を更新
        if (addressResponse != null && StringUtils.isNotBlank(addressResponse.getAddressId())) {
            customerUpdateRequest.setMemberInfoAddressId(addressResponse.getAddressId());
        } else {
            // 住所情報を新規登録していないため、入力画面で取得した変更前の住所IDをセット
            customerUpdateRequest.setMemberInfoAddressId(memberChangeModel.getPreAddressId());
        }

        // 住所情報を設定
        CustomerAddressRegistRequest customerAddressRegistRequest = new CustomerAddressRegistRequest();
        // 住所名は「性＋名」を設定
        customerAddressRegistRequest.setAddressName(
                        memberChangeModel.getMemberInfoLastName() + memberChangeModel.getMemberInfoFirstName());
        customerAddressRegistRequest.setLastName(memberChangeModel.getMemberInfoLastName());
        customerAddressRegistRequest.setFirstName(memberChangeModel.getMemberInfoFirstName());
        customerAddressRegistRequest.setLastKana(memberChangeModel.getMemberInfoLastKana());
        customerAddressRegistRequest.setFirstKana(memberChangeModel.getMemberInfoFirstKana());
        customerAddressRegistRequest.setTel(memberChangeModel.getMemberInfoTel());
        customerAddressRegistRequest.setZipCode(
                        memberChangeModel.getMemberInfoZipCode1() + memberChangeModel.getMemberInfoZipCode2());
        customerAddressRegistRequest.setPrefecture(memberChangeModel.getMemberInfoPrefecture());
        customerAddressRegistRequest.setAddress1(memberChangeModel.getMemberInfoAddress1());
        customerAddressRegistRequest.setAddress2(memberChangeModel.getMemberInfoAddress2());
        customerAddressRegistRequest.setAddress3(memberChangeModel.getMemberInfoAddress3());

        // 入力画面で取得した情報をそのまま引き継ぐ項目
        customerAddressRegistRequest.setShippingMemo(memberChangeModel.getShippingMemo());

        customerUpdateRequest.setAddressBook(customerAddressRegistRequest);

        return customerUpdateRequest;
    }

    /**
     * 変更する会員情報を作成する<br/>
     *
     * @param memberChangeModel 会員情報変更確認画面
     * @return request 顧客住所登録リクエスト
     */
    public AddressBookCustomerAddressRegistRequest toRegistAddressRequestForMemberInfoUpdate(MemberChangeModel memberChangeModel) {

        AddressBookCustomerAddressRegistRequest request = new AddressBookCustomerAddressRegistRequest();
        AddressBookAddressRegistRequest addressBook = new AddressBookAddressRegistRequest();

        // 画面入力情報で上書き
        // 住所情報
        addressBook.setAddressName(memberChangeModel.getMemberInfoLastName() + memberChangeModel.getMemberInfoFirstName());
        addressBook.setLastName(memberChangeModel.getMemberInfoLastName());
        addressBook.setFirstName(memberChangeModel.getMemberInfoFirstName());
        addressBook.setLastKana(memberChangeModel.getMemberInfoLastKana());
        addressBook.setFirstKana(memberChangeModel.getMemberInfoFirstKana());
        addressBook.setTel(memberChangeModel.getMemberInfoTel());
        addressBook.setZipCode(memberChangeModel.getMemberInfoZipCode1() + memberChangeModel.getMemberInfoZipCode2());
        addressBook.setPrefecture(memberChangeModel.getMemberInfoPrefecture());
        addressBook.setAddress1(memberChangeModel.getMemberInfoAddress1());
        addressBook.setAddress2(memberChangeModel.getMemberInfoAddress2());
        addressBook.setAddress3(memberChangeModel.getMemberInfoAddress3());

        // 入力画面で取得した情報をそのまま引き継ぐ項目
        addressBook.setShippingMemo(memberChangeModel.getShippingMemo());

        // 会員住所情報の住所録は、デフォルト指定フラグを立てる
        // 注文フローで初期選択される配送先となる
        addressBook.setDefaultFlag(true);

        // 変更前住所ID ※画面入力外の項目
        request.setPreAddressId(memberChangeModel.getPreAddressId());

        request.setAddressBook(addressBook);

        return request;
    }

}
