/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.memberinfo.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.constant.type.HTypeConfirmMailType;
import jp.co.itechh.quad.core.constant.type.HTypeMemberInfoStatus;
import jp.co.itechh.quad.core.entity.memberinfo.MemberInfoEntity;
import jp.co.itechh.quad.core.entity.memberinfo.confirmmail.ConfirmMailEntity;
import jp.co.itechh.quad.core.logic.mailtemplate.MailTemplateGetLogic;
import jp.co.itechh.quad.core.logic.memberinfo.MemberInfoDataCheckLogic;
import jp.co.itechh.quad.core.logic.memberinfo.confirmmail.ConfirmMailRegistLogic;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.mail.MailSendService;
import jp.co.itechh.quad.core.service.memberinfo.MemberInfoMailUpdateSendMailService;
import jp.co.itechh.quad.core.service.process.AsyncService;
import jp.co.itechh.quad.core.utility.MailUtility;
import jp.co.itechh.quad.core.utility.MemberInfoUtility;
import jp.co.itechh.quad.notificationsub.presentation.api.NotificationSubApi;
import jp.co.itechh.quad.notificationsub.presentation.api.param.EmailModificationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 会員メールアドレス変更メール送信サービス<br/>
 *
 * @author natume
 * @author Kaneko (itec) 2012/08/09 UtilからHelperへ変更　Util使用箇所をgetComponentで取得するように変更
 *
 */
@Service
public class MemberInfoMailUpdateSendMailServiceImpl extends AbstractShopService
                implements MemberInfoMailUpdateSendMailService {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(MemberInfoMailUpdateSendMailServiceImpl.class);

    /**
     * 会員データチェックロジック<br/>
     */
    private final MemberInfoDataCheckLogic memberInfoDataCheckLogic;

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

    /**
     * メールUtility取得
     * */
    private final MailUtility mailUtility;

    /** 通知サブドメインAPI */
    private final NotificationSubApi notificationSubApi;

    /** 非同期処理サービス */
    private final AsyncService asyncService;

    @Autowired
    public MemberInfoMailUpdateSendMailServiceImpl(MemberInfoDataCheckLogic memberInfoDataCheckLogic,
                                                   ConfirmMailRegistLogic confirmMailRegistLogic,
                                                   MailTemplateGetLogic mailTemplateGetLogic,
                                                   MailSendService mailSendService,
                                                   MailUtility mailUtility,
                                                   NotificationSubApi notificationSubApi,
                                                   AsyncService asyncService) {
        this.memberInfoDataCheckLogic = memberInfoDataCheckLogic;
        this.confirmMailRegistLogic = confirmMailRegistLogic;
        this.mailTemplateGetLogic = mailTemplateGetLogic;
        this.mailSendService = mailSendService;
        this.mailUtility = mailUtility;
        this.notificationSubApi = notificationSubApi;
        this.asyncService = asyncService;
    }

    /**
     * 会員メールアドレス変更メール送信処理<br/>
     *
     * @param shopSeq ショップSEQ
     * @param memberInfoSeq 会員SEQ
     * @param mail メールアドレス
     * @return メール送信結果
     */
    @Override
    // @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean execute(Integer shopSeq, Integer memberInfoSeq, String mail) {

        // パラメータチェック
        ArgumentCheckUtil.assertNotNull("memberInfoSeq", memberInfoSeq);
        ArgumentCheckUtil.assertNotEmpty("mail", mail);

        // 会員IDの重複チェック
        checkMemberInfoId(shopSeq, memberInfoSeq, mail);

        // 確認メール情報登録
        ConfirmMailEntity confirmMailEntity = registConfirmMail(shopSeq, memberInfoSeq, mail);

        // メール送信
        EmailModificationRequest emailModificationRequest = new EmailModificationRequest();
        emailModificationRequest.setConfirmMailSeq(confirmMailEntity.getConfirmMailSeq());

        Object[] args = new Object[] {emailModificationRequest};
        Class<?>[] argsClass = new Class<?>[] {EmailModificationRequest.class};
        try {
            asyncService.asyncService(notificationSubApi, "emailModification", args, argsClass);
        } catch (Exception e) {
            LOGGER.error("例外処理が発生しました", e);
            return false;
        }
        return true;
    }

    /**
     * 会員IDの重複チェック<br/>
     *
     * @param shopSeq ショップSEQ
     * @param memberInfoSeq 会員SEQ
     * @param mail メールアドレス
     */
    protected void checkMemberInfoId(Integer shopSeq, Integer memberInfoSeq, String mail) {

        // 会員SEQ, ユニークIDをセット
        MemberInfoEntity memberInfoEntity = ApplicationContextUtility.getBean(MemberInfoEntity.class);
        memberInfoEntity.setMemberInfoSeq(memberInfoSeq);
        // 会員業務Helper取得
        MemberInfoUtility memberInfoUtility = ApplicationContextUtility.getBean(MemberInfoUtility.class);
        memberInfoEntity.setMemberInfoUniqueId(memberInfoUtility.createShopUniqueId(shopSeq, mail));

        // 会員データチェック実行
        memberInfoDataCheckLogic.execute(mail, HTypeMemberInfoStatus.ADMISSION);
    }

    /**
     * 確認メール情報登録<br/>
     *
     * @param shopSeq ショップSEQ
     * @param memberInfoSeq 会員SEQ
     * @param mail メールアドレス
     * @return 確認メールエンティティ
     */
    protected ConfirmMailEntity registConfirmMail(Integer shopSeq, Integer memberInfoSeq, String mail) {

        // 確認メール情報作成
        ConfirmMailEntity confirmMailEntity = ApplicationContextUtility.getBean(ConfirmMailEntity.class);
        confirmMailEntity.setShopSeq(shopSeq);
        confirmMailEntity.setMemberInfoSeq(memberInfoSeq);
        confirmMailEntity.setConfirmMail(mail);
        confirmMailEntity.setConfirmMailType(HTypeConfirmMailType.MEMBERINFO_MAIL_UPDATE);

        // 確認メール情報登録
        int result = confirmMailRegistLogic.execute(confirmMailEntity);
        if (result == 0) {
            throwMessage(MSGCD_CONFIRMMAILENTITYDTO_REGIST_FAIL);
        }
        return confirmMailEntity;
    }
}