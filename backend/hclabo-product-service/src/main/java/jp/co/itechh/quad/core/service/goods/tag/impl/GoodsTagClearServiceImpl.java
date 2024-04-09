/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.goods.tag.impl;

import jp.co.itechh.quad.core.base.util.seasar.AssertionUtil;
import jp.co.itechh.quad.core.logic.goods.goods.GoodsTagClearLogic;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.goods.tag.GoodsTagClearService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

/**
 * 商品タグクリア
 *
 * @author Pham Quang Dieu
 */
@Service
public class GoodsTagClearServiceImpl extends AbstractShopService implements GoodsTagClearService {

    /**
     * 商品タグクリア
     */
    private final GoodsTagClearLogic goodsTagClearLogic;

    @Autowired
    public GoodsTagClearServiceImpl(GoodsTagClearLogic goodsTagClearLogic) {
        this.goodsTagClearLogic = goodsTagClearLogic;
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

        AssertionUtil.assertNotNull("deleteTime", deleteTime);

        return goodsTagClearLogic.execute(deleteTime);
    }

}
