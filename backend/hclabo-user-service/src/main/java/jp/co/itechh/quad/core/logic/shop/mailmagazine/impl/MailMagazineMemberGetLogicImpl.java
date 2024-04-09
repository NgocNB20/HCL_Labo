/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.mailmagazine.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.dao.mailmagazine.MailMagazineMemberDao;
import jp.co.itechh.quad.core.entity.mailmagazine.MailMagazineMemberEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.shop.mailmagazine.MailMagazineMemberGetLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * メルマガ購読者情報取得ロジック<br/>
 *
 * @author kimura
 */
@Component
public class MailMagazineMemberGetLogicImpl extends AbstractShopLogic implements MailMagazineMemberGetLogic {

    /** メルマガ購読者Dao */
    private MailMagazineMemberDao mailMagazineMemberDao;

    /**
     * コンストラクタ
     *
     * @param mailMagazineMemberDao
     */
    @Autowired
    public MailMagazineMemberGetLogicImpl(MailMagazineMemberDao mailMagazineMemberDao) {
        this.mailMagazineMemberDao = mailMagazineMemberDao;
    }

    /**
     * メルマガ購読者情報取得<br/>
     *
     * @param memberInfoSeq 会員SEQ
     * @param uniqueMail ユニークメールアドレス
     * @return メルマガ購読者エンティティ
     */
    @Override
    public MailMagazineMemberEntity execute(Integer memberInfoSeq) {

        // 引数チェック
        ArgumentCheckUtil.assertGreaterThanZero("memberInfoSeq", memberInfoSeq);

        // 取得
        return mailMagazineMemberDao.getEntity(memberInfoSeq);
    }
}