/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.transaction;

import jp.co.itechh.quad.ddd.domain.transaction.entity.OrderReceivedEntity;
import jp.co.itechh.quad.ddd.domain.transaction.entity.TransactionEntity;
import jp.co.itechh.quad.ddd.domain.transaction.valueobject.OrderStatus;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * 受注取得ユースケースDto
 */
@Getter
@RequiredArgsConstructor
public class GetOrderReceivedUseCaseDto {

    /** 受注Entity */
    @NonNull
    private OrderReceivedEntity orderReceivedEntity;

    /** 取引Entity */
    @NonNull
    private TransactionEntity transactionEntity;

    /** 注文状況 */
    @NonNull
    private OrderStatus orderStatus;

}
