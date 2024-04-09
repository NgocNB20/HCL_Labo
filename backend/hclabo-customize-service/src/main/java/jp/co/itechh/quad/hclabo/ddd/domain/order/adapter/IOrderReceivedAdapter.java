/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.hclabo.ddd.domain.order.adapter;

import jp.co.itechh.quad.hclabo.ddd.domain.order.adapter.model.OrderReceived;

/**
 * 受注アダプター
 */
public interface IOrderReceivedAdapter {

    /**
     * 受注情報取得
     *
     * @param orderCode
     * @return OrderReceived
     */
    OrderReceived getByOrderCode(String orderCode);
}
