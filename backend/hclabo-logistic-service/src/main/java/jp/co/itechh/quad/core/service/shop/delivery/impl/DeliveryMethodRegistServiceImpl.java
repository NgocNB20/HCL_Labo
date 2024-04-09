/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.shop.delivery.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.base.util.common.CopyUtil;
import jp.co.itechh.quad.core.constant.type.HTypeDeliveryMethodType;
import jp.co.itechh.quad.core.dto.shop.delivery.DeliveryMethodDetailsDto;
import jp.co.itechh.quad.core.entity.shop.delivery.DeliveryMethodEntity;
import jp.co.itechh.quad.core.entity.shop.delivery.DeliveryMethodTypeCarriageEntity;
import jp.co.itechh.quad.core.logic.shop.delivery.DeliveryMethodMaxOrderDisplayGetLogic;
import jp.co.itechh.quad.core.logic.shop.delivery.DeliveryMethodRegistLogic;
import jp.co.itechh.quad.core.logic.shop.delivery.NewDeliveryMethodSeqGetLogic;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.shop.delivery.DeliveryMethodDataCheckService;
import jp.co.itechh.quad.core.service.shop.delivery.DeliveryMethodRegistService;
import jp.co.itechh.quad.core.service.shop.delivery.DeliveryMethodTypeCarriageRegistService;
import org.apache.commons.lang3.ObjectUtils;
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
public class DeliveryMethodRegistServiceImpl extends AbstractShopService implements DeliveryMethodRegistService {

    /** 配送方法データチェックサービス */
    private final DeliveryMethodDataCheckService deliveryMethodDataCheckService;

    /** 表示順取得ロジック */
    private final DeliveryMethodMaxOrderDisplayGetLogic deliveryMethodMaxOrderDisplayGetLogic;

    /** 新規配送方法SEQ取得ロジック */
    private final NewDeliveryMethodSeqGetLogic newDeliveryMethodSeqGetLogic;

    /** 配送方法登録ロジック */
    private final DeliveryMethodRegistLogic deliveryMethodRegistLogic;

    /** 配送区分別送料登録サービス */
    private final DeliveryMethodTypeCarriageRegistService deliveryMethodTypeCarriageRegistService;

    @Autowired
    public DeliveryMethodRegistServiceImpl(DeliveryMethodDataCheckService deliveryMethodDataCheckService,
                                           DeliveryMethodMaxOrderDisplayGetLogic deliveryMethodMaxOrderDisplayGetLogic,
                                           NewDeliveryMethodSeqGetLogic newDeliveryMethodSeqGetLogic,
                                           DeliveryMethodRegistLogic deliveryMethodRegistLogic,
                                           DeliveryMethodTypeCarriageRegistService deliveryMethodTypeCarriageRegistService) {
        this.deliveryMethodDataCheckService = deliveryMethodDataCheckService;
        this.deliveryMethodMaxOrderDisplayGetLogic = deliveryMethodMaxOrderDisplayGetLogic;
        this.newDeliveryMethodSeqGetLogic = newDeliveryMethodSeqGetLogic;
        this.deliveryMethodRegistLogic = deliveryMethodRegistLogic;
        this.deliveryMethodTypeCarriageRegistService = deliveryMethodTypeCarriageRegistService;
    }

    /**
     * サービス実行
     *
     * @param deliveryMethodDetailsDto 配送方法詳細DTO
     * @return 登録件数
     */
    @Override
    public int execute(DeliveryMethodDetailsDto deliveryMethodDetailsDto) {
        // パラメータチェック
        ArgumentCheckUtil.assertNotNull("deliveryMethodEntity", deliveryMethodDetailsDto.getDeliveryMethodEntity());
        ArgumentCheckUtil.assertNotNull(
                        "deliveryMethodTypeCarriageEntityList",
                        deliveryMethodDetailsDto.getDeliveryMethodTypeCarriageEntityList()
                                       );

        // 配送方法エンティティ取得
        DeliveryMethodEntity deliveryMethodEntity =
                        CopyUtil.deepCopy(deliveryMethodDetailsDto.getDeliveryMethodEntity());

        Integer shopSeq = 1001;

        // 配送方法データチェックサービス実行
        deliveryMethodDataCheckService.execute(deliveryMethodEntity);

        // 新規配送方法SEQ取得ロジック実行
        int newDeliveryMethodSeq = newDeliveryMethodSeqGetLogic.execute();
        // 配送方法SEQ設定
        deliveryMethodEntity.setDeliveryMethodSeq(newDeliveryMethodSeq);
        // 表示順取得ロジック実行
        int orderDisplay = deliveryMethodMaxOrderDisplayGetLogic.execute(shopSeq);
        // 表示順設定
        deliveryMethodEntity.setOrderDisplay(orderDisplay);
        // ショップSEQ設定
        deliveryMethodEntity.setShopSeq(shopSeq);

        // 配送方法登録ロジック実行
        int count = deliveryMethodRegistLogic.execute(deliveryMethodEntity);

        if (count == 0) {
            // 配送方法登録失敗
            throwMessage(MSGCD_DELIVERY_METHOD_REGIST_FAIL);
        }

        // 全国一律以外は、配送区分別送料も登録
        if (ObjectUtils.isNotEmpty(deliveryMethodEntity.getDeliveryMethodType())
            && !HTypeDeliveryMethodType.FLAT.equals(deliveryMethodEntity.getDeliveryMethodType())) {
            // 配送区分別送料エンティティリスト取得
            List<DeliveryMethodTypeCarriageEntity> deliveryMethodTypeCarriageEntityList =
                            deliveryMethodDetailsDto.getDeliveryMethodTypeCarriageEntityList();

            // 配送SEQ設定
            for (DeliveryMethodTypeCarriageEntity deliveryMethodTypeCarriageEntity : deliveryMethodTypeCarriageEntityList) {
                deliveryMethodTypeCarriageEntity.setDeliveryMethodSeq(newDeliveryMethodSeq);
            }

            // 配送区分別送料登録サービス実行
            deliveryMethodTypeCarriageRegistService.execute(deliveryMethodTypeCarriageEntityList);

            deliveryMethodDetailsDto.setDeliveryMethodEntity(deliveryMethodEntity);

        }

        return count;
    }
}