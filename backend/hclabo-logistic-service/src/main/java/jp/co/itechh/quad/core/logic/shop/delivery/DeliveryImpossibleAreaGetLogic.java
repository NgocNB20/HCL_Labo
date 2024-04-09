/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.delivery;

import jp.co.itechh.quad.core.entity.shop.delivery.DeliveryImpossibleAreaEntity;

/**
 * 配送不可能エリアエンティティ取得Logicインターフェース
 *
 * @author $author$
 * @version $Revision: 1.1 $
 */
public interface DeliveryImpossibleAreaGetLogic {

    /**
     * 配送不可能エリアエンティティを取得します
     *
     * @param deliveryMethodSeq Integer
     * @param zipCode String
     *
     * @return DeliveryImpossibleAreaEntity
     */
    DeliveryImpossibleAreaEntity execute(Integer deliveryMethodSeq, String zipCode);
}