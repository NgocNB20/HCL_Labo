/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.billing;

import jp.co.itechh.quad.ddd.domain.billing.entity.BillingSlipEntity;
import jp.co.itechh.quad.ddd.domain.billing.entity.BillingSlipEntityService;
import jp.co.itechh.quad.ddd.domain.billing.repository.IBillingSlipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 請求伝票チェック ユースケース
 */
@Service
public class CheckBillingSlipUseCase {

    /** 請求伝票リポジトリ */
    private final IBillingSlipRepository billingSlipRepository;

    /** 請求伝票ドメインサービス */
    private final BillingSlipEntityService billingSlipEntityService;

    /** コンストラクタ */
    @Autowired
    public CheckBillingSlipUseCase(IBillingSlipRepository billingSlipRepository,
                                   BillingSlipEntityService billingSlipEntityService) {
        this.billingSlipRepository = billingSlipRepository;
        this.billingSlipEntityService = billingSlipEntityService;
    }

    /**
     * 請求伝票をチェックする
     *
     * @param transactionId 取引ID
     */
    public void checkBillingSlip(String transactionId) {

        // 取引IDに紐づく請求伝票を取得する
        BillingSlipEntity billingSlipEntity = billingSlipRepository.getByTransactionId(transactionId);
        // 請求伝票が存在しない場合は処理をスキップする
        if (billingSlipEntity == null) {
            return;
        }

        // 請求チェック
        billingSlipEntityService.checkBilling(billingSlipEntity, billingSlipEntity.getOrderPaymentEntity());
    }
}