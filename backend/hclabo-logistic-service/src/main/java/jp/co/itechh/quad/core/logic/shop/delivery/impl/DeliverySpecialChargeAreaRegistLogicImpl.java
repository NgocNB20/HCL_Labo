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
import jp.co.itechh.quad.core.logic.shop.delivery.DeliverySpecialChargeAreaRegistLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 配送特別料金エリア登録Logic実装クラス
 *
 * @author $author$
 * @version $Revision: 1.1 $
 */
@Component
public class DeliverySpecialChargeAreaRegistLogicImpl extends AbstractShopLogic
                implements DeliverySpecialChargeAreaRegistLogic {
    /**
     * 配送特別料金エリアDao
     */
    private final DeliverySpecialChargeAreaDao deliverySpecialChargeAreaDao;

    @Autowired
    public DeliverySpecialChargeAreaRegistLogicImpl(DeliverySpecialChargeAreaDao deliverySpecialChargeAreaDao) {
        this.deliverySpecialChargeAreaDao = deliverySpecialChargeAreaDao;
    }

    /**
     * 配送特別料金エリア登録処理を行います
     *
     * @param entity DeliverySpecialChargeAreaEntity
     *
     * @return int 処理結果
     */
    @Override
    public int execute(DeliverySpecialChargeAreaEntity entity) {
        return deliverySpecialChargeAreaDao.insert(entity);
    }
}