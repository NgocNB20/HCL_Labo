/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.mailmagazine;

import jp.co.itechh.quad.core.entity.mailmagazine.MailMagazineMemberEntity;

/**
 * メルマガ購読者登録ロジック<br/>
 *
 * @author kimura
 */
public interface MailMagazineMemberRegistLogic {

    /**
     * ロジック実行<br/>
     *
     * @param mailMagazineMemberEntity メルマガ購読者エンティティ
     * @return 登録件数
     */
    int execute(MailMagazineMemberEntity mailMagazineMemberEntity);
}