/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */
package jp.co.itechh.quad.core.logic.shop.delivery;

import jp.co.itechh.quad.core.entity.shop.delivery.HolidayEntity;

/**
 * 休日登録ロジック<br/>
 *
 * @author Author: ogawa
 */
public interface HolidayRegistUpdateLogic {

    /**
     * ロジック実行<br/>
     *
     * @param holidayEntity 休日エンティティ
     * @return 登録件数
     */
    int execute(HolidayEntity holidayEntity);
}
