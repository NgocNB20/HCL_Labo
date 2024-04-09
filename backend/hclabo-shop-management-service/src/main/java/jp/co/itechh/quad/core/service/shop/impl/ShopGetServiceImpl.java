/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.shop.impl;

import jp.co.itechh.quad.core.entity.shop.ShopEntity;
import jp.co.itechh.quad.core.logic.shop.ShopGetLogic;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.shop.ShopGetService;
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
public class ShopGetServiceImpl extends AbstractShopService implements ShopGetService {

    /** ショップ情報取得ロジック */
    private final ShopGetLogic shopGetLogic;

    @Autowired
    public ShopGetServiceImpl(ShopGetLogic shopGetLogic) {
        this.shopGetLogic = shopGetLogic;
    }

    /**
     * システムプロパティのショップSEQを元にショップ情報を取得する<br/>
     *
     * @return ショップエンティティ
     */
    @Override
    public ShopEntity execute() {

        Integer shopSeq = 1001;

        // (2) ショップ情報を取得する
        ShopEntity shopEntity = shopGetLogic.execute(shopSeq);

        if (shopEntity == null) {
            // ショップなしエラー
            throwMessage(MSGCD_SHOP_NONE_FAIL, new Object[] {shopSeq.toString()});
        }

        return shopEntity;
    }
}