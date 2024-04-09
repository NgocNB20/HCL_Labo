/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.goods.category.impl;

import jp.co.itechh.quad.core.base.util.seasar.AssertionUtil;
import jp.co.itechh.quad.core.entity.goods.category.CategoryEntity;
import jp.co.itechh.quad.core.logic.goods.category.CategoryModifyLogic;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.goods.category.CategorySimpleModifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * カテゴリ修正(カテゴリエンティティのみ更新)
 *
 * @author kimura
 * @version $Revision: 1.1 $
 */
@Service
public class CategorySimpleModifyServiceImpl extends AbstractShopService implements CategorySimpleModifyService {

    /**
     * カテゴリ修正
     */
    private final CategoryModifyLogic categoryModifyLogic;

    @Autowired
    public CategorySimpleModifyServiceImpl(CategoryModifyLogic categoryModifyLogic) {
        this.categoryModifyLogic = categoryModifyLogic;
    }

    /**
     * カテゴリの修正
     *
     * @param categoryEntity カテゴリエンティティ
     * @return 件数
     */
    @Override
    public int execute(CategoryEntity categoryEntity) {
        AssertionUtil.assertNotNull("categoryEntity", categoryEntity);
        return categoryModifyLogic.execute(categoryEntity);
    }

}
