/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.memberinfo.confirmmail.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.constant.type.HTypeConfirmMailType;
import jp.co.itechh.quad.core.dao.memberinfo.confirmmail.ConfirmMailDao;
import jp.co.itechh.quad.core.entity.memberinfo.confirmmail.ConfirmMailEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.memberinfo.confirmmail.ConfirmMailGetLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 確認メール情報取得<br/>
 *
 * @author natume
 *
 */
@Component
public class ConfirmMailGetLogicImpl extends AbstractShopLogic implements ConfirmMailGetLogic {

    /**
     * ConfirmMailDao<br/>
     */
    private final ConfirmMailDao confirmMailDao;

    @Autowired
    public ConfirmMailGetLogicImpl(ConfirmMailDao confirmMailDao) {
        this.confirmMailDao = confirmMailDao;
    }

    /**
     * 確認メール情報取得処理<br/>
     *
     * @param password メールパスワード
     * @param confirmMailType 確認メール種別
     * @return 確認メールエンティティ
     */
    @Override
    public ConfirmMailEntity execute(String password, HTypeConfirmMailType confirmMailType) {

        // 入力チェック
        ArgumentCheckUtil.assertNotEmpty("password", password);
        ArgumentCheckUtil.assertNotNull("confirmMailType", confirmMailType);

        // 有効な確認メール情報取得
        return confirmMailDao.getEntityByType(password, confirmMailType);
    }

}