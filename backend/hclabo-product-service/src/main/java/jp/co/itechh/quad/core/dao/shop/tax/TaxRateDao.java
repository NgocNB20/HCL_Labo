/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dao.shop.tax;

import jp.co.itechh.quad.core.entity.shop.tax.TaxRateEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;

import java.util.List;
import java.util.Map;

/**
 * 税率クラス
 *
 * @author thang
 */
@Dao
@ConfigAutowireable
public interface TaxRateDao {

    /**
     * インサート
     *
     * @param taxRateEntity 税率エンティティ
     * @return 処理件数
     */
    @Insert(excludeNull = true)
    int insert(TaxRateEntity taxRateEntity);

    /**
     * アップデート
     *
     * @param taxRateEntity 税率エンティティ
     * @return 処理件数
     */
    @Update
    int update(TaxRateEntity taxRateEntity);

    /**
     * デリート
     *
     * @param taxRateEntity 税率エンティティ
     * @return 処理件数
     */
    @Delete
    int delete(TaxRateEntity taxRateEntity);

    /**
     * エンティティ取得
     *
     * @param taxSeq 消費税SEQ
     * @param rate   税率
     * @return 税率エンティティ
     */
    @Select
    TaxRateEntity getEntity(Integer taxSeq, Integer rate);

    /**
     * 税率エンティティのリストを取得
     *
     * @param taxSeq 消費税SEQ
     * @return 税率エンティティリスト
     */
    @Select
    List<TaxRateEntity> getEffectiveTaxRateEntity(Integer taxSeq);

    /**
     * プルダウンリスト用税率を取得する
     *
     * @param shopSeq ショップSEQ
     * @return 名称一覧
     */
    @Select
    List<Map<String, Object>> getItemMapList();
}
