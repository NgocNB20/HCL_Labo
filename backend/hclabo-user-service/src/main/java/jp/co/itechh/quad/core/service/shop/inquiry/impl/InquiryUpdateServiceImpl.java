/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.shop.inquiry.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.constant.type.HTypeMailTemplateType;
import jp.co.itechh.quad.core.dto.inquiry.InquiryDetailsDto;
import jp.co.itechh.quad.core.dto.mail.MailDto;
import jp.co.itechh.quad.core.entity.inquiry.InquiryEntity;
import jp.co.itechh.quad.core.entity.memberinfo.MemberInfoEntity;
import jp.co.itechh.quad.core.entity.shop.mail.MailTemplateEntity;
import jp.co.itechh.quad.core.transformer.mailtemplate.InquiryTransformer;
import jp.co.itechh.quad.core.helper.mailtemplate.Transformer;
import jp.co.itechh.quad.core.logic.memberinfo.MemberInfoGetLogic;
import jp.co.itechh.quad.core.logic.shop.inquiry.InquiryUpdateLogic;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.mail.MailSendService;
import jp.co.itechh.quad.core.service.shop.inquiry.InquiryGetService;
import jp.co.itechh.quad.core.service.shop.inquiry.InquiryUpdateService;
import jp.co.itechh.quad.core.utility.MailUtility;
import jp.co.itechh.quad.core.utility.MemberInfoUtility;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 問い合わせ更新サービス
 *
 * @author shibuya
 * @author kawakami(itec) 2017/04/20 #3720対応
 * @version $Revision: 1.4 $
 *
 */
@Service
public class InquiryUpdateServiceImpl extends AbstractShopService implements InquiryUpdateService {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(InquiryUpdateServiceImpl.class);

    /** 問い合わせ更新ロジック */
    private final InquiryUpdateLogic inquiryUpdateLogic;

    /** メール送信サービス（同期送信） */
    private final MailSendService mailSendService;

    /** メールUtility取得 */
    private final MailUtility mailUtility;

    /** 問い合わせ情報取得サービス */
    private final InquiryGetService inquiryGetService;

    /** 会員情報取得ロジック */
    private final MemberInfoGetLogic memberInfoGetLogic;

    @Autowired
    public InquiryUpdateServiceImpl(InquiryUpdateLogic inquiryUpdateLogic,
                                    MailSendService mailSendService,
                                    MailUtility mailUtility,
                                    MemberInfoGetLogic memberInfoGetLogic,
                                    InquiryGetService inquiryGetService) {

        this.inquiryUpdateLogic = inquiryUpdateLogic;
        this.mailSendService = mailSendService;
        this.mailUtility = mailUtility;
        this.memberInfoGetLogic = memberInfoGetLogic;
        this.inquiryGetService = inquiryGetService;
    }

    /**
     * 問い合わせ回答メール送信
     * (※送信失敗時も、エラーは投げません)
     *
     * @param inquiryEntity 問い合わせエンティティ
     * @param mailTemplateEntity 加工済みメールテンプレート
     */
    protected void sendMail(InquiryEntity inquiryEntity, MailTemplateEntity mailTemplateEntity) {
        try {
            // 問い合わせ情報取得
            InquiryDetailsDto inquiryDetailsDto = inquiryGetService.execute(inquiryEntity.getInquirySeq());

            // 送信先取得
            List<String> toList = Collections.singletonList(inquiryEntity.getAnswerTo());

            // メール用値マップの作成
            Transformer transformer = ApplicationContextUtility.getBean(InquiryTransformer.class);
            Map<String, String> mailContentsMap = transformer.toValueMap(inquiryDetailsDto);

            // メールDto作成
            MailDto mailDto = mailUtility.createMailDto(HTypeMailTemplateType.EMAIL_MODIFICATION, mailTemplateEntity,
                                                        toList, mailContentsMap
                                                       );

            // メール送信
            mailSendService.execute(mailDto);

        } catch (Exception e) {
            // 仕様によりメール送信でエラーが発生しても対応なし
            LOGGER.warn("問い合わせ回答メール送信エラー：", e);
        }
    }

