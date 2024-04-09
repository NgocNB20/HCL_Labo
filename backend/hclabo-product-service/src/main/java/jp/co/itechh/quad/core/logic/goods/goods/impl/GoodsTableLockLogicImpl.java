/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.goods.impl;

import jp.co.itechh.quad.core.dao.goods.goods.GoodsDao;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.goods.goods.GoodsTableLockLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 商品テーブルロック
 *
 * @author hirata
 * @version $Revision: 1.1 $
 */
@Component
public class GoodsTableLockLogicImpl extends AbstractShopLogic implements GoodsTableLockLogic {

    /** 商品DAO */
    private final GoodsDao goodsDao;

    @Autowired
    public GoodsTableLockLogicImpl(GoodsDao goodsDao) {
        this.goodsDao = goodsDao;
    }

    /**
     * 商品テーブルロック
     *
     */
    @Override
    public void execute() {

        // (1) 商品テーブル排他取得
        goodsDao.updateLockTableShareModeNowait();
    }
}
