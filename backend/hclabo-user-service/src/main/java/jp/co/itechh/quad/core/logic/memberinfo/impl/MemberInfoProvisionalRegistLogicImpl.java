/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.memberinfo.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.constant.type.HTypeMemberInfoStatus;
import jp.co.itechh.quad.core.dao.memberinfo.MemberInfoDao;
import jp.co.itechh.quad.core.entity.memberinfo.MemberInfoEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.memberinfo.MemberInfoProvisionalRegistLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * 暫定会員情報登録
 *
 * @author kimura
 */
@Component
public class MemberInfoProvisionalRegistLogicImpl extends AbstractShopLogic
                implements MemberInfoProvisionalRegistLogic {

    /** MemberInfoDao*/
    private final MemberInfoDao memberInfoDao;

    @Autowired
    public MemberInfoProvisionalRegistLogicImpl(MemberInfoDao memberInfoDao) {
        this.memberInfoDao = memberInfoDao;
    }

    /**
     * 暫定会員情報登録処理
     * メルマガ登録時に呼ばれるlogic
     *
     * @param memberInfoEntity 会員情報エンティティ
     * @return 登録件数
     */
    @Override
    public int execute(MemberInfoEntity memberInfoEntity) {

        // パラメータチェック
        ArgumentCheckUtil.assertNotNull("memberInfoEntity", memberInfoEntity);

        setMemberInfoEntity(memberInfoEntity);

        // 登録
        return memberInfoDao.insert(memberInfoEntity);
    }

    /**
     * 暫定会員情報のセット
     *
     * @param memberInfoEntity 会員エンティティ
     */
    protected void setMemberInfoEntity(MemberInfoEntity memberInfoEntity) {

        // 日付関連Helper取得
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);

        // 日時セット
        Timestamp currentTime = dateUtility.getCurrentTime();
        memberInfoEntity.setRegistTime(currentTime);
        memberInfoEntity.setUpdateTime(currentTime);

        // 会員SEQセット
        int seq = memberInfoDao.getMemberInfoSeqNextVal();
        memberInfoEntity.setMemberInfoSeq(seq);

        // 会員状態：「入会」に設定
        memberInfoEntity.setMemberInfoStatus(HTypeMemberInfoStatus.ADMISSION);

        // ショップSEQ
        memberInfoEntity.setShopSeq(1001);
    }
}