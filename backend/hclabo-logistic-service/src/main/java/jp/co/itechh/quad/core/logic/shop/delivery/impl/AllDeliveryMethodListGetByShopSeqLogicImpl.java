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
import jp.co.itechh.quad.core.logic.shop.delivery.AllDeliveryMethodListGetByShopSeqLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 配送方法エンティティリスト取得ロジック<br/>
 *
 * @author negishi
 * @version $Revision: 1.1 $
 *
 */
@Component
public class AllDeliveryMethodListGetByShopSeqLogicImpl extends AbstractShopLogic
                implements AllDeliveryMethodListGetByShopSeqLogic {

    /**
     * 配送方法Dao
     */
    private final DeliveryMethodDao deliveryMethodDao;

    @Autowired
    public AllDeliveryMethodListGetByShopSeqLogicImpl(DeliveryMethodDao deliveryMethodDao) {
        this.deliveryMethodDao = deliveryMethodDao;
    }

    /**
     * ロジック実行
     *
     * @param shopSeq ショップSEQ
     * @return 配送方法エンティティリスト
     */
    @Override
    public List<DeliveryMethodEntity> execute(Integer shopSeq) {

        // パラメータチェック
        this.checkParameter(shopSeq);

        return deliveryMethodDao.getAllDeliveryMethodList(shopSeq);
    }

    /**
     * パラメータのチェックを行います<br/>
     *
     * @param shopSeq   ショップSeq
     */
    protected void checkParameter(Integer shopSeq) {

        // ショップSEQ
        ArgumentCheckUtil.assertGreaterThanZero("shopSeq", shopSeq);

    }

}