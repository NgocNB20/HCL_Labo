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
 * 会員情報取得ロジック
 *
 * @author negishi
 * @version $Revision: 1.5 $
 *
 */
public interface MemberInfoGetLogic {
    /**
     * 会員情報を取得する<br />
     *
     * @param memberInfoMail メールアドレス
     * @param memberInfoStatus 会員ステータス
     * @return 会員情報エンティティ
     */
    MemberInfoEntity executeByMailStatus(String memberInfoMail, HTypeMemberInfoStatus memberInfoStatus);
}
