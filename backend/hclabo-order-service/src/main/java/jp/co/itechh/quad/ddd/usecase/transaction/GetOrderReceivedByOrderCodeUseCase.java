/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.transaction;

import jp.co.itechh.quad.ddd.domain.transaction.entity.OrderReceivedEntity;
import jp.co.itechh.quad.ddd.domain.transaction.entity.TransactionEntity;
import jp.co.itechh.quad.ddd.domain.transaction.repository.IOrderReceivedRepository;
import jp.co.itechh.quad.ddd.domain.transaction.repository.ITransactionRepository;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.OrderCode;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.OrderStatusFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 受注番号に紐づく受注取得ユースケース
 */
@Service
public class GetOrderReceivedByOrderCodeUseCase {

    /** 受注リポジトリ */
    private final IOrderReceivedRepository orderReceivedRepository;

    /** 取引リポジトリ */
    private final ITransactionRepository transactionRepository;

    /** コンストラクタ */
    @Autowired
    public GetOrderReceivedByOrderCodeUseCase(IOrderReceivedRepository orderReceivedRepository,
                                              ITransactionRepository transactionRepository) {
        this.orderReceivedRepository = orderReceivedRepository;
        this.transactionRepository = transactionRepository;
    }

    /**
     * 受注番号に紐づく受注取得
     *
     * @param orderCode 受注番号
     * @return 受注取得Dto
     */
    public GetOrderReceivedUseCaseDto getOrderReceivedByOrderCode(String orderCode) {

        // 受注を取得
        OrderReceivedEntity orderReceivedEntity = this.orderReceivedRepository.getByOrderCode(new OrderCode(orderCode));
        if (orderReceivedEntity == null) {
            return null;
        }
        // 取引を取得
        TransactionEntity transactionEntity =
                        this.transactionRepository.get(orderReceivedEntity.getLatestTransactionId());

        // Dtoを返却
        return new GetOrderReceivedUseCaseDto(
                        orderReceivedEntity, transactionEntity, Objects.requireNonNull(
                        OrderStatusFactory.constructOrderStatus(transactionEntity.getTransactionStatus(),
                                                                transactionEntity.isPaidFlag(),
                                                                transactionEntity.isShippedFlag(),
                                                                transactionEntity.isBillPaymentErrorFlag(),
                                                                transactionEntity.isPreClaimFlag()
                                                               )));
    }

}