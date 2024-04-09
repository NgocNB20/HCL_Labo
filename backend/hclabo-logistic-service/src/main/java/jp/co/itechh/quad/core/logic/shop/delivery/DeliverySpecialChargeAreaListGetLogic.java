/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 */
package jp.co.itechh.quad.core.logic.shop.delivery;

import jp.co.itechh.quad.core.dto.shop.delivery.DeliverySpecialChargeAreaConditionDto;
import jp.co.itechh.quad.core.dto.shop.delivery.DeliverySpecialChargeAreaResultDto;

import java.util.List;

/**
 * 配送特別料金エリアエンティティリスト取得Logic
 *
 * @author $author$
 * @version $Revision: 1.1 $
 */
public interface DeliverySpecialChargeAreaListGetLogic {
    /**
     * 配送特別料金エリアエンティティリストを取得します
     *
     * @param conditionDto 検索条件
     * @return 取得結果リスト
     */
    List<DeliverySpecialChargeAreaResultDto> execute(DeliverySpecialChargeAreaConditionDto conditionDto);
}