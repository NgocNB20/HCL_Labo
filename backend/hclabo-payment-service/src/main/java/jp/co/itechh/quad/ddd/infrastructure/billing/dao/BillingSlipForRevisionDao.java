/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.infrastructure.billing.dao;

import jp.co.itechh.quad.ddd.infrastructure.billing.dbentity.BillingSlipForRevisionDbEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;

import java.sql.Timestamp;

/**
 * 改訂用請求伝票Dao
 */
@Dao
@ConfigAutowireable
public interface BillingSlipForRevisionDao {

    /**
     * インサート
     *
     * @param billingSlipForRevisionDbEntity 改訂用請求伝票DbEntity
     * @return 処理件数
     */
    @Insert(excludeNull = true)
    int insert(BillingSlipForRevisionDbEntity billingSlipForRevisionDbEntity);

    /**
     * アップデート
     *
     * @param billingSlipForRevisionDbEntity 改訂用請求伝票DbEntity
     * @return 処理件数
     */
    @Update
    int update(BillingSlipForRevisionDbEntity billingSlipForRevisionDbEntity);

    /**
     * 取引IDから改訂用請求伝票DbEntity取得する
     *
     * @param transactionRevisionId 改訂用取引ID
     * @return 改訂用請求伝票DbEntity
     */
    @Select
    BillingSlipForRevisionDbEntity getByTransactionRevisionId(String transactionRevisionId);

    /**
     * 改訂用請求伝票取得
     *
     * @param billingSlipRevisionId 改訂用請求伝票ID
     * @return 改訂用請求伝票DbEntity
     */
    @Select
    BillingSlipForRevisionDbEntity getByBillingSlipRevisionId(String billingSlipRevisionId);

    /**
     * デリート
     */
    @Delete(sqlFile = true)
    int clearBillingSlipForRevision(Timestamp deleteTime);

}
