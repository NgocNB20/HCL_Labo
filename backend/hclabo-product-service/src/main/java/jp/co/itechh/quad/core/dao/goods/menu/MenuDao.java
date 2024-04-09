/*
 * Project Name : HIT-MALL4
 *
 * Copyright (C) 2021 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.dao.goods.menu;

import jp.co.itechh.quad.core.constant.type.HTypeOpenStatus;
import jp.co.itechh.quad.core.entity.goods.category.CategoryEntity;
import jp.co.itechh.quad.core.entity.goods.menu.MenuEntity;
import org.seasar.doma.Dao;
import org.seasar.doma.Select;
import org.seasar.doma.Update;
import org.seasar.doma.boot.ConfigAutowireable;

import java.sql.Timestamp;
import java.util.List;

/**
 * メニューDaoクラス
 *
 * @author PHAM QUANG DIEU (VJP)
 */
@Dao
@ConfigAutowireable
public interface MenuDao {

    /**
     * エンティティ取得
     *
     * @param menuId メニューID
     * @return メニュークラス
     */
    @Select
    MenuEntity getEntity(Integer menuId);

    /**
     * アップデート
     *
     * @param categoryTree カテゴリーツリー
     * @param updateTime 更新日時(更新値)
     * @param  menuId メニューID
     * @return 処理件数
     */
    @Update(sqlFile = true)
    int updateMenuEntity(String categoryTree, Timestamp updateTime, Integer menuId);

    /**
     * カテゴリクラスリスト取得
     *
     * @param menuId メニューID
     * @param targetTime 指定日時
     * @param conditionStatus
     * @return カテゴリクラスリスト
     */
    @Select
    List<CategoryEntity> getCategoryInfo(Integer menuId, Timestamp targetTime, HTypeOpenStatus conditionStatus);
}
