/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.delivery;

import jp.co.itechh.quad.core.entity.shop.delivery.DeliveryMethodEntity;

/**
 * 配送方法登録ロジック
 *
 * @author negishi
 * @version $Revision: 1.1 $
 *
 */
public interface DeliveryMethodRegistLogic {

    /**
     * ロジック実行
     *
     * @param deliveryMethodEntity 配送方法エンティティ
     * @return 登録件数
     */
    int execute(DeliveryMethodEntity deliveryMethodEntity);

}
