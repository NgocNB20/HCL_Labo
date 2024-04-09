/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.memberinfo.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.dao.memberinfo.MemberInfoDao;
import jp.co.itechh.quad.core.entity.memberinfo.MemberInfoEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.memberinfo.MemberInfoRegistLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * 会員情報登録<br/>
 *
 * @author natume
 * @author Kaneko (itec) 2012/08/21 UtilからHelperへ変更　Util使用箇所をgetComponentで取得するように変更
 *
 */
@Component
public class MemberInfoRegistLogicImpl extends AbstractShopLogic implements MemberInfoRegistLogic {

    /**
     * MemberInfoDao<br/>
     */
    private final MemberInfoDao memberInfoDao;

    @Autowired
    public MemberInfoRegistLogicImpl(MemberInfoDao memberInfoDao) {
        this.memberInfoDao = memberInfoDao;
    }

    /**
     * 会員情報登録処理<br/>
     *
     * @param memberInfoEntity 会員情報エンティティ
     * @return 登録件数
     */
    @Override
    public int execute(MemberInfoEntity memberInfoEntity) {

        // パラメータチェック
        ArgumentCheckUtil.assertNotNull("memberInfoEntity", memberInfoEntity);

        // 日付関連Helper取得
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);

        // 日時セット
        Timestamp currentTime = dateUtility.getCurrentTime();
        memberInfoEntity.setRegistTime(currentTime);
        memberInfoEntity.setUpdateTime(currentTime);

        // 登録
        return memberInfoDao.insert(memberInfoEntity);
    }
}