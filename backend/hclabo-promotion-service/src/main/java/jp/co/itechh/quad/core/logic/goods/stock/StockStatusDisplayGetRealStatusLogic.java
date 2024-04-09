/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.stock;

import jp.co.itechh.quad.core.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.core.constant.type.HTypeStockStatusType;
import jp.co.itechh.quad.core.dto.goods.stock.StockStatusDisplayConditionDto;

import java.sql.Timestamp;

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

    /**
     * リアルタイム在庫状況判定ロジック。
     * <pre>
     * 商品の公開状態、公開期間、販売状態、販売期間、在庫数条件に基づいて在庫状態を決定する。
     * 在庫状態判定の詳細は「26_HM3_共通部仕様書_在庫状態表示条件.xls」参照。
     * </pre>
     *
     * @param stockStatusDisplayConditionDto 在庫状態表示判定用DTO
     * @param openStatus 公開状態
     * @param openStartTime 公開開始日時
     * @param openEndTime 公開終了日時
     *
     * @return 在庫状況
     */
    HTypeStockStatusType execute(StockStatusDisplayConditionDto stockStatusDisplayConditionDto,
                                 HTypeOpenDeleteStatus openStatus,
                                 Timestamp openStartTime,
                                 Timestamp openEndTime);
}