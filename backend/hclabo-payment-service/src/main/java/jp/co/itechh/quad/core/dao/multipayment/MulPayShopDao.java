/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dao.multipayment;

import jp.co.itechh.quad.core.entity.multipayment.MulPayShopEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.boot.ConfigAutowireable;

/**
 * マルチペイメント用ショップ設定Daoクラス
 *
 * @author thang
 *
 */
@Dao
@ConfigAutowireable
public interface MulPayShopDao {

    /**
     * ショップSEQからエンティティの取得
     *
     * @param shopSeq ショップSEQ
     * @return マルチペイメント用ショップ設定エンティティ
     */
    @Select
    MulPayShopEntity getEntityByShopSeq(Integer shopSeq);

    /**
     * ショップIDからエンティティ取得
     *
     * @param shopId ショップID
     * @return マルチペイメント用サイト設定エンティティ
     */
    @Select
    MulPayShopEntity getEntityByShopId(String shopId);

}
