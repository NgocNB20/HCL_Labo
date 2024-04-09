/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.goods.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.dao.goods.goods.GoodsDao;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsDetailsDto;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.goods.goods.GoodsDetailsGetBySeqLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * 商品詳細情報取得クラス(商品コード)
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 *
 */
@Component
public class GoodsDetailsGetBySeqLogicImpl extends AbstractShopLogic implements GoodsDetailsGetBySeqLogic {

    /** 商品DAO */
    private final GoodsDao goodsDao;

    @Autowired
    public GoodsDetailsGetBySeqLogicImpl(GoodsDao goodsDao) {
        this.goodsDao = goodsDao;
    }

    /**
     *
     * * 商品詳細情報取得
     *
     * @param goodsSeq 商品SEQ
     * @return 商品詳細Dtoクラス
     */
    @Override
    public GoodsDetailsDto execute(Integer goodsSeq) {

        // 引数チェック
        ArgumentCheckUtil.assertGreaterThanZero("goodsSeq", goodsSeq);

        return goodsDao.getGoodsDetailsByGoodsSeq(goodsSeq);
    }
}