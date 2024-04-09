/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.shop.delivery.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.entity.shop.delivery.DeliveryMethodEntity;
import jp.co.itechh.quad.core.logic.shop.delivery.AllDeliveryMethodListGetByShopSeqLogic;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.shop.delivery.AllDeliveryMethodListGetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 配送方法エンティティリスト取得
 *
 * @author hirata
 * @version $Revision: 1.1 $
 *
 */
@Service
public class AllDeliveryMethodListGetServiceImpl extends AbstractShopService
                implements AllDeliveryMethodListGetService {

    /**
     * 配送方法エンティティリスト取得ロジック<br/>
     */
    private final AllDeliveryMethodListGetByShopSeqLogic deliveryMethodListGetLogic;

    @Autowired
    public AllDeliveryMethodListGetServiceImpl(AllDeliveryMethodListGetByShopSeqLogic deliveryMethodListGetLogic) {
        this.deliveryMethodListGetLogic = deliveryMethodListGetLogic;
    }

    /**
     *
     * 配送方法エンティティリスト取得
     *
     * @param shopSeq ショップSEQ
     * @return 配送方法エンティティリスト
     */
    @Override
    public List<DeliveryMethodEntity> execute(Integer shopSeq) {
        // パラメータチェック
        this.checkParameter(shopSeq);

        return deliveryMethodListGetLogic.execute(shopSeq);
    }

    /**
     *
     * パラメータのチェックを行います<br/>
     *
     * @param shopSeq   ショップSeq
     */
    protected void checkParameter(Integer shopSeq) {
        // ショップSEQ
        ArgumentCheckUtil.assertGreaterThanZero("shopSeq", shopSeq);
    }
}