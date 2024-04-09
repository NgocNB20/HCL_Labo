package jp.co.itechh.quad.core.logic.goods.menu;

import jp.co.itechh.quad.core.entity.goods.menu.MenuEntity;

/**
 *
 * メニュー更新
 *
 * @author Pham Quang Dieu
 *
 */
public interface MenuUpdateLogic {
    /**
     * アップデート
     *
     * @param menuEntity メニューエンティティ
     * @return 処理件数
     */
    int execute(MenuEntity menuEntity);
}