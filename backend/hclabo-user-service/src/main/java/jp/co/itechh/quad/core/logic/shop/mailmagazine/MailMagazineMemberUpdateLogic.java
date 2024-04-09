/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.mailmagazine;

import jp.co.itechh.quad.core.entity.mailmagazine.MailMagazineMemberEntity;

/**
 * メルマガ購読者情報更新ロジック<br/>
 *
 * @author kimura
 */
public interface MailMagazineMemberUpdateLogic {

    /**
     * メルマガ購読者情報更新処理<br/>
     *
     * @param mailMagazineMemberEntity メルマガ購読者エンティティ
     * @return 更新件数
     */
    int execute(MailMagazineMemberEntity mailMagazineMemberEntity);
}