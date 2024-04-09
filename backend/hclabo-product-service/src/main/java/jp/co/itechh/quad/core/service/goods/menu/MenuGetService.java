/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.goods.menu;

import jp.co.itechh.quad.core.entity.goods.menu.MenuEntity;

/**
 * メニュー取得
 *
 * @author Pham Quang Dieu
 */
public interface MenuGetService {

    /**
     * メニュー情報取得
     *
     * @param menuId メニューID
     * @return メニューエンティティ
     */
    MenuEntity execute(Integer menuId);

}
