/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.delivery;

import jp.co.itechh.quad.core.entity.shop.delivery.DeliveryMethodTypeCarriageEntity;

/**
 * 配送区分別送料登録ロジック
 *
 * @author negishi
 * @version $Revision: 1.1 $
 *
 */
public interface DeliveryMethodTypeCarriageRegistLogic {

    /**
     * ロジック実行
     *
     * @param deliveryMethodTypeCarriageEntity 配送区分別送料エンティティ
     * @return 登録件数
     */
    int execute(DeliveryMethodTypeCarriageEntity deliveryMethodTypeCarriageEntity);

}
