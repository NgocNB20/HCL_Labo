/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.memberinfo.password.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.constant.type.HTypeConfirmMailType;
import jp.co.itechh.quad.core.entity.memberinfo.MemberInfoEntity;
import jp.co.itechh.quad.core.entity.memberinfo.confirmmail.ConfirmMailEntity;
import jp.co.itechh.quad.core.logic.mailtemplate.MailTemplateGetLogic;
import jp.co.itechh.quad.core.logic.memberinfo.MemberInfoGetLogic;
import jp.co.itechh.quad.core.logic.memberinfo.confirmmail.ConfirmMailRegistLogic;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.mail.MailSendService;
import jp.co.itechh.quad.core.service.memberinfo.password.PasswordResetSendMailService;
import jp.co.itechh.quad.core.service.process.AsyncService;
import jp.co.itechh.quad.core.utility.MailUtility;
import jp.co.itechh.quad.core.utility.MemberInfoUtility;
import jp.co.itechh.quad.notificationsub.presentation.api.NotificationSubApi;
import jp.co.itechh.quad.notificationsub.presentation.api.param.PasswordNotificationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

/**
 * パスワード再設定メール送信サービス<br/>
 *
 * @author natume
 * @author Kaneko (itec) 2012/08/09 UtilからHelperへ変更　Util使用箇所をgetComponentで取得するように変更
 *
 */
@Service
public class PasswordResetSendMailServiceImpl extends AbstractShopService implements PasswordResetSendMailService {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(PasswordResetSendMailServiceImpl.class);

    /**
     * 会員情報取得ロジック<br/>
     */
    private final MemberInfoGetLogic memberInfoGetLogic;

    /**
     * 確認メール登録ロジック<br/>
     */
    private final ConfirmMailRegistLogic confirmMailRegistLogic;

    /**
     * メールテンプレート取得ロジック<br/>
     */
    private final MailTemplateGetLogic mailTemplateGetLogic;

    /**
     * メール送信サービス（同期送信）
     */
    private final MailSendService mailSendService;

    /** 通知サブドメインAPI */
    private final NotificationSubApi notificationSubApi;

    /** 非同期処理サービス */
    private final AsyncService asyncService;

    /**
     * メールUtility取得
     * */
    private final MailUtility mailUtility;

    @Autowired
    public PasswordResetSendMailServiceImpl(MemberInfoGetLogic memberInfoGetLogic,
                                            ConfirmMailRegistLogic confirmMailRegistLogic,
                                            MailTemplateGetLogic mailTemplateGetLogic,
                                            MailSendService mailSendService,
                                            NotificationSubApi notificationSubApi,
                                            AsyncService asyncService,
                                            MailUtility mailUtility) {
        this.memberInfoGetLogic = memberInfoGetLogic;
        this.confirmMailRegistLogic = confirmMailRegistLogic;
        this.mailTemplateGetLogic = mailTemplateGetLogic;
        this.mailSendService = mailSendService;
        this.notificationSubApi = notificationSubApi;
        this.asyncService = asyncService;
        this.mailUtility = mailUtility;
    }

    /**
     * パスワード再設定メール送信処理<br/>
     *
     * @param mail メールアドレス
     * @param memberInfoBirthDay 生年月日
     * @return メール送信結果
     */
    @Override
    public boolean execute(String mail, Timestamp memberInfoBirthDay) {

        // パラメータチェック
        Integer shopSeq = 1001;
        ArgumentCheckUtil.assertGreaterThanZero("commonInfo.shopSeq", shopSeq);
        ArgumentCheckUtil.assertNotEmpty("mail", mail);
        ArgumentCheckUtil.assertNotNull("memberInfoBirthDay", memberInfoBirthDay);

        // 会員情報取得
        MemberInfoEntity memberInfoEntity = getMemberInfoEntity(shopSeq, mail, memberInfoBirthDay);

        // 確認メール情報登録
        ConfirmMailEntity confirmMailEntity = registConfirmMail(memberInfoEntity);

        // メール送信
        PasswordNotificationRequest passwordNotificationRequest = new PasswordNotificationRequest();
        passwordNotificationRequest.setConfirmMailSeq(confirmMailEntity.getConfirmMailSeq());

        Object[] args = new Object[] {passwordNotificationRequest};
        Class<?>[] argsClass = new Class<?>[] {PasswordNotificationRequest.class};

        try {
            asyncService.asyncService(notificationSubApi, "passwordNotification", args, argsClass);
        } catch (Exception e) {
            LOGGER.error("例外処理が発生しました", e);
            return false;
        }
        return true;
    }

    /**
     * 会員情報取得<br/>
     *
     * @param shopSeq ショップSEQ
     * @param mail メールアドレス
     * @param memberInfoBirthDay 会員生年月日
     * @return 会員エンティティ
     */
    protected MemberInfoEntity getMemberInfoEntity(Integer shopSeq, String mail, Timestamp memberInfoBirthDay) {

        // ユニークIDで取得する 大文字小文字の区別をなくす為
        // 会員業務Helper取得
        MemberInfoUtility memberInfoUtility = ApplicationContextUtility.getBean(MemberInfoUtility.class);
        String shopUniqueId = memberInfoUtility.createShopUniqueId(shopSeq, mail);
        MemberInfoEntity memberInfoEntity = memberInfoGetLogic.execute(shopUniqueId);
        if (memberInfoEntity == null) {
            throwMessage(MSGCD_MEMBERINFOENTITYDTO_NULL);
        }

        // 生年月日不一致エラー メッセージを分けたい時用
        if (memberInfoEntity != null && memberInfoEntity.getMemberInfoBirthday() != null
            && !memberInfoEntity.getMemberInfoBirthday().equals(memberInfoBirthDay)) {
            throwMessage(MSGCD_MEMBERINFOBIRTHDAY_FAIL);
        }
        return memberInfoEntity;
    }

    /**
     * 確認メール情報登録<br/>
     *
     * @param memberInfoEntity 会員エンティティ
     * @return 確認メールエンティティ
     */
    protected ConfirmMailEntity registConfirmMail(MemberInfoEntity memberInfoEntity) {

        // 確認メール情報作成
        ConfirmMailEntity confirmMailEntity = ApplicationContextUtility.getBean(ConfirmMailEntity.class);
        confirmMailEntity.setShopSeq(memberInfoEntity.getShopSeq());
        confirmMailEntity.setMemberInfoSeq(memberInfoEntity.getMemberInfoSeq());
        confirmMailEntity.setConfirmMail(memberInfoEntity.getMemberInfoMail());
        confirmMailEntity.setConfirmMailType(HTypeConfirmMailType.PASSWORD_REISSUE);

        // 確認メール情報登録
        int result = confirmMailRegistLogic.execute(confirmMailEntity);
        if (result == 0) {
            throwMessage(MSGCD_CONFIRMMAIL_REGIST_FAIL);
        }
        return confirmMailEntity;
    }

}