/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */
package jp.co.itechh.quad.core.logic.multipayment.communicate.impl;

import com.gmo_pg.g_pay.client.PaymentClient;
import com.gmo_pg.g_pay.client.common.PaymentException;
import com.gmo_pg.g_pay.client.output.SearchCardOutput;
import jp.co.itechh.quad.core.dto.multipayment.HmPaymentClientInput;
import jp.co.itechh.quad.core.dto.multipayment.HmSearchCardInput;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.multipayment.communicate.SearchCardLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *  カード参照通信Logic実装クラス
 *
 * @author s_nose
 */
@Component
public class SearchCardLogicImpl extends AbstractShopLogic implements SearchCardLogic {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchCardLogicImpl.class);

    /**
     * ＰＧカード決済サービスクライアント
     */
    private final PaymentClient paymentClient;

    @Autowired
    public SearchCardLogicImpl(PaymentClient paymentClient) {
        this.paymentClient = paymentClient;
    }

    /**
     * 実行メソッド
     *
     * @param paymentMemberId 決済代行会員ID
     * @return カード参照出力パラメータ
     */
    @Override
    public SearchCardOutput execute(String paymentMemberId) {
        SearchCardOutput res = new SearchCardOutput();

        HmSearchCardInput input = new HmSearchCardInput();

        input.setMemberId(paymentMemberId);
        // カード登録連番モード
        input.setSeqMode(HmPaymentClientInput.SEQ_MODE_LOGICAL);
        input.setCardSeq(HmPaymentClientInput.CARD_SEQ);

        try {
            res = paymentClient.doSearchCard(input);
        } catch (PaymentException pe) {
            LOGGER.error("例外処理が発生しました", pe);
            // 呼び元でエラーハンドリング実施
            return null;
        }

        return res;
    }
}
