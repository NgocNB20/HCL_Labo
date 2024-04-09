/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.goodsgroup.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.dao.goods.goodsgroup.GoodsGroupDao;
import jp.co.itechh.quad.core.entity.goods.goodsgroup.GoodsGroupEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.goods.goodsgroup.GoodsGroupGetLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品グループ取得Logic
 *
 * @author aoyama
 *
 */
@Component
public class GoodsGroupGetLogicImpl extends AbstractShopLogic implements GoodsGroupGetLogic {

    /** 商品グループDao */
    public GoodsGroupDao goodsGroupDao;

    @Autowired
    public GoodsGroupGetLogicImpl(GoodsGroupDao goodsGroupDao) {
        this.goodsGroupDao = goodsGroupDao;
    }

    /**
     * 商品管理番号のリストからエンティティを取得
     *
     * @param shopSeq ショップSEQ
     * @param goodsGroupCodeList 商品管理番号のリスト
     * @return エンティティのリスト
     */
    @Override
    public List<GoodsGroupEntity> getEntityListByGoodsGroupCodeList(Integer shopSeq, List<String> goodsGroupCodeList) {
        ArgumentCheckUtil.assertNotNull("shopSeq", shopSeq);
        ArgumentCheckUtil.assertNotNull("goodsGroupCodeList", goodsGroupCodeList);

        if (goodsGroupCodeList.isEmpty()) {
            return new ArrayList<>();
        }

        return goodsGroupDao.getEntityByGoodsGroupCodeList(shopSeq, goodsGroupCodeList);
    }

}
