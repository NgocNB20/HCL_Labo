/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dao.goods.stock;

import jp.co.itechh.quad.core.dto.goods.stock.StockResultSearchForDaoConditionDto;
import jp.co.itechh.quad.core.entity.goods.stock.StockResultEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.jdbc.SelectOptions;

import java.util.List;

/**
 * 入庫実績Dao<br/>
 *
 * @author thang
 *
 */
@Dao
@ConfigAutowireable
public interface StockResultDao {
    /**
     * 入庫実績情報リスト取得<br/>
     *
     * @param conditionDto 検索条件DTO
     * @param selectOptions 検索オプション
     * @return 入庫実績エンティティリスト
     */
    @Select
    List<StockResultEntity> getSearchStockResultList(StockResultSearchForDaoConditionDto conditionDto,
                                                     SelectOptions selectOptions);

    /**
     * 追加
     *
     * @param stockResultEntity 入庫実績エンティティ
     * @return 追加件数
     */
    @Insert(excludeNull = true)
    int insert(StockResultEntity stockResultEntity);

    /**
     * 更新
     *
     * @param stockResultEntity 入庫実績エンティティ
     * @return 更新件数
     */
    @Update
    int update(StockResultEntity stockResultEntity);

    /**
     * 削除
     *
     * @param stockResultEntity 入庫実績エンティティ
     * @return 削除件数
     */
    @Delete
    int delete(StockResultEntity stockResultEntity);

    /**
     * 入庫登録　入庫実績情報追加
     *
     * @param shopSeq           ショップSEQ
     * @param goodsSeq         商品SEQ
     * @param stockResultEntity 入庫実績エンティティ
     * @return 更新件数
     */
    @Insert(sqlFile = true, excludeNull = true)
    int insertStockSupplementHistory(int shopSeq, int goodsSeq, StockResultEntity stockResultEntity);

    /**
     * 入庫実績SEQ採番
     *
     * @return 入庫実績SEQ
     */
    @Select
    Integer getStockResultSeqNextVal();

}