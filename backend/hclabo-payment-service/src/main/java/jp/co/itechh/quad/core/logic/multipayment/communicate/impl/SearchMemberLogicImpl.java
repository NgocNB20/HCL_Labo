/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */
package jp.co.itechh.quad.core.logic.multipayment.communicate.impl;

import com.gmo_pg.g_pay.client.PaymentClient;
import com.gmo_pg.g_pay.client.common.PaymentException;
import com.gmo_pg.g_pay.client.output.SearchMemberOutput;
import jp.co.itechh.quad.core.dto.multipayment.HmSearchMemberInput;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.multipayment.communicate.SearchMemberLogic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 会員参照通信Logic実装クラス
 *
 * @author s_tsuru
 */
@Component
public class SearchMemberLogicImpl extends AbstractShopLogic implements SearchMemberLogic {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(SearchMemberLogicImpl.class);

    /**
     * ＰＧカード決済サービスクライアント
     */
    private final PaymentClient paymentClient;

    @Autowired
    public SearchMemberLogicImpl(PaymentClient paymentClient) {
        this.paymentClient = paymentClient;
    }

    /**
     * 実行メソッド
     *
     * @param paymentMemberId 決済代行会員ID
     * @return SearchMemberOutput 会員参照出力パラメータ
     */
    @Override
    public SearchMemberOutput execute(String paymentMemberId) {
        SearchMemberOutput res = new SearchMemberOutput();

        HmSearchMemberInput input = new HmSearchMemberInput();

        input.setMemberId(paymentMemberId);

        try {
            res = paymentClient.doSearchMember(input);
        } catch (PaymentException pe) {
            LOGGER.error("例外処理が発生しました", pe);
            // 呼び元でエラーハンドリング実施
            throwMessage(MSGCD_PAYMENT_COM_FAIL, null, pe);
        }

        return res;
    }
}
