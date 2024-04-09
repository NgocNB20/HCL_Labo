/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.infrastructure.gmo.adapter;

import com.gmo_pg.g_pay.client.PaymentClient;
import com.gmo_pg.g_pay.client.common.PaymentException;
import com.gmo_pg.g_pay.client.output.ChangeTranOutput;
import jp.co.itechh.quad.core.dto.multipayment.HmChangeTranInput;
import jp.co.itechh.quad.ddd.domain.gmo.adapter.ICreditChangeTranAdapter;
import jp.co.itechh.quad.ddd.exception.DomainException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * GMOコンビニ取引登録決済アダプター実装クラス
 *
 * @author kimura
 */
@Component
public class CreditChangeTranAdapterImpl implements ICreditChangeTranAdapter {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(CreditChangeTranAdapterImpl.class);

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
    public CreditChangeTranAdapterImpl(PaymentClient paymentClient) {
        this.paymentClient = paymentClient;
    }

    @Override
    public ChangeTranOutput doChangeTran(HmChangeTranInput hmChangeTranInput) {

        try {

            return paymentClient.doChangeTran(hmChangeTranInput);
        } catch (PaymentException e) {
            LOGGER.error("例外処理が発生しました", e);
            throw new DomainException(CHANGETRAN_COM_ERR_MSG_ID);
        }
    }
}