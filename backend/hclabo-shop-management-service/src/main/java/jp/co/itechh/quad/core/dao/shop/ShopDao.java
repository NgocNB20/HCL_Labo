/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dao.shop;

import jp.co.itechh.quad.core.entity.shop.ShopEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;

/**
 * ショップDaoクラス
 *
 * @author thang
 * @version $Revision: 1.0 $
 */
@Dao
@ConfigAutowireable
public interface ShopDao {
    /**
     * アップデート<br/>
     *
     * @param shopEntity ショップエンティティ
     * @return 処理件数
     */
    @Update
    int update(ShopEntity shopEntity);

    /**
     * エンティティ取得<br/>
     *
     * @param shopSeq ショップSEQ
     * @return ショップエンティティ
     */
    @Select
    ShopEntity getEntity(Integer shopSeq);

    /**
     * エンティティ取得<br/>
     *
     * @param shopSeq ショップSEQ
     * @return ショップエンティティ
     */
    @Select
    ShopEntity getEntityBySiteTypeStatus(Integer shopSeq);
}