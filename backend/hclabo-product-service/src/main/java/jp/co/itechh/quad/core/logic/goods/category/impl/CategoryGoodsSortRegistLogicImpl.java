package jp.co.itechh.quad.core.logic.goods.category.impl;

import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.dao.goods.category.CategoryGoodsSortDao;
import jp.co.itechh.quad.core.entity.goods.category.CategoryGoodsSortEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.goods.category.CategoryGoodsSortRegistLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * カテゴリ登録商品並び順を登録
 *
 * @author Pham Quang Dieu (VJP)
 *
 */
@Component
public class CategoryGoodsSortRegistLogicImpl extends AbstractShopLogic implements CategoryGoodsSortRegistLogic {

    /** カテゴリ登録商品並び順Daoクラス */
    private final CategoryGoodsSortDao categoryGoodsSortDao;

    /** コンストラクタ */
    @Autowired
    public CategoryGoodsSortRegistLogicImpl(CategoryGoodsSortDao categoryGoodsSortDao) {
        this.categoryGoodsSortDao = categoryGoodsSortDao;
    }

    /**
     *
     * カテゴリ登録商品並び順を登録
     *
     * @param categoryGoodsSortEntity カテゴリ登録商品並び順エンティティ
     * @return 件数
     */
    @Override
    public int execute(CategoryGoodsSortEntity categoryGoodsSortEntity) {

        // 日付関連Helper取得
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);

        Timestamp currentTime = dateUtility.getCurrentTime();
        categoryGoodsSortEntity.setRegistTime(currentTime);
        categoryGoodsSortEntity.setUpdateTime(currentTime);

        return categoryGoodsSortDao.insert(categoryGoodsSortEntity);
    }
}