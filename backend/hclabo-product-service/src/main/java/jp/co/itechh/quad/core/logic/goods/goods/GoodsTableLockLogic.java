/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.goods;

/**
 * 商品テーブルロック
 *
 * @author hirata
 * @version $Revision: 1.1 $
 */
public interface GoodsTableLockLogic {
    // LGG0004

    /**
     *
     * 商品テーブルロック
     * 商品テーブルを排他取得する。
     *
     */
    void execute();
}