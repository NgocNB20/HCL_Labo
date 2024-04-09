/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.order;

import jp.co.itechh.quad.ddd.domain.order.entity.OrderSlipEntity;
import jp.co.itechh.quad.ddd.domain.order.entity.OrderSlipForRevisionEntity;
import jp.co.itechh.quad.ddd.domain.order.repository.IOrderSlipForRevisionRepository;
import jp.co.itechh.quad.ddd.domain.order.repository.IOrderSlipRepository;
import jp.co.itechh.quad.ddd.exception.DomainException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 改訂用注文票確定ユースケース
 */
@Service
public class OpenOrderSlipForRevisionUseCase {

    /** 注文票リポジトリ */
    private final IOrderSlipRepository orderSlipRepository;

    /** 改訂用注文票リポジトリ */
    private final IOrderSlipForRevisionRepository orderSlipForRevisionRepository;

    /** コンストラクタ */
    @Autowired
    public OpenOrderSlipForRevisionUseCase(IOrderSlipRepository iOrderSlipRepository,
                                           IOrderSlipForRevisionRepository orderSlipForRevisionRepository) {
        this.orderSlipRepository = iOrderSlipRepository;
        this.orderSlipForRevisionRepository = orderSlipForRevisionRepository;
    }

    /**
     * 改訂用注文票を確定する
     *
     * @param transactionRevisionId 改訂用取引ID
     */
    public void openOrderSlipForRevision(String transactionRevisionId) {

        // 取引IDで注文票取得
        OrderSlipEntity orderSlipEntity = this.orderSlipRepository.getOrderSlipByTransactionId(transactionRevisionId);

        // 検索結果があれば、処理終了
        if (orderSlipEntity != null) {
            return;
        }

        // 改訂用取引IDで改訂用注文票を取得する
        OrderSlipForRevisionEntity orderSlipForRevisionEntity =
                        orderSlipForRevisionRepository.getByTransactionRevisionId(transactionRevisionId);

        // 改訂用注文票が取得できない場合はエラー
        if (orderSlipForRevisionEntity == null) {
            throw new DomainException("PROMOTION-ODER0007-E", new String[] {transactionRevisionId});
        }

        // 注文票発行
        OrderSlipEntity openOrderSlipEntity = new OrderSlipEntity(orderSlipForRevisionEntity);

        // ユーザーエージェントを設定する
        OrderSlipEntity oldOrderSlipEntity = this.orderSlipRepository.getOrderSlipByTransactionId(
                        orderSlipForRevisionEntity.getTransactionId());
        openOrderSlipEntity.settingUserAgent(oldOrderSlipEntity.getUserAgent());

        // 注文票を登録する
        this.orderSlipRepository.save(openOrderSlipEntity);
    }

}