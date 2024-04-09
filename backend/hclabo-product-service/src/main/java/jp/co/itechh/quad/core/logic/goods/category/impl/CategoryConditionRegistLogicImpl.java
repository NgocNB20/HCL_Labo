package jp.co.itechh.quad.core.logic.goods.category.impl;

import jp.co.itechh.quad.core.dao.goods.category.CategoryConditionDao;
import jp.co.itechh.quad.core.entity.goods.category.CategoryConditionEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.goods.category.CategoryConditionRegistLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * カテゴリ条件登録
 *
 * @author Pham Quang Dieu (VJP)
 *
 */
@Component
public class CategoryConditionRegistLogicImpl extends AbstractShopLogic implements CategoryConditionRegistLogic {

    private final CategoryConditionDao categoryConditionDao;

    @Autowired
    public CategoryConditionRegistLogicImpl(CategoryConditionDao categoryConditionDao) {
        this.categoryConditionDao = categoryConditionDao;
    }

    @Override
    public int execute(CategoryConditionEntity categoryConditionEntity) {
        return categoryConditionDao.insert(categoryConditionEntity);
    }
}