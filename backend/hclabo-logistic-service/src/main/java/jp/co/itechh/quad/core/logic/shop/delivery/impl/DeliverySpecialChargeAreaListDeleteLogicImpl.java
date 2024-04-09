/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.delivery.impl;

import jp.co.itechh.quad.core.dao.shop.delivery.DeliverySpecialChargeAreaDao;
import jp.co.itechh.quad.core.entity.shop.delivery.DeliverySpecialChargeAreaEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.shop.delivery.DeliverySpecialChargeAreaListDeleteLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 配送特別料金エリア削除Logic実装クラス
 *
 * @author $author$
 * @version $Revision: 1.1 $
 */
@Component
public class DeliverySpecialChargeAreaListDeleteLogicImpl extends AbstractShopLogic
                implements DeliverySpecialChargeAreaListDeleteLogic {
    /**
     * 配送特別料金エリアDao
     */
    private final DeliverySpecialChargeAreaDao deliverySpecialChargeAreaDao;

    @Autowired
    public DeliverySpecialChargeAreaListDeleteLogicImpl(DeliverySpecialChargeAreaDao deliverySpecialChargeAreaDao) {
        this.deliverySpecialChargeAreaDao = deliverySpecialChargeAreaDao;
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
        int result = 0;

        for (DeliverySpecialChargeAreaEntity entity : entityList) {
            result += deliverySpecialChargeAreaDao.delete(entity);
        }

        return result;
    }
}