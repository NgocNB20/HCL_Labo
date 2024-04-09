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
 * 会員情報取得ロジック<br/>
 *
 * @author negishi
 * @version $Revision: 1.5 $
 *
 */
public interface MemberInfoGetLogic {

    /**
     * 会員情報を取得する<br />
     *
     * @param memberInfoSeq 会員情報SEQ
     * @return 会員情報エンティティ
     */
    MemberInfoEntity execute(Integer memberInfoSeq);

    /**
     * 会員情報を取得する<br />
     *
     * @param memberInfoSeq 会員情報SEQ
     * @param memberInfoStatus 会員状態
     * @return 会員情報エンティティ
     */
    MemberInfoEntity execute(Integer memberInfoSeq, HTypeMemberInfoStatus memberInfoStatus);

    /**
     * 会員情報を取得する<br />
     *
     * @param shopSeq ショップSEQ
     * @param memberInfoId 会員ID
     * @param memberInfoStatus 会員状態
     * @return 会員情報エンティティ
     */
    MemberInfoEntity execute(Integer shopSeq, String memberInfoId, HTypeMemberInfoStatus memberInfoStatus);

    /**
     * 会員情報を取得する<br />
     *
     * @param shopUniqueId ショップユニークId
     * @return 会員情報エンティティ
     */
    MemberInfoEntity execute(String shopUniqueId);

    /**
     * 暫定会員情報を取得する<br />
     *
     * @param memberInfoUniqueId 会員ユニークID
     * @param memberInfoMail メールアドレス
     * @return 暫定会員情報エンティティ
     */
    MemberInfoEntity executeForProvisional(String memberInfoUniqueId, String memberInfoMail);

    /**
     * 会員情報を取得する<br />
     *
     * @param memberInfoMail メールアドレス
     * @param memberInfoStatus 会員ステータス
     * @return 会員情報エンティティ
     */
    MemberInfoEntity executeByMailStatus(String memberInfoMail, HTypeMemberInfoStatus memberInfoStatus);
}