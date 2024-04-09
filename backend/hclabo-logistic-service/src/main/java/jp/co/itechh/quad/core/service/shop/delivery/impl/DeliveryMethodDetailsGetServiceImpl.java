/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.shop.delivery.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.dto.shop.delivery.DeliveryMethodDetailsDto;
import jp.co.itechh.quad.core.entity.shop.delivery.DeliveryMethodEntity;
import jp.co.itechh.quad.core.entity.shop.delivery.DeliveryMethodTypeCarriageEntity;
import jp.co.itechh.quad.core.logic.shop.delivery.DeliveryImpossibleAreaCountGetLogic;
import jp.co.itechh.quad.core.logic.shop.delivery.DeliveryMethodGetLogic;
import jp.co.itechh.quad.core.logic.shop.delivery.DeliveryMethodTypeCarriageListGetLogic;
import jp.co.itechh.quad.core.logic.shop.delivery.DeliverySpecialChargeAreaCountGetLogic;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.shop.delivery.DeliveryMethodDetailsGetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 配送方法詳細取得サービス
 *
 * @author negishi
 * @version $Revision: 1.1 $
 *
 */
@Service
public class DeliveryMethodDetailsGetServiceImpl extends AbstractShopService
                implements DeliveryMethodDetailsGetService {

    /** 配送方法取得ロジック */
    private final DeliveryMethodGetLogic deliveryMethodGetLogic;

    /** 配送区分別送料取得ロジック */
    private final DeliveryMethodTypeCarriageListGetLogic deliveryMethodTypeCarriageListGetLogic;

    /** 配送特別料金エリアカウント取得ロジック */
    private final DeliverySpecialChargeAreaCountGetLogic deliverySpecialChargeAreaCountGetLogic;

    /** 配送不可能エリアカウント取得ロジック */
    private final DeliveryImpossibleAreaCountGetLogic deliveryImpossibleAreaCountGetLogic;

    @Autowired
    public DeliveryMethodDetailsGetServiceImpl(DeliveryMethodGetLogic deliveryMethodGetLogic,
                                               DeliveryMethodTypeCarriageListGetLogic deliveryMethodTypeCarriageListGetLogic,
                                               DeliverySpecialChargeAreaCountGetLogic deliverySpecialChargeAreaCountGetLogic,
                                               DeliveryImpossibleAreaCountGetLogic deliveryImpossibleAreaCountGetLogic) {
        this.deliveryMethodGetLogic = deliveryMethodGetLogic;
        this.deliveryMethodTypeCarriageListGetLogic = deliveryMethodTypeCarriageListGetLogic;
        this.deliverySpecialChargeAreaCountGetLogic = deliverySpecialChargeAreaCountGetLogic;
        this.deliveryImpossibleAreaCountGetLogic = deliveryImpossibleAreaCountGetLogic;
    }

    /**
     * サービス実行
     *
     * @param deliveryMethodSeq 配送方法SEQ
     * @return 配送方法詳細DTO
     */
    @Override
    public DeliveryMethodDetailsDto execute(Integer deliveryMethodSeq) {
        // パラメータチェック
        ArgumentCheckUtil.assertGreaterThanZero("deliveryMethodSeq", deliveryMethodSeq);

        // 配送方法取得ロジック実行
        DeliveryMethodEntity deliveryMethodEntity = deliveryMethodGetLogic.execute(deliveryMethodSeq);

        // 配送区分別送料取得ロジック実行
        List<DeliveryMethodTypeCarriageEntity> deliveryMethodTypeCarriageEntityList =
                        deliveryMethodTypeCarriageListGetLogic.execute(deliveryMethodSeq);

        // 配送特別料金エリアカウントロジック実行
        int deliverySpecialChargeAreaCount = deliverySpecialChargeAreaCountGetLogic.execute(deliveryMethodSeq);

        // 配送不可能エリアカウント取得ロジック実行
        int deliveryImpossibleAreaCount = deliveryImpossibleAreaCountGetLogic.execute(deliveryMethodSeq);

        DeliveryMethodDetailsDto deliveryMethodDetailsDto = getComponent(DeliveryMethodDetailsDto.class);
        deliveryMethodDetailsDto.setDeliveryMethodEntity(deliveryMethodEntity);
        deliveryMethodDetailsDto.setDeliveryMethodTypeCarriageEntityList(deliveryMethodTypeCarriageEntityList);
        deliveryMethodDetailsDto.setDeliverySpecialChargeAreaCount(deliverySpecialChargeAreaCount);
        deliveryMethodDetailsDto.setDeliveryImpossibleAreaCount(deliveryImpossibleAreaCount);

        return deliveryMethodDetailsDto;
    }
}