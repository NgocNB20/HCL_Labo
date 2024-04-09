/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.shop.settlement.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.dto.shop.settlement.SettlementMethodDto;
import jp.co.itechh.quad.core.entity.shop.delivery.DeliveryMethodEntity;
import jp.co.itechh.quad.core.entity.shop.settlement.SettlementMethodEntity;
import jp.co.itechh.quad.core.entity.shop.settlement.SettlementMethodPriceCommissionEntity;
import jp.co.itechh.quad.core.logic.shop.settlement.SettlementMethodGetLogic;
import jp.co.itechh.quad.core.logic.shop.settlement.SettlementMethodPriceCommissionListGetLogic;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.shop.settlement.SettlementMethodConfigGetService;
import jp.co.itechh.quad.method.presentation.api.ShippingMethodApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 決済方法詳細設定取得
 *
 * @author YAMAGUCHI
 * @version $Revision: 1.1 $
 *
 */
@Service
public class SettlementMethodConfigGetServiceImpl extends AbstractShopService
                implements SettlementMethodConfigGetService {

    /** 配送方法API */
    private final ShippingMethodApi shippingMethodApi;

    /**
     * 決済方法エンティティ取得ロジック
     */
    private final SettlementMethodGetLogic settlementMethodGetLogic;

    private final SettlementMethodConfigGetServiceHelper settlementMethodConfigGetServiceHelper;

    /**
     * 決済方法金額別手数料リスト取得ロジック
     */
    private final SettlementMethodPriceCommissionListGetLogic settlementMethodPriceCommissionListGetLogic;

    @Autowired
    public SettlementMethodConfigGetServiceImpl(SettlementMethodGetLogic settlementMethodGetLogic,
                                                ShippingMethodApi shippingMethodApi,
                                                SettlementMethodPriceCommissionListGetLogic settlementMethodPriceCommissionListGetLogic,
                                                SettlementMethodConfigGetServiceHelper settlementMethodConfigGetServiceHelper) {

        this.settlementMethodGetLogic = settlementMethodGetLogic;
        this.settlementMethodConfigGetServiceHelper = settlementMethodConfigGetServiceHelper;
        this.shippingMethodApi = shippingMethodApi;
        this.settlementMethodPriceCommissionListGetLogic = settlementMethodPriceCommissionListGetLogic;
    }

    /**
     * 実行メソッド
     *
     * @param settlementMethodSeq 決済方法SEQ
     * @return 決済方法DTO
     */
    @Override
    public SettlementMethodDto execute(Integer settlementMethodSeq) {

        // 共通情報取得
        Integer shopSeq = 1001;

        // パラメータチェック
        ArgumentCheckUtil.assertGreaterThanZero("settlementMethodSeq", settlementMethodSeq);

        // 決済方法取得
        SettlementMethodEntity settlementMethodEntity = settlementMethodGetLogic.execute(settlementMethodSeq, shopSeq);

        if (settlementMethodEntity == null) {
            return null;
        }

        // 決済方法金額別手数料リスト取得
        List<SettlementMethodPriceCommissionEntity> settlementMethodPriceCommissionEntityList =
                        settlementMethodPriceCommissionListGetLogic.execeute(settlementMethodSeq);

        // 戻り値に取得値をセット
        SettlementMethodDto settlementMethodDto = ApplicationContextUtility.getBean(SettlementMethodDto.class);
        settlementMethodDto.setSettlementMethodEntity(settlementMethodEntity);
        settlementMethodDto.setSettlementMethodPriceCommissionEntityList(settlementMethodPriceCommissionEntityList);

        // 配送方法名の取得
        Integer deliveryMethodSeq = settlementMethodEntity.getDeliveryMethodSeq();
        if (deliveryMethodSeq != null) {
            DeliveryMethodEntity deliveryMethod = settlementMethodConfigGetServiceHelper.toDeliveryMethodEntity(
                            shippingMethodApi.getByDeliveryMethodSeq(deliveryMethodSeq));
            if (deliveryMethod != null) {
                settlementMethodDto.setDeliveryMethodName(deliveryMethod.getDeliveryMethodName());
            }
        }

        return settlementMethodDto;
    }

}