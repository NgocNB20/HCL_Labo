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
import jp.co.itechh.quad.core.logic.memberinfo.MemberInfoUpdateLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * 会員情報更新<br/>
 *
 * @author natume
 * @author Kaneko (itec) 2012/08/21 UtilからHelperへ変更　Util使用箇所をgetComponentで取得するように変更
 *
 */
@Component
public class MemberInfoUpdateLogicImpl extends AbstractShopLogic implements MemberInfoUpdateLogic {

    /**
     * MemberInfoDao<br/>
     */
    private final MemberInfoDao memberInfoDao;

    @Autowired
    public MemberInfoUpdateLogicImpl(MemberInfoDao memberInfoDao) {
        this.memberInfoDao = memberInfoDao;
    }

    /**
     * 会員情報更新処理<br/>
     *
     * @param memberInfoEntity 会員エンティティ
     * @return 更新件数
     */
    @Override
    public int execute(MemberInfoEntity memberInfoEntity) {
        // 日付関連Helper取得
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);

        return execute(memberInfoEntity, dateUtility.getCurrentTime());
    }

    /**
     * 会員情報更新処理<br/>
     *
     * @param memberInfoEntity 会員エンティティ
     * @param currentTime 現在日時
     * @return 更新件数
     */
    @Override
    public int execute(MemberInfoEntity memberInfoEntity, Timestamp currentTime) {
        // パラメータチェック
        ArgumentCheckUtil.assertNotNull("memberInfoEntity", memberInfoEntity);
        // 日時セット
        memberInfoEntity.setUpdateTime(currentTime);
        // 更新
        return memberInfoDao.update(memberInfoEntity);
    }

    /**
     * 会員ログイン情報更新処理<br/>
     *
     * @param memberInfoSeq 会員SEQ
     * @param userAgent ユーザーエージェント
     * @return 更新件数
     */
    @Override
    public int execute(Integer memberInfoSeq, String userAgent) {

        // パラメータチェック
        ArgumentCheckUtil.assertNotNull("memberInfoSeq", memberInfoSeq);
        ArgumentCheckUtil.assertNotNull("userAgent", userAgent);

        // 更新
        return memberInfoDao.updateLogin(memberInfoSeq, userAgent);
    }
}