    /**
     * パラメータチェック
     *
     * @param inquiryEntity 問い合わせエンティティ
     */
    protected void checkParam(InquiryEntity inquiryEntity) {
        ArgumentCheckUtil.assertNotNull("inquiryEntity", inquiryEntity);
        ArgumentCheckUtil.assertGreaterThanZero("inquirySeq", inquiryEntity.getInquirySeq());
        ArgumentCheckUtil.assertGreaterThanZero("shopSeq", inquiryEntity.getShopSeq());
        ArgumentCheckUtil.assertNotEmpty("inquiryCode", inquiryEntity.getInquiryCode());
        ArgumentCheckUtil.assertNotEmpty("lastName", inquiryEntity.getInquiryLastName());
        ArgumentCheckUtil.assertNotNull("inquiryTime", inquiryEntity.getInquiryTime());
        ArgumentCheckUtil.assertNotNull("inquiryStatus", inquiryEntity.getInquiryStatus());
        ArgumentCheckUtil.assertGreaterThanZero("inquiryGroupSeq", inquiryEntity.getInquiryGroupSeq());
    }

    /**
     * 問い合わせ会員紐づけロジック
     *
     * @param inquiryEntity 問い合わせエンティティ
     * @return 処理件数
     */
    @Override
    public MemberInfoEntity executeMemberRegist(InquiryEntity inquiryEntity) {
        // パラメータチェック
        this.checkParam(inquiryEntity);

        // ユニークIDで取得する 大文字小文字の区別をなくす為
        Integer shopSeq = 1001;
        // 会員業務Helper取得
        MemberInfoUtility memberInfoUtility = ApplicationContextUtility.getBean(MemberInfoUtility.class);
        String shopUniqueId = memberInfoUtility.createShopUniqueId(shopSeq, inquiryEntity.getMemberInfoId());

        // 会員情報の取得
        MemberInfoEntity memberInfoEntity = memberInfoGetLogic.execute(shopUniqueId);
        // 会員情報が取得出来ない場合エラー
        if (ObjectUtils.isEmpty(memberInfoEntity)) {
            throwMessage(MSGCD_MEMBER_NOEXIST_ERROR);
        }

        inquiryEntity.setMemberInfoSeq(memberInfoEntity.getMemberInfoSeq());

        // 更新処理
        int result = inquiryUpdateLogic.execute(inquiryEntity);

        if (result == 0) {
            // 更新件数0件の場合、エラー
            throwMessage(MSGCD_INQUIRY_UPDATE_RETRY_FAIL);
        }

        return memberInfoEntity;
    }

    /**
     * 問い合わせ会員紐づけ解除ロジック
     *
     * @param inquiryEntity 問い合わせエンティティ
     * @return 処理件数
     */
    @Override
    public int executeMemberRegistRelease(InquiryEntity inquiryEntity) {

        // パラメータチェック
        this.checkParam(inquiryEntity);
        int result = inquiryUpdateLogic.execute(inquiryEntity);

        // 更新処理
        if (result == 0) {
            // 更新件数0件の場合、エラー
            throwMessage(MSGCD_INQUIRY_UPDATE_RETRY_FAIL);
        }
        return result;
    }

    /**
     * 問い合わせ管理メモ登録ロジック
     *
     * @param inquiryEntity 問い合わせエンティティ
     * @return 処理件数
     */
    @Override
    public int executeMemoRegist(InquiryEntity inquiryEntity) {
        // パラメータチェック
        this.checkParam(inquiryEntity);
        int result = inquiryUpdateLogic.execute(inquiryEntity);

        // 更新処理
        if (result == 0) {
            // 更新件数0件の場合、エラー
            throwMessage(MSGCD_INQUIRY_UPDATE_RETRY_FAIL);
        }
        return result;
    }

    /**
     * 問い合わせ状態登録ロジック
     *
     * @param inquiryEntity 問い合わせエンティティ
     * @return 処理件数
     */
    @Override
    public int executeStatusRegist(InquiryEntity inquiryEntity) {

        ArgumentCheckUtil.assertNotNull("inquiryStatus", inquiryEntity.getInquiryStatus());
        int result = inquiryUpdateLogic.execute(inquiryEntity);

        // 更新処理
        if (result == 0) {
            // 更新件数0件の場合、エラー
            throwMessage(MSGCD_INQUIRY_UPDATE_RETRY_FAIL);
        }
        return result;
    }

}