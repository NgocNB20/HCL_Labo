/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.goods.category.impl;

import jp.co.itechh.quad.core.dao.goods.category.CategoryGoodsDao;
import jp.co.itechh.quad.core.entity.goods.category.CategoryGoodsEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.goods.category.CategoryGoodsRemoveLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * カテゴリ登録商品情報削除
 *
 * @author hirata
 * @version $Revision: 1.1 $
 *
 */
@Component
public class CategoryGoodsRemoveLogicImpl extends AbstractShopLogic implements CategoryGoodsRemoveLogic {

    /** カテゴリ登録商品情報DAO */
    private final CategoryGoodsDao categoryGoodsDao;

    @Autowired
    public CategoryGoodsRemoveLogicImpl(CategoryGoodsDao categoryGoodsDao) {
        this.categoryGoodsDao = categoryGoodsDao;
    }

    /**
     * カテゴリ登録商品情報削除
     *
     * @param categorySeq カテゴリSEQ
     * @return 件数
     */
    @Override
    public int execute(Integer categorySeq) {
        return categoryGoodsDao.deleteByCategorySeq(categorySeq);
    }

    /**
     * カテゴリ登録商品情報を削除
     *
     * @param categoryGoodsEntity カテゴリ登録商品クラス
     * @return int 件数
     */
    public int execute(CategoryGoodsEntity categoryGoodsEntity) {
        return categoryGoodsDao.delete(categoryGoodsEntity);
    }
}