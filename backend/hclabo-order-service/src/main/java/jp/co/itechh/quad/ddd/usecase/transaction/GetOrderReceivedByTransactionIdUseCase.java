/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.transaction;

import jp.co.itechh.quad.ddd.domain.transaction.entity.OrderReceivedEntity;
import jp.co.itechh.quad.ddd.domain.transaction.entity.TransactionEntity;
import jp.co.itechh.quad.ddd.domain.transaction.repository.IOrderReceivedRepository;
import jp.co.itechh.quad.ddd.domain.transaction.repository.ITransactionRepository;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.OrderStatusFactory;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.TransactionId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 取引にひもづく受注取得ユースケース
 */
@Service
public class GetOrderReceivedByTransactionIdUseCase {

    /** 受注リポジトリ */
    private final IOrderReceivedRepository orderReceivedRepository;

    /** 取引リポジトリ */
    private final ITransactionRepository transactionRepository;

    /** コンストラクタ */
    @Autowired
    public GetOrderReceivedByTransactionIdUseCase(IOrderReceivedRepository orderReceivedRepository,
                                                  ITransactionRepository transactionRepository) {
        this.orderReceivedRepository = orderReceivedRepository;
        this.transactionRepository = transactionRepository;
    }

    /**
     * 取引にひもづく受注取得
     *
     * @param transactionId 取引ID
     * @return TransactionForRevisionEntity
     */
    public GetOrderReceivedByTransactionIdUseCaseDto getOrderReceivedByTransactionId(String transactionId) {

        // 取引を取得
        TransactionEntity transactionEntity = this.transactionRepository.get(new TransactionId(transactionId));
        if (transactionEntity == null) {
            return null;
        }

        // 受注を取得
        OrderReceivedEntity orderReceivedEntity =
                        this.orderReceivedRepository.get(transactionEntity.getOrderReceivedId());
        if (orderReceivedEntity == null) {
            return null;
        }

        GetOrderReceivedByTransactionIdUseCaseDto getOrderReceivedByTransactionIdUseCaseDto =
                        new GetOrderReceivedByTransactionIdUseCaseDto();
        getOrderReceivedByTransactionIdUseCaseDto.setOrderReceivedEntity(orderReceivedEntity);
        getOrderReceivedByTransactionIdUseCaseDto.setTransactionEntity(transactionEntity);
        getOrderReceivedByTransactionIdUseCaseDto.setOrderStatus(
                        OrderStatusFactory.constructOrderStatus(transactionEntity.getTransactionStatus(),
                                                                transactionEntity.isPaidFlag(),
                                                                transactionEntity.isShippedFlag(),
                                                                transactionEntity.isBillPaymentErrorFlag(),
                                                                transactionEntity.isPreClaimFlag()
                                                               ));

        // Dtoを返却
        return getOrderReceivedByTransactionIdUseCaseDto;
    }
}