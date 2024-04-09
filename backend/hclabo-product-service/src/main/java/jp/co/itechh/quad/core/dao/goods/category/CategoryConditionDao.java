/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */
package jp.co.itechh.quad.core.dao.goods.category;

import jp.co.itechh.quad.core.entity.goods.category.CategoryConditionEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;

/**
 * カテゴリ条件Daoクラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Dao
@ConfigAutowireable
public interface CategoryConditionDao {

    /**
     * インサート
     *
     * @param categoryConditionEntity カテゴリ条件エンティティ
     * @return 処理件数
     */
    @Insert(excludeNull = true)
    int insert(CategoryConditionEntity categoryConditionEntity);

    /**
     * アップデート
     *
     * @param categoryConditionEntity カテゴリ条件エンティティ
     * @return 処理件数
     */
    @Update
    int update(CategoryConditionEntity categoryConditionEntity);

    /**
     * エンティティ取得
     *
     * @param categorySeq カテゴリSEQ
     * @return カテゴリ条件クラス
     */
    @Select
    CategoryConditionEntity getEntity(Integer categorySeq);

    /**
     * デリート
     *
     * @param categoryConditionEntity カテゴリ条件エンティティクラス
     * @return 処理件数
     */
    @Delete
    int delete(CategoryConditionEntity categoryConditionEntity);
}