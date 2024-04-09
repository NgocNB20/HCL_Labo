/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.billing;

import jp.co.itechh.quad.ddd.domain.billing.entity.BillingSlipEntity;
import jp.co.itechh.quad.ddd.domain.billing.entity.BillingSlipForRevisionEntity;
import jp.co.itechh.quad.ddd.domain.billing.entity.OrderPaymentForRevisionEntity;
import jp.co.itechh.quad.ddd.domain.billing.repository.IBillingSlipForRevisionRepository;
import jp.co.itechh.quad.ddd.domain.billing.repository.IBillingSlipRepository;
import jp.co.itechh.quad.ddd.exception.DomainException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 改訂用請求伝票発行ユースケース
 */
@Service
public class PublishBillingSlipForRevisionUseCase {

    /** 改訂用請求伝票リポジトリ */
    private final IBillingSlipForRevisionRepository billingSlipForRevisionRepository;

    /** 請求伝票リポジトリ */
    private final IBillingSlipRepository billingSlipRepository;

    /** コンストラクタ */
    @Autowired
    public PublishBillingSlipForRevisionUseCase(IBillingSlipForRevisionRepository billingSlipForRevisionRepository,
                                                IBillingSlipRepository billingSlipRepository) {
        this.billingSlipForRevisionRepository = billingSlipForRevisionRepository;
        this.billingSlipRepository = billingSlipRepository;
    }

    /**
     * 改訂用請求伝票を発行する
     *
     * @param transactionId         改訂元取引ID
     * @param transactionRevisionId 改訂用取引ID
     */
    public void publishBillingSlipForRevision(String transactionId, String transactionRevisionId) {

        // 取引IDに紐づく請求伝票を取得する
        BillingSlipEntity billingSlipEntity = billingSlipRepository.getByTransactionId(transactionId);
        if (billingSlipEntity == null) {
            throw new DomainException("PAYMENT_EPAR0002-E", new String[] {transactionId});
        }

        // 改訂用注文決済発行
        OrderPaymentForRevisionEntity orderPaymentForRevisionEntity =
                        new OrderPaymentForRevisionEntity(billingSlipEntity.getOrderPaymentEntity());

        // 改訂用請求伝票発行
        BillingSlipForRevisionEntity billingSlipForRevisionEntity =
                        new BillingSlipForRevisionEntity(billingSlipEntity, transactionRevisionId,
                                                         orderPaymentForRevisionEntity, new Date()
                        );

        // 改訂用請求伝票を登録する
        billingSlipForRevisionRepository.save(billingSlipForRevisionEntity);
    }
}