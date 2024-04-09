/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop;

import jp.co.itechh.quad.core.entity.shop.ShopEntity;

/**
 *
 * ショップ情報取得<br/>
 *
 * @author ozaki
 * @author sakai
 * @version $Revision: 1.1 $
 *
 */
public interface ShopGetLogic {

    /**
     *
     * ショップ情報を取得する<br/>
     *
     * @param shopSeq ショップSEQ
     * @return ショップ情報エンティティ
     */
    ShopEntity execute(Integer shopSeq);
}