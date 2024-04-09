/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */
package jp.co.itechh.quad.core.logic.shop.delivery;

import jp.co.itechh.quad.core.entity.shop.delivery.DeliveryImpossibleDayEntity;

/**
 * お届け不可日削除ロジック<br/>
 *
 * @author Author: ty32113
 */
public interface DeliveryImpossibleDayDeleteLogic {

    /**
     * ロジック実行<br/>
     *
     * @param deliveryImpossibleDayDeleteEntity お届け不可日エンティティ
     * @return 登録件数
     */
    int execute(DeliveryImpossibleDayEntity deliveryImpossibleDayDeleteEntity);
}
