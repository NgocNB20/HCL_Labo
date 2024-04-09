/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.promotion.adapter;

import jp.co.itechh.quad.ddd.domain.promotion.adapter.model.OrderSlip;

/**
 * 注文アダプター
 * プロモーションマイクロサービス
 */
public interface IOrderSlipAdapter {

    /**
     * 注文票取得
     *
     * @param transactionId 取引ID
     * @return 注文票
     */
    OrderSlip getOrderSlipByTransactionId(String transactionId);

}
