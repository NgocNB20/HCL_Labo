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
import jp.co.itechh.quad.core.logic.shop.mailmagazine.MailMagazineMemberUpdateLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * メルマガ購読者情報更新ロジック<br/>
 *
 * @author kimura
 */
@Component
public class MailMagazineMemberUpdateLogicImpl extends AbstractShopLogic implements MailMagazineMemberUpdateLogic {

    /** メルマガ購読者Dao */
    private MailMagazineMemberDao mailMagazineMemberDao;

    /**
     * コンストラクタ
     *
     * @param mailMagazineMemberDao
     */
    @Autowired
    public MailMagazineMemberUpdateLogicImpl(MailMagazineMemberDao mailMagazineMemberDao) {
        this.mailMagazineMemberDao = mailMagazineMemberDao;
    }

    /**
     * メルマガ購読者情報更新処理<br/>
     *
     * @param mailMagazineMemberEntity メルマガ購読者エンティティ
     * @return 更新件数
     */
    @Override
    public int execute(MailMagazineMemberEntity mailMagazineMemberEntity) {

        // 引数チェック
        ArgumentCheckUtil.assertNotNull("mailMagazineMemberEntity", mailMagazineMemberEntity);

        // 日付関連Helper取得
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);

        // メルマガ購読者の更新
        mailMagazineMemberEntity.setUpdateTime(dateUtility.getCurrentTime());
        return mailMagazineMemberDao.update(mailMagazineMemberEntity);
    }
}