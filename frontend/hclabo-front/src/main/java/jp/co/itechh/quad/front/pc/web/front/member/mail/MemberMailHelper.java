/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.pc.web.front.member.mail;

import jp.co.itechh.quad.customer.presentation.api.param.CustomerMailAddressUpdateSendMailRequest;
import jp.co.itechh.quad.customer.presentation.api.param.CustomerResponse;
import jp.co.itechh.quad.front.base.utility.ConversionUtility;
import jp.co.itechh.quad.front.constant.type.HTypeMemberInfoStatus;
import jp.co.itechh.quad.front.constant.type.HTypeSexUnnecessaryAnswer;
import jp.co.itechh.quad.front.entity.memberinfo.MemberInfoEntity;
import jp.co.itechh.quad.front.util.common.EnumTypeUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

/**
 * メールアドレス変更 Helper
 *
 * @author kn23834
 * @version $Revision: 1.0 $
 *
 */
@Component
public class MemberMailHelper {

    /** 変換ユーティリティクラス */
    private final ConversionUtility conversionUtility;

    /**
     * コンストラクタ
     *
     * @param conversionUtility 変換ユーティリティクラス
     */
    public MemberMailHelper(ConversionUtility conversionUtility) {
        this.conversionUtility = conversionUtility;
    }

    /**
     * 初期表示<br/>
     * 既存メールアドレスのセット<br/>
     *
     * @param memberInfoEntity 会員情報
     * @param memberMailModel メールアドレス変更Model
     */
    public void toPageForLoad(MemberInfoEntity memberInfoEntity, MemberMailModel memberMailModel) {
        if (!ObjectUtils.isEmpty(memberInfoEntity)) {
            memberMailModel.setMemberInfoMail(memberInfoEntity.getMemberInfoMail());
        }
    }

    /**
     * 会員クラスに変換<br/>
     *
     * @param customerResponse 会員レスポンス
     *
     * @return 会員クラス
     */
    public MemberInfoEntity toMemberInfoEntity(CustomerResponse customerResponse) {
        // 処理前は存在しないためnullを返す
        if (ObjectUtils.isEmpty(customerResponse)) {
            return null;
        }

        MemberInfoEntity memberInfoEntity = new MemberInfoEntity();

        memberInfoEntity.setMemberInfoSeq(customerResponse.getMemberInfoSeq());
        if (customerResponse.getMemberInfoStatus() != null) {
            memberInfoEntity.setMemberInfoStatus(EnumTypeUtil.getEnumFromValue(HTypeMemberInfoStatus.class,
                                                                               customerResponse.getMemberInfoStatus()
                                                                              ));
        }
        memberInfoEntity.setMemberInfoUniqueId(customerResponse.getMemberInfoUniqueId());
        memberInfoEntity.setMemberInfoId(customerResponse.getMemberInfoId());
        memberInfoEntity.setMemberInfoPassword(customerResponse.getMemberInfoPassword());
        memberInfoEntity.setMemberInfoLastName(customerResponse.getMemberInfoLastName());
        memberInfoEntity.setMemberInfoFirstName(customerResponse.getMemberInfoFirstName());
        memberInfoEntity.setMemberInfoLastKana(customerResponse.getMemberInfoLastKana());
        memberInfoEntity.setMemberInfoFirstKana(customerResponse.getMemberInfoFirstKana());
        if (customerResponse.getMemberInfoSex() != null) {
            memberInfoEntity.setMemberInfoSex(EnumTypeUtil.getEnumFromValue(HTypeSexUnnecessaryAnswer.class,
                                                                            customerResponse.getMemberInfoSex()
                                                                           ));
        }
        memberInfoEntity.setMemberInfoBirthday(conversionUtility.toTimestamp(customerResponse.getMemberInfoBirthday()));
        memberInfoEntity.setMemberInfoTel(customerResponse.getMemberInfoTel());
        memberInfoEntity.setMemberInfoMail(customerResponse.getMemberInfoMail());
        memberInfoEntity.setMemberInfoAddressId(customerResponse.getMemberInfoAddressId());
        memberInfoEntity.setAccessUid(customerResponse.getAccessUid());
        memberInfoEntity.setVersionNo(customerResponse.getVersionNo());
        memberInfoEntity.setAdmissionYmd(customerResponse.getAdmissionYmd());
        memberInfoEntity.setSecessionYmd(customerResponse.getSecessionYmd());
        memberInfoEntity.setMemo(customerResponse.getMemo());
        memberInfoEntity.setLastLoginTime(conversionUtility.toTimestamp(customerResponse.getLastLoginTime()));
        memberInfoEntity.setLastLoginUserAgent(customerResponse.getLastLoginUserAgent());
        memberInfoEntity.setRegistTime(conversionUtility.toTimestamp(customerResponse.getRegistTime()));
        memberInfoEntity.setUpdateTime(conversionUtility.toTimestamp(customerResponse.getUpdateTime()));

        return memberInfoEntity;

    }

    /**
     * 会員メールアドレス変更（メール送信）リクエストに変換<br/>
     *
     * @param memberMailModel メールアドレス変更 Model
     *
     * @return 会員メールアドレス変更（メール送信）リクエスト
     */
    public CustomerMailAddressUpdateSendMailRequest toCustomerMailAddressUpdateSendMailRequest(MemberMailModel memberMailModel) {

        CustomerMailAddressUpdateSendMailRequest customerMailAddressUpdateSendMailRequest =
                        new CustomerMailAddressUpdateSendMailRequest();
        if (memberMailModel != null) {
            customerMailAddressUpdateSendMailRequest.setMemberInfoNewMail(memberMailModel.getMemberInfoNewMail());
        }
        return customerMailAddressUpdateSendMailRequest;
    }
}