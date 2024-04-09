/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.shop.mailmagazine.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.entity.mailmagazine.MailMagazineMemberEntity;
import jp.co.itechh.quad.core.logic.shop.mailmagazine.MailMagazineMemberGetLogic;
import jp.co.itechh.quad.core.logic.shop.mailmagazine.MailMagazineMemberUpdateLogic;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.shop.mailmagazine.MailMagazineMemberChangeMailService;
import jp.co.itechh.quad.core.utility.MemberInfoUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * メルマガ購読者メールアドレス変更サービス<br/>
 *
 * @author kimura
 */
@Service
public class MailMagazineMemberChangeMailServiceImpl extends AbstractShopService
                implements MailMagazineMemberChangeMailService {

    /** メルマガ購読者情報取得ロジック */
    private final MailMagazineMemberGetLogic mailMagazineMemberGetLogic;

    /** メルマガ購読者情報更新ロジック */
    private final MailMagazineMemberUpdateLogic mailMagazineMemberUpdateLogic;

    /**
     * コンストラクタ
     *
     * @param mailMagazineMemberGetLogic
     * @param mailMagazineMemberUpdateLogic
     */
    @Autowired
    public MailMagazineMemberChangeMailServiceImpl(MailMagazineMemberGetLogic mailMagazineMemberGetLogic,
                                                   MailMagazineMemberUpdateLogic mailMagazineMemberUpdateLogic) {
        this.mailMagazineMemberGetLogic = mailMagazineMemberGetLogic;
        this.mailMagazineMemberUpdateLogic = mailMagazineMemberUpdateLogic;
    }

    /**
     * メルマガ購読者メールアドレス変更処理<br/>
     *
     * @param shopSeq ショップSEQ
     * @param mail メールアドレス
     * @param memberInfoSeq 会員SEQ
     * @return 処理件数
     */
    @Override
    public int execute(Integer shopSeq, String mail, Integer memberInfoSeq) {
        // 引数チェック
        ArgumentCheckUtil.assertNotEmpty("mail", mail);
        ArgumentCheckUtil.assertNotNull("memberInfoSeq", memberInfoSeq);

        // メルマガ情報購読者取得
        MailMagazineMemberEntity mailMagazineMemberEntityDB = mailMagazineMemberGetLogic.execute(memberInfoSeq);

        // メルマガ購読者情報のメールアドレスとユニークアドレスを更新
        int result = 0;
        if (mailMagazineMemberEntityDB != null) {

            // メールアドレス
            mailMagazineMemberEntityDB.setMail(mail);

            // 会員業務Helper取得
            MemberInfoUtility memberInfoUtility = ApplicationContextUtility.getBean(MemberInfoUtility.class);

            // ユニークアドレス
            mailMagazineMemberEntityDB.setUniqueMail(memberInfoUtility.createShopUniqueId(shopSeq, mail));

            // メルマガ購読者更新
            result = mailMagazineMemberUpdateLogic.execute(mailMagazineMemberEntityDB);
            if (result == 0) {
                throwMessage(MSGCD_MAILMAGAZINEMEMBER_UPDATE_FAIL);
            }
        }

        return result;
    }
}