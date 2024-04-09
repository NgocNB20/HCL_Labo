package jp.co.itechh.quad.core.logic.goods.category;

import jp.co.itechh.quad.core.entity.goods.category.CategoryConditionEntity;

/**
 * カテゴリ条件登録
 *
 * @author Pham Quang Dieu (VJP)
 */
public interface CategoryConditionRegistLogic {

    /**
     * カテゴリ条件登録
     *
     * @param categoryConditionEntity カテゴリ登録商品並び順エンティティ
     * @return 件数
     */
    int execute(CategoryConditionEntity categoryConditionEntity);
}