/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.billing.repository;

import jp.co.itechh.quad.ddd.domain.billing.entity.BillingSlipForRevisionEntity;

/**
 * 改訂用請求伝票リポジトリ
 */
public interface IBillingSlipForRevisionRepository {

    /**
     * 改訂用請求伝票登録
     *
     * @param billingSlipForRevisionEntity 改訂用請求伝票
     */
    void save(BillingSlipForRevisionEntity billingSlipForRevisionEntity);

    /**
     * 改訂用請求伝票更新
     *
     * @param billingSlipForRevisionEntity 改訂用請求伝票
     * @return 更新件数
     */
    int update(BillingSlipForRevisionEntity billingSlipForRevisionEntity);

    /**
     * 改訂用取引IDで改訂用請求伝票取得
     *
     * @param transactionRevisionId 改訂用取引ID
     * @return BillingSlipForRevisionEntity 改訂用請求伝票
     */
    BillingSlipForRevisionEntity getByTransactionRevisionId(String transactionRevisionId);

}
