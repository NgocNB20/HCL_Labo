/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dao.multipayment;

import jp.co.itechh.quad.core.entity.multipayment.MulPaySiteEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.boot.ConfigAutowireable;

/**
 * マルチペイメント用サイト設定Daoクラス
 *
 * @author thang
 *
 */
@Dao
@ConfigAutowireable
public interface MulPaySiteDao {

    /**
     * エンティティ取得
     *
     * @return マルチペイメント用サイト設定エンティティ
     */
    @Select
    MulPaySiteEntity getEntity();

}
