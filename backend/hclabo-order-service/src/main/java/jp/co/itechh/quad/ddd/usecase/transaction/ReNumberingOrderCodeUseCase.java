/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.transaction;

import jp.co.itechh.quad.ddd.domain.transaction.entity.OrderReceivedEntity;
import jp.co.itechh.quad.ddd.domain.transaction.entity.TransactionEntity;
import jp.co.itechh.quad.ddd.domain.transaction.repository.IOrderReceivedRepository;
import jp.co.itechh.quad.ddd.domain.transaction.repository.ITransactionRepository;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.OrderCode;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.OrderCodeFactory;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.TransactionId;
import jp.co.itechh.quad.ddd.exception.DomainException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;

/**
 * 受注番号再発行ユースケース
 */
@Service
public class ReNumberingOrderCodeUseCase {

    /** 受注リポジトリ */
    private final IOrderReceivedRepository orderReceivedRepository;

    /** 取引リポジトリ */
    private final ITransactionRepository transactionRepository;

    /** 受注番号 ファクトリ */
    private final OrderCodeFactory orderCodeFactory;

    /** コンストラクタ */
    @Autowired
    public ReNumberingOrderCodeUseCase(IOrderReceivedRepository orderReceivedRepository,
                                       ITransactionRepository transactionRepository,
                                       OrderCodeFactory orderCodeFactory) {
        this.orderReceivedRepository = orderReceivedRepository;
        this.transactionRepository = transactionRepository;
        this.orderCodeFactory = orderCodeFactory;
    }

    /**
     * 受注番号再発行
     *
     * @param transactionId 取引ID
     * @return orderCode 受注番号
     */
    public String reNumberingOrderCode(String transactionId) {

        // 取引を取得
        TransactionEntity transactionEntity = transactionRepository.get(new TransactionId(transactionId));
        if (transactionEntity == null) {
            throw new DomainException("ORDER-TRAN0007-E", new String[] {transactionId});
        }

        // 受注を取得
        OrderReceivedEntity orderReceivedEntity = orderReceivedRepository.get(transactionEntity.getOrderReceivedId());
        if (orderReceivedEntity == null) {
            throw new DomainException(
                            "ORDER-ODER0002-E", new String[] {transactionEntity.getOrderReceivedId().getValue()});
        }

        // 受注番号再発行
        OrderCode orderCode = orderCodeFactory.constructOrderCode(new Timestamp(new Date().getTime()));

        // 受注番号再設定
        orderReceivedEntity.reSettingOrderCode(orderCode);

        // 受注を保存する
        orderReceivedRepository.updateWithTranCheck(orderReceivedEntity, null);

        return orderCode.getValue();
    }
}