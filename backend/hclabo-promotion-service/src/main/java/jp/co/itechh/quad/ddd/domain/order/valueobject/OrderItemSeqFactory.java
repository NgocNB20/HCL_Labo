/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.order.valueobject;

import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 注文商品連番 値オブジェクト ファクトリ
 */
public class OrderItemSeqFactory {

    /**
     * 注文商品連番 値オブジェクト 生成
     *
     * @param orderItemList
     */
    public static OrderItemSeq constructOrderItemSeq(List<OrderItem> orderItemList) {

        int orderItemSeqVal = 0;

        if (CollectionUtils.isEmpty(orderItemList)) {
            return new OrderItemSeq(orderItemSeqVal);
        }

        // 注文商品連番の最大値を設定
        for (OrderItem orderItem : orderItemList) {
            if (orderItemSeqVal < orderItem.getOrderItemSeq().getValue()) {
                orderItemSeqVal = orderItem.getOrderItemSeq().getValue();
            }
        }

        return new OrderItemSeq(++orderItemSeqVal);
    }
}
