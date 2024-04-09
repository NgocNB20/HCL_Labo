/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */
package jp.co.itechh.quad.core.logic.shop.delivery.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.dao.shop.delivery.HolidayDao;
import jp.co.itechh.quad.core.dto.shop.delivery.HolidaySearchForDaoConditionDto;
import jp.co.itechh.quad.core.entity.shop.delivery.HolidayEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.shop.delivery.HolidayListGetByYearLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 休日リスト取得ロジック<br/>
 *
 * @author Author: ogawa
 */
@Component
public class HolidayListGetByYearLogicImpl extends AbstractShopLogic implements HolidayListGetByYearLogic {

    /**
     * 休日Dao
     */
    private final HolidayDao holidayDao;

    @Autowired
    public HolidayListGetByYearLogicImpl(HolidayDao holidayDao) {
        this.holidayDao = holidayDao;
    }

    /**
     * ロジック実行<br/>
     *
     * @param holidaySearchForDaoConditionDto 休日検索条件DTO
     * @return 休日エンティティリスト
     */
    @Override
    public List<HolidayEntity> execute(HolidaySearchForDaoConditionDto holidaySearchForDaoConditionDto) {

        // 引数チェック
        ArgumentCheckUtil.assertNotNull("holidaySearchForDaoConditionDto", holidaySearchForDaoConditionDto);

        // 休日の検索
        return holidayDao.getSearchHolidayList(
                        holidaySearchForDaoConditionDto,
                        holidaySearchForDaoConditionDto.getPageInfo().getSelectOptions()
                                              );
    }

}