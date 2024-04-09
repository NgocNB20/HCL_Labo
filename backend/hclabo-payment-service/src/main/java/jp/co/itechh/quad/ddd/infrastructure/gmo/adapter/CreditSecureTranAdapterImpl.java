/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.infrastructure.gmo.adapter;

import com.gmo_pg.g_pay.client.PaymentClient;
import com.gmo_pg.g_pay.client.common.PaymentException;
import com.gmo_pg.g_pay.client.input.SecureTran2Input;
import com.gmo_pg.g_pay.client.output.SecureTran2Output;
import jp.co.itechh.quad.ddd.domain.gmo.adapter.ICreditSecureTranAdapter;
import jp.co.itechh.quad.ddd.exception.DomainException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * GMOクレジット認証後決済アダプター実装クラス
 *
 * @author kimura
 */
@Component
public class CreditSecureTranAdapterImpl implements ICreditSecureTranAdapter {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(CreditSecureTranAdapterImpl.class);

    /**
     * ＰＧカード決済サービスクライアント
     */
    private final PaymentClient paymentClient;

    /**
     * コンストラクタ
     *
     * @param paymentClient ＰＧカード決済サービスクライアント
     */
    @Autowired
    public CreditSecureTranAdapterImpl(PaymentClient paymentClient) {
        this.paymentClient = paymentClient;
    }

    /**
     * GMOクレジット認証後決済実行通信
     *
     * @param secureTran2Input
     * @return SecureTran2Output
     */
    @Override
    public SecureTran2Output creditSecureTran(SecureTran2Input secureTran2Input) {
        try {
            return paymentClient.doSecureTran2(secureTran2Input);
        } catch (PaymentException e) {
            LOGGER.error("例外処理が発生しました", e);
            throw new DomainException(MSGCD_PAYMENT_COM_FAIL);
        }
    }

}