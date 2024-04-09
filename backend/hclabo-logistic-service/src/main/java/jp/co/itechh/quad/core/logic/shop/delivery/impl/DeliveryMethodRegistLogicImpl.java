/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.delivery.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.dao.shop.delivery.DeliveryMethodDao;
import jp.co.itechh.quad.core.entity.shop.delivery.DeliveryMethodEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.shop.delivery.DeliveryMethodRegistLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 配送方法登録ロジック実装クラス
 *
 * @author negishi
 * @author Kaneko (itec) 2012/08/21 UtilからHelperへ変更　Util使用箇所をgetComponentで取得するように変更
 *
 */
@Component
public class DeliveryMethodRegistLogicImpl extends AbstractShopLogic implements DeliveryMethodRegistLogic {

    /** 配送方法DAO */
    private final DeliveryMethodDao deliveryMethodDao;

    @Autowired
    public DeliveryMethodRegistLogicImpl(DeliveryMethodDao deliveryMethodDao) {
        this.deliveryMethodDao = deliveryMethodDao;
    }

    /**
     * ロジック実行
     *
     * @param deliveryMethodEntity 配送方法エンティティ
     * @return 登録件数
     */
    @Override
    public int execute(DeliveryMethodEntity deliveryMethodEntity) {
        // パラメータチェック
        ArgumentCheckUtil.assertNotNull("deliveryMethodEntity", deliveryMethodEntity);

        // 日付関連Helper取得
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);

        // 更新日時設定
        deliveryMethodEntity.setUpdateTime(dateUtility.getCurrentTime());
        // 登録日時設定
        deliveryMethodEntity.setRegistTime(dateUtility.getCurrentTime());

        // 登録処理実行
        return deliveryMethodDao.insert(deliveryMethodEntity);
    }
}