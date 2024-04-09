/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.promotion.adapter;

import jp.co.itechh.quad.ddd.domain.promotion.adapter.model.OrderSlip;

/**
 * 注文アダプター<br/>
 * プロモーションマイクロサービス
 */
public interface IOrderSlipAdapter {

    /**
     * 下書き注文票取得
     *
     * @param transactionId 取引ID
     * @return OrderSlip 注文票
     */
    OrderSlip getDraftOrderSlip(String transactionId);

    /**
     * 改訂用注文票取得
     *
     * @param transactionRevisionId 取引ID
     * @return OrderSlip 改訂用注文票
     */
    OrderSlip getOrderSlipForRevision(String transactionRevisionId);

    /**
     * 注文票取得
     *
     * @param transactionId 取引ID
     * @return OrderSlip 改訂用注文票
     */
    OrderSlip getOrderSlip(String transactionId);
}
