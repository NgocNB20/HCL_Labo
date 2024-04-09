/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */
package jp.co.itechh.quad.core.logic.shop.delivery;

import jp.co.itechh.quad.core.entity.shop.delivery.HolidayEntity;

/**
 * 休日削除ロジック<br/>
 *
 * @author Author: ogawa
 */
public interface HolidayDeleteLogic {

    /**
     * ロジック実行<br/>
     *
     * @param holidayDeleteEntity 休日エンティティ
     * @return 登録件数
     */
    int execute(HolidayEntity holidayDeleteEntity);
}
