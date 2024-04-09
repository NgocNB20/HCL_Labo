/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.logistic.adapter;

import jp.co.itechh.quad.ddd.domain.logistic.adapter.model.ShippingSlip;
import jp.co.itechh.quad.ddd.domain.logistic.adapter.model.ShippingSlipForRevision;

/**
 * 配送アダプター
 */
public interface IShippingAdapter {

    /**
     * 物流マイクロサービス<br/>
     * 配送伝票取得
     *
     * @param transactionId 取引ID
     * @return ShippingSlip 配送伝票
     */
    ShippingSlip getShippingSlip(String transactionId);

    /**
     * 物流マイクロサービス<br/>
     * 配送伝票取得
     *
     * @param transactionRevisionId 改訂用取引ID
     * @return ShippingSlip 改訂用配送伝票
     */
    ShippingSlipForRevision getShippingSlipForRevision(String transactionRevisionId);

}
