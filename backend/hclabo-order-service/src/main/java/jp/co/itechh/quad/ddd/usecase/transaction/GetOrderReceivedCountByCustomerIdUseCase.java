/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.transaction;

import jp.co.itechh.quad.ddd.domain.transaction.repository.IOrderReceivedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 顧客ごとの受注件数取得ユースケース
 */
@Service
public class GetOrderReceivedCountByCustomerIdUseCase {

    /** 受注リポジトリ */
    private final IOrderReceivedRepository orderReceivedRepository;

    /** コンストラクタ */
    @Autowired
    public GetOrderReceivedCountByCustomerIdUseCase(IOrderReceivedRepository orderReceivedRepository) {
        this.orderReceivedRepository = orderReceivedRepository;
    }

    /**
     * 顧客ごとの受注件数取得
     * @param customerId 顧客ID
     * @return 顧客ごとの受注件数
     */
    public int getOrderReceivedCountByCustomerId(String customerId) {
        return this.orderReceivedRepository.getOrderReceivedCountByCustomerId(customerId);
    }
}