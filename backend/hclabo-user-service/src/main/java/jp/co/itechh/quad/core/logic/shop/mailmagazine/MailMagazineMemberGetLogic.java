/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.mailmagazine;

import jp.co.itechh.quad.core.entity.mailmagazine.MailMagazineMemberEntity;

/**
 * メルマガ購読者情報取得ロジック<br/>
 *
 * @author kimura
 */
public interface MailMagazineMemberGetLogic {

    /**
     * メルマガ購読者情報取得<br/>
     *
     * @param memberInfoSeq 会員SEQ
     * @return メルマガ購読者エンティティ
     */
    MailMagazineMemberEntity execute(Integer memberInfoSeq);
}
