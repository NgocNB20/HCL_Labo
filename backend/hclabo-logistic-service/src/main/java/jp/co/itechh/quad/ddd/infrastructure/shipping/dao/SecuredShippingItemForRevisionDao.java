/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.infrastructure.shipping.dao;

import jp.co.itechh.quad.ddd.infrastructure.shipping.dbentity.SecuredShippingItemForRevisionDbEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;

import java.sql.Timestamp;
import java.util.List;

/**
 * 改訂用配送商品Daoクラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Dao
@ConfigAutowireable
public interface SecuredShippingItemForRevisionDao {

    /**
     * インサート
     *
     * @param securedShippingItemForRevisionDbEntity 配送商品DB値オブジェクト
     * @return 処理件数
     */
    @Insert(excludeNull = true)
    int insert(SecuredShippingItemForRevisionDbEntity securedShippingItemForRevisionDbEntity);

    /**
     * アップデート
     *
     * @param securedShippingItemForRevisionDbEntity 配送商品DB値オブジェクト
     * @return 処理件数
     */
    @Update
    int update(SecuredShippingItemForRevisionDbEntity securedShippingItemForRevisionDbEntity);

    /**
     * インサートorアップデート
     *
     * @param securedShippingItemForRevisionDbEntity 改訂用配送商品DB値オブジェクト
     * @return 処理件数
     */
    @Insert(sqlFile = true)
    int insertOrUpdate(SecuredShippingItemForRevisionDbEntity securedShippingItemForRevisionDbEntity);

    /**
     * アップデート
     *
     * @param securedShippingItemForRevisionDbEntity 改訂用配送商品DB値オブジェクト
     * @return 処理件数
     */
    @Delete
    int delete(SecuredShippingItemForRevisionDbEntity securedShippingItemForRevisionDbEntity);

    /**
     * 配送伝票IDで配送商品DB値オブジェクトリスト取得
     *
     * @param shippingSlipRevisionId 改訂用配送伝票ID
     * @return 改訂用配送商品DB値オブジェクトリスト
     */
    @Select
    List<SecuredShippingItemForRevisionDbEntity> getByShippingSlipRevisionId(String shippingSlipRevisionId);

    /**
     * デリート
     */
    @Delete(sqlFile = true)
    int clearSecuredShippingItemForRevision(Timestamp deleteTime);

}
