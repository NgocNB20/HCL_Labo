/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.infrastructure.card.query;

import jp.co.itechh.quad.ddd.exception.AssertChecker;
import jp.co.itechh.quad.ddd.infrastructure.card.dao.CardDao;
import jp.co.itechh.quad.ddd.usecase.card.query.ICardQuery;
import jp.co.itechh.quad.ddd.usecase.card.query.model.AuthExpirationApproachingTransactionQueryModel;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

/**
 * カードクエリー実装クラス
 *
 * @author kimura
 */
@Component
public class CardQueryImpl implements ICardQuery {

    /** カードDao */
    private final CardDao cardDao;

    /** コンストラクタ */
    public CardQueryImpl(CardDao cardDao) {
        this.cardDao = cardDao;
    }

    /**
     * オーソリ期限切れ間近取引リスト取得
     *
     * @param currentDate         処理日
     * @param mailSendStartPeriod メール送信開始期間（日）
     * @return オーソリ期限切れ間近受注リスト
     */
    @Override
    public List<AuthExpirationApproachingTransactionQueryModel> getAuthExpirationApproachingTransactionList(Timestamp currentDate,
                                                                                                            String mailSendStartPeriod) {

        AssertChecker.assertNotNull("currentDate is null", currentDate);
        AssertChecker.assertNotEmpty("mailSendStartPeriod is empty", mailSendStartPeriod);

        return this.cardDao.getAuthExpirationApproachingTransactionList(currentDate, mailSendStartPeriod);
    }

}