/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.infrastructure.shipping.dao;

import jp.co.itechh.quad.ddd.infrastructure.shipping.dbentity.SecuredShippingItemDbEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;

import java.sql.Timestamp;
import java.util.List;

/**
 * 配送商品Daoクラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Dao
@ConfigAutowireable
public interface SecuredShippingItemDao {

    /**
     * インサート
     *
     * @param securedShippingItemDbEntity 配送商品DB値オブジェクト
     * @return 処理件数
     */
    @Insert(excludeNull = true)
    int insert(SecuredShippingItemDbEntity securedShippingItemDbEntity);

    /**
     * アップデート
     *
     * @param securedShippingItemDbEntity 配送商品DB値オブジェクト
     * @return 処理件数
     */
    @Update
    int update(SecuredShippingItemDbEntity securedShippingItemDbEntity);

    /**
     * インサートorアップデート
     *
     * @param securedShippingItemDbEntity 配送商品DB値オブジェクト
     * @return 処理件数
     */
    @Insert(sqlFile = true)
    int insertOrUpdate(SecuredShippingItemDbEntity securedShippingItemDbEntity);

    /**
     * アップデート
     *
     * @param securedShippingItemDbEntity 配送商品DB値オブジェクト
     * @return 処理件数
     */
    @Delete
    int delete(SecuredShippingItemDbEntity securedShippingItemDbEntity);

    /**
     * 配送伝票IDで配送商品DB値オブジェクトリスト取得
     *
     * @param shippingSlipId 配送伝票ID
     * @return 配送商品DB値オブジェクトリスト
     */
    @Select
    List<SecuredShippingItemDbEntity> getByShippingSlipId(String shippingSlipId);

    /**
     * デリート
     */
    @Delete(sqlFile = true)
    int clearSecuredShippingItem(Timestamp deleteTime);

    /**
     * デリート
     *
     * @param transactionId 取引ID
     */
    @Delete(sqlFile = true)
    int deleteSecuredShippingItem(String transactionId);

    /**
     * デリート
     *
     * @param shippingSlipId 配送伝票ID
     */
    @Delete(sqlFile = true)
    int deleteByShippingSlipId(String shippingSlipId);

}
