/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.pc.web.front.mail;

import jp.co.itechh.quad.customer.presentation.api.param.CustomerMailAddressUpdateRequest;
import jp.co.itechh.quad.front.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.front.base.utility.ConversionUtility;
import jp.co.itechh.quad.front.constant.type.HTypeMemberInfoStatus;
import jp.co.itechh.quad.front.constant.type.HTypeSexUnnecessaryAnswer;
import jp.co.itechh.quad.front.entity.memberinfo.MemberInfoEntity;
import jp.co.itechh.quad.front.util.common.EnumTypeUtil;
import jp.co.itechh.quad.mailcertification.presentation.api.param.CustomerResponse;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.InvocationTargetException;

/**
 * メールアドレス変更HELPER
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 *
 */
@Component
public class MailHelper {

    /** 変換ユーティリティクラス */
    private final ConversionUtility conversionUtility;

    /**
     * コンストラクタ
     *
     * @param conversionUtility 変換ユーティリティクラス
     */
    public MailHelper(ConversionUtility conversionUtility) {
        this.conversionUtility = conversionUtility;
    }

    /**
     * 初期表示<br/>
     *
     * @param memberInfoEntity 会員エンティティ
     * @param mailModel        メールアドレス変更 Model
     */
    public void toPageForLoad(MemberInfoEntity memberInfoEntity, MailModel mailModel) {

        // 会員情報
        mailModel.setMemberInfoEntity(memberInfoEntity);

        if (!ObjectUtils.isEmpty(memberInfoEntity)) {
            // メールアドレス
            mailModel.setMemberInfoMail(memberInfoEntity.getMemberInfoMail());
        }
    }

    /**
     * 変更会員情報の作成<br/>
     *
     * @param mailModel メール変更Model
     * @return 会員エンティティ
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public MemberInfoEntity toMemberInfoEntityForMailUpdate(MailModel mailModel)
                    throws IllegalAccessException, InvocationTargetException {

        // 会員情報
        MemberInfoEntity memberInfoEntity = ApplicationContextUtility.getBean(MemberInfoEntity.class);

        BeanUtils.copyProperties(memberInfoEntity, mailModel.getMemberInfoEntity());

        return memberInfoEntity;

    }

    /**
     * 会員クラスに変換<br/>
     *
     * @param customerResponse 会員レスポンス
     *
     * @return 会員エンティティ
     */
    public MemberInfoEntity toMemberInfoEntityForLoad(CustomerResponse customerResponse) {
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
     * 会員クラスに変換<br/>
     *
     * @param customerResponse 会員レスポンス
     *
     * @return 会員エンティティ
     */
    public MemberInfoEntity toMemberInfoEntity(jp.co.itechh.quad.customer.presentation.api.param.CustomerResponse customerResponse) {
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
     * 会員メールアドレス更新リクエストに変換<br/>
     *
     * @param mailModel メールアドレス変更 Model
     *
     * @return 会員メールアドレス更新リクエスト
     */
    public CustomerMailAddressUpdateRequest toCustomerMailAddressUpdateRequest(MailModel mailModel) {
        // 処理前は存在しないためnullを返す
        if (mailModel == null) {
            return null;
        }

        CustomerMailAddressUpdateRequest customerMailAddressUpdateRequest = new CustomerMailAddressUpdateRequest();

        customerMailAddressUpdateRequest.setMailMagazine(mailModel.isMailMagazine());
        customerMailAddressUpdateRequest.setPreMailMagazine(mailModel.isPreMailMagazine());
        customerMailAddressUpdateRequest.setMid(mailModel.getMid());

        return customerMailAddressUpdateRequest;
    }

}