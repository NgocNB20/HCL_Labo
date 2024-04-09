/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.sales.repository;

import jp.co.itechh.quad.ddd.domain.sales.entity.SalesSlipForRevisionEntity;
import jp.co.itechh.quad.ddd.domain.sales.valueobject.SalesSlipRevisionId;

/**
 * 改訂用販売伝票集約リポジトリ
 */
public interface ISalesSlipForRevisionRepository {

    /**
     * 改訂用販売伝票登録
     *
     * @param salesSlipForRevisionEntity
     */
    void save(SalesSlipForRevisionEntity salesSlipForRevisionEntity);

    /**
     * 改訂用販売伝票更新
     *
     * @param salesSlipForRevisionEntity
     * @return 更新件数
     */
    int update(SalesSlipForRevisionEntity salesSlipForRevisionEntity);

    /**
     * 改訂用販売伝票取得
     *
     * @param salesSlipRevisionId
     * @return SalesSlipForRevisionEntity
     */
    SalesSlipForRevisionEntity get(SalesSlipRevisionId salesSlipRevisionId);

    /**
     * 改訂用取引IDで改訂用販売伝票取得
     *
     * @param transactionRevisionId
     * @return SalesSlipForRevisionEntity
     */
    SalesSlipForRevisionEntity getByTransactionRevisionId(String transactionRevisionId);

}
