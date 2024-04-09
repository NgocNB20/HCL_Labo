/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.transaction.service;

import jp.co.itechh.quad.ddd.domain.logistic.adapter.IShippingSlipAdapter;
import jp.co.itechh.quad.ddd.domain.payment.adapter.IBillingSlipAdapter;
import jp.co.itechh.quad.ddd.domain.priceplanning.adapter.ISalesSlipAdapter;
import jp.co.itechh.quad.ddd.domain.promotion.adapter.IOrderSlipAdapter;
import jp.co.itechh.quad.ddd.domain.transaction.entity.TransactionEntity;
import jp.co.itechh.quad.ddd.domain.transaction.entity.TransactionForRevisionEntity;
import jp.co.itechh.quad.ddd.domain.transaction.repository.ITransactionForRevisionRepository;
import jp.co.itechh.quad.ddd.domain.transaction.repository.ITransactionRepository;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.TransactionId;
import jp.co.itechh.quad.ddd.exception.DomainException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 ＊取引改訂開始ユースケース内部ロジック<br/>
 * ※親トランザクションがある場合の呼び出し用（ユースケースから取引改訂確定ユースケース呼出したい場合に利用）
 *
 */
@Service
public class StartTransactionReviseUseCaseExecuter {

    /** 取引リポジトリ */
    private final ITransactionRepository transactionRepository;

    /** 改訂用取引リポジトリ */
    private final ITransactionForRevisionRepository transactionForRevisionRepository;

    /** 注文票アダプター */
    private final IOrderSlipAdapter orderSlipAdapter;

    /** 配送伝票アダプター */
    private final IShippingSlipAdapter shippingAdapter;

    /** 請求伝票アダプター */
    private final IBillingSlipAdapter billingSlipAdapter;

    /** 販売伝票アダプター */
    private final ISalesSlipAdapter salesSlipAdapter;

    /** コンストラクタ */
    @Autowired
    public StartTransactionReviseUseCaseExecuter(ITransactionRepository transactionRepository,
                                                 ITransactionForRevisionRepository transactionForRevisionRepository,
                                                 IOrderSlipAdapter orderAdapter,
                                                 IShippingSlipAdapter shippingAdapter,
                                                 IBillingSlipAdapter billingAdapter,
                                                 ISalesSlipAdapter salesAdapter) {
        this.transactionRepository = transactionRepository;
        this.transactionForRevisionRepository = transactionForRevisionRepository;
        this.orderSlipAdapter = orderAdapter;
        this.shippingAdapter = shippingAdapter;
        this.billingSlipAdapter = billingAdapter;
        this.salesSlipAdapter = salesAdapter;
    }

    /**
     * 取引改訂を開始する
     *
     * @param transactionId 取引ID
     */
    public String startTransactionReviseInnerLogic(String transactionId) {

        // 取引を取得
        TransactionEntity transactionEntity = transactionRepository.get(new TransactionId(transactionId));
        // 取引が存在しない場合はエラー
        if (transactionEntity == null) {
            throw new DomainException("ORDER-TRAN0007-E", new String[] {transactionId});
        }

        // 登録日時
        Date registDate = new Date();

        // 改訂用取引発行
        TransactionForRevisionEntity transactionForRevisionEntity =
                        new TransactionForRevisionEntity(transactionEntity, registDate);

        // 改訂用注文票発行
        orderSlipAdapter.publishTransactionForRevision(
                        transactionEntity.getTransactionId(), transactionForRevisionEntity.getTransactionRevisionId());

        // 改訂用配送伝票発行
        shippingAdapter.publishShippingSlipForRevision(
                        transactionEntity.getTransactionId(), transactionForRevisionEntity.getTransactionRevisionId());

        // 改訂用請求伝票発行
        this.billingSlipAdapter.publishBillingSlipForRevision(
                        transactionEntity.getTransactionId(), transactionForRevisionEntity.getTransactionRevisionId());

        // 改訂用販売伝票発行
        this.salesSlipAdapter.publishSalesSlipForRevision(
                        transactionEntity.getTransactionId(), transactionForRevisionEntity.getTransactionRevisionId());

        // 改訂用取引を登録する
        transactionForRevisionRepository.save(transactionForRevisionEntity);

        return transactionForRevisionEntity.getTransactionRevisionId().getValue();
    }

}