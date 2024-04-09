/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.ddd.domain.inventory.repository;

/**
 * 在庫リポジトリ
 */
public interface IInventoryRepository {

    /**
     * 在庫引当<br/>
     *
     * @param itemId    商品ID（goodsSeq）
     * @param itemCount 商品数量
     * @return 更新件数
     */
    int secureStock(String itemId, int itemCount);

    /**
     * 在庫引当戻し<br/>
     *
     * @param itemId    商品ID（goodsSeq）
     * @param itemCount 商品数量
     * @return 更新件数
     */
    int updateSecuredStockRollback(String itemId, int itemCount);

    /**
     * 在庫出荷<br/>
     *
     * @param itemId    商品ID（goodsSeq）
     * @param itemCount 商品数量
     * @return 更新件数
     */
    int updateStockShipping(String itemId, int itemCount);

    /**
     * 出荷後在庫戻し<br/>
     * ※在庫管理商品の場合、在庫を戻す（stockDao.updateStockShipmentRollback）
     *
     * @param itemId    商品ID（goodsSeq）
     * @param itemCount 商品数量
     * @return 更新件数
     */
    int updateStockShippingRollback(String itemId, int itemCount);

}