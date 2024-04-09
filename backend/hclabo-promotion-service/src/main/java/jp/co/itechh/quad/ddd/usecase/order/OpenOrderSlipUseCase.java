/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.order;

import jp.co.itechh.quad.ddd.domain.order.entity.OrderSlipEntity;
import jp.co.itechh.quad.ddd.domain.order.repository.IOrderSlipRepository;
import jp.co.itechh.quad.ddd.domain.order.valueobject.OrderStatus;
import jp.co.itechh.quad.ddd.exception.DomainException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 注文票確定ユースケース
 */
@Service
public class OpenOrderSlipUseCase {

    /** 注文票リポジトリ */
    private final IOrderSlipRepository orderSlipRepository;

    /** コンストラクタ */
    @Autowired
    public OpenOrderSlipUseCase(IOrderSlipRepository iOrderSlipRepository) {
        this.orderSlipRepository = iOrderSlipRepository;
    }

    /**
     * 注文票を確定する
     *
     * @param transactionId 取引ID
     * @param userAgent     ユーザーエージェント
     */
    public void openOrderSlip(String transactionId, String userAgent) {

        // 取引IDで下書き注文票を取得する
        OrderSlipEntity orderSlipEntity = this.orderSlipRepository.getDraftOrderSlipByTransactionId(transactionId);

        // 下書き注文票が存在しない、または下書き状態でない場合は処理をスキップする
        if (orderSlipEntity == null || orderSlipEntity.getOrderStatus() != OrderStatus.DRAFT) {
            return;
        }

        // ユーザーエージェントを設定する
        orderSlipEntity.settingUserAgent(userAgent);

        // 取引を確定する
        orderSlipEntity.openSlip();

        // 商品リストを個数単位で分割し、数量をすべて1にする
        orderSlipEntity.itemListDivision();

        // 注文票を更新する
        int count = this.orderSlipRepository.update(orderSlipEntity);
        // 更新に失敗していた場合は排他チェックエラーと見なす
        if (count != 1) {
            throw new DomainException("PROMOTION-ODER0025-E");
        }
    }

}
