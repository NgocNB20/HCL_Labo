/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.delivery;

import jp.co.itechh.quad.core.entity.shop.delivery.DeliveryMethodEntity;

import java.util.List;

/**
 * 配送方法エンティティリスト取得ロジック<br/>
 *
 * @author negishi
 * @version $Revision: 1.1 $
 *
 */
public interface AllDeliveryMethodListGetByShopSeqLogic {

    /**
     * ロジック実行
     *
     * @param shopSeq ショップSEQ
     * @return 配送方法エンティティリスト
     */
    List<DeliveryMethodEntity> execute(Integer shopSeq);
}