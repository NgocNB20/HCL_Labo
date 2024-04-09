/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.transaction;

import jp.co.itechh.quad.ddd.domain.transaction.entity.OrderReceivedEntity;
import jp.co.itechh.quad.ddd.domain.transaction.entity.TransactionEntity;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.OrderStatus;
import lombok.Data;

/**
 * 受注取得ユースケースDto
 */
@Data
public class GetOrderReceivedByTransactionIdUseCaseDto {

    /** 受注Entity */
    private OrderReceivedEntity orderReceivedEntity;

    /** 取引Entity */
    private TransactionEntity transactionEntity;

    /** 注文状況 */
    private OrderStatus orderStatus;

}
