/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */
package jp.co.itechh.quad.core.logic.shop.delivery;

import jp.co.itechh.quad.core.dto.shop.delivery.HolidaySearchForDaoConditionDto;
import jp.co.itechh.quad.core.entity.shop.delivery.HolidayEntity;

import java.util.List;

/**
 * 休日リスト取得ロジック<br/>
 *
 * @author Author: ogawa
 */
public interface HolidayListGetByYearLogic {

    /**
     * ロジック実行<br/>
     *
     * @param holidaySearchForDaoConditionDto 休日検索条件DTO
     * @return list 休日エンティティリスト
     */
    List<HolidayEntity> execute(HolidaySearchForDaoConditionDto holidaySearchForDaoConditionDto);
}