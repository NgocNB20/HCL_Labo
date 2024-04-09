/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.shipping.repository;

import jp.co.itechh.quad.ddd.domain.shipping.entity.ShippingSlipEntity;
import jp.co.itechh.quad.ddd.domain.shipping.valueobject.ShippingSlipId;

import java.sql.Timestamp;
import java.util.List;

/**
 * 配送伝票リポジトリ
 */
public interface IShippingSlipRepository {

    /**
     * 配送伝票登録
     *
     * @param shippingSlipEntity 配送伝票
     */
    void save(ShippingSlipEntity shippingSlipEntity);

    /**
     * 配送伝票更新
     *
     * @param shippingSlipEntity 配送伝票
     * @return 更新件数
     */
    int update(ShippingSlipEntity shippingSlipEntity);

    /**
     * 配送伝票取得
     *
     * @param shippingSlipId 配送伝票ID
     * @return ShippingSlipEntity 配送伝票
     */
    ShippingSlipEntity get(ShippingSlipId shippingSlipId);

    /**
     * 取引IDで配送伝票取得
     *
     * @param transactionId 取引ID
     * @return ShippingSlipEntity 配送伝票
     */
    ShippingSlipEntity getByTransactionId(String transactionId);

    /**
     * 一定期間が経過した在庫確保状態の配送伝票リスト取得
     *
     * @param thresholdTime 期間閾値
     * @return ShippingSlipEntityList 配送伝票
     */
    List<ShippingSlipEntity> getSecuredInventoryShippingSlipListTargetElapsedPeriod(Timestamp thresholdTime);

    /**
     * 不要データを削除する
     *
     * @param transactionId 取引ID
     * @return 更新件数
     */
    int deleteUnnecessaryByTransactionId(String transactionId);
}