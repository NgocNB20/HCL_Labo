/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.infrastructure.gmo.adapter;

import com.gmo_pg.g_pay.client.PaymentClient;
import com.gmo_pg.g_pay.client.common.PaymentException;
import com.gmo_pg.g_pay.client.output.EntryTranOutput;
import com.gmo_pg.g_pay.client.output.ExecTranOutput;
import jp.co.itechh.quad.core.dto.multipayment.HmEntryTranInput;
import jp.co.itechh.quad.core.dto.multipayment.HmExecTranInput;
import jp.co.itechh.quad.ddd.domain.gmo.adapter.ICreditEntryExecTranAdapter;
import jp.co.itechh.quad.ddd.exception.DomainException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * GMOクレジット取引登録決済アダプター実装クラス
 *
 * @author kimura
 */
@Component
public class CreditEntryExecTranAdapterImpl implements ICreditEntryExecTranAdapter {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(CreditEntryExecTranAdapterImpl.class);

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
    public CreditEntryExecTranAdapterImpl(PaymentClient paymentClient) {
        this.paymentClient = paymentClient;
    }

    /**
     * GMOクレジット取引登録通信
     *
     * @param hmEntryTranInput
     * @return EntryTranOutput
     */
    @Override
    public EntryTranOutput creditEntryTran(HmEntryTranInput hmEntryTranInput) {
        try {
            return paymentClient.doEntryTran(hmEntryTranInput);
        } catch (PaymentException e) {
            LOGGER.error("例外処理が発生しました", e);
            throw new DomainException(MSGCD_PAYMENT_COM_FAIL);
        }
    }

    /**
     * GMOクレジット決済実行通信
     *
     * @param hmExecTranInput
     * @return ExecTranOutput
     */
    @Override
    public ExecTranOutput creditEntryExecTran(HmExecTranInput hmExecTranInput) {
        try {
            return paymentClient.doExecTran(hmExecTranInput);
        } catch (PaymentException e) {
            LOGGER.error("例外処理が発生しました", e);
            throw new DomainException(MSGCD_PAYMENT_COM_FAIL);
        }
    }

}