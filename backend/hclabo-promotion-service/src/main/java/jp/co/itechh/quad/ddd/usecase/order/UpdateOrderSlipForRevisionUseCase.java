/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.order;

import jp.co.itechh.quad.ddd.domain.order.entity.OrderSlipForRevisionEntity;
import jp.co.itechh.quad.ddd.domain.order.repository.IOrderSlipForRevisionRepository;
import jp.co.itechh.quad.ddd.exception.DomainException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 改訂用注文票(商品一括)更新ユースケース
 */
@Service
public class UpdateOrderSlipForRevisionUseCase {

    /** 改訂用注文票リポジトリ */
    private final IOrderSlipForRevisionRepository orderSlipForRevisionRepository;

    /** コンストラクタ */
    @Autowired
    public UpdateOrderSlipForRevisionUseCase(IOrderSlipForRevisionRepository orderSlipForRevisionRepository) {
        this.orderSlipForRevisionRepository = orderSlipForRevisionRepository;
    }

    /**
     * 改訂用注文票の商品を一括更新する
     *
     * @param transactionRevisionId 改訂用取引ID
     * @param changeItemList        改訂用注文商品数量変更ユースケースパラメータリスト
     */
    public void updateOrderSlipForRevision(String transactionRevisionId,
                                           List<ChangeOrderItemCountForRevisionUseCaseParam> changeItemList) {

        // 改訂用取引IDで改訂用注文票を取得する
        OrderSlipForRevisionEntity orderSlipForRevisionEntity =
                        orderSlipForRevisionRepository.getByTransactionRevisionId(transactionRevisionId);
        // 改訂用注文票が取得できない場合はエラー
        if (orderSlipForRevisionEntity == null) {
            throw new DomainException("PROMOTION-ODER0007-E", new String[] {transactionRevisionId});
        }

        // 改訂用注文商品数量一括変更
        orderSlipForRevisionEntity.changeOrderItemRevision(changeItemList);

        // 改訂用注文票を更新する
        orderSlipForRevisionRepository.update(orderSlipForRevisionEntity);
    }

}