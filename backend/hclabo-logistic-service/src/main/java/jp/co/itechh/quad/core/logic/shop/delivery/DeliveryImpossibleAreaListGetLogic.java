/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.delivery;

import jp.co.itechh.quad.core.dto.shop.delivery.DeliveryImpossibleAreaConditionDto;
import jp.co.itechh.quad.core.dto.shop.delivery.DeliveryImpossibleAreaResultDto;

import java.util.List;

/**
 * 配送不可能エリアエンティティリスト取得Logic
 *
 * @author $author$
 * @version $Revision: 1.1 $
 */
public interface DeliveryImpossibleAreaListGetLogic {

    /**
     * 配送不可能エリアエンティティリストを取得します
     *
     * @param conditionDto DeliveryImpossibleAreaConditionDto
     *
     * @return List&lt;DeliveryImpossibleAreaResultDto&gt;
     */
    List<DeliveryImpossibleAreaResultDto> execute(DeliveryImpossibleAreaConditionDto conditionDto);

}