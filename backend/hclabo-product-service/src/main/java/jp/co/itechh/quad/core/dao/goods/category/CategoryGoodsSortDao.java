/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */
package jp.co.itechh.quad.core.dao.goods.category;

import jp.co.itechh.quad.core.entity.goods.category.CategoryGoodsSortEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;

/**
 * カテゴリ登録商品並び順Daoクラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Dao
@ConfigAutowireable
public interface CategoryGoodsSortDao {

    /**
     * インサート
     *
     * @param categoryGoodsSortEntity カテゴリ登録商品並び順エンティティ
     * @return 処理件数
     */
    @Insert(excludeNull = true)
    int insert(CategoryGoodsSortEntity categoryGoodsSortEntity);

    /**
     * アップデート
     *
     * @param categoryGoodsSortEntity カテゴリ登録商品並び順エンティティ
     * @return 処理件数
     */
    @Update
    int update(CategoryGoodsSortEntity categoryGoodsSortEntity);

    /**
     * カテゴリ登録商品並び順取得
     *
     * @param categoryId カテゴリID
     * @return カテゴリ登録商品並び順
     */
    @Select
    CategoryGoodsSortEntity getEntityByCategoryId(String categoryId);

    /**
     * デリート
     *
     * @param categoryGoodsSortEntity カテゴリ登録商品並び順エンティティクラス
     * @return 処理件数
     */
    @Delete
    int delete(CategoryGoodsSortEntity categoryGoodsSortEntity);
}