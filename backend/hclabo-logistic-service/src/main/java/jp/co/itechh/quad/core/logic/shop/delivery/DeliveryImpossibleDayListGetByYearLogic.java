/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */
package jp.co.itechh.quad.core.logic.shop.delivery;

import jp.co.itechh.quad.core.dto.shop.delivery.DeliveryImpossibleDaySearchForDaoConditionDto;
import jp.co.itechh.quad.core.entity.shop.delivery.DeliveryImpossibleDayEntity;

import java.util.List;

/**
 * お届け不可日リスト取得ロジック<br/>
 *
 * @author Author: ty32113
 */
public interface DeliveryImpossibleDayListGetByYearLogic {

    /**
     * ロジック実行<br/>
     *
     * @param deliveryImpossibleDaySearchForDaoConditionDto お届け不可日検索条件DTO
     * @return list お届け不可日エンティティリスト
     */
    List<DeliveryImpossibleDayEntity> execute(DeliveryImpossibleDaySearchForDaoConditionDto deliveryImpossibleDaySearchForDaoConditionDto);
}