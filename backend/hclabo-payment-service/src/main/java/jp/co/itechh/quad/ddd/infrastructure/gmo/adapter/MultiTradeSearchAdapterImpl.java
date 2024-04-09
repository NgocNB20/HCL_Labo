/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.infrastructure.gmo.adapter;

import com.gmo_pg.g_pay.client.PaymentClient;
import com.gmo_pg.g_pay.client.common.PaymentException;
import com.gmo_pg.g_pay.client.input.SearchTradeMultiInput;
import com.gmo_pg.g_pay.client.output.SearchTradeMultiOutput;
import jp.co.itechh.quad.ddd.domain.gmo.adapter.IMultiTradeSearchAdapter;
import jp.co.itechh.quad.ddd.exception.DomainException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * GMOマルチ決済取引検索アダプター実装クラス
 *
 * @author kimura
 */
@Component
public class MultiTradeSearchAdapterImpl implements IMultiTradeSearchAdapter {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(MultiTradeSearchAdapterImpl.class);

    /**
     * ＰＧカード決済サービスクライアント
     */
    private final PaymentClient paymentClient;

    public MultiTradeSearchAdapterImpl(PaymentClient paymentClient) {
        this.paymentClient = paymentClient;
    }

    /**
     * GMOマルチ決済取引検索
     *
     * @param searchTradeMultiInput
     * @return SearchTradeMultiOutput
     */
    @Override
    public SearchTradeMultiOutput doSearchTradeMulti(SearchTradeMultiInput searchTradeMultiInput) {

        SearchTradeMultiOutput output = null;

        try {
            output = this.paymentClient.doSearchTradeMulti(searchTradeMultiInput);
        } catch (PaymentException e) {
            LOGGER.error("例外処理が発生しました", e);
            throw new DomainException(MSGCD_PAYMENT_COM_FAIL);
        }
        return output;
    }
}
