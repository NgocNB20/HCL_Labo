/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.goods.impl;

import jp.co.itechh.quad.core.dao.goods.goods.GoodsDao;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.goods.goods.NewGoodsSeqGetLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 商品SEQ採番ロジック実装クラス
 *
 * @author hirata
 * @version $Revision: 1.2 $
 *
 */
@Component
public class NewGoodsSeqGetLogicImpl extends AbstractShopLogic implements NewGoodsSeqGetLogic {

    /** 商品Dao */
    private final GoodsDao goodsDao;

    @Autowired
    public NewGoodsSeqGetLogicImpl(GoodsDao goodsDao) {
        this.goodsDao = goodsDao;
    }

    /**
     * 実行メソッド
     *
     * @return 商品グループSEQ
     */
    @Override
    public Integer execute() {
        return goodsDao.getGoodsSeqNextVal();
    }
}
