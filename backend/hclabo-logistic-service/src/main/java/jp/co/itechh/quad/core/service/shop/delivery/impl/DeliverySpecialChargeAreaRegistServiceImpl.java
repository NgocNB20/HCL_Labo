/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.service.shop.delivery.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.entity.shop.delivery.DeliverySpecialChargeAreaEntity;
import jp.co.itechh.quad.core.logic.shop.delivery.DeliverySpecialChargeAreaGetLogic;
import jp.co.itechh.quad.core.logic.shop.delivery.DeliverySpecialChargeAreaRegistLogic;
import jp.co.itechh.quad.core.logic.shop.zipcode.ZipCodeAddressGetLogic;
import jp.co.itechh.quad.core.service.AbstractShopService;
import jp.co.itechh.quad.core.service.shop.delivery.DeliverySpecialChargeAreaRegistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 配送特別料金エリア登録Serviceインターフェース
 *
 * @author $author$
 * @version $Revision: 1.2 $
 */
@Service
public class DeliverySpecialChargeAreaRegistServiceImpl extends AbstractShopService
                implements DeliverySpecialChargeAreaRegistService {
    /**
     * 配送特別料金エリア登録Logic
     */
    private final DeliverySpecialChargeAreaRegistLogic deliverySpecialChargeAreaRegistLogic;

    /**
     * 配送特別料金エリアエンティティ取得Logic
     */
    private final DeliverySpecialChargeAreaGetLogic deliverySpecialChargeAreaGetLogic;

    /**
     * DOCUMENT ME!
     */
    private final ZipCodeAddressGetLogic zipCodeAddressGetLogic;

    @Autowired
    public DeliverySpecialChargeAreaRegistServiceImpl(DeliverySpecialChargeAreaRegistLogic deliverySpecialChargeAreaRegistLogic,
                                                      DeliverySpecialChargeAreaGetLogic deliverySpecialChargeAreaGetLogic,
                                                      ZipCodeAddressGetLogic zipCodeAddressGetLogic) {
        this.deliverySpecialChargeAreaRegistLogic = deliverySpecialChargeAreaRegistLogic;
        this.deliverySpecialChargeAreaGetLogic = deliverySpecialChargeAreaGetLogic;
        this.zipCodeAddressGetLogic = zipCodeAddressGetLogic;
    }

    /**
     * 配送特別料金エリア登録処理を行います
     *
     * @param entity DeliverySpecialChargeAreaEntity
     *
     * @return int 処理結果
     */
    @Override
    public int execute(DeliverySpecialChargeAreaEntity entity) {
        // パラメータチェック
        ArgumentCheckUtil.assertNotNull("entity", entity);
        ArgumentCheckUtil.assertNotNull("deliveryMethodSeq", entity.getDeliveryMethodSeq());
        ArgumentCheckUtil.assertNotNull("zipCode", entity.getZipCode());

        /* 存在チェック */
        zipCodeAddressGetLogic.execute(entity.getZipCode());

        // 配送特別料金エリア
        DeliverySpecialChargeAreaEntity resultEntity =
                        deliverySpecialChargeAreaGetLogic.execute(entity.getDeliveryMethodSeq(), entity.getZipCode());

        if (resultEntity != null) {
            throwMessage("SSD000701");
        }

        return deliverySpecialChargeAreaRegistLogic.execute(entity);
    }
}