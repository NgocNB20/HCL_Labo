/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.shop.delivery.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.entity.shop.delivery.DeliveryMethodEntity;
import jp.co.itechh.quad.core.logic.shop.delivery.DeliveryMethodGetLogic;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.shop.delivery.DeliveryMethodGetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 配送方法取得サービス<br/>
 *
 * @author USER
 * @version $Revision: 1.3 $
 *
 */
@Service
public class DeliveryMethodGetServiceImpl extends AbstractShopService implements DeliveryMethodGetService {

    /** 配送方法取得ロジック */
    private final DeliveryMethodGetLogic deliveryMethodGetLogic;

    @Autowired
    public DeliveryMethodGetServiceImpl(DeliveryMethodGetLogic deliveryMethodGetLogic) {
        this.deliveryMethodGetLogic = deliveryMethodGetLogic;
    }

    /**
     * メソッド概要<br/>
     * メソッドの説明・概要<br/>
     *
     * @param deliveryMethodSeq 配送方法SEQ
     * @return 配送方法エンティティ
     */
    @Override
    public DeliveryMethodEntity execute(Integer deliveryMethodSeq) {

        // パラメータチェック
        ArgumentCheckUtil.assertGreaterThanZero("deliveryMethodSeq", deliveryMethodSeq);

        return deliveryMethodGetLogic.execute(deliveryMethodSeq);
    }

    /**
     * サービス実行
     *
     * @param deliveryMethodName 配送方法名
     * @return 配送方法エンティティ
     */
    @Override
    public DeliveryMethodEntity execute(String deliveryMethodName) {

        // パラメータチェック
        ArgumentCheckUtil.assertNotNull("deliveryMethodName", deliveryMethodName);

        Integer shopSeq = 1001;
        // 配送方法取得ロジック実行
        return deliveryMethodGetLogic.execute(deliveryMethodName, shopSeq);
    }
}