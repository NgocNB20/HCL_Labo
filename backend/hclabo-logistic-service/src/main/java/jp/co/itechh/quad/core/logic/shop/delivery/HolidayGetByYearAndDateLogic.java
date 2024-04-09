/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */
package jp.co.itechh.quad.core.logic.shop.delivery;

import jp.co.itechh.quad.core.entity.shop.delivery.HolidayEntity;

import java.util.Date;

/**
 * 休日取得ロジック<br/>
 *
 * @author Author: ogawa
 */
public interface HolidayGetByYearAndDateLogic {

    // LSC0005

    /**
     * ロジック実行<br/>
     *
     * @param year 年
     * @param date 年月日
     * @param deliveryMethodSeq 配送方法SEQ
     * @return 休日エンティティDTO
     */
    HolidayEntity execute(Integer year, Date date, Integer deliveryMethodSeq);
}