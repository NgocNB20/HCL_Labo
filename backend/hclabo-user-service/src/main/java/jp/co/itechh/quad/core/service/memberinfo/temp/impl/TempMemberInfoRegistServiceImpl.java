/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.memberinfo.temp.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.constant.type.HTypeConfirmMailType;
import jp.co.itechh.quad.core.entity.memberinfo.MemberInfoEntity;
import jp.co.itechh.quad.core.entity.memberinfo.confirmmail.ConfirmMailEntity;
import jp.co.itechh.quad.core.logic.mailtemplate.MailTemplateGetLogic;
import jp.co.itechh.quad.core.logic.memberinfo.MemberInfoDataCheckLogic;
import jp.co.itechh.quad.core.logic.memberinfo.MemberInfoGetLogic;
import jp.co.itechh.quad.core.logic.memberinfo.confirmmail.ConfirmMailRegistLogic;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.mail.MailSendService;
import jp.co.itechh.quad.core.service.memberinfo.temp.TempMemberInfoRegistService;
import jp.co.itechh.quad.core.service.process.AsyncService;
import jp.co.itechh.quad.core.utility.MailUtility;
import jp.co.itechh.quad.core.utility.MemberInfoUtility;
import jp.co.itechh.quad.notificationsub.presentation.api.NotificationSubApi;
import jp.co.itechh.quad.notificationsub.presentation.api.param.MemberPreregistrationRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 仮会員登録サービス実装<br/>
 *
 * @author natume
 * @author Kaneko (itec) 2012/08/09 UtilからHelperへ変更　Util使用箇所をgetComponentで取得するように変更
 *
 */
@Service
public class TempMemberInfoRegistServiceImpl extends AbstractShopService implements TempMemberInfoRegistService {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(TempMemberInfoRegistServiceImpl.class);

    /**
     * 会員データ整合性チェックロジック<br/>
     */
    private final MemberInfoDataCheckLogic memberInfoDataCheckLogic;

    /**
     * 会員情報取得ロジック<br/>
     */
    private final MemberInfoGetLogic memberInfoGetLogic;

    /**
     * 確認メール登録<br/>
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
    public TempMemberInfoRegistServiceImpl(MemberInfoDataCheckLogic memberInfoDataCheckLogic,
                                           MemberInfoGetLogic memberInfoGetLogic,
                                           ConfirmMailRegistLogic confirmMailRegistLogic,
                                           MailTemplateGetLogic mailTemplateGetLogic,
                                           MailSendService mailSendService,
                                           MailUtility mailUtility,
                                           NotificationSubApi notificationSubApi,
                                           AsyncService asyncService) {
        this.memberInfoDataCheckLogic = memberInfoDataCheckLogic;
        this.memberInfoGetLogic = memberInfoGetLogic;
        this.confirmMailRegistLogic = confirmMailRegistLogic;
        this.mailTemplateGetLogic = mailTemplateGetLogic;
        this.mailSendService = mailSendService;
        this.mailUtility = mailUtility;
        this.notificationSubApi = notificationSubApi;
        this.asyncService = asyncService;
    }

    /**
     * 仮会員登録処理<br/>
     *
     * @param mail メールアドレス
     * @param orderSeq 受注SEQ
     * @return 登録件数
     */
    @Override
    public boolean execute(String mail, Integer orderSeq) {

        // 入力チェック
        this.checkParameter(mail);

        // 会員データチェック
        this.checkMemberInfo(mail);

        // 確認メール情報登録
        ConfirmMailEntity confirmMailEntity = this.registConfirmMail(mail, orderSeq);

        // メール送信処理
        MemberPreregistrationRequest memberPreregistrationRequest = new MemberPreregistrationRequest();
        memberPreregistrationRequest.setConfirmMailSeq(confirmMailEntity.getConfirmMailSeq());

        Object[] args = new Object[] {memberPreregistrationRequest};
        Class<?>[] argsClass = new Class<?>[] {MemberPreregistrationRequest.class};
        try {
            asyncService.asyncService(notificationSubApi, "memberPreregistration", args, argsClass);
        } catch (Exception e) {
            LOGGER.error("例外処理が発生しました", e);
            return false;
        }
        return true;
    }

    /**
     * 入力チェック<br/>
     *
     * @param mail メールアドレス
     */
    protected void checkParameter(String mail) {
        ArgumentCheckUtil.assertNotNull("mail", mail);
    }

