/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.shop.mailmagazine.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.base.util.common.PropertiesUtil;
import jp.co.itechh.quad.core.constant.type.HTypeMailTemplateType;
import jp.co.itechh.quad.core.dto.mail.MailDto;
import jp.co.itechh.quad.core.entity.mailmagazine.MailMagazineMemberEntity;
import jp.co.itechh.quad.core.entity.shop.mail.MailTemplateEntity;
import jp.co.itechh.quad.core.logic.mailtemplate.MailTemplateGetLogic;
import jp.co.itechh.quad.core.logic.shop.mailmagazine.MailMagazineMemberGetLogic;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.mail.MailSendService;
import jp.co.itechh.quad.core.service.shop.mailmagazine.MailMagazineProcessCompleteSendMailService;
import jp.co.itechh.quad.core.utility.MailUtility;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * メルマガ登録/解除完了メール送信サービス<br/>
 *
 * @author kimura
 */
@Service
public class MailMagazineProcessCompleteSendMailServiceImpl extends AbstractShopService
                implements MailMagazineProcessCompleteSendMailService {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(MailMagazineProcessCompleteSendMailServiceImpl.class);

    /** 送信アドレス（設定値） */
    protected static final String MAIL_FROM_ADDRESS = "mail.from.address";

    /**  メールマガジン購読者取得 */
    private MailMagazineMemberGetLogic mailMagazineMemberGetLogic;

    /** メール送信ロジック */
    private MailSendService mailSendService;

    /** メールUtility取得 */
    private final MailUtility mailUtility;

    /** メールテンプレート取得ロジック */
    private final MailTemplateGetLogic mailTemplateGetLogic;

    /**
     * コンストラクタ
     *
     * @param mailMagazineMemberGetLogic
     * @param mailSendService
     * @param mailTemplateGetLogic
     * @param mailUtility
     */
    @Autowired
    public MailMagazineProcessCompleteSendMailServiceImpl(MailMagazineMemberGetLogic mailMagazineMemberGetLogic,
                                                          MailSendService mailSendService,
                                                          MailTemplateGetLogic mailTemplateGetLogic,
                                                          MailUtility mailUtility) {
        this.mailMagazineMemberGetLogic = mailMagazineMemberGetLogic;
        this.mailSendService = mailSendService;
        this.mailTemplateGetLogic = mailTemplateGetLogic;
        this.mailUtility = mailUtility;
    }

    /**
     * メルマガ登録/解除完了メール送信処理<br/>
     * ※メアド変更前の購読者が、変更後に購読希望しない場合の解除メールの本文にのみ、「変更前メールアドレス」を利用する
     *
     * @param mail メールアドレス
     * @param preMail 変更前メールアドレス
     * @param memberInfoSeq 会員SEQ
     * @param mailTemplateType メールテンプレートタイプ(メルマガ登録完了/メルマガ解除完了)
     * @return boolean 送信結果
     */
    @Override
    public boolean execute(String mail, String preMail, Integer memberInfoSeq, HTypeMailTemplateType mailTemplateType) {

        // パラメータチェック
        ArgumentCheckUtil.assertNotEmpty("mail", mail);
        ArgumentCheckUtil.assertNotNull("mailTemplateType", mailTemplateType);

        // メルマガ購読者取得
        MailMagazineMemberEntity mailMagazineMemberEntity = mailMagazineMemberGetLogic.execute(memberInfoSeq);
        if (ObjectUtils.isEmpty(mailMagazineMemberEntity)) {
            throwMessage(MSGCD_MAILMAGAZINEMEMBER_NULL);
        }

        // 送信に使用するメールテンプレートを取得する
        MailTemplateEntity entity =
                        mailTemplateGetLogic.execute(mailMagazineMemberEntity.getShopSeq(), mailTemplateType);

        // テンプレートがない場合
        if (entity == null) {
            return false;
        }

        // 送信先取得
        List<String> toList = Collections.singletonList(mail);

        // メール送信
        MailDto mailDto = mailUtility.createMailDto(mailTemplateType, entity, toList, null);

        // メール作成
        createMail(mailDto, mail, preMail);

        // メール送信
        try {
            mailSendService.execute(mailDto);
        } catch (Exception e) {
            LOGGER.error("例外処理が発生しました", e);
            return false;
        }

        return true;
    }

    /**
     * 送信メール作成
     *
     * @param mailDto
     * @param mail
     * @param preMail
     */
    private void createMail(MailDto mailDto, String mail, String preMail) {

        // 送信者メールアドレス作成
        String from = PropertiesUtil.getSystemPropertiesValue(MAIL_FROM_ADDRESS);

        // プレースホルダ作成
        Map<String, Object> ph = new HashMap<>();

        if (StringUtils.isEmpty(preMail)) {
            // 対象メールアドレスをセット
            ph.put("MA_MAIL", mail);
        } else {
            // 変更前のメールアドレスを本文内容セット（解除送信用）
            ph.put("MA_MAIL", preMail);
        }

        mailDto.initializeMailDto(mailDto.getMailTemplateType(), mailDto.getSubject(), from, mailDto.getToList(), null,
                                  null, ph, null
                                 );
    }
}