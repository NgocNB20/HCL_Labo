/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.memberinfo.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.entity.memberinfo.MemberInfoEntity;
import jp.co.itechh.quad.core.logic.memberinfo.MemberInfoUpdateLogic;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.memberinfo.MemberInfoUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

/**
 * 会員情報更新サービス実装クラス<br/>
 *
 * @author negishi
 * @version $Revision: 1.2 $
 *
 */
@Service
public class MemberInfoUpdateServiceImpl extends AbstractShopService implements MemberInfoUpdateService {

    /** 会員情報更新ロジック */
    private final MemberInfoUpdateLogic memberInfoUpdateLogic;

    @Autowired
    public MemberInfoUpdateServiceImpl(MemberInfoUpdateLogic memberInfoUpdateLogic) {
        this.memberInfoUpdateLogic = memberInfoUpdateLogic;
    }

    /**
     * 会員情報更新処理
     *
     * @param memberInfoEntity 会員情報エンティティ
     * @return 更新件数
     */
    @Override
    public int execute(MemberInfoEntity memberInfoEntity) {
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);
        return execute(memberInfoEntity, dateUtility.getCurrentTime());
    }

    /**
     * 更新日時指定付き会員情報更新処理<br/>
     *
     * @param memberInfoEntity 会員エンティティ
     * @param currentTime 現在日時
     * @return 更新件数
     */
    @Override
    public int execute(MemberInfoEntity memberInfoEntity, Timestamp currentTime) {

        // パラメータチェック
        ArgumentCheckUtil.assertNotNull("memberInfoEntity", memberInfoEntity);

        // 会員情報更新
        int result = memberInfoUpdateLogic.execute(memberInfoEntity, currentTime);
        if (result == 0) {
            throwMessage(MSGCD_MEMBERINFO_UPDATE_FAIL);
        }
        return result;
    }
}