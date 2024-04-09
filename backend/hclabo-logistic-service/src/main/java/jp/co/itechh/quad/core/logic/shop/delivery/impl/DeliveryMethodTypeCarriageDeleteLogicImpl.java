/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.delivery.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.dao.shop.delivery.DeliveryMethodTypeCarriageDao;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.shop.delivery.DeliveryMethodTypeCarriageDeleteLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 配送区分別送料削除ロジック実装クラス
 *
 * @author negishi
 * @version $Revision: 1.1 $
 *
 */
@Component
public class DeliveryMethodTypeCarriageDeleteLogicImpl extends AbstractShopLogic
                implements DeliveryMethodTypeCarriageDeleteLogic {

    /** 配送方法DAO */
    private final DeliveryMethodTypeCarriageDao deliveryMethodTypeCarriageDao;

    @Autowired
    public DeliveryMethodTypeCarriageDeleteLogicImpl(DeliveryMethodTypeCarriageDao deliveryMethodTypeCarriageDao) {
        this.deliveryMethodTypeCarriageDao = deliveryMethodTypeCarriageDao;
    }

    /**
     * ロジック実行
     *
     * @param deliveryMethodSeq 配送方法SEQ
     * @return 削除件数
     */
    @Override
    public int execute(Integer deliveryMethodSeq) {
        // パラメータチェック
        ArgumentCheckUtil.assertNotNull("deliveryMethodSeq", deliveryMethodSeq);

        // 削除処理実行
        return deliveryMethodTypeCarriageDao.deleteList(deliveryMethodSeq);
    }
}