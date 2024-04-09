/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.stock;

import jp.co.itechh.quad.core.dto.goods.stock.StockResultSearchForDaoConditionDto;
import jp.co.itechh.quad.core.entity.goods.stock.StockResultEntity;

import java.util.List;

/**
 * 入庫実績リスト取得インターフェースクラス<br/>
 * 検索条件に該当する入庫実績リストを取得する。<br/>
 *
 * @author MN7017
 * @version $Revision: 1.4 $
 *
 */
public interface StockResultListGetLogic {

    /**
     * 入庫実績リスト取得<br/>
     *
     * @param stockResultSearchForDaoConditionDto 入庫実績Dao用検索条件Dto
     * @return 入庫実績エンティティリスト
     */
    List<StockResultEntity> execute(StockResultSearchForDaoConditionDto stockResultSearchForDaoConditionDto);

}