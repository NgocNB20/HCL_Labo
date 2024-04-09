/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.transaction;

import jp.co.itechh.quad.ddd.domain.logistic.adapter.IShippingSlipAdapter;
import jp.co.itechh.quad.ddd.domain.payment.adapter.IBillingSlipAdapter;
import jp.co.itechh.quad.ddd.domain.priceplanning.adapter.ISalesSlipAdapter;
import jp.co.itechh.quad.ddd.domain.promotion.adapter.IOrderSlipAdapter;
import jp.co.itechh.quad.ddd.domain.transaction.entity.OrderReceivedEntity;
import jp.co.itechh.quad.ddd.domain.transaction.entity.TransactionEntity;
import jp.co.itechh.quad.ddd.domain.transaction.repository.IOrderReceivedRepository;
import jp.co.itechh.quad.ddd.domain.transaction.repository.ITransactionRepository;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.OrderCode;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.OrderCodeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;

/**
 * 取引開始ユースケース
 */
@Service
public class StartTransactionUseCase {

    /** 受注番号 ファクトリ */
    private final OrderCodeFactory orderCodeFactory;

    /** 取引リポジトリ */
    private final ITransactionRepository transactionRepository;

    /** 受注リポジトリ */
    private final IOrderReceivedRepository orderReceivedRepository;

    /** 注文アダプター */
    private final IOrderSlipAdapter orderAdapter;

    /** 配送アダプター */
    private final IShippingSlipAdapter shippingAdapter;

    /** 請求アダプター */
    private final IBillingSlipAdapter billingAdapter;

    /** 販売アダプター */
    private final ISalesSlipAdapter salesAdapter;

    /** コンストラクタ */
    @Autowired
    public StartTransactionUseCase(OrderCodeFactory orderCodeFactory,
                                   ITransactionRepository iTransactionRepository,
                                   IOrderReceivedRepository orderReceivedRepository,
                                   IOrderSlipAdapter orderAdapter,
                                   IShippingSlipAdapter shippingAdapter,
                                   IBillingSlipAdapter billingAdapter,
                                   ISalesSlipAdapter salesAdapter) {
        this.orderCodeFactory = orderCodeFactory;
        this.transactionRepository = iTransactionRepository;
        this.orderReceivedRepository = orderReceivedRepository;
        this.orderAdapter = orderAdapter;
        this.shippingAdapter = shippingAdapter;
        this.billingAdapter = billingAdapter;
        this.salesAdapter = salesAdapter;
    }

    /**
     * 取引を開始する
     *
     * @param customerId       顧客ID
     * @param customerBirthday 顧客生年月日
     * @param addressId        住所ID
     * @return transactionId 取引ID
     */
    public String startTransaction(String customerId, Date customerBirthday, String addressId) {

        // 登録日時
        Date registDate = new Date();

        // 受注コード
        OrderCode orderCode = orderCodeFactory.constructOrderCode(new Timestamp(registDate.getTime()));

        // 受注作成
        OrderReceivedEntity orderReceivedEntity = new OrderReceivedEntity(registDate, orderCode);

        // 取引を開始する
        TransactionEntity transactionEntity =
                        new TransactionEntity(customerId, orderReceivedEntity.getOrderReceivedId(), registDate);

        // ***ここから、各サービスの取引処理を下記の順番で実行***
        // ①：注文票の取引を開始する（プロモーションサービス）
        this.orderAdapter.startTransaction(transactionEntity.getTransactionId(), customerBirthday);

        // ②：配送伝票を発行する（物流サービス）
        this.shippingAdapter.publishShippingSlip(transactionEntity.getTransactionId());

        // ③：請求伝票を発行する（決済サービス）
        this.billingAdapter.publishBillingSlip(transactionEntity.getTransactionId(), orderCode, addressId);

        // ④：販売伝票を発行する（販売企画サービス）
        this.salesAdapter.publishSalesSlip(transactionEntity.getTransactionId());
        // ***ここまで、各サービスの取引処理***

        // 受注を登録する
        orderReceivedRepository.save(orderReceivedEntity);

        // 取引を登録する
        transactionRepository.save(transactionEntity);

        return transactionEntity.getTransactionId().getValue();
    }
}