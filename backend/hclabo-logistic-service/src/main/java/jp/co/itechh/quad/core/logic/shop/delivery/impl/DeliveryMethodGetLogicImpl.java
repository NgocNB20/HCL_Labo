/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.delivery.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.dao.shop.delivery.DeliveryMethodDao;
import jp.co.itechh.quad.core.entity.shop.delivery.DeliveryMethodEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.shop.delivery.DeliveryMethodGetLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 配送方法取得ロジック
 *
 * @author ueshima
 * @version $Revision: 1.2 $
 */
@Component
public class DeliveryMethodGetLogicImpl extends AbstractShopLogic implements DeliveryMethodGetLogic {

    /**
     * 配送方法Dao
     */
    private final DeliveryMethodDao deliveryMethodDao;

    @Autowired
    public DeliveryMethodGetLogicImpl(DeliveryMethodDao deliveryMethodDao) {
        this.deliveryMethodDao = deliveryMethodDao;
    }

    /**
     * ロジック実行<br/>
     *
     * @param deliveryMethodSeq 配送方法SEQ
     * @return 配送方法エンティティ
     */
    @Override
    public DeliveryMethodEntity execute(Integer deliveryMethodSeq) {

        // 引数チェック
        ArgumentCheckUtil.assertNotNull("deliveryMethodSeq", deliveryMethodSeq);

        // 配送方法の検索
        return deliveryMethodDao.getEntity(deliveryMethodSeq);
    }

    /**
     * ロジック実行<br/>
     *
     * @param deliveryMethodName 配送方法名
     * @param shopSeq ショップSEQ
     * @return 配送方法エンティティ
     */
    @Override
    public DeliveryMethodEntity execute(String deliveryMethodName, Integer shopSeq) {

        // 引数チェック
        ArgumentCheckUtil.assertNotNull("deliveryMethodSeq", deliveryMethodName);
        ArgumentCheckUtil.assertNotNull("shopSeq", shopSeq);

        // 配送方法の検索
        return deliveryMethodDao.getDeliveryMethodByName(deliveryMethodName, shopSeq);
    }
}
