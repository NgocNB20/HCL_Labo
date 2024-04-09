/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.stock.impl;

import jp.co.itechh.quad.core.dao.goods.stock.StockSettingDao;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.goods.stock.StockSettingUpdateStockManagementFlagLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 在庫登録
 *
 * @author hirata
 * @author Kaneko (itec) 2012/08/21 UtilからHelperへ変更　Util使用箇所をgetComponentで取得するように変更
 */
@Component
public class StockSettingUpdateStockManagementFlagLogicImpl extends AbstractShopLogic
                implements StockSettingUpdateStockManagementFlagLogic {

    private final StockSettingDao stockSettingDao;

    @Autowired
    public StockSettingUpdateStockManagementFlagLogicImpl(StockSettingDao stockSettingDao) {
        this.stockSettingDao = stockSettingDao;
    }

    /**
     * 在庫設定情報の登録処理
     *
     * @param goodsSeq 商品SEQ
     * @param stockManagementFlag 在庫管理フラグ
     */
    public int execute(Integer goodsSeq, String stockManagementFlag) {
        return stockSettingDao.updateStockManagementFlag(goodsSeq, stockManagementFlag);
    }
}