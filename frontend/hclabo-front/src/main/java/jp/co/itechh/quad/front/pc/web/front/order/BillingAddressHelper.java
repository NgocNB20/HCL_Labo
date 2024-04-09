package jp.co.itechh.quad.front.pc.web.front.order;

import jp.co.itechh.quad.addressbook.presentation.api.param.AddressBookAddressListResponse;
import jp.co.itechh.quad.addressbook.presentation.api.param.AddressBookAddressRegistRequest;
import jp.co.itechh.quad.customer.presentation.api.param.CustomerResponse;
import jp.co.itechh.quad.front.base.utility.ConversionUtility;
import jp.co.itechh.quad.front.constant.type.HTypeSexUnnecessaryAnswer;
import jp.co.itechh.quad.front.entity.memberinfo.MemberInfoEntity;
import jp.co.itechh.quad.front.pc.web.front.order.common.AddressBookModel;
import jp.co.itechh.quad.front.util.common.EnumTypeUtil;
import jp.co.itechh.quad.salesslip.presentation.api.SalesSlipApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * お客様情報入力画面 Helper
 *
 * @author Pham Quang Dieu
 */
@Component
public class BillingAddressHelper {

    /**
     * 変換Helper
     */
    private final ConversionUtility conversionUtility;

    /** 販売伝票API */
    private SalesSlipApi salesSlipApi;

    /**
     * コンストラクタ
     *
     * @param conversionUtility 変換Helper
     * @param salesSlipApi 販売伝票API
     */
    @Autowired
    public BillingAddressHelper(ConversionUtility conversionUtility, SalesSlipApi salesSlipApi) {
        this.conversionUtility = conversionUtility;
        this.salesSlipApi = salesSlipApi;
    }

    /**
     * 会員エンティティに変換
     *
     * @param customerResponse 会員レスポンス
     * @return 会員クラス
     */
    public MemberInfoEntity toMemberInfoEntity(CustomerResponse customerResponse) {
        // 処理前は存在しないためnullを返す
        if (customerResponse == null) {
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
     * アドレス帳クラス
     *
     * @param addressBookAddressListResponse 住所録レスポンス
     * @return アドレス帳クラス
     */
    public List<AddressBookModel> convertToAddressBookEntityList(AddressBookAddressListResponse addressBookAddressListResponse) {
        // 処理前は存在しないためnullを返す
        if (addressBookAddressListResponse == null || CollectionUtils.isEmpty(
                        addressBookAddressListResponse.getAddressList())) {
            return null;
        }

        List<AddressBookModel> addressBookModelList = new ArrayList<>();

        addressBookAddressListResponse.getAddressList().forEach(address -> {
            AddressBookModel model = new AddressBookModel();
            model.setBillingAddressId(address.getAddressId());
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
        });
        return addressBookModelList;
    }

    /**
     * 住所登録リクエストに変換
     *
     * @param billingAddressModel 注文Model
     * @return 住所登録リクエスト
     */
    public AddressBookAddressRegistRequest toAddressBookRegistRequest(BillingAddressModel billingAddressModel) {
        // 処理前は存在しないためnullを返す
        if (ObjectUtils.isEmpty(billingAddressModel)) {
            return null;
        }

        AddressBookAddressRegistRequest addressBookAddressRegistRequest = new AddressBookAddressRegistRequest();

        addressBookAddressRegistRequest.setAddressName(billingAddressModel.getAddressName());
        addressBookAddressRegistRequest.setLastName(billingAddressModel.getBillingAddressLastName());
        addressBookAddressRegistRequest.setFirstName(billingAddressModel.getBillingAddressFirstName());
        addressBookAddressRegistRequest.setFirstKana(billingAddressModel.getBillingAddressFirstKana());
        addressBookAddressRegistRequest.setLastKana(billingAddressModel.getBillingAddressLastKana());
        addressBookAddressRegistRequest.setTel(billingAddressModel.getBillingAddressTel());
        addressBookAddressRegistRequest.setZipCode(billingAddressModel.getBillingAddressZipCode1()
                                                   + billingAddressModel.getBillingAddressZipCode2());
        addressBookAddressRegistRequest.setPrefecture(billingAddressModel.getBillingAddressPrefecture());
        addressBookAddressRegistRequest.setAddress1(billingAddressModel.getBillingAddress1());
        addressBookAddressRegistRequest.setAddress2(billingAddressModel.getBillingAddress2());
        addressBookAddressRegistRequest.setAddress3(billingAddressModel.getBillingAddress3() != null ?
                                                                    billingAddressModel.getBillingAddress3() :
                                                                    "");
        addressBookAddressRegistRequest.setShippingMemo(billingAddressModel.getShippingMemo());

        return addressBookAddressRegistRequest;
    }

}