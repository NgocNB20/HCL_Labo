/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.delivery.impl;

import jp.co.itechh.quad.core.dao.shop.delivery.DeliveryMethodDao;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.shop.delivery.NewDeliveryMethodSeqGetLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 新規配送方法SEQ取得ロジック実装クラス
 *
 * @author negishi
 * @version $Revision: 1.1 $
 *
 */
@Component
public class NewDeliveryMethodSeqGetLogicImpl extends AbstractShopLogic implements NewDeliveryMethodSeqGetLogic {

    /** 配送方法Dao */
    private final DeliveryMethodDao deliveryMethodDao;

    @Autowired
    public NewDeliveryMethodSeqGetLogicImpl(DeliveryMethodDao deliveryMethodDao) {
        this.deliveryMethodDao = deliveryMethodDao;
    }

    /**
     * 実行メソッド
     *
     * @return 配送方法SEQ
     */
    @Override
    public Integer execute() {
        return deliveryMethodDao.getDeliveryMethodSeqNextVal();
    }
}
