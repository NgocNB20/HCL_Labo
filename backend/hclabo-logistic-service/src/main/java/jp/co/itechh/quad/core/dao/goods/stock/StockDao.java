/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dao.goods.stock;

import jp.co.itechh.quad.core.dto.goods.stock.StockDto;
import jp.co.itechh.quad.core.entity.goods.stock.StockEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;

import java.math.BigDecimal;
import java.util.List;

/**
 * 在庫情報Dao
 *
 * @author thang
 * @version $Revision: 1.0 $
 */
@Dao
@ConfigAutowireable
public interface StockDao {

    /**
     * 一括在庫引当<br/>
     *
     * @param itemId    商品SEQ
     * @param itemCount 数量
     * @return 更新件数
     */
    @Update(sqlFile = true)
    int updateStock(Integer itemId, int itemCount);

    /**
     * 一括在庫引当戻し<br/>
     *
     * @param itemId    商品SEQ
     * @param itemCount 数量
     * @return 更新件数
     */
    @Update(sqlFile = true)
    int updateStockRollback(Integer itemId, int itemCount);

    /**
     * 一括在庫出荷<br/>
     *
     * @param itemId    商品SEQ
     * @param itemCount 数量
     * @return 更新件数
     */
    @Update(sqlFile = true)
    int updateStockShipment(Integer itemId, int itemCount);

    /**
     * 追加
     *
     * @param stockEntity 在庫エンティティ
     * @return 追加件数
     */
    @Insert(excludeNull = true)
    int insert(StockEntity stockEntity);

    /**
     * 更新
     *
     * @param stockEntity 在庫エンティティ
     * @return 更新件数
     */
    @Update
    int update(StockEntity stockEntity);

    /**
     * 削除
     *
     * @param stockEntity 在庫エンティティ
     * @return 削除件数
     */
    @Delete
    int delete(StockEntity stockEntity);

    /**
     * エンティティ取得
     *
     * @param goodsSeq 商品SEQ
     * @return 在庫情報エンティティ
     */
    @Select
    StockEntity getEntity(Integer goodsSeq);

    /**
     * 在庫情報リスト取得
     *
     * @param goodsSeqList 商品SEQリスト
     * @return 在庫情報DTOリスト
     */
    @Select
    List<StockDto> getStockList(List<Integer> goodsSeqList);

    /**
     * 在庫情報取得
     *
     * @param goodsSeq 商品SEQ
     * @return 在庫情報DTO
     */
    @Select
    StockDto getStock(Integer goodsSeq);

    /**
     * 在庫情報　入庫登録
     *
     * @param shopSeq         ショップSEQ
     * @param goodsSeq       商品SEQ
     * @param supplementCount 入庫数
     * @return 更新件数
     */
    @Update(sqlFile = true)
    int updateStockSupplement(int shopSeq, int goodsSeq, BigDecimal supplementCount);

    /**
     * 一括出荷済み在庫戻し<br/>
     *
     * @param goodsSeq    商品ID（goodsSeq）
     * @param itemCount 商品数量
     * @return 更新件数
     */
    @Update(sqlFile = true)
    int updateStockShipmentRollback(int goodsSeq, int itemCount);
}
