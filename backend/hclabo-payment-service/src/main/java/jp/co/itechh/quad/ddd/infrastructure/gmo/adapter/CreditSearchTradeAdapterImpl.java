/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.infrastructure.gmo.adapter;

import com.gmo_pg.g_pay.client.PaymentClient;
import com.gmo_pg.g_pay.client.common.PaymentException;
import com.gmo_pg.g_pay.client.input.SearchTradeInput;
import com.gmo_pg.g_pay.client.output.SearchTradeOutput;
import jp.co.itechh.quad.ddd.domain.gmo.adapter.ICreditSearchTradeAdapter;
import jp.co.itechh.quad.ddd.exception.DomainException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * GMOクレジット取引状態参照 アダプター実装クラス
 *
 * @author kimura
 */
@Component
public class CreditSearchTradeAdapterImpl implements ICreditSearchTradeAdapter {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(CreditSearchTradeAdapterImpl.class);

    /** GMOモジュールクライアント */
    private final PaymentClient paymentClient;

    @Autowired
    public CreditSearchTradeAdapterImpl(PaymentClient paymentClient) {
        this.paymentClient = paymentClient;
    }

    /**
     * GMOクレジット決済変更通信
     *
     * @param searchTradeInput GMO取引状態参照通信パラメータ
     * @return SearchTradeOutput 取引状態照会結果
     */
    @Override
    public SearchTradeOutput doSearchTrade(SearchTradeInput searchTradeInput) {

        SearchTradeOutput output = null;

        try {
            output = this.paymentClient.doSearchTrade(searchTradeInput);
        } catch (PaymentException e) {
            LOGGER.error("例外処理が発生しました", e);
            throw new DomainException(MSGCD_PAYMENT_COM_FAIL);
        }
        return output;
    }

}
