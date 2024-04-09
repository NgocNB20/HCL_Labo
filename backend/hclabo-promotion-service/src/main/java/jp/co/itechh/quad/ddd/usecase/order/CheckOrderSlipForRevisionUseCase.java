/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.order;

import jp.co.itechh.quad.ddd.domain.order.entity.OrderSlipForRevisionEntity;
import jp.co.itechh.quad.ddd.domain.order.entity.OrderSlipForRevisionEntityService;
import jp.co.itechh.quad.ddd.domain.order.repository.IOrderSlipForRevisionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 改訂用注文票チェックユースケース
 */
@Service
public class CheckOrderSlipForRevisionUseCase {

    /** 改訂用注文票リポジトリ */
    private final IOrderSlipForRevisionRepository shippingSlipForRevisionRepository;

    /** 改訂用注文票ドメインサービス */
    private final OrderSlipForRevisionEntityService orderSlipForRevisionEntityService;

    /** コンストラクタ */
    @Autowired
    public CheckOrderSlipForRevisionUseCase(IOrderSlipForRevisionRepository shippingSlipForRevisionRepository,
                                            OrderSlipForRevisionEntityService orderSlipForRevisionEntityService) {
        this.shippingSlipForRevisionRepository = shippingSlipForRevisionRepository;
        this.orderSlipForRevisionEntityService = orderSlipForRevisionEntityService;
    }

    /**
     * 改訂用注文票をチェックする
     *
     * @param transactionRevisionId
     */
    public void checkOrderSlipForRevision(String transactionRevisionId) {

        // 改訂用取引IDに紐づく改訂用注文票を取得する
        OrderSlipForRevisionEntity orderSlipForRevisionEntity =
                        shippingSlipForRevisionRepository.getByTransactionRevisionId(transactionRevisionId);
        // 注文票が存在しない場合は処理をスキップする
        if (orderSlipForRevisionEntity == null) {
            return;
        }

        // 改訂用注文票チェック
        orderSlipForRevisionEntityService.checkOrderSlipForRevision(orderSlipForRevisionEntity);
    }

}