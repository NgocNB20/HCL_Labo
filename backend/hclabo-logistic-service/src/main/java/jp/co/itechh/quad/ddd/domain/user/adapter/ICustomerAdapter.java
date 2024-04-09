/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.user.adapter;

import jp.co.itechh.quad.core.dto.memberinfo.MemberInfoDetailsDto;

/**
 * 顧客アダプター
 */
public interface ICustomerAdapter {

    /**
     * ユーザーマイクロサービス<br/>
     * 会員詳細情報取得
     *
     * @param memberinfoSeq 会員SEQ
     * @return 会員詳細
     */
    MemberInfoDetailsDto getMemberInfoDetailsDto(Integer memberinfoSeq);

}
