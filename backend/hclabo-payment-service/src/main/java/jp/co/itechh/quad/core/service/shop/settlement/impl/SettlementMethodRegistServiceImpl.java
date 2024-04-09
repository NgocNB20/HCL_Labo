/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.shop.settlement.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.constant.type.HTypeOpenDeleteStatus;
import jp.co.itechh.quad.core.constant.type.HTypeSettlementMethodPriceCommissionFlag;
import jp.co.itechh.quad.core.dto.shop.settlement.SettlementMethodDto;
import jp.co.itechh.quad.core.entity.shop.delivery.DeliveryMethodEntity;
import jp.co.itechh.quad.core.entity.shop.settlement.SettlementMethodEntity;
import jp.co.itechh.quad.core.entity.shop.settlement.SettlementMethodPriceCommissionEntity;
import jp.co.itechh.quad.core.logic.shop.settlement.SettlementMethodMaxOrderDisplayGetLogic;
import jp.co.itechh.quad.core.logic.shop.settlement.SettlementMethodPriceCommissionRegistLogic;
import jp.co.itechh.quad.core.logic.shop.settlement.SettlementMethodRegistLogic;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.shop.settlement.SettlementMethodRegistService;
import jp.co.itechh.quad.method.presentation.api.ShippingMethodApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 決済方法登録
 *
 * @author YAMAGUCHI
 * @version $Revision: 1.1 $
 *
 */
@Service
public class SettlementMethodRegistServiceImpl extends AbstractShopService implements SettlementMethodRegistService {

    /** 配送方法API */
    private final ShippingMethodApi shippingMethodApi;

    /**
     * 決済方法登録ロジック
     */
    private final SettlementMethodRegistLogic settlementMethodRegistLogic;

    /** 表示順取得ロジック */
    private final SettlementMethodMaxOrderDisplayGetLogic settlementMethodMaxOrderDisplayGetLogic;

    /**
     * 決済方法金額別手数料登録ロジック
     */
    private final SettlementMethodPriceCommissionRegistLogic settlementMethodPriceCommissionRegistLogic;

    private final SettlementMethodConfigGetServiceHelper settlementMethodConfigGetServiceHelper;

    @Autowired
    public SettlementMethodRegistServiceImpl(SettlementMethodRegistLogic settlementMethodRegistLogic,
                                             SettlementMethodMaxOrderDisplayGetLogic settlementMethodMaxOrderDisplayGetLogic,
                                             SettlementMethodPriceCommissionRegistLogic settlementMethodPriceCommissionRegistLogic,
                                             SettlementMethodConfigGetServiceHelper settlementMethodConfigGetServiceHelper,
                                             ShippingMethodApi shippingMethodApi) {

        this.settlementMethodRegistLogic = settlementMethodRegistLogic;
        this.settlementMethodMaxOrderDisplayGetLogic = settlementMethodMaxOrderDisplayGetLogic;
        this.settlementMethodPriceCommissionRegistLogic = settlementMethodPriceCommissionRegistLogic;
        this.settlementMethodConfigGetServiceHelper = settlementMethodConfigGetServiceHelper;
        this.shippingMethodApi = shippingMethodApi;
    }

    /**
     * 実行メソッド
     *
     * @param settlementMethodDto 決済方法DTO
     * @return 処理件数
     */
    @Override
    public int execute(SettlementMethodDto settlementMethodDto) {

        Integer shopSeq = 1001;

        // パラメータチェック
        ArgumentCheckUtil.assertNotNull("settlementMethodDto", settlementMethodDto);

        SettlementMethodEntity settlementMethodEntity = settlementMethodDto.getSettlementMethodEntity();

        // パラメータチェック
        ArgumentCheckUtil.assertNotNull("settlementMethodEntity", settlementMethodEntity);

        // 決済方法登録
        // 表示順取得ロジック実行
        int orderDisplay = settlementMethodMaxOrderDisplayGetLogic.execute(shopSeq);
        // 表示順設定
        settlementMethodEntity.setOrderDisplay(orderDisplay);
        settlementMethodEntity.setShopSeq(shopSeq);

        int res = settlementMethodRegistLogic.execute(settlementMethodEntity);

        // 手数料フラグ
        HTypeSettlementMethodPriceCommissionFlag flag = settlementMethodEntity.getSettlementMethodPriceCommissionFlag();

        // 手数料フラグ=金額別
        if (HTypeSettlementMethodPriceCommissionFlag.EACH_AMOUNT == flag) {
            List<SettlementMethodPriceCommissionEntity> priceCommissionList =
                            settlementMethodDto.getSettlementMethodPriceCommissionEntityList();
            // 決済方法金額別手数料リスト登録処理
            int count = registSettlementMethodPriceCommission(priceCommissionList,
                                                              settlementMethodEntity.getSettlementMethodSeq()
                                                             );
            if (count != priceCommissionList.size()) {
                throwMessage(MSGCD_COMMISSION_INSERT_ERROR);
            }
        }

        // 配送方法削除チェック
        Integer deliveryMethodSeq = settlementMethodEntity.getDeliveryMethodSeq();
        if (deliveryMethodSeq != null) {
            DeliveryMethodEntity deliveryMethod = settlementMethodConfigGetServiceHelper.toDeliveryMethodEntity(
                            shippingMethodApi.getByDeliveryMethodSeq(deliveryMethodSeq));
            if (deliveryMethod == null) {
                throwMessage(MSGCD_DELIVERY_NULL_ERROR);
            } else if (HTypeOpenDeleteStatus.DELETED == deliveryMethod.getOpenStatusPC()) {
                throwMessage(MSGCD_DELIVERY_DELETE_ERROR, new Object[] {deliveryMethod.getDeliveryMethodName()});
            }
        }

        return res;
    }

    /**
     * 決済方法金額別手数料リスト登録処理
     *
     * @param priceCommissionList 決済方法金額別手数料登録リスト
     * @param settlementMethodSeq 決済SEQ
     * @return 処理件数
     */
    protected int registSettlementMethodPriceCommission(List<SettlementMethodPriceCommissionEntity> priceCommissionList,
                                                        Integer settlementMethodSeq) {
        int count = 0;

        for (SettlementMethodPriceCommissionEntity settlementMethodPriceCommissionEntity : priceCommissionList) {
            // 決済方法金額別手数料登録処理
            settlementMethodPriceCommissionEntity.setSettlementMethodSeq(settlementMethodSeq);
            count += settlementMethodPriceCommissionRegistLogic.execute(settlementMethodPriceCommissionEntity);
        }

        return count;
    }

}