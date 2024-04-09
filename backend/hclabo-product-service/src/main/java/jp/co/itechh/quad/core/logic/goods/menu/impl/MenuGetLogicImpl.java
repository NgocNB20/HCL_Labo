package jp.co.itechh.quad.core.logic.goods.menu.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.constant.type.HTypeOpenStatus;
import jp.co.itechh.quad.core.dao.goods.menu.MenuDao;
import jp.co.itechh.quad.core.entity.goods.category.CategoryEntity;
import jp.co.itechh.quad.core.entity.goods.menu.MenuEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.goods.menu.MenuGetLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.List;

/**
 * メニューLogic
 *
 * @author Pham Quang Dieu (VTI Japan Co., Ltd.)
 *
 */
@Component
public class MenuGetLogicImpl extends AbstractShopLogic implements MenuGetLogic {

    /** メニューDao */
    private final MenuDao menuDao;

    /**
     * コンストラクタ
     *
     * @param menuDao メニューDaoクラス
     */
    @Autowired
    public MenuGetLogicImpl(MenuDao menuDao) {
        this.menuDao = menuDao;
    }

    /**
     * エンティティ取得
     *
     * @param menuId メニューID
     * @return メニュークラス
     */
    @Override
    public MenuEntity execute(Integer menuId) {
        ArgumentCheckUtil.assertNotNull("menuId", menuId);
        return menuDao.getEntity(menuId);
    }

    /**
     * カテゴリクラスリスト取得
     *
     * @param menuId メニューID
     * @param targetTime 指定日時
     * @param conditionStatus 公開状態
     * @return カテゴリクラスリスト
     */
    @Override
    public List<CategoryEntity> getCategoryInfo(Integer menuId, Timestamp targetTime, HTypeOpenStatus conditionStatus) {
        ArgumentCheckUtil.assertNotNull("menuId", menuId);
        return menuDao.getCategoryInfo(menuId, targetTime, conditionStatus);
    }
}