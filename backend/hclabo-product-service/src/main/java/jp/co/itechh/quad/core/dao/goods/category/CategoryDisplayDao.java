/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dao.goods.category;

import jp.co.itechh.quad.core.entity.goods.category.CategoryDisplayEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;

/**
 * カテゴリ表示Daoクラス
 *
 * @author thang
 * @version $Revision: 1.0 $
 */
@Dao
@ConfigAutowireable
public interface CategoryDisplayDao {

    /**
     * インサート
     *
     * @param categoryDisplayEntity カテゴリ表示エンティティ
     * @return 処理件数
     */
    @Insert(excludeNull = true)
    int insert(CategoryDisplayEntity categoryDisplayEntity);

    /**
     * アップデート
     *
     * @param categoryDisplayEntity カテゴリ表示エンティティ
     * @return 処理件数
     */
    @Update
    int update(CategoryDisplayEntity categoryDisplayEntity);

    /**
     * デリート
     *
     * @param categoryDisplayEntity カテゴリ表示エンティティ
     * @return 処理件数
     */
    @Delete
    int delete(CategoryDisplayEntity categoryDisplayEntity);

    /**
     * エンティティ取得
     *
     * @param categorySeq カテゴリSEQ
     * @return カテゴリ表示エンティティ
     */
    @Select
    CategoryDisplayEntity getEntity(Integer categorySeq);

}
