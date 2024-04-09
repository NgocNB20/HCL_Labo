/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.pc.web.front.member.pass;

import jp.co.itechh.quad.customer.presentation.api.param.CustomerPasswordUpdateRequest;
import jp.co.itechh.quad.customer.presentation.api.param.CustomerResponse;
import jp.co.itechh.quad.front.base.utility.ConversionUtility;
import jp.co.itechh.quad.front.constant.type.HTypeMemberInfoStatus;
import jp.co.itechh.quad.front.constant.type.HTypeSexUnnecessaryAnswer;
import jp.co.itechh.quad.front.entity.memberinfo.MemberInfoEntity;
import jp.co.itechh.quad.front.util.common.EnumTypeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

/**
 * パスワード変更 Helper
 *
 * @author PHAM QUANG DIEU (VJP)
 * @version $Revision: 1.0 $
 */
@Component
public class MemberPassHelper {

    /** Utility */
    private final ConversionUtility conversionUtility;

    /**
     * コンストラクタ
     *
     * @param conversionUtility Utility
     */
    @Autowired
    public MemberPassHelper(ConversionUtility conversionUtility) {
        this.conversionUtility = conversionUtility;
    }

    /**
     * 会員クラスに変換
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
        memberInfoEntity.setShopSeq(customerResponse.getMemberInfoSeq());
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
     * 会員パスワード更新リクエストに変換
     *
     * @param memberPassModel パスワード変更 Model
     * @return 会員パスワード更新リクエスト
     */
    public CustomerPasswordUpdateRequest toCustomerPasswordUpdateRequest(MemberPassModel memberPassModel) {
        // 処理前は存在しないためnullを返す
        if (memberPassModel == null) {
            return null;
        }

        CustomerPasswordUpdateRequest customerPasswordUpdateRequest = new CustomerPasswordUpdateRequest();

        customerPasswordUpdateRequest.setMemberInfoPassword(memberPassModel.getMemberInfoPassWord());
        customerPasswordUpdateRequest.setMemberInfoNewPassWord(memberPassModel.getMemberInfoNewPassWord());

        return customerPasswordUpdateRequest;
    }

}