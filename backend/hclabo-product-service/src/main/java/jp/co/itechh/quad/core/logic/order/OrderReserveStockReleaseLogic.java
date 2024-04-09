/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.order;

/**
 * 在庫解放ロジック
 * @author yt13605
 */
public interface OrderReserveStockReleaseLogic {

    /**
     * 実行メソッド
     * @param orderSeq 受注SEQ
     * @param orderGoodsVersionNo 受注商品連番
     */
    void execute(Integer orderSeq, Integer orderGoodsVersionNo);
}
