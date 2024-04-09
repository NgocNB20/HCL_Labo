/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.goodsgroup.impl;

import jp.co.itechh.quad.core.dao.goods.goodsgroup.GoodsGroupDao;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.goods.goodsgroup.GoodsGroupTableLockLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 商品グループテーブルロック
 *
 * @author hirata
 * @version $Revision: 1.2 $
 */
@Component
public class GoodsGroupTableLockLogicImpl extends AbstractShopLogic implements GoodsGroupTableLockLogic {

    /** 商品グループDAO */
    private final GoodsGroupDao goodsGroupDao;

    @Autowired
    public GoodsGroupTableLockLogicImpl(GoodsGroupDao goodsGroupDao) {
        this.goodsGroupDao = goodsGroupDao;
    }

    /**
     * 商品グループテーブルロック
     */
    @Override
    public void execute() {

        // (1) カテゴリテーブル排他取得
        goodsGroupDao.updateLockTableShareModeNowait();
    }
}
