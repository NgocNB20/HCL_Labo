/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.shop.delivery.impl;

import jp.co.itechh.quad.core.entity.shop.delivery.DeliverySpecialChargeAreaEntity;
import jp.co.itechh.quad.core.logic.shop.delivery.DeliverySpecialChargeAreaListDeleteLogic;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.shop.delivery.DeliverySpecialChargeAreaListDeleteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 配送特別料金エリア削除Service実装クラス
 *
 * @author $author$
 * @version $Revision: 1.1 $
 */
@Service
public class DeliverySpecialChargeAreaListDeleteServiceImpl extends AbstractShopService
                implements DeliverySpecialChargeAreaListDeleteService {
    /**
     * 配送特別料金エリア削除Logic
     */
    private final DeliverySpecialChargeAreaListDeleteLogic deliverySpecialChargeAreaListDeleteLogic;

    @Autowired
    public DeliverySpecialChargeAreaListDeleteServiceImpl(DeliverySpecialChargeAreaListDeleteLogic deliverySpecialChargeAreaListDeleteLogic) {
        this.deliverySpecialChargeAreaListDeleteLogic = deliverySpecialChargeAreaListDeleteLogic;
    }

    /**
     * 配送特別料金エリア情報を削除します
     *
     * @param entityList List&lt;DeliverySpecialChargeAreaEntity&gt;
     *
     * @return int 処理結果
     */
    @Override
    public int execute(List<DeliverySpecialChargeAreaEntity> entityList) {
        return deliverySpecialChargeAreaListDeleteLogic.execute(entityList);
    }
}