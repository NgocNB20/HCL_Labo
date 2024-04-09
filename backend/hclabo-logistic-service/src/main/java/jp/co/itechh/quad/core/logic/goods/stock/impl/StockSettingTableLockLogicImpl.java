/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.stock.impl;

import jp.co.itechh.quad.core.dao.goods.stock.StockSettingDao;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.goods.stock.StockSettingTableLockLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * 在庫設定テーブルロック取得ロジック
 *
 * @author hirata
 * @version $Revision: 1.1 $
 *
 */
@Component
public class StockSettingTableLockLogicImpl extends AbstractShopLogic implements StockSettingTableLockLogic {

    /** 在庫設定DAO */
    private final StockSettingDao stockSettingDao;

    @Autowired
    public StockSettingTableLockLogicImpl(StockSettingDao stockSettingDao) {
        this.stockSettingDao = stockSettingDao;
    }

    /**
     * 在庫設定テーブルロック
     * 在庫設定テーブルのテーブルを排他取得する。
     *
     */
    @Override
    public void execute() {

        // (1) 在庫設定テーブル排他取得
        stockSettingDao.updateLockTableShareModeNowait();
    }
}
