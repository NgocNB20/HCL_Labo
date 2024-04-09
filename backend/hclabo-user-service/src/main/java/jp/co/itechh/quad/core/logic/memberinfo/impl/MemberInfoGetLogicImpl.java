/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.memberinfo.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.constant.type.HTypeMemberInfoStatus;
import jp.co.itechh.quad.core.dao.memberinfo.MemberInfoDao;
import jp.co.itechh.quad.core.entity.memberinfo.MemberInfoEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.memberinfo.MemberInfoGetLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 会員情報取得<br/>
 *
 * @author natume
 * @version $Revision: 1.7 $
 *
 */
@Component
public class MemberInfoGetLogicImpl extends AbstractShopLogic implements MemberInfoGetLogic {

    /**
     * MemberInfoDao<br/>
     */
    private final MemberInfoDao memberInfoDao;

    @Autowired
    public MemberInfoGetLogicImpl(MemberInfoDao memberInfoDao) {
        this.memberInfoDao = memberInfoDao;
    }

    /**
     * 会員情報を取得する<br />
     *
     * @param memberInfoSeq 会員情報SEQ
     * @return 会員情報エンティティ
     */
    @Override
    public MemberInfoEntity execute(Integer memberInfoSeq) {

        // パラメータチェック
        ArgumentCheckUtil.assertNotNull("memberInfoSeq", memberInfoSeq);

        // 会員情報取得
        return memberInfoDao.getEntity(memberInfoSeq);
    }

    /**
     * 会員情報を取得する<br />
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
        return memberInfoDao.getEntityByStatus(memberInfoSeq, memberInfoStatus);
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
        ArgumentCheckUtil.assertGreaterThanZero("shopSeq", shopSeq);
        ArgumentCheckUtil.assertNotEmpty("memberInfoId", memberInfoId);
        ArgumentCheckUtil.assertNotNull("memberInfoStatus", memberInfoStatus);

        // 会員情報取得
        return memberInfoDao.getEntityByIdStatus(shopSeq, memberInfoId, memberInfoStatus);
    }

    /**
     * 会員情報を取得する<br />
     *
     * @param shopUniqueId ショップユニークId
     * @return 会員情報エンティティ
     */
    @Override
    public MemberInfoEntity execute(String shopUniqueId) {

        // パラメータチェック
        ArgumentCheckUtil.assertNotEmpty("shopUniqueId", shopUniqueId);

        // 会員情報取得
        return memberInfoDao.getEntityByMemberInfoUniqueId(shopUniqueId);
    }

    /**
     * 暫定会員情報を取得する<br />
     *
     * @param memberInfoUniqueId 会員ユニークID
     * @param memberInfoMail メールアドレス
     * @return 暫定会員情報エンティティ
     */
    @Override
    public MemberInfoEntity executeForProvisional(String memberInfoUniqueId, String memberInfoMail) {

        // ユニークIDで会員情報取得
        MemberInfoEntity memberInfoEntityByUniqueId = memberInfoDao.getEntityByMemberInfoUniqueId(memberInfoUniqueId);

        // メールアドレスとステータスで会員情報取得
        MemberInfoEntity memberInfoEntityByMail =
                        memberInfoDao.getEntityByMailStatus(memberInfoMail, HTypeMemberInfoStatus.ADMISSION);

        if (memberInfoEntityByUniqueId == null && memberInfoEntityByMail != null) {
            // 暫定会員の情報のみ登録されている場合
            return memberInfoEntityByMail;
        }

        return null;
    }

    /**
     * 会員情報を取得する<br />
     *
     * @param memberInfoMail メールアドレス
     * @param memberInfoStatus 会員ステータス
     * @return 会員情報エンティティ
     */
    @Override
    public MemberInfoEntity executeByMailStatus(String memberInfoMail, HTypeMemberInfoStatus memberInfoStatus) {
        // パラメータチェック
        ArgumentCheckUtil.assertNotEmpty("memberInfoMail", memberInfoMail);
        ArgumentCheckUtil.assertNotNull("memberInfoStatus", memberInfoStatus);

        // 会員情報取得
        return memberInfoDao.getEntityByMailStatus(memberInfoMail, memberInfoStatus);
    }
}