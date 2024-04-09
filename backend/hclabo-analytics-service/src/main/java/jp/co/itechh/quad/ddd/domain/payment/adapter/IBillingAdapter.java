/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.payment.adapter;

import jp.co.itechh.quad.ddd.domain.payment.adapter.model.BillingSlip;

/**
 * 請求アダプター
 */
public interface IBillingAdapter {

    /**
     * 請求伝票取得
     * ※注文決済情報含む
     *
     * @param transactionId 取引ID
     * @return 請求伝票
     */
    BillingSlip getBillingSlip(String transactionId);
}
