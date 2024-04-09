/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.delivery.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.dao.shop.delivery.DeliveryMethodTypeCarriageDao;
import jp.co.itechh.quad.core.entity.shop.delivery.DeliveryMethodTypeCarriageEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.shop.delivery.DeliveryMethodTypeCarriageListGetLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 配送区分別送料リスト取得ロジック実装クラス
 *
 * @author negishi
 * @version $Revision: 1.1 $
 *
 */
@Component
public class DeliveryMethodTypeCarriageListGetLogicImpl extends AbstractShopLogic
                implements DeliveryMethodTypeCarriageListGetLogic {

    /** 配送方法DAO */
    private final DeliveryMethodTypeCarriageDao deliveryMethodTypeCarriageDao;

    @Autowired
    public DeliveryMethodTypeCarriageListGetLogicImpl(DeliveryMethodTypeCarriageDao deliveryMethodTypeCarriageDao) {
        this.deliveryMethodTypeCarriageDao = deliveryMethodTypeCarriageDao;
    }

    /**
     * ロジック実行
     *
     * @param deliveryMethodSeq 配送方法SEQ
     * @return 登録件数
     */
    @Override
    public List<DeliveryMethodTypeCarriageEntity> execute(Integer deliveryMethodSeq) {
        // パラメータチェック
        ArgumentCheckUtil.assertGreaterThanZero("deliveryMethodSeq", deliveryMethodSeq);

        // 取得処理実行
        return deliveryMethodTypeCarriageDao.getEntityList(deliveryMethodSeq);
    }
}