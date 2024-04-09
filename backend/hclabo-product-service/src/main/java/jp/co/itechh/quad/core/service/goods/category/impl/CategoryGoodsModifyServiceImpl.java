/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2023 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.goods.category.impl;

import jp.co.itechh.quad.core.base.util.seasar.AssertionUtil;
import jp.co.itechh.quad.core.dao.goods.category.CategoryGoodsDao;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.goods.category.CategoryGoodsModifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * カテゴリ商品修正
 * @author PHAM QUANG DIEU (VJP)
 */
@Service
public class CategoryGoodsModifyServiceImpl extends AbstractShopService implements CategoryGoodsModifyService {

    /** カテゴリ登録商品Daoクラス */
    private final CategoryGoodsDao categoryGoodsDao;

    /**
     * コンストラクタ
     *
     * @param categoryGoodsDao カテゴリ登録商品Daoクラス
     */
    @Autowired
    public CategoryGoodsModifyServiceImpl(CategoryGoodsDao categoryGoodsDao) {
        this.categoryGoodsDao = categoryGoodsDao;
    }

    /**
     * カテゴリー登録商品の登録・更新
     *
     * @param categorySeq    カテゴリーSEQ
     * @param goodsGroupList 商品グループリスト
     * @return 件数
     */
    @Override
    public int execute(Integer categorySeq, List<Integer> goodsGroupList) {

        int size = 0;

        AssertionUtil.assertNotNull("categorySeq", categorySeq);
        AssertionUtil.assertNotNull("goodsGroupList", goodsGroupList);

        // カテゴリー登録商品の登録・更新
        for (Integer goodsGroupSeq : goodsGroupList) {
            size += categoryGoodsDao.upsertCategoryGoods(categorySeq, goodsGroupSeq);
        }

        return size;
    }
}