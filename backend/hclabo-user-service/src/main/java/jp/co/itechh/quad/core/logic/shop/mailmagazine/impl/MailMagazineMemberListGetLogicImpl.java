/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.mailmagazine.impl;

import jp.co.itechh.quad.core.dao.mailmagazine.MailMagazineMemberDao;
import jp.co.itechh.quad.core.entity.mailmagazine.MailMagazineMemberEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.shop.mailmagazine.MailMagazineMemberListGetLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * メルマガ購読者情報リスト取得ロジック
 *
 * @author kimura
 */
@Component
public class MailMagazineMemberListGetLogicImpl extends AbstractShopLogic implements MailMagazineMemberListGetLogic {

    /** メルマガ購読者Dao */
    private MailMagazineMemberDao mailMagazineMemberDao;

    /**
     * コンストラクタ
     *
     * @param mailMagazineMemberDao
     */
    @Autowired
    public MailMagazineMemberListGetLogicImpl(MailMagazineMemberDao mailMagazineMemberDao) {
        this.mailMagazineMemberDao = mailMagazineMemberDao;
    }

    /**
     * ロジック実行
     *
     * @return メルマガ購読者エンティティリスト
     */
    @Override
    public List<MailMagazineMemberEntity> execute() {
        return mailMagazineMemberDao.getEntityList();
    }

    /**
     * メールマガジン購読者情報リストをメールで取得<br/>
     *
     * @param memberInfoUniqueId 一意制約用メールアドレス
     * @return ニュースレター購読者エンティティ リスト
     */
    @Override
    public List<MailMagazineMemberEntity> execute(String memberInfoUniqueId) {
        return mailMagazineMemberDao.getByMemberInfoUniqueId(memberInfoUniqueId);
    }
}