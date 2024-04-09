/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2022 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */
package jp.co.itechh.quad.core.dao.goods.category;

import jp.co.itechh.quad.core.entity.goods.category.CategoryConditionDetailEntity;
import org.seasar.doma.BatchInsert;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;

import java.util.List;

/**
 * カテゴリ条件詳細Daoクラス
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 */
@Dao
@ConfigAutowireable
public interface CategoryConditionDetailDao {

    /**
     * インサート
     *
     * @param categoryConditionDetailEntityList カテゴリ条件詳細エンティティリスト
     * @return 処理件数
     */
    @BatchInsert
    int[] insert(List<CategoryConditionDetailEntity> categoryConditionDetailEntityList);

    /**
     * インサート
     *
     * @param categoryConditionDetailEntity カテゴリ条件詳細エンティティ
     * @return 処理件数
     */
    @Insert(excludeNull = true)
    int insert(CategoryConditionDetailEntity categoryConditionDetailEntity);

    /**
     * アップデート
     *
     * @param categoryConditionDetailEntity カテゴリ条件詳細エンティティ
     * @return 処理件数
     */
    @Update
    int update(CategoryConditionDetailEntity categoryConditionDetailEntity);

    /**
     * エンティティ取得
     *
     * @param categorySeq カテゴリSEQ
     * @return カテゴリ条件詳細リスト
     */
    @Select
    List<CategoryConditionDetailEntity> getEntityListByCategorySeq(Integer categorySeq);

    /**
     * デリート
     *
     * @param categorySeq カテゴリSEQ
     * @return 処理件数
     */
    @Delete(sqlFile = true)
    int deleteByCategorySeq(Integer categorySeq);

}