/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.user.adapter;

import jp.co.itechh.quad.core.entity.memberinfo.MemberInfoEntity;

/**
 * 顧客アダプター
 *
 * @author yt23807
 */
public interface ICustomerAdapter {

    /**
     * ユーザーマイクロサービス<br/>
     * 会員詳細情報取得
     *
     * @param memberinfoSeq 会員SEQ
     * @return 会員詳細
     */
    MemberInfoEntity getMemberInfoEntity(Integer memberinfoSeq);

}
