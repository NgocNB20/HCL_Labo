/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.goods.stock.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.dto.goods.stock.StockResultSearchForDaoConditionDto;
import jp.co.itechh.quad.core.entity.goods.stock.StockResultEntity;
import jp.co.itechh.quad.core.logic.goods.stock.StockResultListGetLogic;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.goods.stock.StockResultListGetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 入庫実績リスト取得<br/>
 * 入庫実績リストを取得する。<br/>
 *
 * @author MN7017
 * @version $Revision: 1.6 $
 *
 */
@Service
public class StockResultListGetServiceImpl extends AbstractShopService implements StockResultListGetService {

    /**
     * 入庫実績リスト取得サービスクラス<br/>
     */
    private final StockResultListGetLogic stockResultListGetLogic;

    @Autowired
    public StockResultListGetServiceImpl(StockResultListGetLogic stockResultListGetLogic) {
        this.stockResultListGetLogic = stockResultListGetLogic;
    }

    /**
     * 実行メソッド<br/>
     *
     * @param stockResultSearchForDaoConditionDto 入庫実績Dao用検索条件DTO
     * @return 入庫実績エンティティリスト
     */
    @Override
    public List<StockResultEntity> execute(StockResultSearchForDaoConditionDto stockResultSearchForDaoConditionDto) {

        // (1)パラメータチェック
        ArgumentCheckUtil.assertNotNull("検索条件が取得できません。", stockResultSearchForDaoConditionDto);

        // (2)入庫実績リスト取得実行
        return stockResultListGetLogic.execute(stockResultSearchForDaoConditionDto);
    }
}