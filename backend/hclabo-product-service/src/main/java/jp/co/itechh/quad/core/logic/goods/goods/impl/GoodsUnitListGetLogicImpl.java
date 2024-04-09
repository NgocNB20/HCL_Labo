/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.goods.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.dao.goods.goods.GoodsDao;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsUnitDto;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.goods.goods.GoodsUnitListGetLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 商品規格リスト取得ロジック
 *
 * @author hs32101
 *
 */
@Component
public class GoodsUnitListGetLogicImpl extends AbstractShopLogic implements GoodsUnitListGetLogic {

    /** 商品DAO */
    private final GoodsDao goodsDao;

    @Autowired
    public GoodsUnitListGetLogicImpl(GoodsDao goodsDao) {
        this.goodsDao = goodsDao;
    }

    /**
     * 規格値2リスト取得処理
     *
     * @param ggcd 商品グループコード
     * @param gcd 商品コード
     * @return 規格値2リスト
     */
    @Override
    public List<GoodsUnitDto> getUnit2List(String ggcd, String gcd) {

        // 入力チェック
        ArgumentCheckUtil.assertNotEmpty("ggcd", ggcd);

        // 規格値2リスト取得
        return goodsDao.getUnit2ListByGoodsCode(ggcd, gcd);
    }

}