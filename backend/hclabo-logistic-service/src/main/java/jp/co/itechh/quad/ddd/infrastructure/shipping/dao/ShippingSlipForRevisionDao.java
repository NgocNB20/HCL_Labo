/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.infrastructure.shipping.dao;

import jp.co.itechh.quad.ddd.infrastructure.shipping.dbentity.ShippingSlipForRevisionDbEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;

import java.sql.Timestamp;

/**
 * 改訂用配送伝票Daoクラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Dao
@ConfigAutowireable
public interface ShippingSlipForRevisionDao {

    /**
     * インサート
     *
     * @param shippingSlipForRevisionDbEntity 改訂用配送伝票DBエンティティ
     * @return 処理件数
     */
    @Insert(excludeNull = true)
    int insert(ShippingSlipForRevisionDbEntity shippingSlipForRevisionDbEntity);

    /**
     * アップデート
     *
     * @param shippingSlipForRevisionDbEntity 改訂用配送伝票DBエンティティ
     * @return 処理件数
     */
    @Update
    int update(ShippingSlipForRevisionDbEntity shippingSlipForRevisionDbEntity);

    /**
     * 改訂用配送伝票ID改訂用配送伝票DBエンティティ取得
     *
     * @param shippingSlipRevisionId 改訂用配送伝票ID
     * @return 改訂用配送伝票DBエンティティ
     */
    @Select
    ShippingSlipForRevisionDbEntity getByShippingSlipRevisionId(String shippingSlipRevisionId);

    /**
     * 改訂用取引IDで改訂用配送伝票DBエンティティ取得
     *
     * @param transactionId 改訂用取引ID
     * @return 改訂用配送伝票DBエンティティ
     */
    @Select
    ShippingSlipForRevisionDbEntity getByTransactionRevisionId(String transactionId);

    /**
     * デリート
     */
    @Delete(sqlFile = true)
    int clearShippingSlipRevision(Timestamp deleteTime);

}
