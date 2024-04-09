/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dao.shop.tax;

import jp.co.itechh.quad.core.entity.shop.tax.TaxEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;

import java.sql.Timestamp;

/**
 * 消費税Daoクラス
 *
 * @author thang
 */
@Dao
@ConfigAutowireable
public interface TaxDao {

    /**
     * インサート
     *
     * @param taxEntity 消費税エンティティ
     * @return 処理件数
     */
    @Insert(excludeNull = true)
    int insert(TaxEntity taxEntity);

    /**
     * アップデート
     *
     * @param taxEntity 消費税エンティティ
     * @return 処理件数
     */
    @Update
    int update(TaxEntity taxEntity);

    /**
     * デリート
     *
     * @param taxEntity 消費税エンティティ
     * @return 処理件数
     */
    @Delete
    int delete(TaxEntity taxEntity);

    /**
     * エンティティ取得
     *
     * @param taxSeq 消費税SEQ
     * @return 消費税エンティティ
     */
    @Select
    TaxEntity getEntity(Integer taxSeq);

    /**
     * 現在施行中の消費税Entityを取得
     *
     * @param currentTime 現在日時
     * @return 消費税エンティティ
     */
    @Select
    TaxEntity getCurrentTaxEntity(Timestamp currentTime);

}
