/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.shop.delivery;

import jp.co.itechh.quad.core.entity.shop.delivery.DeliveryMethodEntity;

/**
 * 配送方法データチェックサービス
 *
 * @author negishi
 * @version $Revision: 1.1 $
 *
 */
public interface DeliveryMethodDataCheckService {

    /**
     * サービス実行
     *
     * @param deliveryMethodEntity 配送方法エンティティ
     */
    void execute(DeliveryMethodEntity deliveryMethodEntity);

}
