/*
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */

package jp.co.itechh.quad.core.logic.goods.stock;

import jp.co.itechh.quad.core.dto.goods.stock.StockDto;

import java.util.List;

/**
 * 在庫登録と入庫処理用ロジック
 *
 * @author kimura
 */
public interface StockRegistAndSupplementLogic {

    /** 在庫登録エラー */
    String MSGCD_REGIST_FAIL = "STOCK-002-";

    /** 入庫登録エラー */
    String MSGCD_SUPPLEMENT_FAIL = "SGS000101";

    /**
     * 在庫登録または入庫処理を行う
     *
     * @param stockDtoList     在庫Dtoリスト
     * @return 結果格納用商品SEQリスト
     */
    List<Integer> execute(List<StockDto> stockDtoList);
}
