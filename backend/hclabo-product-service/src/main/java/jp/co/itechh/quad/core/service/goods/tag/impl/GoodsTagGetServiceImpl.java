/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.goods.tag.impl;

import jp.co.itechh.quad.core.base.util.seasar.AssertionUtil;
import jp.co.itechh.quad.core.dto.goods.goodstag.GoodsTagDto;
import jp.co.itechh.quad.core.entity.goods.goods.GoodsTagEntity;
import jp.co.itechh.quad.core.logic.goods.goods.GoodsTagGetLogic;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.goods.tag.GoodsTagGetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * タグ商品ロサービス
 *
 * @author Pham Quang Dieu
 */
@Service
public class GoodsTagGetServiceImpl extends AbstractShopService implements GoodsTagGetService {

    /**
     * 商品タグ取得
     */
    private final GoodsTagGetLogic goodsTagGetLogic;

    @Autowired
    public GoodsTagGetServiceImpl(GoodsTagGetLogic goodsTagGetLogic) {
        this.goodsTagGetLogic = goodsTagGetLogic;
    }

    /**
     *
     * 商品タグリスト取得
     *
     * @param dto タグ商品Dtoクラス
     * @return タグ商品リスト
     */
    @Override
    public List<GoodsTagEntity> execute(GoodsTagDto dto) {

        AssertionUtil.assertNotNull("dto", dto);

        List<GoodsTagEntity> goodsTagEntityList = goodsTagGetLogic.execute(dto);

        return goodsTagEntityList;
    }

}
