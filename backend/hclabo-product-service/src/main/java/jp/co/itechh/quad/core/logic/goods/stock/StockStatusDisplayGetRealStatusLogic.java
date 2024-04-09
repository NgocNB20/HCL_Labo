/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.stock;

import jp.co.itechh.quad.core.constant.type.HTypeStockStatusType;
import jp.co.itechh.quad.core.dto.goods.stock.StockStatusDisplayConditionDto;

/**
 * 在庫状態表示
 * リアルタイム在庫状況判定
 *
 * @author Kaneko
 *
 */
public interface StockStatusDisplayGetRealStatusLogic {

    /**
     * リアルタイム在庫状況判定ロジック。
     * <pre>
     * 商品の販売状態、販売期間、在庫数条件に基づいて在庫状態を決定する。
     * 在庫状態判定の詳細は「26_HM3_共通部仕様書_在庫状態表示条件.xls」参照。
     * </pre>
     *
     * @param stockStatusDisplayConditionDto 在庫状態表示判定用DTO
     *
     * @return 在庫状況
     */
    HTypeStockStatusType execute(StockStatusDisplayConditionDto stockStatusDisplayConditionDto);

}