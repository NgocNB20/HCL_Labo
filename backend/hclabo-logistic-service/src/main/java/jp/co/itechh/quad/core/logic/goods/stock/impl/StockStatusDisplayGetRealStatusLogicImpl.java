/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.stock.impl;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.constant.type.HTypeGoodsSaleStatus;
import jp.co.itechh.quad.core.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.core.constant.type.HTypeStockManagementFlag;
import jp.co.itechh.quad.core.constant.type.HTypeStockStatusType;
import jp.co.itechh.quad.core.dto.goods.stock.StockStatusDisplayConditionDto;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.goods.stock.StockStatusDisplayGetRealStatusLogic;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 在庫状態表示
 * リアルタイム在庫状況判定
 *
 * @author Kaneko
 *
 */
@Component
public class StockStatusDisplayGetRealStatusLogicImpl extends AbstractShopLogic
                implements StockStatusDisplayGetRealStatusLogic {

    /**
     * リアルタイム在庫状況判定ロジック。<br/>
     * <pre>
     * 商品の販売状態、販売期間、在庫数条件に基づいて在庫状態を決定する。
     * 在庫状態判定の詳細は「26_HM3_共通部仕様書_在庫状態表示条件.xls」参照。
     * </pre>
     *
     * @param conditionDto 在庫状態表示判定用DTO
     *
     * @return 在庫状況
     */
    @Override
    public HTypeStockStatusType execute(StockStatusDisplayConditionDto conditionDto) {

        HTypeStockStatusType currentStatus = HTypeStockStatusType.NO_SALE;
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);

        // 商品情報取得用の日時を設定
        Timestamp currentTime = dateUtility.getCurrentTime();

        // 予約商品でない場合、商品の販売と在庫状態で判定
        currentStatus = judgStatusForGoodsInfo(conditionDto, currentTime);

        return currentStatus;
    }

    /**
     * リアルタイム在庫状況判定ロジック。<br/>
     * <pre>
     * 商品の公開状態、公開期間、販売状態、販売期間、在庫数条件に基づいて在庫状態を決定する。
     * 在庫状態判定の詳細は「26_HM3_共通部仕様書_在庫状態表示条件.xls」参照。
     * </pre>
     *
     * @param conditionDto 在庫状態表示判定用DTO
     * @param openStatus 公開状態
     * @param openStartTime 公開開始日時
     * @param openEndTime 公開終了日時
     *
     * @return 在庫状況
     */
    @Override
    public HTypeStockStatusType execute(StockStatusDisplayConditionDto conditionDto,
                                        HTypeOpenDeleteStatus openStatus,
                                        Timestamp openStartTime,
                                        Timestamp openEndTime) {

        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);

        // 公開ステータス判定
        if (HTypeOpenDeleteStatus.OPEN.equals(openStatus)) {
            // 公開ステータスが公開の場合、公開期間の判定を行う
            if (openEndTime != null && dateUtility.getCurrentTime().compareTo(openEndTime) > 0) {
                // 公開期間終了の場合、「公開期間終了」
                return HTypeStockStatusType.OPEN_END;
            } else if (openStartTime != null && dateUtility.getCurrentTime().compareTo(openStartTime) < 0) {
                // 公開開始前の場合、「公開前」
                return HTypeStockStatusType.BEFORE_OPEN;
            }
        } else {
            // 公開ステータスが公開以外の場合、「非公開」
            return HTypeStockStatusType.NO_OPEN;
        }

        // 公開ステータスが公開中の場合、在庫状況判定を行う
        return execute(conditionDto);
    }

    /**
     * (予約商品以外用)在庫状態を判定し、在庫状況を返却<br/>
     *
     * @param conditionDto 在庫状態表示判定用DTO
     * @param currentTime 現在時刻orプレビュー時間
     * @return 在庫状況
     */
    protected HTypeStockStatusType judgStatusForGoodsInfo(StockStatusDisplayConditionDto conditionDto,
                                                          Timestamp currentTime) {
        HTypeStockStatusType currentStatus = HTypeStockStatusType.NO_SALE;
        if (HTypeGoodsSaleStatus.SALE.equals(conditionDto.getSaleStatus())) {
            // 販売中の場合、「残りわずか」「在庫あり」「在庫なし」「販売前」「販売期間終了」の判定を行う
            if (conditionDto.getSaleEndTime() != null && currentTime.compareTo(conditionDto.getSaleEndTime()) > 0) {
                // 販売期間終了の場合、「販売期間終了」
                currentStatus = HTypeStockStatusType.SOLDOUT;
            } else if (conditionDto.getSaleStartTime() != null
                       && currentTime.compareTo(conditionDto.getSaleStartTime()) < 0) {
                // 販売開始前の場合、「販売前」
                currentStatus = HTypeStockStatusType.BEFORE_SALE;
            } else if (HTypeStockManagementFlag.ON.equals(conditionDto.getStockManagementFlag())
                       && conditionDto.getSalesPossibleStock() != null
                       && conditionDto.getSalesPossibleStock().compareTo(BigDecimal.ZERO) <= 0) {
                // 在庫管理する、かつ販売可能在庫数 <= 0 の場合、「在庫なし」
                currentStatus = HTypeStockStatusType.STOCK_NOSTOCK;
            } else if (HTypeStockManagementFlag.OFF.equals(conditionDto.getStockManagementFlag()) || (
                            conditionDto.getSalesPossibleStock() != null
                            && conditionDto.getSalesPossibleStock().compareTo(conditionDto.getRemainderFewStock())
                               > 0)) {
                // 在庫管理しない、または、販売可能在庫数 > 残少表示在庫数の場合、「在庫あり」
                currentStatus = HTypeStockStatusType.STOCK_POSSIBLE_SALES;
            } else if (conditionDto.getSalesPossibleStock() != null
                       && conditionDto.getSalesPossibleStock().compareTo(conditionDto.getRemainderFewStock()) <= 0
                       && conditionDto.getSalesPossibleStock().compareTo(BigDecimal.ZERO) > 0) {
                // 販売可能在庫数<= 残少表示在庫数で販売可能在庫数 > 0の場合、「残りわずか」
                currentStatus = HTypeStockStatusType.STOCK_FEW;
            } else {
                // 販売可能在庫数 <= 0 の場合、「在庫なし」
                currentStatus = HTypeStockStatusType.STOCK_NOSTOCK;
            }
        } else if (HTypeGoodsSaleStatus.NO_SALE.equals(conditionDto.getSaleStatus())) {
            // 非販売の場合、「非販売」
            currentStatus = HTypeStockStatusType.NO_SALE;
        } else {
            // 削除の場合、「非公開」
            currentStatus = HTypeStockStatusType.NO_OPEN;
        }
        return currentStatus;
    }
}