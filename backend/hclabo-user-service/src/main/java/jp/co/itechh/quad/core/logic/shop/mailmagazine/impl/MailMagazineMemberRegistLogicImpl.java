/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.mailmagazine.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.dao.mailmagazine.MailMagazineMemberDao;
import jp.co.itechh.quad.core.entity.mailmagazine.MailMagazineMemberEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.shop.mailmagazine.MailMagazineMemberRegistLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * メルマガ購読者登録ロジック<br/>
 *
 * @author kimura
 */
@Component
public class MailMagazineMemberRegistLogicImpl extends AbstractShopLogic implements MailMagazineMemberRegistLogic {

    /** メルマガ購読者Dao */
    private MailMagazineMemberDao mailMagazineMemberDao;

    /**
     * コンストラクタ
     *
     * @param mailMagazineMemberDao
     */
    @Autowired
    public MailMagazineMemberRegistLogicImpl(MailMagazineMemberDao mailMagazineMemberDao) {
        this.mailMagazineMemberDao = mailMagazineMemberDao;
    }

    /**
     * ロジック実行<br/>
     *
     * @param mailMagazineMemberEntity メルマガ購読者エンティティ
     * @return 登録件数
     */
    @Override
    public int execute(MailMagazineMemberEntity mailMagazineMemberEntity) {

        // 引数チェック
        ArgumentCheckUtil.assertNotNull("mailMagazineMemberEntity", mailMagazineMemberEntity);

        // 日付関連Helper取得
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);

        // 登録日時の設定
        Timestamp currentTime = dateUtility.getCurrentTime();
        mailMagazineMemberEntity.setRegistTime(currentTime);
        mailMagazineMemberEntity.setUpdateTime(currentTime);

        // メルマガ購読者の登録
        return mailMagazineMemberDao.insert(mailMagazineMemberEntity);
    }
}