/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.shop.delivery;

import jp.co.itechh.quad.core.dto.shop.delivery.DeliveryMethodDetailsDto;

/**
 * 配送方法詳細取得サービス<br/>
 *
 * @author negishi
 * @version $Revision: 1.1 $
 *
 */
public interface DeliveryMethodDetailsGetService {

    /**
     * サービス実行
     *
     * @param deliveryMethodSeq 配送方法SEQ
     * @return 配送方法詳細DTO
     */
    DeliveryMethodDetailsDto execute(Integer deliveryMethodSeq);
}