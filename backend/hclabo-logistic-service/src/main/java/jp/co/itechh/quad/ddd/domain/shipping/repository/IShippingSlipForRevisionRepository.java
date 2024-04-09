/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.shipping.repository;

import jp.co.itechh.quad.ddd.domain.shipping.entity.ShippingSlipForRevisionEntity;
import jp.co.itechh.quad.ddd.domain.shipping.valueobject.ShippingSlipRevisionId;

/**
 * 改訂用配送伝票リポジトリ
 */
public interface IShippingSlipForRevisionRepository {

    /**
     * 改訂用配送伝票登録
     *
     * @param shippingSlipForRevision
     */
    void save(ShippingSlipForRevisionEntity shippingSlipForRevision);

    /**
     * 改訂用配送伝票更新
     *
     * @param shippingSlipForRevision
     * @return 更新件数
     */
    int update(ShippingSlipForRevisionEntity shippingSlipForRevision);

    /**
     * 改訂用配送伝票取得
     *
     * @param shippingSlipRevisionId
     * @return ShippingSlipForRevisionEntity
     */
    ShippingSlipForRevisionEntity get(ShippingSlipRevisionId shippingSlipRevisionId);

    /**
     * 改訂用取引IDで改訂用配送伝票取得
     *
     * @param transactionRevisionId
     * @return ShippingSlipForRevisionEntity
     */
    ShippingSlipForRevisionEntity getByTransactionRevisionId(String transactionRevisionId);

}
