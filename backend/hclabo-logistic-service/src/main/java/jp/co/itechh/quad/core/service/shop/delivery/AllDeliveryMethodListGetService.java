/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.shop.delivery;

import jp.co.itechh.quad.core.entity.shop.delivery.DeliveryMethodEntity;

import java.util.List;

/**
 * 全配送方法エンティティリスト取得
 *
 * @author negishi
 * @version $Revision: 1.1 $
 *
 */
public interface AllDeliveryMethodListGetService {

    /**
     *
     * 配送方法エンティティリスト取得
     *
     * @param shopSeq ショップSEQ
     * @return 配送方法エンティティリスト
     */
    List<DeliveryMethodEntity> execute(Integer shopSeq);
}