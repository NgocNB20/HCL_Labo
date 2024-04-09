/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.order;

import jp.co.itechh.quad.ddd.domain.order.entity.OrderSlipForRevisionEntity;
import jp.co.itechh.quad.ddd.domain.order.repository.IOrderSlipForRevisionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 改訂用注文票取得ユースケース
 */
@Service
public class GetOrderSlipForRevisionUseCase {

    /** 改訂用注文票リポジトリ */
    private final IOrderSlipForRevisionRepository orderSlipForRevisionRepository;

    /** コンストラクタ */
    @Autowired
    public GetOrderSlipForRevisionUseCase(IOrderSlipForRevisionRepository orderSlipForRevisionRepository) {
        this.orderSlipForRevisionRepository = orderSlipForRevisionRepository;
    }

    /**
     * 取引に紐づく改訂用注文票を取得する
     *
     * @param transactionRevisionId 改訂用取引ID
     * @return 存在する ... 改訂用注文票 / 存在しない ... Null
     */
    public OrderSlipForRevisionEntity getOrderSlipForRevision(String transactionRevisionId) {
        // 改訂用取引IDで改訂用注文票を取得する
        return orderSlipForRevisionRepository.getByTransactionRevisionId(transactionRevisionId);
    }

}
