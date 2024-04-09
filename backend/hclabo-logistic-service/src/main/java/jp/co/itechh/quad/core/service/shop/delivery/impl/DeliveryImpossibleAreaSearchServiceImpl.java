/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.shop.delivery.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.dto.shop.delivery.DeliveryImpossibleAreaConditionDto;
import jp.co.itechh.quad.core.dto.shop.delivery.DeliveryImpossibleAreaResultDto;
import jp.co.itechh.quad.core.logic.shop.delivery.DeliveryImpossibleAreaListGetLogic;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.shop.delivery.DeliveryImpossibleAreaSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 配送不可能エリア検索Service実装クラス
 *
 * @author $author$
 * @version $Revision: 1.1 $
 */
@Service
public class DeliveryImpossibleAreaSearchServiceImpl extends AbstractShopService
                implements DeliveryImpossibleAreaSearchService {

    /**
     * 配送不可能エリアエンティティリスト取得Logic
     */
    private final DeliveryImpossibleAreaListGetLogic deliveryImpossibleAreaListGetLogic;

    @Autowired
    public DeliveryImpossibleAreaSearchServiceImpl(DeliveryImpossibleAreaListGetLogic deliveryImpossibleAreaListGetLogic) {
        this.deliveryImpossibleAreaListGetLogic = deliveryImpossibleAreaListGetLogic;
    }

    /**
     * 配送不可能エリア検索を実行します
     *
     * @param conditionDto 検索条件DTO
     *
     * @return List&lt;DeliveryImpossibleAreaResultDto&gt;
     */
    @Override
    public List<DeliveryImpossibleAreaResultDto> execute(DeliveryImpossibleAreaConditionDto conditionDto) {

        // パラメータチェック
        ArgumentCheckUtil.assertNotNull("conditionDto", conditionDto);

        // 検索処理を実行
        return deliveryImpossibleAreaListGetLogic.execute(conditionDto);
    }
}