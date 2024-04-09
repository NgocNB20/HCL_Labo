/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */
package jp.co.itechh.quad.core.logic.shop.delivery.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.dao.shop.delivery.DeliveryImpossibleDayDao;
import jp.co.itechh.quad.core.entity.shop.delivery.DeliveryImpossibleDayEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.shop.delivery.DeliveryImpossibleDayGetByYearAndDateLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * お届け不可日取得ロジック<br/>
 *
 * @author Author: ty32113
 */
@Component
public class DeliveryImpossibleDayGetByYearAndDateLogicImpl extends AbstractShopLogic
                implements DeliveryImpossibleDayGetByYearAndDateLogic {

    /**
     * お届け不可日Dao
     */
    private final DeliveryImpossibleDayDao deliveryImpossibleDayDao;

    @Autowired
    public DeliveryImpossibleDayGetByYearAndDateLogicImpl(DeliveryImpossibleDayDao deliveryImpossibleDayDao) {
        this.deliveryImpossibleDayDao = deliveryImpossibleDayDao;
    }

    /**
     * ロジック実行<br/>
     *
     * @param year 年
     * @param date 年月日
     * @param deliveryMethodSeq 配送方法SEQ
     * @return お届け不可日エンティティ
     */
    @Override
    public DeliveryImpossibleDayEntity execute(Integer year, Date date, Integer deliveryMethodSeq) {

        // 引数チェック
        ArgumentCheckUtil.assertNotNull("year", year);
        ArgumentCheckUtil.assertNotNull("date", date);
        ArgumentCheckUtil.assertNotNull("deliveryMethodSeq", deliveryMethodSeq);

        // お届け不可日の検索
        return deliveryImpossibleDayDao.getDeliveryImpossibleDayByYearAndDate(year, date, deliveryMethodSeq);
    }

}