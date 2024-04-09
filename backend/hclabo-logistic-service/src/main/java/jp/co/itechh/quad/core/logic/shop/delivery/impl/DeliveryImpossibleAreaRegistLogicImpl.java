/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.delivery.impl;

import jp.co.itechh.quad.core.dao.shop.delivery.DeliveryImpossibleAreaDao;
import jp.co.itechh.quad.core.entity.shop.delivery.DeliveryImpossibleAreaEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.shop.delivery.DeliveryImpossibleAreaRegistLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 配送不可能エリア登録Logic実装クラス
 *
 * @author $author$
 * @version $Revision: 1.1 $
 */
@Component
public class DeliveryImpossibleAreaRegistLogicImpl extends AbstractShopLogic
                implements DeliveryImpossibleAreaRegistLogic {

    /**
     * 配送不可能エリアDao
     */
    private final DeliveryImpossibleAreaDao deliveryImpossibleAreaDao;

    @Autowired
    public DeliveryImpossibleAreaRegistLogicImpl(DeliveryImpossibleAreaDao deliveryImpossibleAreaDao) {
        this.deliveryImpossibleAreaDao = deliveryImpossibleAreaDao;
    }

    /**
     * 配送不可能エリア登録処理を行います
     *
     * @param entity DeliveryImpossibleAreaEntity
     *
     * @return int 処理結果
     */
    @Override
    public int execute(DeliveryImpossibleAreaEntity entity) {
        return deliveryImpossibleAreaDao.insert(entity);
    }
}