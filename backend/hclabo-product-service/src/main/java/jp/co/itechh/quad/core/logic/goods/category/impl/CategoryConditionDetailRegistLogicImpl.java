package jp.co.itechh.quad.core.logic.goods.category.impl;

import jp.co.itechh.quad.core.dao.goods.category.CategoryConditionDetailDao;
import jp.co.itechh.quad.core.entity.goods.category.CategoryConditionDetailEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.goods.category.CategoryConditionDetailRegistLogic;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * カテゴリ条件詳細登録
 */
@Component
public class CategoryConditionDetailRegistLogicImpl extends AbstractShopLogic
                implements CategoryConditionDetailRegistLogic {

    /** カテゴリ条件詳細Daoクラス */
    private final CategoryConditionDetailDao categoryConditionDetailDao;

    /**
     * コンストラクター
     *
     * @param categoryConditionDetailDao カテゴリ条件詳細Daoクラス
     */
    public CategoryConditionDetailRegistLogicImpl(CategoryConditionDetailDao categoryConditionDetailDao) {
        this.categoryConditionDetailDao = categoryConditionDetailDao;
    }

    @Override
    public int[] execute(List<CategoryConditionDetailEntity> categoryConditionDetailEntityList) {
        return categoryConditionDetailDao.insert(categoryConditionDetailEntityList);
    }
}