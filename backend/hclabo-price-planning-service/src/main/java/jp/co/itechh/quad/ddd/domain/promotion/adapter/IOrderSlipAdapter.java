/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.promotion.adapter;

import jp.co.itechh.quad.ddd.domain.promotion.adapter.model.OrderSlip;
import jp.co.itechh.quad.ddd.domain.promotion.adapter.model.OrderSlipForRevision;

/**
 * 注文票アダプター
 */
public interface IOrderSlipAdapter {

    /**
     * トランサクションIDで下書き注文票を取得する
     *
     * @param transactionId
     * @return DraftOrderSlip
     */
    OrderSlip getDraftOrderSlipByTransactionId(String transactionId);

    /**
     * 注文票IDで下書き注文票を取得する
     *
     * @param orderSlipId
     * @return DraftOrderSlip
     */
    OrderSlip getDraftOrderSlipById(String orderSlipId);

    /**
     * トランサクションIDで注文票を取得する
     *
     * @param transactionId
     * @return OrderSlip
     */
    OrderSlip getOrderSlipByTransactionId(String transactionId);

    /**
     * 改訂用トランサクションIDで改訂用注文票を取得する
     *
     * @param transactionRevisionId
     * @return OrderSlipForRevision
     */
    OrderSlipForRevision getOrderSlipByTransactionRevisionId(String transactionRevisionId);
}
