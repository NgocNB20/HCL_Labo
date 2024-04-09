/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.memberinfo.confirmmail.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.dao.memberinfo.confirmmail.ConfirmMailDao;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.memberinfo.confirmmail.ConfirmMailDeleteLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 確認メール情報削除<br/>
 *
 * @author hasegawa(itec)
 *
 */
@Component
public class ConfirmMailDeleteLogicImpl extends AbstractShopLogic implements ConfirmMailDeleteLogic {

    /** 確認メールDaoクラス */
    private final ConfirmMailDao confirmMailDao;

    @Autowired
    public ConfirmMailDeleteLogicImpl(ConfirmMailDao confirmMailDao) {
        this.confirmMailDao = confirmMailDao;
    }

    /**
     * 確認メール情報削除処理<br/>
     *
     * @param confirmMailPassword 確認メールパスワード
     * @return 削除件数
     */
    @Override
    public int execute(String confirmMailPassword) {

        // 入力チェック
        ArgumentCheckUtil.assertNotEmpty("confirmmailpassword", confirmMailPassword);

        // 有効な確認メール情報取得
        return confirmMailDao.deleteByConfirmMailPassword(confirmMailPassword);
    }
}