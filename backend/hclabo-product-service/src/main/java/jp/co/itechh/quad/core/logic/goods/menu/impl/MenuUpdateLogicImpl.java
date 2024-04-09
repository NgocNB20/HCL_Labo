package jp.co.itechh.quad.core.logic.goods.menu.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.dao.goods.menu.MenuDao;
import jp.co.itechh.quad.core.entity.goods.menu.MenuEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.goods.menu.MenuUpdateLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 *
 * メニュー更新
 *
 * @author Pham Quang Dieu
 *
 */
@Component
public class MenuUpdateLogicImpl extends AbstractShopLogic implements MenuUpdateLogic {

    /** メニューDao */
    private final MenuDao menuDao;

    @Autowired
    public MenuUpdateLogicImpl(MenuDao menuDao) {
        this.menuDao = menuDao;
    }

    /**
     * アップデート
     *
     * @param menuEntity メニューエンティティ
     * @return 処理件数
     */
    @Override
    public int execute(MenuEntity menuEntity) {
        // パラメータチェック
        ArgumentCheckUtil.assertNotNull("menuEntity", menuEntity);
        // 日付関連Helper取得
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);

        Timestamp currentTime = dateUtility.getCurrentTime();
        // 更新
        return menuDao.updateMenuEntity(menuEntity.getCategoryTree(), currentTime, menuEntity.getMenuId());
    }
}