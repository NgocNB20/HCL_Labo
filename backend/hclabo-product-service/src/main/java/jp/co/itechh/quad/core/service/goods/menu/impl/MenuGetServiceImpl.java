/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.goods.menu.impl;

import jp.co.itechh.quad.core.base.util.seasar.AssertionUtil;
import jp.co.itechh.quad.core.entity.goods.menu.MenuEntity;
import jp.co.itechh.quad.core.logic.goods.menu.MenuGetLogic;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.goods.menu.MenuGetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * メニュー取得
 *
 * @author Pham Quang Dieu
 */
@Service
public class MenuGetServiceImpl extends AbstractShopService implements MenuGetService {

    /**
     * メニュー取得
     */
    private final MenuGetLogic menuGetLogic;

    @Autowired
    public MenuGetServiceImpl(MenuGetLogic menuGetLogic) {
        this.menuGetLogic = menuGetLogic;
    }

    /**
     * メニュー情報取得
     *
     * @param menuId メニューID
     * @return メニューエンティティ
     */
    @Override
    public MenuEntity execute(Integer menuId) {

        AssertionUtil.assertNotNull("menuId", menuId);

        MenuEntity menuEntity = menuGetLogic.execute(menuId);

        return menuEntity;
    }

}
