/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.infrastructure.card.dao;

import jp.co.itechh.quad.ddd.usecase.card.query.model.AuthExpirationApproachingTransactionQueryModel;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.boot.ConfigAutowireable;

import java.sql.Timestamp;
import java.util.List;

/**
 * カードDao
 *
 * @author kimura
 */
@Dao
@ConfigAutowireable
public interface CardDao {

    /**
     * オーソリ期限切れ間近取引リスト取得
     *
     * @param currentDate         処理日
     * @param mailSendStartPeriod メール送信開始期間（日）
     * @return オーソリ期限切れ間近受注リスト
     */
    @Select
    List<AuthExpirationApproachingTransactionQueryModel> getAuthExpirationApproachingTransactionList(Timestamp currentDate,
                                                                                                     String mailSendStartPeriod);

}