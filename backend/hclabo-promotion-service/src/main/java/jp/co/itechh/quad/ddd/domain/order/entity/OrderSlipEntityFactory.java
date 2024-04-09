/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.order.entity;

import jp.co.itechh.quad.ddd.domain.order.valueobject.OrderStatus;
import jp.co.itechh.quad.ddd.exception.DomainException;

import java.util.Date;

/**
 * 注文票エンティティファクトリ
 */
public class OrderSlipEntityFactory {

    /**
     * 注文票発行
     *
     * @param orderSlipEntity 注文票エンティティ
     */
    public static OrderSlipEntity orderSlipEntity(OrderSlipEntity orderSlipEntity, String customerId) {

        // 指定された顧客IDで、既に下書き状態の注文票が存在している場合はエラー
        if (orderSlipEntity != null && orderSlipEntity.getOrderStatus() == OrderStatus.DRAFT) {
            throw new DomainException("PROMOTION-ODER0001-E");
        }

        // 注文票発行
        OrderSlipEntity newOderSlipEntity = new OrderSlipEntity(customerId, new Date());

        return newOderSlipEntity;
    }

}
