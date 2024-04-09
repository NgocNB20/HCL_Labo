/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.dao.shop.ShopDao;
import jp.co.itechh.quad.core.entity.shop.ShopEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.shop.ShopUpdateLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 *
 * ショップ情報更新<br/>
 *
 * @author hirata
 * @author Kaneko (itec) 2012/08/21 UtilからHelperへ変更　Util使用箇所をgetComponentで取得するように変更
 *
 */
@Component
public class ShopUpdateLogicImpl extends AbstractShopLogic implements ShopUpdateLogic {

    /**
     * ショップDao
     */
    private final ShopDao shopDao;

    @Autowired
    public ShopUpdateLogicImpl(ShopDao shopDao) {
        this.shopDao = shopDao;
    }

    /**
     *
     * ショップ情報を更新する<br/>
     *
     * @param shopEntity ショップエンティティ
     * @return 更新した件数
     */
    @Override
    public int execute(ShopEntity shopEntity) {

        // (1) パラメータチェック
        // ショップエンティティが null でないかをチェック
        ArgumentCheckUtil.assertNotNull("shopEntity", shopEntity);

        // 日付関連Helper取得
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);

        // (2) 日時セット
        Timestamp currentTime = dateUtility.getCurrentTime();
        shopEntity.setUpdateTime(currentTime);

        // (3) ショップ情報更新
        // ショップDaoのショップ情報更新処理を実行する。
        return shopDao.update(shopEntity);
    }

}