/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.shop.delivery.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.entity.shop.delivery.DeliveryMethodTypeCarriageEntity;
import jp.co.itechh.quad.core.logic.shop.delivery.DeliveryMethodTypeCarriageRegistLogic;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.shop.delivery.DeliveryMethodTypeCarriageRegistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 配送方法登録サービス実装クラス
 *
 * @author negishi
 * @version $Revision: 1.2 $
 *
 */
@Service
public class DeliveryMethodTypeCarriageRegistServiceImpl extends AbstractShopService
                implements DeliveryMethodTypeCarriageRegistService {

    /** 配送区分別送料登録ロジック */
    private final DeliveryMethodTypeCarriageRegistLogic deliveryMethodTypeCarriageRegistLogic;

    @Autowired
    public DeliveryMethodTypeCarriageRegistServiceImpl(DeliveryMethodTypeCarriageRegistLogic deliveryMethodTypeCarriageRegistLogic) {
        this.deliveryMethodTypeCarriageRegistLogic = deliveryMethodTypeCarriageRegistLogic;
    }

    /**
     * サービス実行
     *
     * @param deliveryMethodTypeCarriageEntity 配送区分別送料エンティティ
     * @return 登録件数
     */
    @Override
    public int execute(DeliveryMethodTypeCarriageEntity deliveryMethodTypeCarriageEntity) {
        // パラメータチェック
        ArgumentCheckUtil.assertNotNull("deliveryMethodTypeCarriageEntity", deliveryMethodTypeCarriageEntity);

        // 配送方法登録ロジック実行
        int count = deliveryMethodTypeCarriageRegistLogic.execute(deliveryMethodTypeCarriageEntity);

        if (count == 0) {
            // 登録失敗
            throwMessage(MSGCD_DELIVERY_METHOD_TYPE_CARRIAGE_REGIST_FAIL);
        }

        return count;
    }

    /**
     * サービス実行
     *
     * @param deliveryMethodTypeCarriageEntityList 配送区分別送料エンティティリスト
     * @return 登録件数
     */
    @Override
    public int execute(List<DeliveryMethodTypeCarriageEntity> deliveryMethodTypeCarriageEntityList) {
        // パラメータチェック
        ArgumentCheckUtil.assertNotNull("deliveryMethodEntityList", deliveryMethodTypeCarriageEntityList);
        // 登録件数
        int count = 0;
        for (DeliveryMethodTypeCarriageEntity deliveryMethodTypeCarriageEntity : deliveryMethodTypeCarriageEntityList) {
            // 登録処理実行
            count += execute(deliveryMethodTypeCarriageEntity);
        }

        return count;
    }
}