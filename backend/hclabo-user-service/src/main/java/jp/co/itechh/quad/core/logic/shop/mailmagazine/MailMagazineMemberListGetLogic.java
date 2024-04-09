/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.mailmagazine;

import jp.co.itechh.quad.core.entity.mailmagazine.MailMagazineMemberEntity;

import java.util.List;

/**
 * メルマガ購読者情報リスト取得ロジック
 *
 * @author kimura
 */
public interface MailMagazineMemberListGetLogic {

    /**
     * ロジック実行
     *
     * @return メルマガ購読者エンティティリスト
     */
    List<MailMagazineMemberEntity> execute();

    /**
     * メールマガジン購読者情報リストをメールで取得<br/>
     *
     * @param memberInfoUniqueId 一意制約用メールアドレス
     * @return ニュースレター購読者エンティティ リスト
     */
    List<MailMagazineMemberEntity> execute(String memberInfoUniqueId);
}