    /**
     * 仮会員情報が登録可能かをチェックする<br/>
     *
     * @param mail メールアドレス
     */
    protected void checkMemberInfo(String mail) {

        // 仮会員情報の作成
        MemberInfoEntity memberInfoEntity = this.createMemberInfoEntity(mail);

        // 会員情報データチェックロジックを実行
        memberInfoDataCheckLogic.execute(memberInfoEntity);
    }

    /**
     * 確認メール登録処理<br/>
     *
     * @param mail メールアドレス
     * @param orderSeq 受注SEQ
     * @return 確認メールエンティティ
     */
    protected ConfirmMailEntity registConfirmMail(String mail, Integer orderSeq) {

        // 確認メール情報作成
        ConfirmMailEntity confirmMailEntity = this.createConfirmMailEntity(mail, orderSeq);

        // 確認メール情報登録
        int result = confirmMailRegistLogic.execute(confirmMailEntity);

        // 登録失敗
        if (result == 0) {
            throwMessage(MSGCD_CONFIRMMAIL_REGIST_FAIL);
        }
        return confirmMailEntity;
    }

    /**
     * 仮会員情報の作成<br/>
     *
     * @param mail メールアドレス
     * @return 会員エンティティ
     */
    protected MemberInfoEntity createMemberInfoEntity(String mail) {

        // 会員情報作成
        Integer shopSeq = 1001;
        MemberInfoEntity memberInfoEntity = ApplicationContextUtility.getBean(MemberInfoEntity.class);
        memberInfoEntity.setMemberInfoMail(mail);
        memberInfoEntity.setMemberInfoId(mail);
        memberInfoEntity.setShopSeq(shopSeq);

        // 会員業務Helper取得
        MemberInfoUtility memberInfoUtility = ApplicationContextUtility.getBean(MemberInfoUtility.class);

        // 会員ユニークIDの作成・セット
        memberInfoEntity.setMemberInfoUniqueId(
                        memberInfoUtility.createShopUniqueId(shopSeq, memberInfoEntity.getMemberInfoMail()));
        return memberInfoEntity;
    }

    /**
     * 確認メール情報を作成<br/>
     *
     * @param mail メールアドレス
     * @param orderSeq 受注SEQ
     * @return 確認メールエンティティ
     */
    protected ConfirmMailEntity createConfirmMailEntity(String mail, Integer orderSeq) {
        ConfirmMailEntity confirmMailEntity = ApplicationContextUtility.getBean(ConfirmMailEntity.class);
        Integer shopSeq = 1001;
        confirmMailEntity.setShopSeq(shopSeq);
        confirmMailEntity.setConfirmMail(mail);
        confirmMailEntity.setConfirmMailType(HTypeConfirmMailType.TEMP_MEMBERINFO_REGIST);
        confirmMailEntity.setOrderSeq(orderSeq);

        // 会員業務Helper取得
        MemberInfoUtility memberInfoUtility = ApplicationContextUtility.getBean(MemberInfoUtility.class);

        // 暫定会員を取得する
        MemberInfoEntity proMemberInfoEntity =
                        memberInfoGetLogic.executeForProvisional(memberInfoUtility.createShopUniqueId(shopSeq, mail),
                                                                 mail
                                                                );

        // 暫定会員がある場合、会員SEQをセットする
        if (proMemberInfoEntity != null) {
            confirmMailEntity.setMemberInfoSeq(proMemberInfoEntity.getMemberInfoSeq());
        }

        return confirmMailEntity;
    }

    /**
     * 仮会員登録処理<br/>
     *
     * @param mail メールアドレス
     * @param orderSeq 受注SEQ
     * @return 確認メール情報
     */
    @Override
    public ConfirmMailEntity executeMemberRegist(String mail, Integer orderSeq) {

        // 入力チェック
        this.checkParameter(mail);

        // 会員データチェック
        this.checkMemberInfo(mail);

        // 確認メール情報登録
        ConfirmMailEntity confirmMailEntity = this.registConfirmMail(mail, orderSeq);

        // メール送信処理
        MemberPreregistrationRequest memberPreregistrationRequest = new MemberPreregistrationRequest();
        memberPreregistrationRequest.setConfirmMailSeq(confirmMailEntity.getConfirmMailSeq());

        Object[] args = new Object[] {memberPreregistrationRequest};
        Class<?>[] argsClass = new Class<?>[] {MemberPreregistrationRequest.class};
        asyncService.asyncService(notificationSubApi, "memberPreregistration", args, argsClass);

        return confirmMailEntity;
    }
}