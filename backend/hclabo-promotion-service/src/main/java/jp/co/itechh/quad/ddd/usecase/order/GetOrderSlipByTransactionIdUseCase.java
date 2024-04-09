/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.order;

import jp.co.itechh.quad.ddd.domain.order.entity.OrderSlipEntity;
import jp.co.itechh.quad.ddd.domain.order.repository.IOrderSlipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 取引に紐づく注文票取得ユースケース
 */
@Service
public class GetOrderSlipByTransactionIdUseCase {

    /** 注文票リポジトリ */
    private final IOrderSlipRepository orderSlipRepository;

    /** コンストラクタ */
    @Autowired
    public GetOrderSlipByTransactionIdUseCase(IOrderSlipRepository OrderSlipRepository) {
        this.orderSlipRepository = OrderSlipRepository;
    }

    /**
     * 取引IDに紐づく注文票を取得する
     *
     * @param transactionId 取引ID
     * @return 存在する ... 注文票 / 存在しない ... Null
     */
    public OrderSlipEntity getOrderSlipByTransactionId(String transactionId) {

        // 取引IDに紐づく注文票を取得する
        OrderSlipEntity OrderSlipEntity = this.orderSlipRepository.getOrderSlipByTransactionId(transactionId);

        // 注文票が取得できなかった場合、nullを返却する
        return OrderSlipEntity;
    }

}
