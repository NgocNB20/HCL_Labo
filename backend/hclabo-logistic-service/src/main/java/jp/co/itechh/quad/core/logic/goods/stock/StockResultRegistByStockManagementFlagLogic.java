/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.stock;

/**
 * 在庫管理フラグ変更の入庫実績を登録
 * <pre>
 * 入庫数は0固定
 * 商品稼働率分析の棚卸在庫数を計算する際に、
 * 指定された集計日の商品情報が必要となるため、
 * 在庫管理フラグ変更時に入庫実績を登録する。
 * </pre>
 *
 * @author ito
 *
 */
public interface StockResultRegistByStockManagementFlagLogic {

    /**
     * 実行メソッド
     *
     * @param administratorSeq 管理者SEQ
     * @param shopSeq ショップSEQ
     * @param goodsSeq 商品SEQ
     * @param stockManagementFlag 在庫管理フラグ
     * @return 登録件数
     */
    int execute(Integer administratorSeq, Integer shopSeq, Integer goodsSeq, String stockManagementFlag);

}
