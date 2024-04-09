/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.billing;

import jp.co.itechh.quad.ddd.domain.billing.entity.BillingSlipEntity;
import jp.co.itechh.quad.ddd.domain.billing.entity.OrderPaymentEntityService;
import jp.co.itechh.quad.ddd.domain.billing.repository.IBillingSlipRepository;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 請求伝票最新化ユースケース
 */
@Service
public class ModernizeBillingSlipUseCase {

    /** 請求伝票リポジトリ */
    private final IBillingSlipRepository billingSlipRepository;

    /** 注文決済ドメインサービス */
    private final OrderPaymentEntityService orderPaymentService;

    /** コンストラクタ */
    @Autowired
    public ModernizeBillingSlipUseCase(IBillingSlipRepository billingSlipRepository,
                                       OrderPaymentEntityService orderPaymentService) {
        this.billingSlipRepository = billingSlipRepository;
        this.orderPaymentService = orderPaymentService;
    }

    /**
     * 請求伝票を最新化する
     *
     * @param transactionId 取引ID
     */
    public void modernizeBillingSlip(String transactionId) {

        // 取引IDに紐づく請求伝票を取得する
        BillingSlipEntity billingSlipEntity = billingSlipRepository.getByTransactionId(transactionId);
        // 請求伝票が存在しない または　注文決済が存在しない場合は処理をスキップする
        if (ObjectUtils.isEmpty(billingSlipEntity) || ObjectUtils.isEmpty(billingSlipEntity.getOrderPaymentEntity())) {
            return;
        }
        // 決済方法が未設定の場合は処理をスキップする
        if (StringUtils.isEmpty(billingSlipEntity.getOrderPaymentEntity().getPaymentMethodId())) {
            return;
        }

        // 注文決済のトランザクションデータを最新化
        orderPaymentService.modernizeOrderPayment(billingSlipEntity.getOrderPaymentEntity());

        // 請求伝票更新
        billingSlipRepository.update(billingSlipEntity);
    }
}