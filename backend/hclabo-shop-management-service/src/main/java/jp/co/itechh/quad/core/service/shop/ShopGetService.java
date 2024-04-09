/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.shop;

import jp.co.itechh.quad.core.entity.shop.ShopEntity;

/**
 *
 * ショップ情報取得<br/>
 *
 * @author hirata
 * @version $Revision: 1.1 $
 *
 */
public interface ShopGetService {
    // SSS0006

    /**
     * ショップなしエラー<br/>
     * <code>MSGCD_SHOP_NONE_FAIL</code>
     */
    String MSGCD_SHOP_NONE_FAIL = "SSS000601";

    /**
     * システムプロパティのショップSEQを元にショップ情報を取得する<br/>
     *
     * @return ショップエンティティ
     */
    ShopEntity execute();
}
