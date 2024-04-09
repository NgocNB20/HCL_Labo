/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2023 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.goods.category.impl;

import jp.co.itechh.quad.core.dao.goods.category.CategoryConditionDao;
import jp.co.itechh.quad.core.dao.goods.category.CategoryConditionDetailDao;
import jp.co.itechh.quad.core.entity.goods.category.CategoryConditionEntity;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.goods.category.CategoryConditionRemoveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * カテゴリ条件削除
 *
 * @author PHAM QUANG DIEU (VJP)
 */
@Service
public class CategoryConditionRemoveServiceImpl extends AbstractShopService implements CategoryConditionRemoveService {

    /** カテゴリ条件Daoクラス */
    private final CategoryConditionDao categoryConditionDao;

    /** カテゴリ条件詳細Daoクラス */
    private final CategoryConditionDetailDao categoryConditionDetailDao;

    @Autowired
    public CategoryConditionRemoveServiceImpl(CategoryConditionDao categoryConditionDao,
                                              CategoryConditionDetailDao categoryConditionDetailDao) {
        this.categoryConditionDao = categoryConditionDao;
        this.categoryConditionDetailDao = categoryConditionDetailDao;
    }

    /**
     *
     * カテゴリ条件削除
     *
     * @param categoryConditionEntity カテゴリ条件エンティティクラス
     * @return 件数
     */
    @Override
    public int execute(CategoryConditionEntity categoryConditionEntity) {
        categoryConditionDao.delete(categoryConditionEntity);
        return categoryConditionDetailDao.deleteByCategorySeq(categoryConditionEntity.getCategorySeq());
    }
}