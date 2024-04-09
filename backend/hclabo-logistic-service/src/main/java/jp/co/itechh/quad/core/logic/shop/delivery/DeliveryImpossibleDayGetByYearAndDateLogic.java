/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */
package jp.co.itechh.quad.core.logic.shop.delivery;

import jp.co.itechh.quad.core.entity.shop.delivery.DeliveryImpossibleDayEntity;

import java.util.Date;

/**
 * お届け不可日取得ロジック<br/>
 *
 * @author Author: ty32113
 */
public interface DeliveryImpossibleDayGetByYearAndDateLogic {

    /**
     * ロジック実行<br/>
     *
     * @param year 年
     * @param date 年月日
     * @param deliveryMethodSeq 配送方法SEQ
     * @return お届け不可日エンティティDTO
     */
    DeliveryImpossibleDayEntity execute(Integer year, Date date, Integer deliveryMethodSeq);
}