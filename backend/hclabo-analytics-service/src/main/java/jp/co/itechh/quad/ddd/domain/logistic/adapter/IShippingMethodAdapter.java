/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.logistic.adapter;

import jp.co.itechh.quad.ddd.domain.logistic.adapter.model.ShippingMethod;

/**
 * 配送伝票アダプター
 */
public interface IShippingMethodAdapter {

    /**
     * 配送方法取得
     *
     * @param deliveryMethodSeq 配送方法SEQ
     * @return 配送方法
     */
    ShippingMethod getShippingMethod(Integer deliveryMethodSeq);

}