/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.shop.delivery;

import jp.co.itechh.quad.core.dto.shop.delivery.DeliveryMethodDetailsDto;

/**
 * 配送方法登録サービス<br/>
 * サービスコード：SSD0003
 *
 * @author negishi
 * @version $Revision: 1.2 $
 *
 */
public interface DeliveryMethodRegistService {

    /** 配送方法登録に失敗 */
    String MSGCD_DELIVERY_METHOD_REGIST_FAIL = "SSD000301";

    /**
     * サービス実行
     *
     * @param deliveryMethodDetailsDto 配送方法詳細DTO
     * @return 登録件数
     */
    int execute(DeliveryMethodDetailsDto deliveryMethodDetailsDto);

}
