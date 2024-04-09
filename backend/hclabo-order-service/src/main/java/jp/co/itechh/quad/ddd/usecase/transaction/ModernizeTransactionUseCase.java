/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.transaction;

import jp.co.itechh.quad.ddd.domain.logistic.adapter.IShippingSlipAdapter;
import jp.co.itechh.quad.ddd.domain.payment.adapter.IBillingSlipAdapter;
import jp.co.itechh.quad.ddd.domain.priceplanning.adapter.ISalesSlipAdapter;
import jp.co.itechh.quad.ddd.domain.promotion.adapter.IOrderSlipAdapter;
import jp.co.itechh.quad.ddd.domain.transaction.entity.TransactionEntity;
import jp.co.itechh.quad.ddd.domain.transaction.repository.ITransactionRepository;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.TransactionId;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.TransactionStatus;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 取引全体最新化ユースケース
 */
@Service
public class ModernizeTransactionUseCase {

    /** 取引リポジトリ */
    private final ITransactionRepository transactionRepository;

    /** 注文票アダプター */
    private final IOrderSlipAdapter orderSlipAdapter;

    /** 配送アダプター */
    private final IShippingSlipAdapter shippingAdapter;

    /** 販売アダプター */
    private final ISalesSlipAdapter salesAdapter;

    /** 請求アダプター */
    private final IBillingSlipAdapter billingAdapter;

    /** コンストラクタ */
    @Autowired
    public ModernizeTransactionUseCase(ITransactionRepository transactionRepository,
                                       IOrderSlipAdapter orderSlipAdapter,
                                       IShippingSlipAdapter shippingAdapter,
                                       ISalesSlipAdapter salesAdapter,
                                       IBillingSlipAdapter billingAdapter) {
        this.transactionRepository = transactionRepository;
        this.orderSlipAdapter = orderSlipAdapter;
        this.shippingAdapter = shippingAdapter;
        this.salesAdapter = salesAdapter;
        this.billingAdapter = billingAdapter;
    }

    /**
     * 取引全体を最新化する
     *
     * @return transactionId 取引ID
     */
    public void modernizeTransaction(String transactionId) {

        // 取引を取得
        TransactionEntity transactionEntity = this.transactionRepository.get(new TransactionId(transactionId));
        // 取引が存在しない または　ステータスが下書きではない場合は処理をスキップする
        if (ObjectUtils.isEmpty(transactionEntity)
            || transactionEntity.getTransactionStatus() != TransactionStatus.DRAFT) {
            return;
        }

        // 販売伝票を最新化する（販売企画サービス）
        salesAdapter.modernizeSalesSlip(new TransactionId(transactionId));

        // 注文票を最新化する（プロモーションサービス）
        orderSlipAdapter.modernizeOrderSlip(new TransactionId(transactionId));

        // 配送伝票を最新化する（物流サービス）
        shippingAdapter.modernizeShippingSlip(new TransactionId(transactionId));

        // 請求伝票を最新化する（決済サービス）
        billingAdapter.modernizeBillingSlip(new TransactionId(transactionId));
    }
}