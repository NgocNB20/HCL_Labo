/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.stock.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.dao.goods.stock.StockResultDao;
import jp.co.itechh.quad.core.dto.goods.stock.StockResultSearchForDaoConditionDto;
import jp.co.itechh.quad.core.entity.goods.stock.StockResultEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.goods.stock.StockResultListGetLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 入庫実績リスト取得<br/>
 * 検索条件に該当する入庫実績リストを取得する。<br/>
 *
 * @author MN7017
 * @version $Revision: 1.7 $
 *
 */
@Component
public class StockResultListGetLogicImpl extends AbstractShopLogic implements StockResultListGetLogic {

    /**
     * 入庫実績Dao<br/>
     */
    private final StockResultDao stockResultDao;

    @Autowired
    public StockResultListGetLogicImpl(StockResultDao stockResultDao) {
        this.stockResultDao = stockResultDao;
    }

    /**
     * 実行処理<br/>
     *
     * @param stockResultSearchForDaoConditionDto 入庫実績Dao用検索条件Dto
     * @return 入庫実績エンティティリスト
     */
    @Override
    public List<StockResultEntity> execute(StockResultSearchForDaoConditionDto stockResultSearchForDaoConditionDto) {

        // (1)パラメータチェック
        ArgumentCheckUtil.assertNotNull("検索条件が取得できません。", stockResultSearchForDaoConditionDto);

        // (2)入庫実績情報リスト取得
        return stockResultDao.getSearchStockResultList(
                        stockResultSearchForDaoConditionDto,
                        stockResultSearchForDaoConditionDto.getPageInfo().getSelectOptions()
                                                      );

    }
}