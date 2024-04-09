/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.infrastructure.gmo.adapter;

import com.gmo_pg.g_pay.client.PaymentClient;
import com.gmo_pg.g_pay.client.common.PaymentException;
import com.gmo_pg.g_pay.client.output.AlterTranOutput;
import jp.co.itechh.quad.core.dto.multipayment.HmAlterTranInput;
import jp.co.itechh.quad.ddd.domain.gmo.adapter.ICreditAlterTranAdapter;
import jp.co.itechh.quad.ddd.exception.DomainException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * GMOクレジット取引通信（取消取消・取引再開・売上実行）アダプター実装クラス
 *
 * @author kimura
 */
@Component
public class CreditAlterTranAdapterImpl implements ICreditAlterTranAdapter {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(CreditAlterTranAdapterImpl.class);

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
    public CreditAlterTranAdapterImpl(PaymentClient paymentClient) {
        this.paymentClient = paymentClient;
    }

    /**
     * GMO変更通信
     *
     * @param hmAlterTranInput
     * @return AlterTranOutput　GMO取引変更結果
     */
    @Override
    public AlterTranOutput doAlterTran(HmAlterTranInput hmAlterTranInput) {
        try {
            return paymentClient.doAlterTran(hmAlterTranInput);
        } catch (PaymentException e) {
            LOGGER.error("例外処理が発生しました", e);
            throw new DomainException(ALTERTRAN_COM_ERR_MSG_ID);
        }
    }

}
