package jp.co.itechh.quad.core.logic.goods.menu;

import jp.co.itechh.quad.core.constant.type.HTypeOpenStatus;
import jp.co.itechh.quad.core.entity.goods.category.CategoryEntity;
import jp.co.itechh.quad.core.entity.goods.menu.MenuEntity;

import java.sql.Timestamp;
import java.util.List;

/**
 * メニューLogic
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 *
 */
public interface MenuGetLogic {

    /**
     * エンティティ取得
     *
     * @param menuId メニューID
     * @return メニュークラス
     */
    MenuEntity execute(Integer menuId);

    /**
     * カテゴリクラスリスト取得
     *
     * @param menuId メニューID
     * @param targetTime 指定日時
     * @param conditionStatus 公開状態
     * @return カテゴリクラスリスト
     */
    List<CategoryEntity> getCategoryInfo(Integer menuId, Timestamp targetTime, HTypeOpenStatus conditionStatus);

}
