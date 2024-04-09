/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.memberinfo.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.constant.type.HTypeMailTemplateType;
import jp.co.itechh.quad.core.constant.type.HTypeMemberInfoStatus;
import jp.co.itechh.quad.core.entity.memberinfo.MemberInfoEntity;
import jp.co.itechh.quad.core.logic.memberinfo.MemberInfoGetLogic;
import jp.co.itechh.quad.core.logic.memberinfo.MemberInfoUpdateLogic;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.card.CardAdapter;
import jp.co.itechh.quad.core.service.memberinfo.MemberInfoRemoveService;
import jp.co.itechh.quad.core.service.process.AsyncService;
import jp.co.itechh.quad.core.service.shop.mailmagazine.MailMagazineMemberRemoveService;
import jp.co.itechh.quad.core.utility.MemberInfoUtility;
import jp.co.itechh.quad.notificationsub.presentation.api.NotificationSubApi;
import jp.co.itechh.quad.notificationsub.presentation.api.param.MailMagazineProcessCompleteRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.sql.Timestamp;

/**
 * 会員退会更新サービス実装<br/>
 *
 * @author natume
 * @author Kaneko (itec) 2012/08/09 UtilからHelperへ変更　Util使用箇所をgetComponentで取得するように変更
 */
@Service
public class MemberInfoRemoveServiceImpl extends AbstractShopService implements MemberInfoRemoveService {

    /**
     * 会員情報取得
     */
    private final MemberInfoGetLogic memberInfoGetLogic;

    /**
     * 会員情報更新
     */
    private final MemberInfoUpdateLogic memberInfoUpdateLogic;

    /** メルマガ解除サービス */
    private final MailMagazineMemberRemoveService mailMagazineMemberRemoveService;

    /** 非同期処理サービス */
    private final AsyncService asyncService;

    /** 日付Utility */
    private final DateUtility dateUtility;

    /** 会員Utility */
    private final MemberInfoUtility memberInfoUtility;

    /** 通知サブドメインAPI */
    private final NotificationSubApi notificationSubApi;
    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(MemberInfoRemoveServiceImpl.class);

    /** 通知サブドメインAPI */
    private final CardAdapter cardAdapter;

    /** コンストラクタ */
    @Autowired
    public MemberInfoRemoveServiceImpl(MemberInfoGetLogic memberInfoGetLogic,
                                       MemberInfoUpdateLogic memberInfoUpdateLogic,
                                       MailMagazineMemberRemoveService mailMagazineMemberRemoveService,
                                       AsyncService asyncService,
                                       DateUtility dateUtility,
                                       MemberInfoUtility memberInfoUtility,
                                       NotificationSubApi notificationSubApi,
                                       CardAdapter cardAdapter) {
        this.memberInfoGetLogic = memberInfoGetLogic;
        this.memberInfoUpdateLogic = memberInfoUpdateLogic;
        this.mailMagazineMemberRemoveService = mailMagazineMemberRemoveService;
        this.asyncService = asyncService;
        this.dateUtility = dateUtility;
        this.memberInfoUtility = memberInfoUtility;
        this.notificationSubApi = notificationSubApi;
        this.cardAdapter = cardAdapter;
    }

    /**
     * 会員退会処理<br/>
     *
     * @param accessUid 端末識別情報
     * @param memberInfoSeq 会員SEQ
     * @param memberInfoId 会員ID
     * @param memberInfoPassWord 会員パスワード
     */
    @Override
    public void execute(String accessUid, Integer memberInfoSeq, String memberInfoId, String memberInfoPassWord) {
        ArgumentCheckUtil.assertNotEmpty("memberInfoId", memberInfoId);
        ArgumentCheckUtil.assertNotEmpty("memberInfoPassWord", memberInfoPassWord);

        Integer shopSeq = 1001;
        ArgumentCheckUtil.assertGreaterThanZero("shopSeq", shopSeq);

        ArgumentCheckUtil.assertGreaterThanZero("memberInfoSeq", memberInfoSeq);

        // 会員情報取得
        MemberInfoEntity memberInfoEntity = memberInfoGetLogic.execute(memberInfoSeq);
        if (ObjectUtils.isEmpty(memberInfoEntity)) {
            throwMessage(MSGCD_NOT_EXSIT);
        }

        // メールアドレスの照合処理
        String shopUniqueId = memberInfoUtility.createShopUniqueId(shopSeq, memberInfoId);
        if (shopUniqueId != null && !shopUniqueId.equals(memberInfoEntity.getMemberInfoUniqueId())) {
            throwMessage(MSGCD_ID_FAIL);
        }

        // パスワードの照合処理
        // SpringSecurity準拠の方式で暗号化されたパスワードと入力パスワードをBCryptによる標準コンペアで比較
        if (!BCrypt.checkpw(memberInfoPassWord, memberInfoEntity.getMemberInfoPassword())) {
            throwMessage(MSGCD_PASSWORD_FAIL);
        }

        Timestamp currentTime = dateUtility.getCurrentTime();

        // 会員退会更新
        int result = removeMemberInfo(memberInfoEntity, currentTime);
        if (result == 0) {
            throwMessage(MSGCD_UPDATE_FAIL);
        }

        // メルマガ解除
        boolean removeFlg = mailMagazineMemberRemoveService.execute(memberInfoEntity.getMemberInfoMail(),
                                                                    memberInfoEntity.getMemberInfoSeq()
                                                                   );

        // 登録済みクレジットカード情報削除
        cardAdapter.deleteCardAdapter();

        try {
            // 解除した場合
            if (removeFlg) {

                MailMagazineProcessCompleteRequest mailMagazineProcessCompleteRequest =
                                new MailMagazineProcessCompleteRequest();
                mailMagazineProcessCompleteRequest.setMemberInfoSeq(memberInfoEntity.getMemberInfoSeq());
                mailMagazineProcessCompleteRequest.setMail(memberInfoEntity.getMemberInfoMail());
                mailMagazineProcessCompleteRequest.setMailTemplateType(
                                HTypeMailTemplateType.MAILMAGAZINE_UNREGISTRATION.getValue());

                Object[] args = new Object[] {mailMagazineProcessCompleteRequest};
                Class<?>[] argsClass = new Class<?>[] {MailMagazineProcessCompleteRequest.class};

                asyncService.asyncService(notificationSubApi, "mailMagazineProcessComplete", args, argsClass);

            }
        } catch (Exception e) {
            LOGGER.warn("メルマガ解除時のメール送信失敗", e);
        }

    }

    /**
     * 会員退会処理
     *
     * @param memberInfoEntity 会員エンティティ
     * @param currentTime 現在日時
     * @return 更新件数
     */
    protected int removeMemberInfo(MemberInfoEntity memberInfoEntity, Timestamp currentTime) {
        // 会員状態 = 退会
        memberInfoEntity.setMemberInfoStatus(HTypeMemberInfoStatus.REMOVE);
        // 会員一意制約用ID = null
        memberInfoEntity.setMemberInfoUniqueId(null);
        // 退会日 = 現在日付
        memberInfoEntity.setSecessionYmd(dateUtility.getCurrentYmd());

        return memberInfoUpdateLogic.execute(memberInfoEntity, currentTime);
    }

}