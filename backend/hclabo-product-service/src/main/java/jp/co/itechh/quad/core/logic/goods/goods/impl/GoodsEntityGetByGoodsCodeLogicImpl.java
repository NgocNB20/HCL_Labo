/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.goods.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.dao.goods.goods.GoodsDao;
import jp.co.itechh.quad.core.entity.goods.goods.GoodsEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.goods.goods.GoodsEntityGetByGoodsCodeLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 商品エンティティ取得（商品コード）
 * 商品コード、ショップSEQをキーに商品エンティティを取得する。
 *
 * @author MN7017
 * @version $Revision: 1.1 $
 *
 */
@Component
public class GoodsEntityGetByGoodsCodeLogicImpl extends AbstractShopLogic implements GoodsEntityGetByGoodsCodeLogic {

    /**
     * 商品Dao
     */
    private final GoodsDao goodsDao;

    @Autowired
    public GoodsEntityGetByGoodsCodeLogicImpl(GoodsDao goodsDao) {
        this.goodsDao = goodsDao;
    }

    /**
     * 取得処理実行
     * メソッドの説明・概要
     *
     * @param shopSeq ショップSEQ
     * @param goodsCode 商品コード
     * @return 商品エンティティ
     */
    @Override
    public GoodsEntity execute(Integer shopSeq, String goodsCode) {

        // パラメータチェック
        ArgumentCheckUtil.assertNotNull("shopSeq", shopSeq);
        ArgumentCheckUtil.assertNotNull("goodsCode", goodsCode);

        // 取得処理実行
        return goodsDao.getGoodsByCode(shopSeq, goodsCode);

    }
}