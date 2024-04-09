/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2023 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.goods.category.impl;

import jp.co.itechh.quad.core.dao.goods.category.CategoryGoodsSortDao;
import jp.co.itechh.quad.core.entity.goods.category.CategoryGoodsSortEntity;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.goods.category.CategoryGoodsSortRemoveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * カテゴリ登録商品並び順削除
 *
 * @author PHAM QUANG DIEU (VJP)
 */
@Service
public class CategoryGoodsSortRemoveServiceImpl extends AbstractShopService implements CategoryGoodsSortRemoveService {

    /** カテゴリ登録商品並び順Daoクラス */
    private final CategoryGoodsSortDao categoryGoodsSortDao;

    @Autowired
    public CategoryGoodsSortRemoveServiceImpl(CategoryGoodsSortDao categoryGoodsSortDao) {
        this.categoryGoodsSortDao = categoryGoodsSortDao;
    }

    /**
     * カテゴリ登録商品並び順削除
     *
     * @param categoryGoodsSortEntity カテゴリ登録商品並び順エンティティクラス
     * @return 件数
     */
    @Override
    public int execute(CategoryGoodsSortEntity categoryGoodsSortEntity) {
        return categoryGoodsSortDao.delete(categoryGoodsSortEntity);
    }
}
