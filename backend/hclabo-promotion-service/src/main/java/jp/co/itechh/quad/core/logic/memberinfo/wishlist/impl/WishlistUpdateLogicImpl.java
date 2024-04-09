/*
 * Project Name : HIT-MALL3
 *
 * Copyright (C) 2008 i-TEC HANKYU HANSHIN INC. All Rights Reserved.
 *
 */

package jp.co.itechh.quad.core.logic.memberinfo.wishlist.impl;

import jp.co.itechh.quad.core.base.util.common.ArgumentCheckUtil;
import jp.co.itechh.quad.core.base.utility.ApplicationContextUtility;
import jp.co.itechh.quad.core.base.utility.DateUtility;
import jp.co.itechh.quad.core.dao.memberinfo.wishlist.WishlistDao;
import jp.co.itechh.quad.core.entity.memberinfo.wishlist.WishlistEntity;
import jp.co.itechh.quad.core.logic.AbstractShopLogic;
import jp.co.itechh.quad.core.logic.memberinfo.wishlist.WishlistUpdateLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * お気に入り更新ロジック
 *
 * @author natume
 * @author Kaneko (itec) 2012/08/21 UtilからHelperへ変更　Util使用箇所をgetComponentで取得するように変更
 *
 */
@Component
public class WishlistUpdateLogicImpl extends AbstractShopLogic implements WishlistUpdateLogic {

    /**
     * お気に入りDao
     */
    private final WishlistDao wishlistDao;

    @Autowired
    public WishlistUpdateLogicImpl(WishlistDao wishlistDao) {
        this.wishlistDao = wishlistDao;
    }

    /**
     * お気に入り更新処理
     *
     * @param wishlistEntity お気に入りエンティティ
     * @return 更新処理
     */
    @Override
    public int execute(WishlistEntity wishlistEntity) {

        // パラメータチェック
        ArgumentCheckUtil.assertNotNull("wishlistEntity", wishlistEntity);

        // 日付関連Helper取得
        DateUtility dateUtility = ApplicationContextUtility.getBean(DateUtility.class);

        // 更新日時設定
        wishlistEntity.setUpdateTime(dateUtility.getCurrentTime());

        // お気に入り商品更新
        return wishlistDao.update(wishlistEntity);
    }

}
