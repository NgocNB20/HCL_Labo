/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.memberinfo.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.constant.type.HTypeMemberInfoStatus;
import jp.co.itechh.quad.core.entity.memberinfo.MemberInfoEntity;
import jp.co.itechh.quad.core.logic.memberinfo.MemberInfoGetLogic;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.memberinfo.MemberInfoGetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 会員情報取得サービス実装クラス<br/>
 *
 * @author negishi
 * @version $Revision: 1.4 $
 *
 */
@Service
public class MemberInfoGetServiceImpl extends AbstractShopService implements MemberInfoGetService {

    /** 会員情報取得ロジック */
    private final MemberInfoGetLogic memberInfoGetLogic;

    @Autowired
    public MemberInfoGetServiceImpl(MemberInfoGetLogic memberInfoGetLogic) {
        this.memberInfoGetLogic = memberInfoGetLogic;
    }

    /**
     * 会員情報取得<br/>
     * 条件：会員SEQ
     *
     * @param memberInfoSeq 会員情報SEQ
     * @return 会員情報エンティティ
     */
    @Override
    public MemberInfoEntity execute(Integer memberInfoSeq) {

        // パラメータチェック
        ArgumentCheckUtil.assertNotNull("memberInfoSeq", memberInfoSeq);

        // 会員情報取得
        MemberInfoEntity memberInfoEntity = memberInfoGetLogic.execute(memberInfoSeq);
        if (memberInfoEntity == null) {
            throwMessage(MSGCD_MEMBERINFOENTITYDTO_NULL);
        }
        return memberInfoEntity;
    }

    /**
     * 会員情報取得<br/>
     * 条件：会員SEQ,会員状態
     *
     * @param memberInfoSeq 会員情報SEQ
     * @param memberInfoStatus 会員状態
     * @return 会員情報エンティティ
     */
    @Override
    public MemberInfoEntity execute(Integer memberInfoSeq, HTypeMemberInfoStatus memberInfoStatus) {

        // パラメータチェック
        ArgumentCheckUtil.assertNotNull("memberInfoSeq", memberInfoSeq);
        ArgumentCheckUtil.assertNotNull("memberInfoStatus", memberInfoStatus);

        // 会員情報取得
        MemberInfoEntity memberInfoEntity = memberInfoGetLogic.execute(memberInfoSeq, memberInfoStatus);
        if (memberInfoEntity == null) {
            throwMessage(MSGCD_MEMBERINFOENTITYDTO_NULL);
        }
        return memberInfoEntity;
    }

    /**
     * 会員情報を取得する<br />
     *
     * @param shopSeq ショップSEQ
     * @param memberInfoId 会員ID
     * @param memberInfoStatus 会員状態
     * @return 会員情報エンティティ
     */
    @Override
    public MemberInfoEntity execute(Integer shopSeq, String memberInfoId, HTypeMemberInfoStatus memberInfoStatus) {
        // パラメータチェック
        ArgumentCheckUtil.assertNotNull("memberInfoSeq", shopSeq);
        ArgumentCheckUtil.assertNotNull("memberInfoId", memberInfoId);
        ArgumentCheckUtil.assertNotNull("memberInfoStatus", memberInfoStatus);

        // 会員情報取得
        MemberInfoEntity memberInfoEntity = memberInfoGetLogic.execute(shopSeq, memberInfoId, memberInfoStatus);
        if (memberInfoEntity == null) {
            throwMessage(MSGCD_MEMBERINFOENTITYDTO_NULL);
        }
        return memberInfoEntity;
    }
}
