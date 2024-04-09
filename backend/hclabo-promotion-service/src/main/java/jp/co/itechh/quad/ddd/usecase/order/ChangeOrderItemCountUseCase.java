/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.usecase.order;

import jp.co.itechh.quad.ddd.domain.order.entity.ChangeOrderItemCountDomainParam;
import jp.co.itechh.quad.ddd.domain.order.entity.OrderSlipEntity;
import jp.co.itechh.quad.ddd.domain.order.entity.OrderSlipEntityService;
import jp.co.itechh.quad.ddd.domain.order.repository.IOrderSlipRepository;
import jp.co.itechh.quad.ddd.exception.DomainException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 注文商品数量更新ユースケース
 */
@Service
public class ChangeOrderItemCountUseCase {

    /** 注文票ドメインサービス */
    private final OrderSlipEntityService orderSlipEntityService;

    /** 注文票リポジトリ */
    private final IOrderSlipRepository orderSlipRepository;

    /** コンストラクタ */
    @Autowired
    public ChangeOrderItemCountUseCase(OrderSlipEntityService orderSlipEntityService,
                                       IOrderSlipRepository orderSlipRepository) {
        this.orderSlipEntityService = orderSlipEntityService;
        this.orderSlipRepository = orderSlipRepository;
    }

    /**
     * 注文数量を一括変更する
     *
     * @param customerId                顧客ID
     * @param changeOrderCountParamList 注文商品数量変更ユースケースパラメータリスト
     */
    public void changeOrderItem(String customerId, List<ChangeOrderItemCountDomainParam> changeOrderCountParamList) {

        // 顧客IDで下書き注文票を取得する
        OrderSlipEntity orderSlipEntity = this.orderSlipRepository.getDraftOrderSlipByCustomerId(customerId);

        // 下書き注文票が取得できない場合はエラー
        if (orderSlipEntity == null) {
            throw new DomainException("PROMOTION-ODER0011-E", new String[] {customerId});
        }

        // 注文数量変更
        orderSlipEntity.changeOrderItemCount(changeOrderCountParamList);

        // 注文商品をチェック
        this.orderSlipEntityService.checkOrderItem(orderSlipEntity.getOrderItemList(), null);

        // 注文票を更新する
        this.orderSlipRepository.update(orderSlipEntity);

    }

}