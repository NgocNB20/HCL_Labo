/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.infrastructure.shipping.dao;

import jp.co.itechh.quad.ddd.infrastructure.shipping.dbentity.ShippingSlipDbEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;

import java.sql.Timestamp;
import java.util.List;

/**
 * 配送伝票Daoクラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Dao
@ConfigAutowireable
public interface ShippingSlipDao {

    /**
     * インサート
     *
     * @param shippingSlipDbEntity 配送伝票DBエンティティ
     * @return 処理件数
     */
    @Insert(excludeNull = true)
    int insert(ShippingSlipDbEntity shippingSlipDbEntity);

    /**
     * アップデート
     *
     * @param shippingSlipDbEntity 配送伝票DBエンティティ
     * @return 処理件数
     */
    @Update
    int update(ShippingSlipDbEntity shippingSlipDbEntity);

    /**
     * 配送伝票IDで配送伝票DBエンティティ取得
     *
     * @param shippingSlipId 配送伝票ID
     * @return 配送伝票DBエンティティ
     */
    @Select
    ShippingSlipDbEntity getByShippingSlipId(String shippingSlipId);

    /**
     * 取引IDで配送伝票DBエンティティ取得
     *
     * @param transactionId 取引ID
     * @return 配送伝票DBエンティティ
     */
    @Select
    ShippingSlipDbEntity getByTransactionId(String transactionId);

    /**
     * 一定期間が経過した在庫確保状態の配送伝票リスト取得
     *
     * @param thresholdTime 期間閾値
     * @return ShippingSlipEntityList 配送伝票
     */
    @Select
    List<ShippingSlipDbEntity> getSecuredInventoryShippingSlipListTargetElapsedPeriod(Timestamp thresholdTime);

    /**
     * デリート
     */
    @Delete(sqlFile = true)
    int clearShippingSlip(Timestamp deleteTime);

    /**
     * デリート
     *
     * @param transactionId 取引ID
     */
    @Delete(sqlFile = true)
    int deleteShippingSlip(String transactionId);

}