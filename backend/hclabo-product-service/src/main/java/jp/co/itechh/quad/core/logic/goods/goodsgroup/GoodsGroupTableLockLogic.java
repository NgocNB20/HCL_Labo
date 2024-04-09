/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.goodsgroup;

/**
 * 商品グループテーブルロック
 *
 * @author hirata
 * @version $Revision: 1.1 $
 */
public interface GoodsGroupTableLockLogic {
    // LGP0017

    /**
     * 商品グループテーブルロック
     * 商品グループテーブルを排他取得する。
     *
     */
    void execute();
}
