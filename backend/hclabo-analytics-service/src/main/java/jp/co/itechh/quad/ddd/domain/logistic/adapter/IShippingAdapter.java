/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.logistic.adapter;

import jp.co.itechh.quad.ddd.domain.logistic.adapter.model.ShippingSlip;

/**
 * 配送伝票アダプター
 */
public interface IShippingAdapter {

    /**
     * 配送伝票取得
     *
     * @param transactionId 取引ID
     * @return 配送伝票
     */
    ShippingSlip getShippingSlip(String transactionId);

}
