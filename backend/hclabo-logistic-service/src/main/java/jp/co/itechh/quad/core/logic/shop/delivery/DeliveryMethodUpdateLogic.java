/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.delivery;

import jp.co.itechh.quad.core.entity.shop.delivery.DeliveryMethodEntity;

/**
 * 配送方法更新ロジック
 *
 * @author negishi
 * @version $Revision: 1.1 $
 *
 */
public interface DeliveryMethodUpdateLogic {

    /**
     * ロジック実行
     *
     * @param deliveryMethodEntity 配送方法エンティティ
     * @return 更新件数
     */
    int execute(DeliveryMethodEntity deliveryMethodEntity);

}
