/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.delivery;

import jp.co.itechh.quad.core.entity.shop.delivery.DeliveryMethodTypeCarriageEntity;

import java.util.List;

/**
 * 配送区分別送料リスト取得ロジック
 *
 * @author negishi
 * @version $Revision: 1.1 $
 *
 */
public interface DeliveryMethodTypeCarriageListGetLogic {

    /**
     * ロジック実行
     *
     * @param deliveryMethodSeq 配送方法SEQ
     * @return 配送区分別送料エンティティリスト
     */
    List<DeliveryMethodTypeCarriageEntity> execute(Integer deliveryMethodSeq);

}