/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.delivery.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.dao.shop.delivery.DeliveryImpossibleAreaDao;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.shop.delivery.DeliveryImpossibleAreaCountGetLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 配送不可能エリアカウント取得ロジック実装クラス
 *
 * @author negishi
 * @version $Revision: 1.1 $
 */
@Component
public class DeliveryImpossibleAreaCountGetLogicImpl extends AbstractShopLogic
                implements DeliveryImpossibleAreaCountGetLogic {

    /** 配送方法Dao */
    private final DeliveryImpossibleAreaDao deliveryImpossibleAreaDao;

    @Autowired
    public DeliveryImpossibleAreaCountGetLogicImpl(DeliveryImpossibleAreaDao deliveryImpossibleAreaDao) {
        this.deliveryImpossibleAreaDao = deliveryImpossibleAreaDao;
    }

    /**
     * ロジック実行<br/>
     *
     * @param deliveryMethodSeq 配送方法SEQ
     * @return 件数
     */
    @Override
    public int execute(Integer deliveryMethodSeq) {
        // 引数チェック
        ArgumentCheckUtil.assertNotNull("deliveryMethodSeq", deliveryMethodSeq);

        // 件数取得実行
        return deliveryImpossibleAreaDao.getCount(deliveryMethodSeq);
    }
}