/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.payment.adapter;

import jp.co.itechh.quad.ddd.domain.payment.adapter.model.BillingSlip;
import jp.co.itechh.quad.ddd.domain.payment.adapter.model.BillingSlipForRevision;

/**
 * 請求アダプター
 */
public interface IBillingAdapter {

    /**
     * 請求伝票取得
     * ※注文決済情報含む
     *
     * @param transactionId 取引ID
     * @return BillingSlip
     */
    BillingSlip getBillingSlip(String transactionId);

    /**
     * 改訂用請求伝票取得
     * ※注文決済情報含む
     *
     * @param transactionRevisionId 改訂用取引ID
     * @return BillingSlipForRevision
     */
    BillingSlipForRevision getBillingSlipForRevision(String transactionRevisionId);
}
