/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */
package jp.co.itechh.quad.core.logic.shop.delivery.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.dao.shop.delivery.DeliveryImpossibleDayDao;
import jp.co.itechh.quad.core.dto.shop.delivery.DeliveryImpossibleDaySearchForDaoConditionDto;
import jp.co.itechh.quad.core.entity.shop.delivery.DeliveryImpossibleDayEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.shop.delivery.DeliveryImpossibleDayListGetByYearLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * お届け不可日リスト取得ロジック<br/>
 *
 * @author Author: ty32113
 */
@Component
public class DeliveryImpossibleDayListGetByYearLogicImpl extends AbstractShopLogic
                implements DeliveryImpossibleDayListGetByYearLogic {

    /**
     * お届け不可日Dao
     */
    private final DeliveryImpossibleDayDao deliveryImpossibleDayDao;

    @Autowired
    public DeliveryImpossibleDayListGetByYearLogicImpl(DeliveryImpossibleDayDao deliveryImpossibleDayDao) {
        this.deliveryImpossibleDayDao = deliveryImpossibleDayDao;
    }

    /**
     * ロジック実行<br/>
     *
     * @param deliveryImpossibleDaySearchForDaoConditionDto お届け不可日検索条件DTO
     * @return お届け不可日エンティティリスト
     */
    @Override
    public List<DeliveryImpossibleDayEntity> execute(DeliveryImpossibleDaySearchForDaoConditionDto deliveryImpossibleDaySearchForDaoConditionDto) {

        // 引数チェック
        ArgumentCheckUtil.assertNotNull(
                        "deliveryImpossibleDaySearchForDaoConditionDto", deliveryImpossibleDaySearchForDaoConditionDto);

        // お届け不可日の検索
        return deliveryImpossibleDayDao.getSearchDeliveryImpossibleDayList(
                        deliveryImpossibleDaySearchForDaoConditionDto,
                        deliveryImpossibleDaySearchForDaoConditionDto.getPageInfo().getSelectOptions()
                                                                          );
    }

}