/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.shop.impl;

import jp.co.itechh.quad.core.entity.shop.ShopEntity;
import jp.co.itechh.quad.core.logic.shop.ShopUpdateLogic;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.shop.ShopUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * ショップ情報取得<br/>
 *
 * @author hirata
 * @version $Revision: 1.1 $
 *
 */
@Service
public class ShopUpdateServiceImpl extends AbstractShopService implements ShopUpdateService {

    /** ショップ情報更新ロジック */
    private final ShopUpdateLogic shopUpdateLogic;

    @Autowired
    public ShopUpdateServiceImpl(ShopUpdateLogic shopUpdateLogic) {
        this.shopUpdateLogic = shopUpdateLogic;
    }

    /**
     * ショップ情報を更新する<br/>
     *　@param shopEntity ショップエンティティ
     * @return 更新した件数
     */
    @Override
    public int execute(ShopEntity shopEntity) {

        // (1) ショップ情報を更新する
        int ret = shopUpdateLogic.execute(shopEntity);

        if (ret == 0) {
            // ショップなしエラー
            throwMessage(MSGCD_SHOP_NONE_FAIL);
        }

        return ret;
    }
}
