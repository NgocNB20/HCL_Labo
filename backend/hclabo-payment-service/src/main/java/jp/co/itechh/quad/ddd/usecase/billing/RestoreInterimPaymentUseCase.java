/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.billing;

import jp.co.itechh.quad.ddd.domain.billing.entity.BillingSlipEntity;
import jp.co.itechh.quad.ddd.domain.billing.repository.IBillingSlipRepository;
import jp.co.itechh.quad.ddd.exception.DomainException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 途中決済復元ユースケース
 */
@Service
public class RestoreInterimPaymentUseCase {

    /** 請求伝票リポジトリ */
    private final IBillingSlipRepository billingSlipRepository;

    /** コンストラクタ */
    @Autowired
    public RestoreInterimPaymentUseCase(IBillingSlipRepository billingSlipRepository) {
        this.billingSlipRepository = billingSlipRepository;
    }

    /**
     * 途中決済を復元する<br/>
     * ※注文決済.受注番号を渡された受注番号で更新し、ステータスを下書きに戻す
     *
     * @param transactionId 取引ID
     * @param orderCode     管理者ID
     */
    public void restoreInterimPayment(String transactionId, String orderCode) {

        // 取引IDに紐づく請求伝票を取得する
        BillingSlipEntity billingSlipEntity = billingSlipRepository.getByTransactionId(transactionId);
        if (billingSlipEntity == null) {
            throw new DomainException("PAYMENT_EPAR0002-E", new String[] {transactionId});
        }

        billingSlipEntity.getOrderPaymentEntity().restoreUnderSettlement(orderCode);

        // 改訂用請求伝票を更新
        billingSlipRepository.update(billingSlipEntity);
    }
}