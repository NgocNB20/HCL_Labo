/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.memberinfo;

import jp.co.itechh.quad.core.constant.type.HTypeMemberInfoStatus;
import jp.co.itechh.quad.core.entity.memberinfo.MemberInfoEntity;

/**
 * 会員情報データチェック<br/>
 *
 * @author natume
 * @version $Revision: 1.2 $
 *
 */
public interface MemberInfoDataCheckLogic {

    /**
     * 会員ID重複エラー<br/>
     * <code>MSGCD_MEMBERINFOENTITYDTO_NULL</code>
     */
    public static final String MSGCD_MEMBERINFO_ID_MULTI = "LMM000501";

    /**
     * 会員情報データチェック処理<br/>
     *
     * @param memberInfoEntity 会員エンティティ
     */
    void execute(MemberInfoEntity memberInfoEntity);

    /**
     * 会員情報データチェック処理<br/>
     *
     * @param memberInfoMail メールアドレス
     * @param memberInfoStatus 会員ステータス
     */
    void execute(String memberInfoMail, HTypeMemberInfoStatus memberInfoStatus);
}
