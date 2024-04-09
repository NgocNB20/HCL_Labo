/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.pc.web.front.reset;

import jp.co.itechh.quad.front.base.utility.ConversionUtility;
import jp.co.itechh.quad.front.constant.type.HTypeMemberInfoStatus;
import jp.co.itechh.quad.front.constant.type.HTypeSexUnnecessaryAnswer;
import jp.co.itechh.quad.front.entity.memberinfo.MemberInfoEntity;
import jp.co.itechh.quad.front.util.common.EnumTypeUtil;
import jp.co.itechh.quad.mailcertification.presentation.api.param.CustomerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * パスワードリセット Helperクラス<br/>
 *
 * @author kn23834
 * @version $Revision: 1.0 $
 *
 */
@Component
public class ResetPwregistHelper {

    /** 変換Helper */
    private final ConversionUtility conversionUtility;

    /**
     * コンストラクタ
     *
     * @param conversionUtility
     */
    @Autowired
    public ResetPwregistHelper(ConversionUtility conversionUtility) {
        this.conversionUtility = conversionUtility;
    }

    /**
     * 取得した会員情報をセット<br/>
     *
     * @param customerResponse 会員レスポンス
     * @param resetPwregistModel パスワードリセット Model
     */
    public void toPageForLoad(CustomerResponse customerResponse, ResetPwregistModel resetPwregistModel) {
        if (customerResponse != null) {
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
            memberInfoEntity.setMemberInfoBirthday(
                            conversionUtility.toTimestamp(customerResponse.getMemberInfoBirthday()));
            memberInfoEntity.setMemberInfoTel(customerResponse.getMemberInfoTel());
            memberInfoEntity.setMemberInfoMail(customerResponse.getMemberInfoMail());
            memberInfoEntity.setMemberInfoAddressId(customerResponse.getMemberInfoAddressId());
            memberInfoEntity.setShopSeq(1001);
            memberInfoEntity.setAccessUid(customerResponse.getAccessUid());
            memberInfoEntity.setVersionNo(customerResponse.getVersionNo());
            memberInfoEntity.setAdmissionYmd(customerResponse.getAdmissionYmd());
            memberInfoEntity.setSecessionYmd(customerResponse.getSecessionYmd());
            memberInfoEntity.setMemo(customerResponse.getMemo());
            memberInfoEntity.setLastLoginTime(conversionUtility.toTimestamp(customerResponse.getLastLoginTime()));
            memberInfoEntity.setLastLoginUserAgent(customerResponse.getLastLoginUserAgent());
            memberInfoEntity.setRegistTime(conversionUtility.toTimestamp(customerResponse.getRegistTime()));
            memberInfoEntity.setUpdateTime(conversionUtility.toTimestamp(customerResponse.getUpdateTime()));

            // 会員情報セット
            resetPwregistModel.setMemberInfoEntity(memberInfoEntity);
        }
    }

}