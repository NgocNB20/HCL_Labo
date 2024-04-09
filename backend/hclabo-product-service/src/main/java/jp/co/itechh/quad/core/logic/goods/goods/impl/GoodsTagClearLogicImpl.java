/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.goods.impl;

import jp.co.itechh.quad.core.dao.goods.goodstag.GoodsTagDao;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.goods.goods.GoodsTagClearLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * 商品タグクリア
 *
 @author PHAM QUANG DIEU (VJP)
 */
@Component
public class GoodsTagClearLogicImpl extends AbstractShopLogic implements GoodsTagClearLogic {

    /**
     * 商品タグ Daoクラス
     */
    private final GoodsTagDao goodsTagDao;

    @Autowired
    public GoodsTagClearLogicImpl(GoodsTagDao goodsTagDao) {
        this.goodsTagDao = goodsTagDao;
    }

    /**
     *
     * 商品タグクリア
     *
     * @param deleteTime クリア基準日時
     * @return 処理件数
     */
    @Override
    public int execute(Timestamp deleteTime) {

        return goodsTagDao.clearGoodsTag(deleteTime);
    }
}