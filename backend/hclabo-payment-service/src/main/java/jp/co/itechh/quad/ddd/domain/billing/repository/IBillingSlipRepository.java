/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.billing.repository;

import jp.co.itechh.quad.ddd.domain.billing.entity.BillingSlipEntity;
import jp.co.itechh.quad.ddd.infrastructure.billing.dbentity.OrderPaymentDbEntity;

/**
 * 請求伝票リポジトリ
 */
public interface IBillingSlipRepository {

    /**
     * 請求伝票登録
     *
     * @param billingSlipEntity 請求伝票
     */
    void save(BillingSlipEntity billingSlipEntity);

    /**
     * 請求伝票更新
     *
     * @param billingSlipEntity 請求伝票
     * @return 更新件数
     */
    int update(BillingSlipEntity billingSlipEntity);

    /**
     * 取引IDで請求伝票取得
     *
     * @param transactionId 取引ID
     * @return BillingSlipEntity 請求伝票
     */
    BillingSlipEntity getByTransactionId(String transactionId);

    /**
     * 注文決済登録
     *
     * @param orderPaymentDbEntity 注文決済DbEntityクラス
     */
    void saveOrderPayment(OrderPaymentDbEntity orderPaymentDbEntity);

    /**
     * 不要確定データを削除する
     *
     * @param transactionId 取引ID
     * @return 更新件数
     */
    int deleteUnnecessaryByTransactionId(String transactionId);
}
