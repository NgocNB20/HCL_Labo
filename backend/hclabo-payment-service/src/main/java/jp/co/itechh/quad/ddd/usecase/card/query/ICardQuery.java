/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.card.query;

import jp.co.itechh.quad.ddd.usecase.card.query.model.AuthExpirationApproachingTransactionQueryModel;

import java.sql.Timestamp;
import java.util.List;

/**
 * クレジットカードクエリー
 */
public interface ICardQuery {

    /**
     * オーソリ期限切れ間近受注リスト取得
     *
     * @param currentDate         処理日
     * @param mailSendStartPeriod メール送信開始期間（日）
     * @return オーソリ期限切れ間近受注リスト
     */
    List<AuthExpirationApproachingTransactionQueryModel> getAuthExpirationApproachingTransactionList(Timestamp currentDate,
                                                                                                     String mailSendStartPeriod);

}