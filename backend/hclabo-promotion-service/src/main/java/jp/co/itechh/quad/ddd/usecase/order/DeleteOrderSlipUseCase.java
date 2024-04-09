/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.order;

import jp.co.itechh.quad.ddd.domain.order.entity.OrderSlipEntity;
import jp.co.itechh.quad.ddd.domain.order.repository.IOrderSlipRepository;
import jp.co.itechh.quad.ddd.exception.AssertChecker;
import jp.co.itechh.quad.ddd.exception.DomainException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 注文商品削除ユースケース
 */
@Service
public class DeleteOrderSlipUseCase {

    /** 注文票リポジトリ */
    private final IOrderSlipRepository orderSlipRepository;

    /** コンストラクタ */
    @Autowired
    public DeleteOrderSlipUseCase(IOrderSlipRepository orderSlipRepository) {
        this.orderSlipRepository = orderSlipRepository;
    }

    /**
     * 注文商品を削除する
     *
     * @param customerId 顧客ID
     * @param itemId     商品ID
     */
    public void deleteOrderItem(String customerId, String itemId) {

        // 顧客IDで下書き注文票を取得する
        OrderSlipEntity orderSlipEntity = this.orderSlipRepository.getDraftOrderSlipByCustomerId(customerId);

        // 下書き注文票が取得できない場合はエラー
        if (orderSlipEntity == null) {
            throw new DomainException("PROMOTION-ODER0011-E", new String[] {customerId});
        }

        // 注文商品を削除する
        orderSlipEntity.deleteOrderItem(itemId);

        // 注文票を更新する
        this.orderSlipRepository.update(orderSlipEntity);
    }

    /**
     * 注文票票除する
     *
     * @param transactionId 取引ID
     */
    public void deleteUnnecessaryByTransactionId(String transactionId) {

        AssertChecker.assertNotEmpty("transactionId is empty", transactionId);

        // 注文票削除
        this.orderSlipRepository.deleteUnnecessaryByTransactionId(transactionId);
    }
}