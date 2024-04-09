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
import jp.co.itechh.quad.core.logic.shop.mailmagazine.MailMagazineMemberRegistLogic;
import jp.co.itechh.quad.core.logic.shop.mailmagazine.MailMagazineMemberUpdateLogic;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.shop.mailmagazine.MailMagazineMemberRegistService;
import jp.co.itechh.quad.core.utility.MemberInfoUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * メルマガ購読者登録サービス<br/>
 *
 * @author kimura
 */
@Service
public class MailMagazineMemberRegistServiceImpl extends AbstractShopService
                implements MailMagazineMemberRegistService {

    /** メルマガ購読者情報取得ロジック */
    private MailMagazineMemberGetLogic mailMagazineMemberGetLogic;

    /**  メルマガ購読者情報登録ロジック */
    private MailMagazineMemberRegistLogic mailMagazineMemberRegistLogic;

    /** メルマガ購読者情報更新ロジック */
    private MailMagazineMemberUpdateLogic mailMagazineMemberUpdateLogic;

    /**
     * コンストラクタ
     *
     * @param mailMagazineMemberGetLogic
     * @param mailMagazineMemberRegistLogic
     * @param mailMagazineMemberUpdateLogic
     */
    @Autowired
    public MailMagazineMemberRegistServiceImpl(MailMagazineMemberGetLogic mailMagazineMemberGetLogic,
                                               MailMagazineMemberRegistLogic mailMagazineMemberRegistLogic,
                                               MailMagazineMemberUpdateLogic mailMagazineMemberUpdateLogic) {
        this.mailMagazineMemberGetLogic = mailMagazineMemberGetLogic;
        this.mailMagazineMemberRegistLogic = mailMagazineMemberRegistLogic;
        this.mailMagazineMemberUpdateLogic = mailMagazineMemberUpdateLogic;
    }

    /**
     * メルマガ購読者登録処理<br/>
     *
     * @param mail メールアドレス
     * @param memberInfoSeq 会員SEQ
     * @return 処理件数
     */
    @Override
    public int execute(String mail, Integer memberInfoSeq) {

        // 引数チェック
        ArgumentCheckUtil.assertNotEmpty("mail", mail);
        ArgumentCheckUtil.assertNotNull("memberInfoSeq", memberInfoSeq);

        // 会員業務Helper取得
        MemberInfoUtility memberInfoUtility = ApplicationContextUtility.getBean(MemberInfoUtility.class);

        // ショップSEQ
        Integer shopSeq = 1001;

        // ユニークメール作成 会員メールとの紐付けに利用
        String uniqueMail = memberInfoUtility.createShopUniqueId(shopSeq, mail);

        // メルマガ情報購読者取得
        MailMagazineMemberEntity mailMagazineMemberEntityDB = mailMagazineMemberGetLogic.execute(memberInfoSeq);

        // メルマガ購読者情報登録・更新
        int result = 0;
        if (mailMagazineMemberEntityDB == null) {

            /* メルマガ情報登録 */
            MailMagazineMemberEntity mailMagazineMemberEntity =
                            ApplicationContextUtility.getBean(MailMagazineMemberEntity.class);

            // ショップSEQ
            mailMagazineMemberEntity.setShopSeq(shopSeq);

            // 会員SEQ
            mailMagazineMemberEntity.setMemberinfoSeq(memberInfoSeq);

            // ユニークアドレス
            mailMagazineMemberEntity.setUniqueMail(uniqueMail);

            // メールアドレス
            mailMagazineMemberEntity.setMail(mail);

            // 配信状態「配信中」
            mailMagazineMemberEntity.setSendStatus(HTypeSendStatus.SENDING);

            // メルマガ購読者登録
            result = mailMagazineMemberRegistLogic.execute(mailMagazineMemberEntity);
            if (result == 0) {
                throwMessage(MSGCD_MAILMAGAZINEMEMBER_REGIST_FAIL);
            }

        } else {

            /* メルマガ情報更新 */

            // メールアドレス
            mailMagazineMemberEntityDB.setMail(mail);

            // ユニークアドレス
            mailMagazineMemberEntityDB.setUniqueMail(uniqueMail);

            // 配信中
            mailMagazineMemberEntityDB.setSendStatus(HTypeSendStatus.SENDING);

            // メルマガ購読者更新
            result = mailMagazineMemberUpdateLogic.execute(mailMagazineMemberEntityDB);
            if (result == 0) {
                throwMessage(MSGCD_MAILMAGAZINEMEMBER_UPDATE_FAIL);
            }
        }

        return result;
    }
}