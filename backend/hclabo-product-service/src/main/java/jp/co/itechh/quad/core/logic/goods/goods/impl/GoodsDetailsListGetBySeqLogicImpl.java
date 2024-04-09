/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.goods.impl;

import jp.co.itechh.quad.core.dao.goods.goods.GoodsDao;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsDetailsDto;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.goods.goods.GoodsDetailsListGetBySeqLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品エンティティリスト取得ロジック
 *
 * @author USER
 * @version $Revision: 1.3 $
 *
 */
@Component
public class GoodsDetailsListGetBySeqLogicImpl extends AbstractShopLogic implements GoodsDetailsListGetBySeqLogic {

    /**
     * 商品Dao
     */
    private final GoodsDao goodsDao;

    @Autowired
    public GoodsDetailsListGetBySeqLogicImpl(GoodsDao goodsDao) {
        this.goodsDao = goodsDao;
    }

    /**
     *
     * 実行メソッド
     *
     * @param goodsSeqList 商品SEQリスト
     * @return 商品エンティティリスト
     */
    @Override
    public List<GoodsDetailsDto> execute(List<Integer> goodsSeqList) {
        if (goodsSeqList == null || goodsSeqList.isEmpty()) {
            return new ArrayList<>();
        }
        return goodsDao.getGoodsDetailsList(goodsSeqList);
    }
}