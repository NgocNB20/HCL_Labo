/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.delivery;

import jp.co.itechh.quad.core.entity.shop.delivery.DeliverySpecialChargeAreaEntity;

/**
 * 配送特別料金エリアエンティティ取得Logicインターフェース
 *
 * @author $author$
 * @version $Revision: 1.1 $
 */
public interface DeliverySpecialChargeAreaGetLogic {
    /**
     * 配送特別料金エリアエンティティを取得します
     *
     * @param deliveryMethodSeq Integer
     * @param zipCode String
     *
     * @return DeliverySpecialChargeAreaEntity
     */
    DeliverySpecialChargeAreaEntity execute(Integer deliveryMethodSeq, String zipCode);
}