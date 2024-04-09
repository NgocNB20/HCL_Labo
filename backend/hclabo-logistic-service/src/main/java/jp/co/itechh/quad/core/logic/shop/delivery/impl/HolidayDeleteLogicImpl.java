/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */
package jp.co.itechh.quad.core.logic.shop.delivery.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.dao.shop.delivery.HolidayDao;
import jp.co.itechh.quad.core.entity.shop.delivery.HolidayEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.shop.delivery.HolidayDeleteLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 休日削除ロジック<br/>
 *
 * @author Author: ogawa
 */
@Component
public class HolidayDeleteLogicImpl extends AbstractShopLogic implements HolidayDeleteLogic {

    /**
     * 休日Dao
     */
    private final HolidayDao holidayDao;

    @Autowired
    public HolidayDeleteLogicImpl(HolidayDao holidayDao) {
        this.holidayDao = holidayDao;
    }

    /**
     * ロジック実行<br/>
     *
     * @param holidayDeleteEntity 休日エンティティ
     * @return 登録件数
     */
    @Override
    public int execute(HolidayEntity holidayDeleteEntity) {

        // 引数チェック
        ArgumentCheckUtil.assertNotNull("holidayEntity", holidayDeleteEntity);

        // 削除
        return holidayDao.deleteHolidayByYear(holidayDeleteEntity);
    }
}