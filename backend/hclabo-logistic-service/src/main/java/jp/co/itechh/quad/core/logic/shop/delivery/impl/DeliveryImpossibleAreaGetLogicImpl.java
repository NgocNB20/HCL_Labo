/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.delivery.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.dao.shop.delivery.DeliveryImpossibleAreaDao;
import jp.co.itechh.quad.core.entity.shop.delivery.DeliveryImpossibleAreaEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.shop.delivery.DeliveryImpossibleAreaGetLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 配送不可能エリアエンティティ取得Logicインターフェース
 *
 * @author $author$
 * @version $Revision: 1.1 $
 */
@Component
public class DeliveryImpossibleAreaGetLogicImpl extends AbstractShopLogic implements DeliveryImpossibleAreaGetLogic {

    /**
     * 配送特別料金エリアDao
     */
    private final DeliveryImpossibleAreaDao deliveryImpossibleAreaDao;

    @Autowired
    public DeliveryImpossibleAreaGetLogicImpl(DeliveryImpossibleAreaDao deliveryImpossibleAreaDao) {
        this.deliveryImpossibleAreaDao = deliveryImpossibleAreaDao;
    }

    /**
     * 配送不可能エリアエンティティを取得します
     *
     * @param deliveryMethodSeq Integer
     * @param zipCode String
     *
     * @return DeliverySpecialChargeAreaEntity
     */
    @Override
    public DeliveryImpossibleAreaEntity execute(Integer deliveryMethodSeq, String zipCode) {
        // パラメータチェック
        ArgumentCheckUtil.assertNotNull("deliveryMethodSeq", deliveryMethodSeq);
        ArgumentCheckUtil.assertNotNull("zipCode", zipCode);

        return deliveryImpossibleAreaDao.getEntity(deliveryMethodSeq, zipCode);
    }
}