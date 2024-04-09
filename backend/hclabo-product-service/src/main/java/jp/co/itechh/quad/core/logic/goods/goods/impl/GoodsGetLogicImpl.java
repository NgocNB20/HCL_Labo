/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.goods.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.dao.goods.goods.GoodsDao;
import jp.co.itechh.quad.core.dto.goods.goods.GoodsSearchResultDto;
import jp.co.itechh.quad.core.dto.shop.noveltypresent.NoveltyGoodsCountConditionDto;
import jp.co.itechh.quad.core.entity.goods.goods.GoodsEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.goods.goods.GoodsGetLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 商品取得Logic
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 *
 */
@Component
public class GoodsGetLogicImpl extends AbstractShopLogic implements GoodsGetLogic {

    /** 商品Dao */
    public GoodsDao goodsDao;

    @Autowired
    public GoodsGetLogicImpl(GoodsDao goodsDao) {
        this.goodsDao = goodsDao;
    }

    /**
     * ノベルティプレゼント商品の対象件数を取得する
     *
     * @param conditionDto ノベルティプレゼント商品数確認用検索条件
     * @return 商品件数
     */
    @Override
    public int getNoveltyGoodsCount(NoveltyGoodsCountConditionDto conditionDto) {
        ArgumentCheckUtil.assertNotNull("conditionDto", conditionDto);
        return goodsDao.getNoveltyGoodsCount(conditionDto);
    }

    /**
     * 商品コードの一覧からエンティティを取得
     *
     * @param shopSeq ショップSEQ
     * @param goodsCodeList 商品コードのリスト
     * @return エンティティのリスト
     */
    @Override
    public List<GoodsEntity> getEntityByGoodsCodeList(Integer shopSeq, List<String> goodsCodeList) {
        ArgumentCheckUtil.assertNotNull("shopSeq", shopSeq);
        ArgumentCheckUtil.assertNotNull("goodsCodeList", goodsCodeList);

        if (goodsCodeList.isEmpty()) {
            return null;
        }

        return goodsDao.getEntityByGoodsCodeList(shopSeq, goodsCodeList);
    }

    /**
     * 商品名（部分一致）からエンティティを取得
     *
     * @param shopSeq ショップSEQ
     * @param goodsName 商品名
     * @return エンティティのリスト
     */
    @Override
    public List<GoodsEntity> getEntityByGoodsName(Integer shopSeq, String goodsName) {
        ArgumentCheckUtil.assertNotNull("shopSeq", shopSeq);
        ArgumentCheckUtil.assertNotNull("goodsName", goodsName);

        return goodsDao.getEntityByLikeGoodsName(shopSeq, goodsName);
    }

    /**
     * 商品コードのリストから商品グループを結合して公開状態、販売状態を取得する
     *
     * @param shopSeq ショップSEQ
     * @param goodsCodeList 商品コードのリスト
     * @return 検索結果
     */
    @Override
    public List<GoodsSearchResultDto> getStatusByGoodsCodeList(Integer shopSeq, List<String> goodsCodeList) {
        ArgumentCheckUtil.assertNotNull("shopSeq", shopSeq);
        ArgumentCheckUtil.assertNotNull("goodsCodeList", goodsCodeList);

        return goodsDao.getStatusByGoodsCodeList(shopSeq, goodsCodeList);
    }

}
