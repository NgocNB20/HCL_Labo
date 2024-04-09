/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.shop.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.dao.shop.ShopDao;
import jp.co.itechh.quad.core.entity.shop.ShopEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.shop.ShopGetLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 *
 * ショップ情報取得<br/>
 *
 * @author ozaki
 * @author sakai
 * @version $Revision: 1.4 $
 *
 */
@Component
public class ShopGetLogicImpl extends AbstractShopLogic implements ShopGetLogic {

    /** shopDao */
    private final ShopDao shopDao;

    @Autowired
    public ShopGetLogicImpl(ShopDao shopDao) {
        this.shopDao = shopDao;
    }

    /**
     *
     * ショップ情報を取得する<br/>
     *
     * @param shopSeq ショップSEQ
     * @return ショップエンティティ
     */
    @Override
    public ShopEntity execute(Integer shopSeq) {

        // (1) パラメータチェック
        // ショップSEQが null でないかをチェック
        ArgumentCheckUtil.assertNotNull("shopSeq", shopSeq);

        // (2) ショップ情報取得
        // ショップDaoのショップ情報取得処理を実行する。
        // DAO ShopDao
        // メソッド ショップエンティティ getEntity( （パラメータ）ショップSEQ, （パラメータ）サイト区分,
        // （パラメータ）公開状態)
        ShopEntity shopEntity = shopDao.getEntityBySiteTypeStatus(shopSeq);

        // (3) 戻り値
        // 取得したショップエンティティを返す。
        return shopEntity;
    }
}