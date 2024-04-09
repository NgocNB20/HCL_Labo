/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.shop.mailmagazine.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.constant.type.HTypeSendStatus;
import jp.co.itechh.quad.core.entity.mailmagazine.MailMagazineMemberEntity;
import jp.co.itechh.quad.core.logic.shop.mailmagazine.MailMagazineMemberGetLogic;
import jp.co.itechh.quad.core.logic.shop.mailmagazine.MailMagazineMemberUpdateLogic;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.shop.mailmagazine.MailMagazineMemberRemoveService;
import jp.co.itechh.quad.core.utility.MemberInfoUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * メルマガ解除サービス<br/>
 *
 * @author kimura
 */
@Service
public class MailMagazineMemberRemoveServiceImpl extends AbstractShopService
                implements MailMagazineMemberRemoveService {

    /** メルマガ購読者情報取得ロジック */
    private MailMagazineMemberGetLogic mailMagazineMemberGetLogic;

    /** メルマガ購読者登録ロジック */
    private MailMagazineMemberUpdateLogic mailMagazineMemberUpdateLogic;

    /**
     * コンストラクタ
     *
     * @param mailMagazineMemberGetLogic
     * @param mailMagazineMemberUpdateLogic
     */
    @Autowired
    public MailMagazineMemberRemoveServiceImpl(MailMagazineMemberGetLogic mailMagazineMemberGetLogic,
                                               MailMagazineMemberUpdateLogic mailMagazineMemberUpdateLogic) {
        this.mailMagazineMemberGetLogic = mailMagazineMemberGetLogic;
        this.mailMagazineMemberUpdateLogic = mailMagazineMemberUpdateLogic;

    }

    /**
     * サービス実行<br/>
     *
     * @param mail メールアドレス
     * @param memberInfoSeq 会員SEQ
     * @return 処理結果
     */
    @Override
    public boolean execute(String mail, Integer memberInfoSeq) {

        // 引数チェック
        ArgumentCheckUtil.assertNotEmpty("mail", mail);
        ArgumentCheckUtil.assertNotNull("memberInfoSeq", memberInfoSeq);

        // メルマガ情報取得
        MailMagazineMemberEntity mailMagazineMemberEntity = mailMagazineMemberGetLogic.execute(memberInfoSeq);

        Integer shopSeq = 1001;

        // メルマガ登録がある場合
        if (mailMagazineMemberEntity != null && mailMagazineMemberEntity.getSendStatus() != HTypeSendStatus.REMOVE) {

            // 会員業務Helper取得
            MemberInfoUtility memberInfoUtility = ApplicationContextUtility.getBean(MemberInfoUtility.class);

            // ユニークメールを更新（会員メールアドレス変更機能でのメール更新に対応）
            mailMagazineMemberEntity.setUniqueMail(memberInfoUtility.createShopUniqueId(shopSeq, mail));

            // メールアドレスを更新（会員メールアドレス変更機能でのメール更新に対応）
            mailMagazineMemberEntity.setMail(mail);

            // 配信状態「解除」
            mailMagazineMemberEntity.setSendStatus(HTypeSendStatus.REMOVE);

            // メルマガ購読者更新
            int result = mailMagazineMemberUpdateLogic.execute(mailMagazineMemberEntity);
            if (result == 0) {
                throwMessage(MSGCD_MAILMAGAZINEMEMBER_UPDATE_FAIL);
            }
            return true;
        }
        return false;
    }
}