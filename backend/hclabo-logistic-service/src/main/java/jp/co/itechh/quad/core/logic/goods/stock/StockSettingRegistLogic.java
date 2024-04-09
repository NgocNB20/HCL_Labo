/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.core.logic.goods.stock;

import jp.co.itechh.quad.core.entity.goods.stock.StockSettingEntity;

import java.util.List;

/**
 * 在庫設定登録
 *
 * @author kimura
 */
public interface StockSettingRegistLogic {

    /** 在庫設定登録更新エラー */
    String MSGCD_REGIST_UPDATE_FAIL = "STOCK-001-";

    /**
     * 在庫設定登録
     *
     * @param goodsSeq               商品SEQリスト
     * @param stockSettingEntityList 在庫設定エンティティリスト
     * @return 結果格納用商品SEQリスト
     */
    List<Integer> execute(List<Integer> goodsSeq, List<StockSettingEntity> stockSettingEntityList);
}
