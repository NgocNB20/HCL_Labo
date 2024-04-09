/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.memberinfo;

import jp.co.itechh.quad.core.constant.type.HTypeMemberInfoStatus;
import jp.co.itechh.quad.core.entity.memberinfo.MemberInfoEntity;

/**
 * 会員情報取得サービス<br/>
 *
 * @author negishi
 * @version $Revision: 1.4 $
 *
 */
public interface MemberInfoGetService {

    /**
     * 会員情報失敗エラー<br/>
     * <code>MSGCD_MEMBERINFOENTITYDTO_NULL</code>
     */
    String MSGCD_MEMBERINFOENTITYDTO_NULL = "SMM000201";

    /**
     * 会員情報取得<br/>
     * 条件：会員SEQ
     *
     * @param memberInfoSeq 会員情報SEQ
     * @return 会員情報エンティティ
     */
    MemberInfoEntity execute(Integer memberInfoSeq);

    /**
     * 会員情報取得<br/>
     * 条件：会員SEQ,会員状態
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
}
