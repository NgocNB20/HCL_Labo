/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.stock;

/**
 * 在庫詳細情報取得ロジック<br/>
 *
 * @author MN7017
 * @version $Revision: 1.1 $
 *
 */
public interface StockSettingUpdateStockManagementFlagLogic {

    /**
     * 在庫設定情報の登録処理
     *
     * @param goodsSeq 商品SEQ
     * @param stockManagementFlag 在庫管理フラグ
     */
    int execute(Integer goodsSeq, String stockManagementFlag);

}