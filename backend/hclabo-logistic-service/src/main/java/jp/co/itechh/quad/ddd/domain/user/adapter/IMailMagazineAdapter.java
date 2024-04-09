/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.user.adapter;

import java.util.List;

/**
 * メールマガアダプター
 */
public interface IMailMagazineAdapter {

    /**
     * ユーザーマイクロサービス<br/>
     * メールマガ配信状態一覧取得
     *
     * @param memberId 会員Id
     * @return メールマガ一覧
     */
    List<String> getMailMagazineSendStatus(String memberId);

}