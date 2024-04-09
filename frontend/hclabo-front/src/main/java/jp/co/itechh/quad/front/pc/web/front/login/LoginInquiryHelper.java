/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.front.pc.web.front.login;

import jp.co.itechh.quad.front.base.utility.ConversionUtility;
import jp.co.itechh.quad.front.constant.type.HTypeInquiryStatus;
import jp.co.itechh.quad.front.constant.type.HTypeInquiryType;
import jp.co.itechh.quad.front.entity.inquiry.InquiryEntity;
import jp.co.itechh.quad.front.util.common.EnumTypeUtil;
import jp.co.itechh.quad.inquiry.presentation.api.param.InquiryGetRequest;
import jp.co.itechh.quad.inquiry.presentation.api.param.InquiryResponse;
import jp.co.itechh.quad.inquiry.presentation.api.param.InquirySubResponse;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

/**
 * 問合せ認証 Helper
 *
 * @author Pham Quang Dieu (VJP)
 */
@Component
public class LoginInquiryHelper {

    /**
     * 変換Utility取得
     */
    private final ConversionUtility conversionUtility;

    /**
     * コンストラクタ
     *
     * @param conversionUtility 変換Utility取得
     */
    public LoginInquiryHelper(ConversionUtility conversionUtility) {
        this.conversionUtility = conversionUtility;
    }

    /**
     * 問い合わせ情報取得リクエストに変換
     *
     * @param inquiryTel 電話番号
     * @return 問い合わせ情報取得リクエスト
     */
    public InquiryGetRequest toInquiryGetRequest(String inquiryTel) {
        InquiryGetRequest inquiryGetRequest = new InquiryGetRequest();

        inquiryGetRequest.setInquiryTel(inquiryTel);

        return inquiryGetRequest;
    }

    /**
     * 問い合わせクラスに変換
     *
     * @param inquiryResponse 問い合わせ情報レスポンス
     * @return 問い合わせクラス
     */
    public InquiryEntity toInquiryEntity(InquiryResponse inquiryResponse) {
        // 処理前は存在しないためnullを返す
        if (ObjectUtils.isEmpty(inquiryResponse) || ObjectUtils.isEmpty(inquiryResponse.getInquirySubResponse())) {
            return null;
        }

        InquiryEntity inquiryEntity = new InquiryEntity();

        InquirySubResponse inquirySubResponse = inquiryResponse.getInquirySubResponse();

        inquiryEntity.setInquirySeq(inquirySubResponse.getInquirySeq());
        inquiryEntity.setInquiryCode(inquirySubResponse.getInquiryCode());
        inquiryEntity.setInquiryLastName(inquirySubResponse.getInquiryLastName());
        inquiryEntity.setInquiryFirstName(inquirySubResponse.getInquiryFirstName());
        inquiryEntity.setInquiryMail(inquirySubResponse.getInquiryMail());
        inquiryEntity.setInquiryTitle(inquirySubResponse.getInquiryTitle());
        inquiryEntity.setInquiryBody(inquirySubResponse.getInquiryBody());
        inquiryEntity.setInquiryTime(conversionUtility.toTimestamp(inquirySubResponse.getInquiryTime()));
        if (inquirySubResponse.getInquiryStatus() != null) {
            inquiryEntity.setInquiryStatus(EnumTypeUtil.getEnumFromValue(HTypeInquiryStatus.class,
                                                                         inquirySubResponse.getInquiryStatus()
                                                                        ));
        }
        inquiryEntity.setAnswerTime(conversionUtility.toTimestamp(inquirySubResponse.getAnswerTime()));
        inquiryEntity.setAnswerTitle(inquirySubResponse.getAnswerTitle());
        inquiryEntity.setAnswerBody(inquirySubResponse.getAnswerBody());
        inquiryEntity.setAnswerFrom(inquirySubResponse.getAnswerFrom());
        inquiryEntity.setAnswerTo(inquirySubResponse.getAnswerTo());
        inquiryEntity.setAnswerBcc(inquirySubResponse.getAnswerBcc());
        inquiryEntity.setInquiryGroupSeq(inquirySubResponse.getInquiryGroupSeq());
        inquiryEntity.setInquiryLastKana(inquirySubResponse.getInquiryLastKana());
        inquiryEntity.setInquiryFirstKana(inquirySubResponse.getInquiryFirstKana());
        inquiryEntity.setInquiryZipCode(inquirySubResponse.getInquiryZipCode());
        inquiryEntity.setInquiryPrefecture(inquirySubResponse.getInquiryPrefecture());
        inquiryEntity.setInquiryAddress1(inquirySubResponse.getInquiryAddress1());
        inquiryEntity.setInquiryAddress2(inquirySubResponse.getInquiryAddress2());
        inquiryEntity.setInquiryAddress3(inquirySubResponse.getInquiryAddress3());
        inquiryEntity.setInquiryTel(inquirySubResponse.getInquiryStatus());
        inquiryEntity.setInquiryMobileTel(inquirySubResponse.getInquiryMobileTel());
        inquiryEntity.setProcessPersonName(inquirySubResponse.getProcessPersonName());
        inquiryEntity.setVersionNo(inquirySubResponse.getVersionNo());
        inquiryEntity.setRegistTime(conversionUtility.toTimestamp(inquirySubResponse.getRegistTime()));
        inquiryEntity.setUpdateTime(conversionUtility.toTimestamp(inquirySubResponse.getUpdateTime()));
        if (inquirySubResponse.getInquiryType() != null) {
            inquiryEntity.setInquiryType(
                            EnumTypeUtil.getEnumFromValue(HTypeInquiryType.class, inquirySubResponse.getInquiryType()));
        }
        inquiryEntity.setMemberInfoSeq(inquirySubResponse.getMemberInfoSeq());
        inquiryEntity.setOrderCode(inquirySubResponse.getOrderCode());
        inquiryEntity.setFirstInquiryTime(conversionUtility.toTimestamp(inquirySubResponse.getFirstInquiryTime()));
        inquiryEntity.setLastUserInquiryTime(
                        conversionUtility.toTimestamp(inquirySubResponse.getLastUserInquiryTime()));
        inquiryEntity.setLastOperatorInquiryTime(
                        conversionUtility.toTimestamp(inquirySubResponse.getLastOperatorInquiryTime()));
        inquiryEntity.setLastRepresentative(inquirySubResponse.getLastRepresentative());
        inquiryEntity.setMemo(inquirySubResponse.getMemo());
        inquiryEntity.setCooperationMemo(inquirySubResponse.getCooperationMemo());
        inquiryEntity.setMemberInfoId(inquirySubResponse.getMemberInfoId());

        return inquiryEntity;
    }
}