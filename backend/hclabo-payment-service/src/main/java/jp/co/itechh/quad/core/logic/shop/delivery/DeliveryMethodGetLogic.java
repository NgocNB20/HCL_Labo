/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.delivery;

import jp.co.itechh.quad.core.entity.shop.delivery.DeliveryMethodEntity;

/**
 * 配送方法取得ロジック
 *
 * @author ueshima
 * @version $Revision: 1.3 $
 */
public interface DeliveryMethodGetLogic {

    // LSD0001;

    /**
     * ロジック実行
     *
     * @param deliveryMethodSeq 配送方法SEQ
     * @return 配送方法エンティティ
     */
    DeliveryMethodEntity execute(Integer deliveryMethodSeq);

    /**
     * ロジック実行
     *
     * @param deliveryMethodName 配送方法名
     * @param shopSeq ショップSEQ
     * @return 配送方法エンティティ
     */
    DeliveryMethodEntity execute(String deliveryMethodName, Integer shopSeq);

}
