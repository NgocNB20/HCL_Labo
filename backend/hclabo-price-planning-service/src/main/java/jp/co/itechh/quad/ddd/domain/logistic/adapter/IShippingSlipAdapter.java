/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.logistic.adapter;

import jp.co.itechh.quad.ddd.domain.logistic.adapter.model.ShippingSlip;
import jp.co.itechh.quad.ddd.domain.logistic.adapter.model.ShippingSlipForRevision;

/**
 * 配送伝票アダプター
 */
public interface IShippingSlipAdapter {

    /**
     * 配送伝票取得
     *
     * @param transactionId
     * @return ShippingSlip
     */
    ShippingSlip getShippingSlip(String transactionId);

    /**
     * 改訂用配送伝票取得
     *
     * @param transactionRevisionId
     * @return ShippingSlipForRevision
     */
    ShippingSlipForRevision getShippingSlipForRevision(String transactionRevisionId);
}
